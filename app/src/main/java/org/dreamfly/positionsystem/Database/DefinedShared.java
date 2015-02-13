package org.dreamfly.positionsystem.Database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by zhengyl on 15-2-12.
 */
public class DefinedShared {

    private Context context;

    public DefinedShared(Context context){
        this.context=context;
    }

    /**
     * 用sharedpreference对象存储数据
     * @param key 键
     * @param Value 值
     * @param name 文件名
     */
    public  void putString(String name,String key,String Value){
        SharedPreferences mSharedPreferences =context.getSharedPreferences(name, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putString(key,Value);
        editor.commit();
    }


    /**
     * 用sharedpreference对象存储数据
     * @param name 文件名
     * @param key 键
     *
     */
    public String getString(String name,String key){
        SharedPreferences mSharedPreferences =context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        return mSharedPreferences.getString(key,"");
    }
}
