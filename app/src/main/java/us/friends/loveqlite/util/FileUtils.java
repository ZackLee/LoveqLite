package us.friends.loveqlite.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    /**
     * 需要知道当前SD卡的目录，Environment.getExternalStorageDierctory()
     */
    public static String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    // 在sdcard卡上创建文件
    public static File createSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + "/Music/" + fileName);
        boolean result = file.createNewFile();
        return file;
    }


    // 在sd卡上创建目录
    public static File createSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        // mkdir只能创建一级目录 ,mkdirs可以创建多级目录
        dir.mkdir();
        return dir;
    }


    // 判断sd卡上的文件夹是否存在
    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + "/Music/" + fileName);
        return file.exists();
    }


    public static void deleteFile(String fileName) {
        File file = new File(SDPATH + fileName);
        file.delete();
    }


    /**
     * 将一个inputstream里面的数据写入SD卡中 第一个参数为目录名 第二个参数为文件名
     */
    public static File write2SDFromInput(String path, InputStream inputstream) {
        File file = null;
        OutputStream output = null;
        try {
            file = createSDFile(path);
            output = new FileOutputStream(file);
            // 4k为单位，每4K写一次
            byte buffer[] = new byte[10 * 1024];
            int temp = 0;
            while ((temp = inputstream.read(buffer)) != -1) {
                // 获取指定信,防止写入没用的信息
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * 确保 /sdcard/Music 文件夹存在
     */
    public static void makeDirectory() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Music");
        if (!file.exists()) {
            file.mkdir();
        }
    }
}