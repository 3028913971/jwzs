package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpenHelper extends SQLiteOpenHelper {

    //课程建表语句
    public static final String CREATE_COURSE = "create table Course("
            + " id integer primary key autoincrement,"
            + " number text,"
            + " courseInfo text,"
            + " rowspan text,"
            + " row integer,"
            + " col integer)";

    //成绩建表语句
    public static final String CREATE_GRADE = "create table Grade("
            + " id integer primary key autoincrement,"
            + " xn text,"
            + " xq text,"
            + " kcdm text,"
            + " kcmc text,"
            + " kcxz text,"
            + " kcgs text,"
            + " cj text,"
            + " bkcj text,"
            + " cxcj text,"
            + " kkxy text,"
            + " number text,"
            + " xf text,"
            + " jd text)";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSE);
        db.execSQL(CREATE_GRADE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
