package cyj.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import Database.DB;
import Model.MessageModel;
import Util.HttpUtil;
import Util.LogUtil;
import Util.Utility;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;


//主页面
public class IndexActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        Toolbar.OnMenuItemClickListener{

    private Toolbar toolbar;
    private TextView nav_xsxm, nav_xsxh;
    private String TAG = "HttpUtil";
    private boolean isSaveCourseSuccess = false;
    private boolean isSaveGradeSuccess = false;
    private Dialog dialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Utility.SAVE_COURSE_SUCCESS:
                    isSaveCourseSuccess = true;
                    if (isSaveGradeSuccess == true)
                        dialog.dismiss();
                    break;

                case Utility.SAVE_GRADE_SUCCESS:
                    isSaveGradeSuccess = true;
                    if (isSaveCourseSuccess == true)
                        dialog.dismiss();
                    break;
            }
        }
    };

    //获取当前实例
    public static void getInstance(Context context){
        Intent intent = new Intent(context, IndexActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("首页");
        toolbar.setOnMenuItemClickListener(this);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        nav_xsxm = (TextView)view.findViewById(R.id.nav_name);
        nav_xsxh = (TextView)view.findViewById(R.id.nav_username);

        nav_xsxm.setText(HttpUtil.getXSXM());
        nav_xsxh.setText(HttpUtil.getXSXH());

        if (DB.getInstance(this).queryGrade("select * from Grade where number=?",
                        new String[]{HttpUtil.getXSXH()})
                .getCount() == 0){
            dialog = Utility.showDialog(IndexActivity.this, "初始化中...");
            initDatas();
            LogUtil.d(TAG, "数据初始化操作执行");
        }

        Bmob.initialize(this, "c5f686db53dc2b5bc11c9dbc12949641");
    }

    //按back键时先检查drawerLayout是否拉出
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(findViewById(R.id.nav_view))){
            drawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_kbcx:
                KBCXActivity.getInstance(IndexActivity.this);
                break;

            case R.id.nav_cjcx:
                CJCXActivity.getInstance(IndexActivity.this);
                break;

            case R.id.nav_setting:
                showSystemSettingDialog();
                break;

            case R.id.nav_send_message:
                showSendMessageDialog();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //退出程序
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_exit){
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
        return true;
    }

    //toolbar上的menu实例化
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }

    //系统设置选项弹框
    private void showSystemSettingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("并没有系统设置233333");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    //给我留言选项弹框
    private void showSendMessageDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.dialog_layout));
        final EditText editText = (EditText) view.findViewById(R.id.dialog_edt);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("给我留言");
        builder.setView(view, 20, 20, 20, 20);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveMessage(editText);
            }
        });
        builder.show();
    }

    //保存留言内容到bmob
    private void saveMessage(EditText editText){
        if (editText.getText().toString().equals("")){
            Toast.makeText(this, "请先输入", Toast.LENGTH_SHORT).show();
        }else {
            MessageModel messageModel = new MessageModel();
            messageModel.setXsxm(HttpUtil.getXSXM());
            messageModel.setXsxh(HttpUtil.getXSXH());
            messageModel.setMessage(editText.getText().toString());
            messageModel.save(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(IndexActivity.this, "留言成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(IndexActivity.this, "留言失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //初始化数据
    private void initDatas(){
        HttpUtil.getCourse();
        HttpUtil.saveCourse(this, new Utility.SaveCourseCallback() {

            @Override
            public void saveCourseSuccess() {
                handler.sendEmptyMessage(Utility.SAVE_COURSE_SUCCESS);
            }
        });
        HttpUtil.downGradeViewstate();
        HttpUtil.getGradeViewstate(this);
        HttpUtil.getGrade(this);
        HttpUtil.saveGrade(this, new Utility.SaveGradeCallback() {
            @Override
            public void saveGradeSuccess() {
                handler.sendEmptyMessage(Utility.SAVE_GRADE_SUCCESS);
            }
        });
    }

}
