package cvb.com.br.projetophoto.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class SDCardUtils {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static File createFile(File dir, String fileName) {
        if (!dir.exists()) {
             dir.mkdir(); // Cria o diretório se não existe
        }
        // Retorna o arquivo para ler ou salvar no sd card
        File file = new File(dir, fileName);
        return file;
    }

    public static File createTempFile(File dir, String filename) throws IOException {
        if (!dir.exists()) {
            dir.mkdir(); // Cria o diretório se não existe
        }
        // Retorna o arquivo para ler ou salvar no sd card
        File file = File.createTempFile(filename, ".jpg", dir);

        return file;
    }
}
