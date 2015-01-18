package org.dreamfly.positionsystem.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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
    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db){
        try {
            db.execSQL("CREATE TABLE items(" + "id INTEGER primary key ," + "name TEXT," +
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
     * 向数据库中插入一行
     * @param name 设备名
     * @param subname   备注名
     * @param position 上次定位位置
     * @param time 上次定位时间
     */
    public void items_newItem(int id,String name,String subname,String position,String time) {
        try {
                db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("id", id);
                cv.put("name", name);
                cv.put("subname", subname);
                cv.put("position", position);
                cv.put("time", time);
                db.insert("items", null, cv);
                Log.v(TAG,"insert into items ok");
        }
        catch (Exception e){
            Log.v(TAG,"insert into items err");
        }
    }

    /**
     * 修改数据库某一行中的某列
     * @param columName 列名
     * @param Values 想要修改的值
     * @param id  行名
     */
    public void items_changeValue(String columName,String Values,int id){
        db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(columName,Values);
        long l=db.update("items",cv,"id=?",new String[]{id+""});
        if(l==-1){
            Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 查询id=指定值的数据
     * @param id 行
     * @return cur
     */
    public Cursor Selector(int id){
        Cursor cur;
        db=this.getReadableDatabase();
        cur=db.query("items",new String[]{"id","name","subname","position","time"},
                "id=?",new String[]{id+""},null,null,null);
        return cur;
    }

    /**
     * 删除某一行
     * @param id
     */
    public void delItems(int id){
        db=this.getWritableDatabase();
        db.execSQL("delete from items where id="+id);
    }
}
