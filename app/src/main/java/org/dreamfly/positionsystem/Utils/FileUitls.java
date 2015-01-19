package org.dreamfly.positionsystem.Utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by liaozhiwei on 2015/1/12.
 * 关于文件的处理的工具类
 */
public class FileUitls {

    private static String DIRNAME = "/Android/data/org.dreamfly.positionSystem/";

    private File mCacheFileDir;

    /**
     * 建立该APP的缓存文件夹x
     */
    public FileUitls() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            this.mCacheFileDir = new File(android.os.Environment.getExternalStorageDirectory(), DIRNAME);
            if (!this.mCacheFileDir.exists()) {
                this.mCacheFileDir.mkdirs();
            }
        } else {

        }
    }

    public File getCacheFileDir() {
        return (this.mCacheFileDir);
    }

    public void createFoler(String folerName) {
        File createFolder = new File(this.mCacheFileDir, "/" + folerName + "/");
        if (!createFolder.exists()) {
            createFolder.mkdir();
        }
    }


}
