package cvb.com.br.projetophoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import cvb.com.br.projetophoto.util.ImageUtils;
import cvb.com.br.projetophoto.util.LongEventType;
import cvb.com.br.projetophoto.util.PermissionUtils;
import cvb.com.br.projetophoto.util.SDCardUtils;
import cvb.com.br.projetophoto.util.ShareMediaUtils;

public class MainActivity extends AppCompatActivity {

    private class ViewHolder {

        private ImageView ivZoomI;
        private ImageView ivZoomO;
        private ImageView ivRotateL;
        private ImageView ivRotateR;
        private ImageView ivRemove;
        private ImageView ivFinish;

        private ImageView ivShareFace;
        private ImageView ivShareInst;
        private ImageView ivShareTwit;
        private ImageView ivShareWath;

        private ImageView ivTakePhoto;
        private ImageView ivPhoto;

        private LinearLayout llPanelShare;
        private LinearLayout llPanelControl;
        private LinearLayout llPanelScroll;

        private RelativeLayout rlPanelImage;

        private String photoFilePath;

        public void init(Activity act) {
            ivZoomI   = act.findViewById(R.id.iv_zoom_in);
            ivZoomO   = act.findViewById(R.id.iv_zoom_out);
            ivRotateL = act.findViewById(R.id.iv_rotate_l);
            ivRotateR = act.findViewById(R.id.iv_rotate_r);
            ivRemove  = act.findViewById(R.id.iv_remove);
            ivFinish  = act.findViewById(R.id.iv_finish);

            ivShareFace = act.findViewById(R.id.iv_share_face);
            ivShareInst = act.findViewById(R.id.iv_share_inst);
            ivShareTwit = act.findViewById(R.id.iv_share_twit);
            ivShareWath = act.findViewById(R.id.iv_share_what);

            ivTakePhoto = act.findViewById(R.id.iv_take_photo);
            ivPhoto     = act.findViewById(R.id.iv_photo);

            llPanelShare   = act.findViewById(R.id.linear_share_panel);
            llPanelControl = act.findViewById(R.id.linear_control_panel);
            llPanelScroll  = act.findViewById(R.id.linear_scroll_panel);

            rlPanelImage = act.findViewById(R.id.relative_photo_panel);
        }
    }

    //---------

    private static final int C_REQUEST_CAMERA = 1;

    private ViewHolder vh = new ViewHolder();

    private ImageView ivSelected;

    private boolean executeThread;

    private LongEventType longEventType;

    private Handler handlerUpdateImage = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);

        vh.init(this);

        loadImageScroll();

        setListeners();
    }

    private void setListeners() {
        vh.ivZoomI.setOnClickListener(onClickListenerZoomI);
        vh.ivZoomI.setOnLongClickListener(onLongClickListener);
        vh.ivZoomI.setOnTouchListener(onTouchListener);

        vh.ivZoomO.setOnClickListener(onClickListenerZoomO);
        vh.ivZoomO.setOnLongClickListener(onLongClickListener);
        vh.ivZoomO.setOnTouchListener(onTouchListener);

        vh.ivRotateL.setOnClickListener(onClickListenerRotateL);
        vh.ivRotateL.setOnLongClickListener(onLongClickListener);
        vh.ivRotateL.setOnTouchListener(onTouchListener);

        vh.ivRotateR.setOnClickListener(onClickListenerRotateR);
        vh.ivRotateR.setOnLongClickListener(onLongClickListener);
        vh.ivRotateR.setOnTouchListener(onTouchListener);

        vh.ivFinish.setOnClickListener(onClickListenerFinish);
        vh.ivRemove.setOnClickListener(onClickListenerRemove);

        vh.ivShareFace.setOnClickListener(onClickListenerShareFace);
        vh.ivShareInst.setOnClickListener(onClickListenerShareInst);
        vh.ivShareWath.setOnClickListener(onClickListenerShareWhat);
        vh.ivShareTwit.setOnClickListener(onClickListenerShareTwit);

        vh.ivTakePhoto.setOnClickListener(onClickListenerTakePhoto);
    }

    private View.OnClickListener onClickListenerTakePhoto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!PermissionUtils.hasCameraPermission(getContext())) {
                PermissionUtils.askCameraPermission(MainActivity.this);
            }
            else
                utilizarCamera();
        }
    };

    private void utilizarCamera() {
        if (!SDCardUtils.isExternalStorageWritable()) {
            Toast.makeText(getContext(), "Falha de acesso para escrita de arquivos!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (it.resolveActivity(getPackageManager()) != null) {
            vh.photoFilePath = null;
            File photoFile;

            Uri photoURI = null;
            try {
                photoFile = ImageUtils.createImageFile(getContext());

                vh.photoFilePath = photoFile.getAbsolutePath();

                photoURI = FileProvider.getUriForFile(
                        getContext(),
                        getContext().getApplicationContext().getPackageName() + ".provider",
                        photoFile);
            }
            catch (IOException E) {
                Toast.makeText(getContext(), "Erro ao inicilizar a camera!", Toast.LENGTH_LONG).show();
            }

            if (vh.photoFilePath != null) {
                it.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(it, C_REQUEST_CAMERA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == C_REQUEST_CAMERA && resultCode == Activity.RESULT_OK)
            this.setPhotoAsBackground();
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPhotoAsBackground() {
        int targetW = this.vh.ivPhoto.getWidth();
        int targetH = this.vh.ivPhoto.getHeight();

        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(vh.photoFilePath, bmpOptions);

        int photoW = bmpOptions.outWidth;
        int photoH = bmpOptions.outHeight;

        int scale = Math.min((photoW / targetW), (photoH / targetH));

        bmpOptions.inJustDecodeBounds = false;
        bmpOptions.inSampleSize = scale;

        Bitmap bmp = BitmapFactory.decodeFile(vh.photoFilePath, bmpOptions);

        Bitmap bmpRotate = ImageUtils.rotateImageIfRequired(bmp, vh.photoFilePath);

        vh.ivPhoto.setImageBitmap(bmpRotate);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.REQUEST_PERMISSION) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("As permissões não foram fornecidas.\n\nO aplicativo não pode utilizar a camera!")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }
            }
            utilizarCamera();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //================
    //================

    private View.OnClickListener onClickListenerShareFace = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ShareMediaUtils.shareFacebook(getMainActivity(), vh.rlPanelImage, view);
        }
    };

    private View.OnClickListener onClickListenerShareTwit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ShareMediaUtils.shareTwitter(getMainActivity(), vh.rlPanelImage, view);

        }
    };

    private View.OnClickListener onClickListenerShareWhat = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ShareMediaUtils.shareWhatsup(getMainActivity(), vh.rlPanelImage, view);
        }
    };

    private View.OnClickListener onClickListenerShareInst = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ShareMediaUtils.shareInstagran(getMainActivity(), vh.rlPanelImage, view);
        }
    };

    //================
    //================

    private View.OnClickListener onClickListenerZoomI = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageUtils.handleZoomIn(ivSelected);
        }
    };

    private View.OnClickListener onClickListenerZoomO = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageUtils.handleZoomOut(ivSelected);
        }
    };

    private View.OnClickListener onClickListenerRotateL = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageUtils.handleRotateLeft(ivSelected);
        }
    };

    private View.OnClickListener onClickListenerRotateR = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageUtils.handleRotateRigth(ivSelected);
        }
    };


    private View.OnClickListener onClickListenerFinish = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showControlPanel(false);
        }
    };

    private View.OnClickListener onClickListenerRemove = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            vh.rlPanelImage.removeView(ivSelected);
            showControlPanel(false);
        }
    };

    //================
    //================

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (view.getId() == R.id.iv_zoom_in)
                longEventType = LongEventType.ZoomIn;
            if (view.getId() == R.id.iv_zoom_out)
                longEventType = LongEventType.ZoomOut;
            if (view.getId() == R.id.iv_rotate_l)
                longEventType = LongEventType.RotateLeft;
            if (view.getId() == R.id.iv_rotate_r)
                longEventType = LongEventType.RotateRight;

            executeThread = true;

            (new ThreadUpdateImage()).run();

            return false;
        }
    };

    //================
    //================

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP && executeThread) {
                executeThread = false;
                longEventType = null;
            }

            return false;
        }
    };

    //================
    //================

    private void loadImageScroll() {
        List<Integer> listaImages = ImageUtils.getImageList();

        for (Integer idImage : listaImages) {

            ImageView image = new ImageView(this);
            Bitmap bmp = ImageUtils.decodeSampledBitmapFromResource(getResources(), idImage, 50, 50);
            image.setImageBitmap(bmp);
            image.setPadding(10,10,10,10);

            BitmapFactory.Options dimensions = new BitmapFactory.Options();
            dimensions.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), idImage, dimensions);

            final int w = dimensions.outWidth;
            final int h = dimensions.outHeight;

            image.setOnClickListener(onClickListenerImageScroll(vh.rlPanelImage, idImage, w, h));

            vh.llPanelScroll.addView(image);
        }
    }

    private Context getContext() {
        return this;
    }

    private MainActivity getMainActivity() {
        return this;
    }

    private View.OnClickListener onClickListenerImageScroll(final RelativeLayout relativeLayout, final int idImage, final int w, final int h) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ImageView image = new ImageView(getContext());
                image.setBackgroundResource(idImage);
                relativeLayout.addView(image);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

                image.setLayoutParams(layoutParams);
                image.setAdjustViewBounds(true);

                ivSelected = image;

                showControlPanel(true);

                ViewGroup.LayoutParams params = image.getLayoutParams();
                params.width  = (int) (w * 0.5);
                params.height = (int) (h * 0.5);
                image.setLayoutParams(params);

                image.setOnTouchListener(onTouchListenerSelectedImage(image));
            }
        };
    }

    private View.OnTouchListener onTouchListenerSelectedImage(final ImageView imageView) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float x, y;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ivSelected = imageView;
                        showControlPanel(true);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int[] coords = {0, 0};
                        vh.rlPanelImage.getLocationOnScreen(coords);

                        x = motionEvent.getRawX() - (imageView.getWidth() / 2);
                        y = motionEvent.getRawY() - (coords[1] + (imageView.getHeight() / 2));

                        imageView.setX(x);
                        imageView.setY(y);
                        break;

                    case MotionEvent.ACTION_UP:
                        break;
                }

                return true;
            }
        };
    }

    private void showControlPanel(boolean showControlPanel) {
        vh.llPanelControl.setVisibility(showControlPanel ? View.VISIBLE : View.GONE);
        vh.llPanelShare.setVisibility(showControlPanel ? View.GONE : View.VISIBLE);
    }

    private class ThreadUpdateImage implements Runnable {
        @Override
        public void run() {
            if (executeThread)
                handlerUpdateImage.postDelayed(new ThreadUpdateImage(), 50);

            if (longEventType != null) {
                switch (longEventType) {
                    case ZoomIn:
                        ImageUtils.handleZoomIn(ivSelected);
                        break;
                    case ZoomOut:
                        ImageUtils.handleZoomOut(ivSelected);
                        break;
                    case RotateLeft:
                        ImageUtils.handleRotateLeft(ivSelected);
                        break;
                    case RotateRight:
                        ImageUtils.handleRotateRigth(ivSelected);
                        break;
                }
            }
        }
    }
}
