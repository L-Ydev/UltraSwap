package eq.larry.dev.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists())
                dest.mkdir();
            String[] files = src.list();
            byte b;
            int i;
            String[] arrayOfString1;
            for (i = (arrayOfString1 = files).length, b = 0; b < i; ) {
                String file = arrayOfString1[b];
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);
                b++;
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0)
                out.write(buffer, 0, length);
            in.close();
            out.close();
        }
    }

    public static boolean delete(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            byte b;
            int i;
            File[] arrayOfFile1;
            for (i = (arrayOfFile1 = files).length, b = 0; b < i; ) {
                File file = arrayOfFile1[b];
                if (file.isDirectory()) {
                    delete(file);
                } else {
                    file.delete();
                }
                b++;
            }
        }
        return path.delete();
    }
}

