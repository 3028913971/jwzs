package Util;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.TimeZone;

//全局变量类
public class Utility {
    public static final int CONNECTION_ERROR = 0;
    public static final int LOAD_SUCCESS = 1;
    public static final int LOAD_ERROR = -1;
    public static final int LOGIN_SUCCESS = 2;
    public static final int LOGIN_ERROR = -2;
    public static final int SAVE_COURSE_SUCCESS = 3;
    public static final int SAVE_GRADE_SUCCESS = -3;
    public static final int UPDATE_COURSE_SUCCESS = 4;

    //根据页面是否有class="yzm"来判断是否登录成功
    public static boolean isLoginSuccess(Connection.Response response){
        Document document = Jsoup.parse(response.body());
        Elements elements = document.select(".yzm");
        if (elements.size() == 0){
            return true;
        }
        else{
            return false;
        }
    }


    //获得学生姓名
    public static String getStudentName(Connection.Response response){
        Document document = Jsoup.parse(response.body());
        Element element = document.getElementById("xhxm");
        return element.text().substring(0, element.text().length() - 2);
    }


    //获得当前是星期几
    public static int getDayOfWeek(){
        int day = 0;
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mDay)){
            day = 6;
        }else if("2".equals(mDay)){
            day = 0;
        }else if("3".equals(mDay)){
            day = 1;
        }else if("4".equals(mDay)){
            day = 2;
        }else if("5".equals(mDay)){
            day = 3;
        }else if("6".equals(mDay)){
            day = 4;
        }else if("7".equals(mDay)){
            day = 5;
        }
        return day;
    }

    public static Dialog showDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setCancelable(false);
        builder.setMessage(message);
        return builder.show();
    }


    //网络出错回调函数
    public interface GetConnectionCallBack{
        void connectionError();
    }

    //下载验证码的回调函数
    public interface GetCheckcodeCallBack{
        void doSuccess();
        void doError();
    }

    //登录的回调函数
    public interface LoginCallBack {
        void loginSuccess();
        void loginError();
    }

    //关闭AlertDialog
    public interface HideDialogCallBack{
        void hideDialog();
    }

    //成绩查询的RecycleView OnItemClick监听事件
    public interface RecycleViewOnItemClick{
        void onItemClick(View view, int position);
    }

    //saveCourse函数执行完毕回调函数
    public interface SaveCourseCallback{
        void saveCourseSuccess();
    }

    //saveGrade函数执行完毕回调函数
    public interface SaveGradeCallback{
        void saveGradeSuccess();
    }

    //updateCourse函数执行完毕回调函数
    public interface UpdateCourseCallback{
        void updateCourseSuccess();
    }
}

