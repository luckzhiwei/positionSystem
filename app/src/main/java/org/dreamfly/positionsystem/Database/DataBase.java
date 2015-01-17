package org.dreamfly.positionsystem.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zhengyl on 15-1-16.
 */
public class DataBase extends SQLiteOpenHelper{
    private static final String TAG="Position_System";
    private static final String DATABASE_NAME="positionsystem.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "items";
    SQLiteDatabase db;
    private Cursor cursor;
    Context context;
    public DataBase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;

    }
    public void onCreate(SQLiteDatabase db){
        try {
            db.execSQL("CREATE TABLE items(" + "id INTEGER primary key autoincrement," + "name TEXT," +
                    "subname TEXT," + "position TEXT," + "time TEXT" + ");");
            Log.v(TAG, "create table ok");
        }
        catch (Exception e){
            Log.v(TAG,"create table err");
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
    /**
     * 判断表是否存在
     *@param tableName 表名
     *
    public boolean tabbleIsExist(String tableName){
        boolean result=false;
        if(tableName ==null){
            return false;
        }
        try{
            db=this.getReadableDatabase();
            String sql="select count(*) as c from Sqlite_master  " +
                    "where type ='table' and name ='\"+items.trim()+\"'";
            cursor=db.rawQuery(sql,null);
            if(cursor.moveToNext()){
                int count=cursor.getInt(0);
                if(count>0){result=true;}
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }
        return result;
    }*/


}
