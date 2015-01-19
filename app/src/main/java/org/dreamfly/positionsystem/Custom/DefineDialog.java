package org.dreamfly.positionsystem.Custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.dreamfly.positionsystem.R;

/**
 * Created by asus on 2015/1/17.
 */
public class DefineDialog {


    private Dialog mDefDialog;
    private Context mContext;
    private Display mDisplay;//获取屏幕相关信息的类


    private TextView txtDefineDialogTitile;
    private Button btnDefineDialogPositive;
    private Button btnDefineDialogNegetive;

    /**
     * 自定义对话框的构造方法：这里一定要把activity.this传入作为上下文参数,
     * 不能使用getAplicationContext()函数,因为是进程上下文
     *
     * @param mContext
     */
    public DefineDialog(Context mContext) {
        this.mContext = mContext;
        WindowManager winManager =
                (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        this.mDisplay = winManager.getDefaultDisplay();

    }

    /**
     * 初始化dialog的视图,加载自定义的layout文件
     *
     * @return (返回自身, 链式调用)
     */
    public DefineDialog buiider() {
        View dialogView = LayoutInflater.from(this.mContext).inflate(R.layout.dialogdef_layout, null);
        this.bindID(dialogView);
        dialogView.setMinimumWidth(this.mDisplay.getWidth());
        this.mDefDialog = new Dialog(this.mContext, R.style.ActionSheetDialogStyle);
        this.mDefDialog.setContentView(dialogView);
        Window dialogWindow = this.mDefDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        this.bindNegBtnListener();
        return (this);
    }

    /**
     * 绑定控件ID
     *
     * @param contentView
     */
    private void bindID(View contentView) {
        this.txtDefineDialogTitile = (TextView) contentView.findViewById(R.id.txt_definedialog_title);
        this.btnDefineDialogPositive = (Button) contentView.findViewById(R.id.btn_dialogdefine_positive);
        this.btnDefineDialogNegetive = (Button) contentView.findViewById(R.id.btn_dialogdefine_negetive);
    }

    public DefineDialog setTitle(String dialgTitle) {
        this.txtDefineDialogTitile.setText(dialgTitle);
        return (this);
    }

    public DefineDialog setPosBtnTxt(String posBtnTxt) {
        this.btnDefineDialogPositive.setText(posBtnTxt);
        return (this);
    }

    public DefineDialog setNegBtnTxt(String ngeBtnTxt) {
        this.btnDefineDialogNegetive.setText(ngeBtnTxt);
        return (this);
    }

    private void bindNegBtnListener() {
        this.btnDefineDialogNegetive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mDefDialog.dismiss();
            }
        });
    }

    /**
     * 点击对话框区域之外后是否可以让对话框消失
     *
     * @param canceable
     * @return
     */
    public DefineDialog setDefineDialogCanceable(boolean canceable) {
        this.mDefDialog.setCancelable(canceable);
        return (this);
    }

    /**
     * 设置确定按键按下后的事件
     *
     * @param defineClickListener
     * @return
     */
    public DefineDialog setPosBtnClickListener(View.OnClickListener defineClickListener) {
        this.btnDefineDialogPositive.setOnClickListener(defineClickListener);
        return (this);
    }

    public DefineDialog setNegBtnClickListenr(View.OnClickListener defineClickListener) {
        this.btnDefineDialogNegetive.setOnClickListener(defineClickListener);
        return (this);
    }

    /**
     * 出现对话框
     *
     * @return
     */
    public DefineDialog show() {
        this.mDefDialog.show();
        return (this);
    }

    /**
     * 消失
     */
    public void dismiss() {
        this.mDefDialog.dismiss();
    }
}
