package org.dreamfly.positionsystem.Custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.dreamfly.positionsystem.R;

/**
 * Created by asus on 2015/1/16.
 */
public class DefineListViewHeader extends LinearLayout {

    private Context mContext;
    private LinearLayout.LayoutParams mLaytParams;
    private LinearLayout mLinearLayout;

    private TextView lastFreshTime;

    public DefineListViewHeader(Context context) {
        super(context);
        this.initial(context);
    }

    public DefineListViewHeader(Context context, AttributeSet attr) {
        super(context, attr);
        this.initial(context);
    }

    /**
     * 初始化的头部布局，默认高度为0
     *
     * @param context
     */
    private void initial(Context context) {
        this.mContext = context;
        this.mLaytParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);//初始化高度
        this.mLinearLayout =
                (LinearLayout) LayoutInflater.from(this.mContext).inflate(R.layout.listviewhead_layout, null);
        this.lastFreshTime = (TextView) this.mLinearLayout.findViewById(R.id.txt_listviewhead_lastfreshtime);
        this.mLinearLayout.setLayoutParams(this.mLaytParams);
        this.addView(this.mLinearLayout);
    }

    /**
     * 手指划过的高度
     *
     * @param height
     */
    public void setDynHeight(int height) {
        if (height == -1) {
            height = 0;
        }
        this.mLaytParams.height = height;
        this.mLinearLayout.setLayoutParams(this.mLaytParams);
    }

    public int getCurrentHeight() {
        return (this.mLaytParams.height);
    }


}
