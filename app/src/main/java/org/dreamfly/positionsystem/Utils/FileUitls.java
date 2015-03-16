package org.dreamfly.positionsystem.Utils;

import android.content.Context;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by liaozhiwei on 2015/1/12.
 * 关于文件的处理的工具类
 */
public class FileUitls {

    private static String DIRNAME = "/Android/data/org.dreamfly.positionsystem/";

    private File mCacheFileDir = null;

    /**
     * 建立该APP的缓存文件夹
     */
    public FileUitls(Context mContext) {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File dir = android.os.Environment.getExternalStorageDirectory();
            this.mCacheFileDir = new File(dir, DIRNAME);
            dir.renameTo(mCacheFileDir);
            mCacheFileDir.delete();
            if (!this.mCacheFileDir.exists()) {
                this.mCacheFileDir.mkdirs();
            }
        } else {
            this.mCacheFileDir = null;
            Toast.makeText(mContext, "您的存储设备不正常,自动登录功能不可用", Toast.LENGTH_SHORT).show();
        }
    }

    public File getCacheFileDir() {
        if (this.mCacheFileDir != null) {
            return (this.mCacheFileDir);
        } else {
            return null;
        }

    }

    public void createFoler(String folerName) {
        File createFolder = new File(this.mCacheFileDir, "/" + folerName + "/");
        if (!createFolder.exists()) {
            createFolder.mkdir();
        }
    }


}
