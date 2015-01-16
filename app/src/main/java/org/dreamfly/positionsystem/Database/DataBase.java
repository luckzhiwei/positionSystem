package org.dreamfly.positionsystem.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by zhengyl on 15-1-16.
 */
public class DataBase {
    private static final String TAG="Position_System";
    private static final String DATABASE_NAME="positionsystem.db";
    SQLiteDatabase db;
    Context context;
    public DataBase(Context context){
        this.context=context;
        db=context.openOrCreateDatabase(DATABASE_NAME,0,null);
        Log.v(TAG,"db path="+db.getPath());
    }
    public void CreateTable_items(){
        try{
            db.execSQL("CREATE TABLE items("+"_id INTEGER primary key autoincrement,"+"name TEXT,"+
            "position TEXT,"+"time TEXT"+");");
            Log.v(TAG,"create table ok");
        }
        catch (Exception e){
            Log.v(TAG,"create table err");
        }
    }
    public boolean items_save(String name,String position,String time){
        String sql="";
        try {
            sql="insert into items values(null,'"+name+"','"+position+"','"+time+"')";
            db.execSQL(sql);
            return true;
        }
        catch (Exception e){
            Log.v("TAG","insert into table items err="+sql);
            return false;
        }
    }
    public Cursor getItems(){
        Log.v("TAG","get items from database");
        String[] colums=new String[]{"_id","name","position","time"};
        return db.query("items",colums,null,null,null,null,null);
    }

}
