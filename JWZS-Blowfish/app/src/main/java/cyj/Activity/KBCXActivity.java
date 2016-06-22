package cyj.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import Fragment.KBCXViewPagerAdapter;
import Fragment.KBCXViewPagerFragment;
import Util.HttpUtil;
import Util.LogUtil;
import Util.Utility;


//课表查询页面
public class KBCXActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private KBCXViewPagerAdapter kbcxViewPagerAdapter;
    private KBCXViewPagerFragment fragment_mo, fragment_tu, fragment_we,
            fragment_th, fragment_fr, fragment_sa, fragment_su;
    private ViewPager viewPager;
    private String TAG = "HttpUtil";
    private Dialog dialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Utility.UPDATE_COURSE_SUCCESS:
                    dialog.dismiss();
                    getInstance(KBCXActivity.this);
                    KBCXActivity.this.finish();
                    break;
            }
        }
    };

    //获取当前实例
    public static void getInstance(Context context){
        Intent intent = new Intent(context, KBCXActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kbcx_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("课表查询");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(this);

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        kbcxViewPagerAdapter = new KBCXViewPagerAdapter(getSupportFragmentManager());

        addFragment();

        viewPager.setAdapter(kbcxViewPagerAdapter);

        viewPager.setCurrentItem(Utility.getDayOfWeek());

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    //menu按钮监听
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_kbcx_update){
            dialog = Utility.showDialog(KBCXActivity.this, "请稍后...");
            updateDialog(new Utility.UpdateCourseCallback() {
                @Override
                public void updateCourseSuccess() {
                    handler.sendEmptyMessage(Utility.UPDATE_COURSE_SUCCESS);
                }
            });
        }
        return true;
    }

    //初始化Toolbar上的按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_kbcx, menu);
        return true;
    }

    //添加Fragment到viewpager
    private void addFragment(){
        fragment_mo = KBCXViewPagerFragment.getInstance(1);
        kbcxViewPagerAdapter.addFragment(fragment_mo, "周一");

        fragment_tu = KBCXViewPagerFragment.getInstance(2);
        kbcxViewPagerAdapter.addFragment(fragment_tu, "周二");

        fragment_we = KBCXViewPagerFragment.getInstance(3);
        kbcxViewPagerAdapter.addFragment(fragment_we, "周三");

        fragment_th = KBCXViewPagerFragment.getInstance(4);
        kbcxViewPagerAdapter.addFragment(fragment_th, "周四");

        fragment_fr = KBCXViewPagerFragment.getInstance(5);
        kbcxViewPagerAdapter.addFragment(fragment_fr, "周五");

        fragment_sa = KBCXViewPagerFragment.getInstance(6);
        kbcxViewPagerAdapter.addFragment(fragment_sa, "周六");

        fragment_su = KBCXViewPagerFragment.getInstance(7);
        kbcxViewPagerAdapter.addFragment(fragment_su, "周日");
    }

    //更新课程操作
    private void updateDialog(final Utility.UpdateCourseCallback callBack){
        HttpUtil.getCourse();
        HttpUtil.updateCourse(KBCXActivity.this, callBack);
    }
}
