package cvb.com.br.projetophoto.util;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import cvb.com.br.projetophoto.MainActivity;

public class ShareMediaUtils {

    private static final String C_INFO_HASHTAG = "#MaisQueBonitoHein...";

    public static void shareFacebook(MainActivity act, RelativeLayout layoutImage, View view) {
        try {
            File imageFile = ImageUtils.drawBitmap(act, layoutImage);

            //Uri imageUri = FileProvider.getUriForFile(act, act.getApplicationContext().getPackageName() + ".provider", imageFile);

            Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bmp)
                    .build();

            ShareHashtag tag = new ShareHashtag.Builder().setHashtag(C_INFO_HASHTAG).build();

            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .setShareHashtag(tag)
                    .build();

            new ShareDialog(act).show(content);

        } catch (Exception E) {
            Toast.makeText(act, "Erro ao realizar share com App Facebook!", Toast.LENGTH_LONG).show();
        }
    }

    public static void shareTwitter(MainActivity act, RelativeLayout layoutImage, View view) {
        if (!validarAppInstalado(act, "com.twitter.android", "Twitter"))
            return;

        try {
            File imageFile = ImageUtils.drawBitmap(act, layoutImage);

            Uri imageUri = FileProvider.getUriForFile(act, act.getApplicationContext().getPackageName() + ".provider", imageFile);

            Intent it = new Intent();
            it.setAction(Intent.ACTION_SEND);
            it.putExtra(Intent.EXTRA_TEXT, C_INFO_HASHTAG);
            it.setType("text/plain");
            it.putExtra(Intent.EXTRA_STREAM, imageUri);
            it.setType("image/jpeg");

            PackageManager packageManager = act.getPackageManager();
            List<ResolveInfo> lista = packageManager.queryIntentActivities(it, PackageManager.MATCH_DEFAULT_ONLY);
            boolean resolved = false;
            for (ResolveInfo ri : lista) {
                if (ri.activityInfo.name.contains("twitter")) {
                    it.setClassName(ri.activityInfo.packageName, ri.activityInfo.name);
                    resolved = true;
                    break;
                }
            }

            act.startActivity(resolved ? it : Intent.createChooser(it, "Compartilhar..."));
        } catch (Exception E) {
            Toast.makeText(act, "Erro ao realizar share com App Twitter!", Toast.LENGTH_LONG).show();
        }
    }

    public static void shareWhatsup(MainActivity act, RelativeLayout layoutImage, View view) {
        if (!validarAppInstalado(act, "com.whatsapp", "WhatsApp"))
            return;

        try {
            File imageFile = ImageUtils.drawBitmap(act, layoutImage);

            Uri imageUri = FileProvider.getUriForFile(act, act.getApplicationContext().getPackageName() + ".provider", imageFile);

            Intent it = new Intent();
            it.setAction(Intent.ACTION_SEND);
            it.putExtra(Intent.EXTRA_TEXT, C_INFO_HASHTAG);
            it.setType("text/plain");
            it.putExtra(Intent.EXTRA_STREAM, imageUri);
            it.setType("image/jpeg");

            it.setPackage("com.whatsapp");

            act.startActivity(it);
        } catch (Exception E) {
            Toast.makeText(act, "Erro ao realizar compartilhamento com App Whatsup!", Toast.LENGTH_LONG).show();
        }
    }

    public static void shareInstagran(MainActivity act, RelativeLayout layoutImage, View view) {
        if (!validarAppInstalado(act, "com.instagram.android", "Instagram"))
            return;

        try {
            File imageFile = ImageUtils.drawBitmap(act, layoutImage);

            Uri imageUri = FileProvider.getUriForFile(act, act.getApplicationContext().getPackageName() + ".provider", imageFile);

            Intent it = new Intent();
            it.setAction(Intent.ACTION_SEND);
            it.putExtra(Intent.EXTRA_STREAM, imageUri);
            it.setType("image/*");

            it.setPackage("com.instagram.android");

            act.startActivity(it);
        } catch (Exception E) {
            Toast.makeText(act, "Erro ao realizar compartilhamento com App Instagram!", Toast.LENGTH_LONG).show();
        }
    }

    private static boolean validarAppInstalado(MainActivity act, String appInfo, String appLabel) {
        PackageManager packageManager = act.getPackageManager();
        try {
            packageManager.getPackageInfo(appInfo, 0);
            return true;
        } catch (Exception E) {
            Toast.makeText(act, "App " + appLabel + " não esta instalado!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
