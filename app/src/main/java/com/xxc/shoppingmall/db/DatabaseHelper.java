package com.xxc.shoppingmall.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
/**
 * Created by scy on 2017/3/3.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String SWORD="SWORD";
    private static final String TABLE_NAME = "create table shopcatlist" +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "shopid int," +
            "description varchar(20)," +
            "imgUrl varchar(20),"+
            "name varchar(20)," +
            "price double," +
            "productCode vatchar(20)," +
            "status int," +
            "num int," +
            "checked boolean)";
    //三个不同参数的构造函数
    //带全部参数的构造函数，此构造函数必不可少
    public DatabaseHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);

    }
    //带两个参数的构造函数，调用的其实是带三个参数的构造函数
    public DatabaseHelper(Context context,String name){
        this(context,name,VERSION);
    }
    //带三个参数的构造函数，调用的是带所有参数的构造函数
    public DatabaseHelper(Context context,String name,int version){
        this(context, name,null,version);
    }
    //创建数据库
    public void onCreate(SQLiteDatabase db) {
        Log.i(SWORD,"create a Database");
        //创建数据库sql语句
        String sql = "create table user(id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(20),age int,pwd varchar(20))";
        //执行创建数据库操作
        db.execSQL(TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //创建成功，日志输出提示
        Log.i(SWORD,"create success");
    }
}
