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
public class DataBase extends SQLiteOpenHelper {
    private static final String TAG = "Position_System";
    private static final String DATABASE_NAME = "positionsystem.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "regulatoritems";
    SQLiteDatabase db;
    Context context;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * 建立表格
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE regulatoritems(" + "id INTEGER primary key ,"
                    + "subid TEXT," + "name TEXT," + "subname TEXT," + "position TEXT," + "time TEXT," + "isconnect  TEXT" + ");");
            db.execSQL("CREATE TABLE manageritems(" + "id INTEGER primary key ," + "name TEXT," +
                    "subid TEXT," + "subname TEXT," + "position TEXT," + "time TEXT," + "isconnect  TEXT" + ");");
            Log.v(TAG, "create table ok");
        } catch (Exception e) {
            Log.v(TAG, "create table err");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String sql1 = "DROP TABLE IF EXISTS " + "manageritems";
        db.execSQL(sql);
        db.execSQL(sql1);
        onCreate(db);
    }


    /**
     * 向数据表manageritems插入一行
     *
     * @param id
     * @param name
     * @param subname
     * @param position
     * @param time
     * @param isconnect
     */
    public void itemsInsert(String tableName, int id, String subid, String name, String subname, String position,
                            String time, String isconnect) {
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("subid", subid);
            cv.put("name", name);
            cv.put("subname", subname);
            cv.put("position", position);
            cv.put("time", time);
            cv.put("isconnect", isconnect);
            db.insert(tableName, null, cv);
            Log.v(TAG, "insert into" + " " + tableName + " " + "ok");
        } catch (Exception e) {
            Log.v(TAG, "insert into " + " " + tableName + " " + "err");
        }
    }

    /**
     * 初始化表deviceinformation的数据
     *
     * @param telnumber
     * @param location
     * @param latitude
     * @param longitude
     */
    public void devicesInsert(int id, String telnumber, String location, String latitude, String longitude) {
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("telnumber", telnumber);
            cv.put("location", location);
            cv.put("latitude", latitude);
            cv.put("longitude", longitude);
            db.insert("deviceinformation", null, cv);
            Log.v(TAG, "insert into deviceinformation ok");
        } catch (Exception e) {
            Log.v(TAG, "insert into deviceinformation err");
        }
    }

    /**
     * 修改数据表中的某一行中的某列
     *
     * @param tableName 表名
     * @param columName 列名
     * @param Values    想要修改的值
     * @param id        行名
     */
    public void items_changeValue(String tableName, String columName, String Values, int id) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columName, Values);
        long l = db.update(tableName, cv, "id=?", new String[]{id + ""});
        if (l == -1) {
            Log.i("zyl", "修改失败");
        } else {
            Log.i("zyl", "修改成功");
        }

    }


    /**
     * 查询id=指定值的数据
     *
     * @param id 行
     * @return cur
     */
    public Cursor Selector(int id, String tableName) {
        Cursor cur;
        db = this.getReadableDatabase();
        cur = db.query(tableName, new String[]{"id", "subid", "name", "subname", "position", "time", "isconnect"},
                "id=?", new String[]{id + ""}, null, null, null);
        return cur;
    }


    /**
     * 删除某一行
     *
     * @param id
     */
    public void delitems(int id, String tableName) {
        db = this.getWritableDatabase();
        db.execSQL("delete from " + tableName + " where id=" + id);
    }
}
