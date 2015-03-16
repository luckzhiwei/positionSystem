package org.dreamfly.positionsystem.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by asus on 2015/1/28.
 */
public class ToastUtils {

    public static void showToast(Context context, String showInfo) {
        Toast.makeText(context, showInfo, Toast.LENGTH_SHORT).show();

    }

}
