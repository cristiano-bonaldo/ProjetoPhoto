package cvb.com.br.projetophoto.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cvb.com.br.projetophoto.R;

public class ImageUtils {

    private static double C_FATOR_ZOOM = 0.1;
    private static int C_FATOR_ROTACAO = 5;

    private static final int C_ZOOM_IN  = 1;
    private static final int C_ZOOM_OUT = 2;

    public static List<Integer> getImageList() {
        List<Integer> lista = new ArrayList<>();
        lista.add(R.drawable.st_animal_1);
        lista.add(R.drawable.st_animal_2);
        lista.add(R.drawable.st_animal_3);
        lista.add(R.drawable.st_animal_4);
        lista.add(R.drawable.st_animal_5);
        lista.add(R.drawable.st_animal_6);
        lista.add(R.drawable.st_animal_7);
        lista.add(R.drawable.st_animal_8);
        lista.add(R.drawable.st_animal_9);
        lista.add(R.drawable.st_animal_10);
        lista.add(R.drawable.st_animal_11);
        lista.add(R.drawable.st_animal_12);
        lista.add(R.drawable.st_animal_13);
        lista.add(R.drawable.st_animal_14);
        lista.add(R.drawable.st_animal_15);
        lista.add(R.drawable.st_animal_16);
        lista.add(R.drawable.st_animal_17);
        lista.add(R.drawable.st_animal_18);
        lista.add(R.drawable.st_animal_19);
        lista.add(R.drawable.st_animal_20);
        lista.add(R.drawable.st_animal_21);
        lista.add(R.drawable.st_animal_22);
        lista.add(R.drawable.st_animal_23);
        lista.add(R.drawable.st_animal_24);
        lista.add(R.drawable.st_animal_25);
        lista.add(R.drawable.st_animal_26);
        lista.add(R.drawable.st_animal_27);

        lista.add(R.drawable.st_comida_1);
        lista.add(R.drawable.st_comida_2);
        lista.add(R.drawable.st_comida_3);
        lista.add(R.drawable.st_comida_4);
        lista.add(R.drawable.st_comida_5);

        lista.add(R.drawable.st_coracao_1);
        lista.add(R.drawable.st_coracao_2);
        lista.add(R.drawable.st_coracao_3);
        lista.add(R.drawable.st_coracao_4);
        lista.add(R.drawable.st_coracao_5);
        lista.add(R.drawable.st_coracao_6);

        lista.add(R.drawable.st_drink_1);
        lista.add(R.drawable.st_drink_2);
        lista.add(R.drawable.st_drink_3);
        lista.add(R.drawable.st_drink_4);
        lista.add(R.drawable.st_drink_5);
        lista.add(R.drawable.st_drink_6);
        lista.add(R.drawable.st_drink_7);
        lista.add(R.drawable.st_drink_8);
        lista.add(R.drawable.st_drink_9);
        lista.add(R.drawable.st_drink_10);

        lista.add(R.drawable.st_facial_1);
        lista.add(R.drawable.st_facial_2);
        lista.add(R.drawable.st_facial_3);
        lista.add(R.drawable.st_facial_4);
        lista.add(R.drawable.st_facial_5);
        lista.add(R.drawable.st_facial_6);
        lista.add(R.drawable.st_facial_7);
        lista.add(R.drawable.st_facial_8);
        lista.add(R.drawable.st_facial_9);
        lista.add(R.drawable.st_facial_10);
        lista.add(R.drawable.st_facial_11);
        lista.add(R.drawable.st_facial_12);
        lista.add(R.drawable.st_facial_13);
        lista.add(R.drawable.st_facial_14);

        lista.add(R.drawable.st_misc_1);

        lista.add(R.drawable.st_objeto_1);
        lista.add(R.drawable.st_objeto_2);
        lista.add(R.drawable.st_objeto_3);
        lista.add(R.drawable.st_objeto_4);
        lista.add(R.drawable.st_objeto_5);

        lista.add(R.drawable.st_sticker_1);
        lista.add(R.drawable.st_sticker_2);
        lista.add(R.drawable.st_sticker_3);
        lista.add(R.drawable.st_sticker_4);
        lista.add(R.drawable.st_sticker_5);
        lista.add(R.drawable.st_sticker_6);
        lista.add(R.drawable.st_sticker_7);

        lista.add(R.drawable.st_tatto_1);
        lista.add(R.drawable.st_tatto_2);
        lista.add(R.drawable.st_tatto_3);
        lista.add(R.drawable.st_tatto_4);
        lista.add(R.drawable.st_tatto_5);
        lista.add(R.drawable.st_tatto_6);

        return lista;
    }

    // Site Android = https://developer.android.com/topic/performance/graphics/load-bitmap.html
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width  = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth  = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // Site Android = https://developer.android.com/topic/performance/graphics/load-bitmap.html
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static void handleZoomIn(ImageView ivSelected) {
        if (ivSelected.getWidth() > 800)
            return;

        zoom(C_ZOOM_IN, ivSelected);
    }

    public static void handleZoomOut(ImageView ivSelected) {
        if (ivSelected.getWidth() < 50)
            return;

        zoom(C_ZOOM_OUT, ivSelected);
    }

    public static void handleRotateLeft(ImageView ivSelected) {
        ivSelected.setRotation(ivSelected.getRotation() - C_FATOR_ROTACAO);
    }

    public static void handleRotateRigth(ImageView ivSelected) {
        ivSelected.setRotation(ivSelected.getRotation() + C_FATOR_ROTACAO);
    }

    private static void zoom(int tipoZoom, ImageView ivSelected) {
        ViewGroup.LayoutParams params = ivSelected.getLayoutParams();

        double fator = C_FATOR_ZOOM;
        if (tipoZoom == C_ZOOM_OUT)
            fator = fator * -1;

        params.width  = (int) (ivSelected.getWidth()  + (ivSelected.getWidth()  * fator));
        params.height = (int) (ivSelected.getHeight() + (ivSelected.getHeight() * fator));

        ivSelected.setLayoutParams(params);
    }

    public static File createImageFile(Context ctx) throws IOException {
        String fileName = "photo_udemy";

        File dir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = SDCardUtils.createTempFile(dir, fileName);

        return image;
    }

    public static Bitmap rotateImageIfRequired(Bitmap bmp, String photoFilePath) {
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(photoFilePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotateImage(bmp, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotateImage(bmp, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotateImage(bmp, 270);
                default:
                    return bmp;
            }
        } catch (IOException e) {
            return bmp;
        }
    }

    private static Bitmap rotateImage(Bitmap bmp, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        Bitmap bmpRotate = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);

        bmp.recycle();

        return bmpRotate;
    }

    public static File drawBitmap(Activity act, RelativeLayout layoutImage) throws Exception {
        String filename = "temp_file" + System.currentTimeMillis() + ".jpg";

        layoutImage.setDrawingCacheEnabled(true);
        layoutImage.buildDrawingCache();

        File dir = act.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = SDCardUtils.createFile(dir, filename);
        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);

        layoutImage.getDrawingCache(true).compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

        fileOutputStream.close();

        layoutImage.setDrawingCacheEnabled(false);
        layoutImage.destroyDrawingCache();

        return imageFile;
    }
}
