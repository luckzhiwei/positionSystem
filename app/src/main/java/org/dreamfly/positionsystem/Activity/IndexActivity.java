package org.dreamfly.positionsystem.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;



/**
 * @author liaozhiwei create 2015/1/12
 * 出场动画的加载Activiy类
 */
public class IndexActivity extends ActionBarActivity {

    //变量名与View层id一致
    ImageView imv_indexactivity_background;
    TextView txt_indexactivity_textwelcome;
    SQLiteDatabase db;
    private String TAG="dataDase";

    int alpha=10;//声明控制渐变时间的变量
    int b=0;//声明子线程控制标志的变量
    //声明以控制线程通信的handler类的引用
    private Handler mHandler = new Handler();
    /**
     * 类初始化的函数
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.index_layout);
        DataBase mDataBase=new DataBase(this);
        dataBaseStart(this,mDataBase);
        imv_indexactivity_background=(ImageView)findViewById(R.id.imv_indexactivity_background);
        /**
         * 设置图片渐变的函数
         * @param alpha:
         */
        imv_indexactivity_background.setImageAlpha(alpha);
        /**
         * 定义并实例化子线程该线程用以控制动画加载并启动主界面Activity
         * @param b变量用以控制线程状态
         */
        new Thread(new Runnable() {
            public void run() {
                while (b < 2) {
                    try {
                        if (b == 0) {
                            Thread.sleep(1000);
                            b = 1;
                        } else {
                            Thread.sleep(50);
                        }
                        updateApp();
                        //dataBaseStart();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        }).start();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //调用渐变淡出方法
                imv_indexactivity_background.setAlpha(alpha);
                //调用该方法使imagview淡出
                imv_indexactivity_background.invalidate();
            }
        };
    }

    /**
     *该方法用于启动LoginActivity,调用Handler类"线程通信"方法,在子线程内被调用
     * @param
     * @return
     */
    public void updateApp() {
        alpha += 10;
        if (alpha >= 200) {
            b = 2;
            //LoginActivity启动
            Intent in = new Intent(this,LoginActivity.class);
            startActivity(in);
            this.finish();
        }
        mHandler.sendMessage(mHandler.obtainMessage());

    }
    public void dataBaseStart(Context context,DataBase mDataBase){

            db = mDataBase.getWritableDatabase();
            Log.v(TAG, "creat database");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
         return super.onOptionsItemSelected(item);
    }
}
