package org.dreamfly.positionsystem.Custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * Created by asus on 2015/1/16.
 */
public class DefineListView extends ListView {


         private Context mContext;
         private DefineListViewHeader mListViewHeader;

         private float touchY;

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
             this.mContext=mContext;
             this.mListViewHeader=new DefineListViewHeader(this.mContext);
             this.addHeaderView(this.mListViewHeader);
             this.touchY=0.0f;
         }

         public boolean onTouchEvent(MotionEvent event)
         {
              if(event.getAction()==MotionEvent.ACTION_DOWN)
              {
                   this.touchY=event.getRawY();
              }
              else if(event.getAction()==MotionEvent.ACTION_MOVE)
              {
                   float detalY=event.getRawY()-this.touchY;
                   this.touchY=event.getRawY();
                  this.dynSetHeadViewHeight(this.calDistance(detalY));
              }
              else if(event.getAction()==MotionEvent.ACTION_UP)
              {
                    if(this.getFirstVisiblePosition()==0)
                    {
                          this.dynSetHeadViewHeight(250);
                    }
              }

              return(super.onTouchEvent(event));
         }


         private void dynSetHeadViewHeight(int height)
         {
               this.mListViewHeader.setDynHeight(height);
         }

         private int calDistance(float deltaY)
         {
              int realDistance=0;
              if(deltaY>0 && this.getFirstVisiblePosition()==0)
              {
                    realDistance=(int)deltaY+this.mListViewHeader.getCurrentHeight();
                    if(realDistance>=300)
                    {
                        realDistance=300;
                    }
              }
              return (realDistance);
         }

}
