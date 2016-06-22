package cyj.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import Fragment.CJCXRecycleAdapter;
import Util.HttpUtil;
import Util.LogUtil;
import Util.Utility;

//成绩查询页面
public class CJCXActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CJCXRecycleAdapter cjcxRecycleAdapter;
    private String TAG = "HttpUtil";
    private Dialog dialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Utility.SAVE_GRADE_SUCCESS:
                    dialog.dismiss();
                    getInstance(CJCXActivity.this);
                    CJCXActivity.this.finish();
                    break;
            }
        }
    };

    public static void getInstance(Context context){
        Intent intent = new Intent(context, CJCXActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjcx_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("成绩查询");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.cjcx_recycle_view);
        cjcxRecycleAdapter = new CJCXRecycleAdapter(this);
        cjcxRecycleAdapter.setOnClick(new Utility.RecycleViewOnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                CJCXContentActivity.getInstance(CJCXActivity.this, cjcxRecycleAdapter.getId(position));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cjcxRecycleAdapter);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_cjcx_update){
            dialog = Utility.showDialog(CJCXActivity.this, "请稍后...");
            updateDialog(new Utility.SaveGradeCallback() {
                @Override
                public void saveGradeSuccess() {
                    handler.sendEmptyMessage(Utility.SAVE_GRADE_SUCCESS);
                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_cjcx, menu);
        return true;
    }

    //更新成绩操作
    private void updateDialog(final Utility.SaveGradeCallback callBack){
        HttpUtil.deleteGrade(CJCXActivity.this);
        HttpUtil.downGradeViewstate();
        HttpUtil.getGradeViewstate(CJCXActivity.this);
        HttpUtil.getGrade(CJCXActivity.this);
        HttpUtil.saveGrade(CJCXActivity.this, callBack);
    }
}
