package org.dreamfly.positionsystem.Custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by asus on 2015/1/17.
 */
public class DefineDialog {


           private Dialog  mDefDialog;
           private Context mContext;
           private Display mDisplay;

           public DefineDialog(Context  mContext)
           {
               this.mContext=mContext;
               WindowManager winManager=
                       (WindowManager)this.mContext.getSystemService(Context.WINDOW_SERVICE);
               this.mDisplay=winManager.getDefaultDisplay();

           }

           public DefineDialog buiidler()
           {
//                 View dialogView= LayoutInflater.from(this.mContext)

                 return this;
           }


}
