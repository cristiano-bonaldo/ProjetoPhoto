package cvb.com.br.projetophoto.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import cvb.com.br.projetophoto.R;

public class PermissionUtils {

    public static final int REQUEST_PERMISSION = 0;

    public static boolean hasCameraPermission(Context ctx) {
        if (needToAskPermission()) {
            return ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private static boolean needToAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public static void askCameraPermission(final Activity act) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(act)
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setMessage("O aplicativo precisa utilizar a camera.\n\nPor favor, forneça a permissão de acesso necessárias!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            act.requestPermissions(
                                    new String[] {
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                    REQUEST_PERMISSION);
                        }
                    })
                    .show();
        }
        else {
            act.requestPermissions(
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
    }
}
