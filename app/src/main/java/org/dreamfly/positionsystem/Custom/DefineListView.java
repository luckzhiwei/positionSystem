package org.dreamfly.positionsystem.Custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * Created by asus on 2015/1/16.
 */
public class DefineListView extends ListView {


    private Context mContext;
    private DefineListViewHeader mListViewHeader;
    private int userTouchDistance;//用户实际划过的距离

    private float touchY;

    private static final int MINDISTANCE = 250;
    private static final int MAXDISTATNCE = 600;

    public DefineListView(Context context) {
        super(context);
        this.initial(context);
    }

    public DefineListView(Context context, AttributeSet attr) {
        super(context, attr);
        this.initial(context);
    }

    public DefineListView(Context context, AttributeSet attr, int defstyle) {
        super(context, attr, defstyle);
        this.initial(context);
    }

    /**
     * 初始化参数,主要把listview顶部的高度设置为0
     *
     * @param mContext
     */
    private void initial(Context mContext) {
        this.mContext = mContext;
        this.mListViewHeader = new DefineListViewHeader(this.mContext);
        this.addHeaderView(this.mListViewHeader);
        this.touchY = 0.0f;
        this.userTouchDistance = 0;
    }

    /**
     * 为listivew设置事件监听
     *
     * @param event
     * @return
     */
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            this.touchY = event.getRawY();
//        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            float detalY = event.getRawY() - this.touchY;
//            this.touchY = event.getRawY();
//            this.dynSetHeadViewHeight(this.calDistance(detalY));
//        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (this.getFirstVisiblePosition() == 0
//                    && this.userTouchDistance > MINDISTANCE) {
//                this.dynSetHeadViewHeight(250);
//                this.userTouchDistance = 0;
//            }
//
//            //用户划过的距离必须超过最小才行，对顶部设置一个合适的大小来显示
//            else if (this.userTouchDistance < MINDISTANCE) {
//                this.dynSetHeadViewHeight(0);
//            }
//            //如果用户实际划过的距离小于最小距离,那么listview的头部是不会显示的
//            this.userTouchDistance = 0;
//            //每次划过之后都将用户的实际划过的距离清零
//        }
//
//        return (super.onTouchEvent(event));
//    }

    /**
     * 动态设置设置listview头顶部的长度
     *
     * @param height
     */
    public void dynSetHeadViewHeight(int height) {
        this.mListViewHeader.setDynHeight(height);
    }

    public int getUserTouchDistance(){
        return this.userTouchDistance;
    }
    /**
     * 参数表示计算划长度过的距离,累加算法来计算下来的
     *
     * @param deltaY
     * @return
     */

    public int calDistance(float deltaY) {
        int realDistance = 0;
        if (deltaY > 0 && this.getFirstVisiblePosition() == 0) {
            realDistance = (int) deltaY + this.mListViewHeader.getCurrentHeight();
            this.userTouchDistance = realDistance;
            if (realDistance >= MAXDISTATNCE) {
                realDistance = MAXDISTATNCE;
            }

        }
        return (realDistance);
    }

}
