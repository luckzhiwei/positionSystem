package org.dreamfly.positionsystem.Custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * Created by asus on 2015/1/16.
 */
public class DefineListView extends ListView {


         private Context mContext;
         private DefineListViewHeader mListViewHeader;

         public DefineListView(Context context)
         {
              super(context);
              this.initial(context);
         }

         public  DefineListView(Context context,AttributeSet attr)
         {
               super(context,attr);
               this.initial(context);
         }

         public  DefineListView(Context context,AttributeSet attr,int defstyle)
         {
               super(context,attr,defstyle);
               this.initial(context);
         }

         private void initial(Context mContext)
         {

         }


}
