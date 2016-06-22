package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Model.CourseModel;
import Model.GradeModel;
import Util.LogUtil;

//数据库操作类
public class DB {

    //数据库名
    public static final String DB_NAME = "jwzs.db";

    //数据库版本
    public static final int VERSION = 1;

    private static DB DB;

    private SQLiteDatabase db;

    //私有化构造方法
    private DB(Context context){
        DBOpenHelper dbHelper = new DBOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    //获取CourseDB实例
    public synchronized static DB getInstance(Context context){
        if(DB == null){
            DB = new DB(context);
        }
        return DB;
    }


    //储存课程实例
    public void saveCourse(CourseModel course){
        if(course != null){
            ContentValues values = new ContentValues();
            values.put("number", course.getNumber());
            values.put("courseInfo", course.getCourseInfo());
            values.put("rowspan", course.getRowspan());
            values.put("row", course.getRow());
            values.put("col", course.getCol());
            db.insert("Course", null, values);
        }
    }

    //储存成绩实例
    public void saveGrade(GradeModel gradeModel){
        if(gradeModel != null){
            ContentValues values = new ContentValues();
            values.put("number", gradeModel.getNumber());
            values.put("kcmc", gradeModel.getKcmc());
            values.put("xf", gradeModel.getXf());
            values.put("jd", gradeModel.getJd());
            values.put("xn", gradeModel.getXn());
            values.put("xq", gradeModel.getXq());
            values.put("kcdm", gradeModel.getKcdm());
            values.put("kcxz", gradeModel.getKcxz());
            values.put("kcgs", gradeModel.getKcgs());
            values.put("cj", gradeModel.getCj());
            values.put("bkcj", gradeModel.getBkcj());
            values.put("cxcj", gradeModel.getCxcj());
            values.put("kkxy", gradeModel.getKkxy());
            db.insert("Grade", null, values);
        }
    }

    //查询课程表
    public Cursor query(String sql, String[] args){
        Cursor cursor = db.rawQuery(sql, args);
        return cursor;
    }

    //查询成绩表
    public Cursor queryGrade(String sql, String[] args){
        Cursor cursor = db.rawQuery(sql, args);
        return cursor;
    }

    //更新课程表
    public int updateCourse(CourseModel course){
        if(course != null){
            ContentValues values = new ContentValues();
            values.put("courseInfo", course.getCourseInfo());
            values.put("rowspan", course.getRowspan());
            return db.update("Course", values, "number=? and row=? and col=?",
                    new String[]{course.getNumber(),
                            "" + course.getRow(),
                            "" +course.getCol()});
        }
        return 0;
    }

    //删除成绩表
    public int deleteGrade(String number){
        return db.delete("Grade", "number=?", new String[]{number});
    }
}
