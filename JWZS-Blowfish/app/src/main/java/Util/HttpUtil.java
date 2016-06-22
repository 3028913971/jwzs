package Util;

import android.content.Context;
import android.content.SharedPreferences;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import Database.DB;
import Model.CourseModel;
import Model.GradeModel;

//网络请求类
public class HttpUtil {

    //主页面网址
    private final static String url_index = "http://jw1.wucc.cn/default2.aspx";

    //验证码网址
    private final static String url_checkcode = "http://jw1.wucc.cn/CheckCode.aspx";

    //存放cookie
    private static Map<String, String> cookies = null;

    //存放viewstate
    private static String viewstate = null;

    //学生学号
    private static String xsxh;

    //学生姓名
    private static String xsxm;

    private static HttpUtil httpUtil;
    private static Thread connection, getCourseThread,
            downGradeViewstateThread, getGradeThread,
            getGradeViewstateThread;
    private static Context context;
    private static String TAG = "HttpUtil";

    //私有构造函数
    private HttpUtil() {
    }

    //获得HttpUtil实例
    private static HttpUtil getInstance() {
        if (httpUtil == null) {
            synchronized (HttpUtil.class) {
                if (httpUtil == null) {
                    httpUtil = new HttpUtil();
                }
            }
        }
        return httpUtil;
    }


    //获得连接，并保存cookie和viewstate在内存中
    private void _getConnection(Utility.GetConnectionCallBack callback) {
        try {
            Connection conn = Jsoup.connect(url_index)
                    .timeout(20000)
                    .method(Connection.Method.GET);
            Connection.Response response = conn.execute();
            if (response.statusCode() == 200) {
                cookies = response.cookies();
                LogUtil.d(TAG, "cookies = " + cookies.toString());
            } else {
                callback.connectionError();
            }
            Document document = Jsoup.parse(response.body());
            viewstate = document.select("input").first().attr("value");
            LogUtil.d(TAG, "viewstate = " + viewstate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //下载验证码并调用回调函数。若成功，则显示验证码，否则提示错误
    private void _getCheckcode(final Utility.GetCheckcodeCallBack callback) {
        try {
            Connection.Response resultImageResponse = Jsoup.connect(url_checkcode)
                    .cookies(cookies)
                    .ignoreContentType(true)
                    .execute();
            File file = new File(context.getFilesDir(),
                    "checkcode.png");
            LogUtil.d(TAG, "验证码储存路径: " + context.getFilesDir());
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(resultImageResponse.bodyAsBytes());
            fos.flush();
            fos.close();
            callback.doSuccess();
        } catch (Exception e) {
            callback.doError();
            e.printStackTrace();
        }
    }

    //post发送数据登录,并储存学生的姓名、学号
    private void _login(final Map<String, String> data, final Utility.LoginCallBack callback) {
        data.put("__VIEWSTATE", viewstate);
        data.put("RadioButtonList1", "学生");
        data.put("Button1", "");
        Connection conn = Jsoup.connect(url_index)
                .cookies(cookies)
                .header("Origin", "http://jw1.wucc.cn")
                .data(data)
                .method(Connection.Method.POST)
                .timeout(20000);
        try {
            Connection.Response response = conn.execute();
            if (Utility.isLoginSuccess(response)) {
                xsxh = data.get("txtUserName");
                xsxm = Utility.getStudentName(response);
                LogUtil.d(TAG, "学生学号: " + xsxh + ", 学生姓名: " + xsxm);
                callback.loginSuccess();
            } else {
                callback.loginError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //下载课程信息保存到本地
    private void _getCourse(){
        String url_course = null;
        try {
            url_course = "http://jw1.wucc.cn/xskbcx.aspx?" +
                    "xh=" + xsxh + "&" +
                    "xm=" + URLEncoder.encode(xsxm, "gb2312") + "&" +
                    "gnmkdm=N121603";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String referer = "http://jw1.wucc.cn/xs_main.aspx?xh=" + xsxh;
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(url_course);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(2*1000);
            conn.setRequestProperty("Cookie", cookies.toString().replace("{", "").replace("}", "").trim());
            conn.setRequestProperty("Referer", referer);
            conn.setRequestMethod("GET");
            is = conn.getInputStream();
            File file = new File(context.getFilesDir(),
                    "html_" + xsxh + ".txt");
            os = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //保存课程到数据库
    private void _saveCourse(Context context){
        File file = new File(context.getFilesDir(),
                "html_" + xsxh + ".txt");
        Document document = null;
        try {
            document = Jsoup.parse(file, "gb2312");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements trs = document.select("table").get(1).select("tr");

        //删除最上面两列
        trs.remove(0);
        trs.remove(0);

        //上午、中午、晚上 标记
        trs.get(0).select("td").get(0).remove();
        trs.get(4).select("td").get(0).remove();
        trs.get(8).select("td").get(0).remove();

        //删除 第一节 标记
        for (int i = 0; i < trs.size(); i++) {
            trs.get(i).select("td").get(0).remove();
        }

        //trs.size()表示一个有几节课
        for (int i = 0; i < trs.size(); i++) {
            //trs.select("td").size()表示有几列
            for (int j = 0; j < trs.get(i).select("td").size(); j++) {
                CourseModel course = new CourseModel();
                Element element = trs.get(i).select("td").get(j);
                if(element.hasAttr("rowspan")){
                    course.setRowspan(element.attr("rowspan"));
                }else{
                    course.setRowspan("0");
                }
                course.setCourseInfo(element.text());
                course.setRow(i+1);
                course.setCol(j+1);
                course.setNumber(xsxh);
                DB.getInstance(context).saveCourse(course);
            }
        }
    }

    //更新数据库的课程
    private int _updateCourse(Context context){
        int result = 0;
        File file = new File(context.getFilesDir(),
                "html_" + xsxh + ".txt");
        Document document = null;
        try {
            document = Jsoup.parse(file, "gb2312");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements trs = document.select("table").get(1).select("tr");

        //删除最上面两列
        trs.remove(0);
        trs.remove(0);

        //上午、中午、晚上 标记
        trs.get(0).select("td").get(0).remove();
        trs.get(4).select("td").get(0).remove();
        trs.get(8).select("td").get(0).remove();

        //删除 第一节 标记
        for (int i = 0; i < trs.size(); i++) {
            trs.get(i).select("td").get(0).remove();
        }

        //trs.size()表示一个有几节课
        for (int i = 0; i < trs.size(); i++) {
            //trs.select("td").size()表示有几列
            for (int j = 0; j < trs.get(i).select("td").size(); j++) {
                CourseModel course = new CourseModel();
                Element element = trs.get(i).select("td").get(j);
                if(element.hasAttr("rowspan")){
                    course.setRowspan(element.attr("rowspan"));
                }else{
                    course.setRowspan("0");
                }
                course.setCourseInfo(element.text());
                course.setRow(i+1);
                course.setCol(j+1);
                course.setNumber(xsxh);
                result = DB.getInstance(context).updateCourse(course);
            }
        }
        return result;
    }

    //下载成绩查询页面的viewstate，保存当前页面内容到文件
    private void _downGradeViewstate(){
        String url_grade = null;
        try {
            url_grade = "http://jw1.wucc.cn/xscjcx.aspx?" +
                    "xh=" + xsxh + "&xm=" +
                    URLEncoder.encode(xsxm, "gb2312")+
                    "&gnmkdm=N121605";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String referer = "http://jw1.wucc.cn/xs_main.aspx?xh=" + xsxh;
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(url_grade);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(2*1000);
            conn.setRequestProperty("Cookie", cookies.toString().replace("{", "").replace("}", "").trim());
            conn.setRequestProperty("Referer", referer);
            conn.setRequestMethod("GET");
            is = conn.getInputStream();
            File file = new File(context.getFilesDir(),
                    "gradeviewstate_" + xsxh + ".txt");
            os = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //从文件中读取成绩查询的viewstate
    private void _getGradeViewstate(Context context){
        File file = new File(context.getFilesDir(),
                "gradeviewstate_" + xsxh + ".txt");
        Document document = null;
        try {
            document = Jsoup.parse(file, "gb2312");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element element = document.select("input[name=__VIEWSTATE]").first();
        SharedPreferences.Editor editor = context.getSharedPreferences("data", Context.MODE_PRIVATE)
                .edit();
        editor.putString("viewstate", element.attr("value"));
        editor.commit();
    }

    //下载成绩内容到文件中
    private void _getGrade(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);

        String url_grade = null;
        String btn_zcj_str = null;
        String gradeViewstate = null;
        try {
            url_grade = "http://jw1.wucc.cn/xscjcx.aspx?" +
                    "xh=" + xsxh + "&xm=" +
                    URLEncoder.encode(xsxm, "gb2312")+
                    "&gnmkdm=N121605";
            gradeViewstate = URLEncoder.encode(sharedPreferences.getString("viewstate", ""), "gb2312");
            btn_zcj_str = URLEncoder.encode("历年成绩", "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String referer = url_grade;

        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(url_grade);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(2*1000);
            conn.setRequestProperty("Cookie", cookies.toString().replace("{", "").replace("}", "").trim());
            conn.setRequestProperty("Referer", referer);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            StringBuffer params = new StringBuffer();
            params.append("__VIEWSTATE").append("=")
                    .append(gradeViewstate)
                    .append("&")
                    .append("btn_zcj").append("=").append(btn_zcj_str)
                    .append("&")
                    .append("__EVENTTARGET").append("=").append("")
                    .append("&")
                    .append("__EVENTARGUMENT").append("=").append("")
                    .append("&")
                    .append("hidLanguage").append("=").append("")
                    .append("&")
                    .append("ddlXN").append("=").append("")
                    .append("&")
                    .append("ddlXQ").append("=").append("")
                    .append("&")
                    .append("ddl_kcxz").append("=").append("");
            byte[] bypes = params.toString().getBytes();
            conn.getOutputStream().write(bypes);
            is = conn.getInputStream();
            File file = new File(context.getFilesDir(),
                    "grade_" + xsxh + ".txt");
            os = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //保存成绩内容到数据库
    private void _saveGrade(Context context){
        File file = new File(context.getFilesDir(),
                "grade_" + xsxh + ".txt");
        Document document = null;
        try {
            document = Jsoup.parse(file, "gb2312");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements trs = document.select("table").get(1).select("tr");

        //删除最上面一列
        trs.remove(0);

        //trs.size()表示一共有多少项
        for (int i = 0; i < trs.size(); i++) {

            GradeModel gradeModel = new GradeModel();
            Elements elements = trs.get(i).select("td");
            gradeModel.setXn(elements.get(0).text());
            gradeModel.setXq(elements.get(1).text());
            gradeModel.setKcdm(elements.get(2).text());
            gradeModel.setKcmc(elements.get(3).text());
            gradeModel.setKcxz(elements.get(4).text());
            gradeModel.setKcgs(elements.get(5).text());
            gradeModel.setXf(elements.get(6).text());
            gradeModel.setJd(elements.get(7).text());
            gradeModel.setCj(elements.get(8).text());
            gradeModel.setBkcj(elements.get(10).text());
            gradeModel.setCxcj(elements.get(11).text());
            gradeModel.setKkxy(elements.get(12).text());
            gradeModel.setNumber(xsxh);
            DB.getInstance(context).saveGrade(gradeModel);
        }
    }

    private int _deleteGrade(Context context){
        return DB.getInstance(context).deleteGrade(xsxh);
    }

    /*******************************对外方法******************************/
    //获得连接
    public static void getConnection(final Utility.GetConnectionCallBack callback) {
        connection = new Thread() {
            @Override
            public void run() {
                super.run();
                getInstance()._getConnection(callback);
                LogUtil.e(TAG, "getConnection执行");
            }
        };
        connection.start();
    }

    //连接后，下载验证码
    public static void getCheckcode(final Utility.GetCheckcodeCallBack callback) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    connection.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getInstance()._getCheckcode(callback);
                LogUtil.e(TAG, "getCheckcode执行");
            }
        }.start();
    }

    //登陆
    public static void login(final Map<String, String> data, final Utility.LoginCallBack callback) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                getInstance()._login(data, callback);
                LogUtil.e(TAG, "login执行");
            }
        }.start();
    }

    //下载课程页面内容到文件
    public static void getCourse(){
        getCourseThread = new Thread(){
            @Override
            public void run() {
                super.run();
                getInstance()._getCourse();
                LogUtil.e(TAG, "getCourse执行");
            }
        };
        getCourseThread.start();
    }

    //下载课程页面内容到文件后，保存课程信息到数据库
    public static void saveCourse(final Context context, final Utility.SaveCourseCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    getCourseThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getInstance()._saveCourse(context);
                callback.saveCourseSuccess();
                LogUtil.e(TAG, "saveCourse执行");
            }
        }.start();
    }

    //下载课程页面内容到文件后，更新数据库的课程信息
    public static void updateCourse(final Context context, final Utility.UpdateCourseCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    getCourseThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getInstance()._updateCourse(context);
                callback.updateCourseSuccess();
                LogUtil.e(TAG, "updateCourse执行");
            }
        }.start();
    }

    //下载成绩查询的viewstate页面内容到文件
    public static void downGradeViewstate(){
        downGradeViewstateThread = new Thread(){
            @Override
            public void run() {
                super.run();
                getInstance()._downGradeViewstate();
                LogUtil.e(TAG, "downGradeViewstate执行");
            }
        };
        downGradeViewstateThread.start();
    }

    //在成绩查询的viewstate下载完成后，获取viewstate
    public static void getGradeViewstate(final Context context){
        getGradeViewstateThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    downGradeViewstateThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getInstance()._getGradeViewstate(context);
                LogUtil.e(TAG, "getGradeViewstate执行");
            }
        };
        getGradeViewstateThread.start();
    }

    //成绩查询的viewstate获得后，下载成绩页面内容到文件
    public static void getGrade(final Context context){
        getGradeThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    getGradeViewstateThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getInstance()._getGrade(context);
                LogUtil.e(TAG, "getGrade执行");
            }
        };
        getGradeThread.start();
    }

    //成绩下载完成后，保存成绩到数据库
    public static void saveGrade(final Context context, final Utility.SaveGradeCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    getGradeThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getInstance()._saveGrade(context);
                callback.saveGradeSuccess();
                LogUtil.e(TAG, "saveGrade执行");
            }
        }.start();
    }

    //删除数据库内成绩信息
    public static int deleteGrade(Context context){
        LogUtil.e(TAG, "deleteGrade执行");
        return getInstance()._deleteGrade(context);
    }

    //获得学生学号
    public static String getXSXH() {
        return xsxh;
    }

    //获得学生姓名
    public static String getXSXM() {
        return xsxm;
    }

    public static void setContext(Context context) {
        HttpUtil.context = context;
    }

}


