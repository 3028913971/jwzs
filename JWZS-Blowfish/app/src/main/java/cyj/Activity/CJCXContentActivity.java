package cyj.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import Database.DB;
import Util.LogUtil;


public class CJCXContentActivity extends AppCompatActivity {

    private static int position;
    private TextView xn, xq, kcdm, kcmc, kcxz, kcgs, xf, jd, cj, bkcj, cxcj, kkxy;
    private String TAG = "HttpUtil";

    public static void getInstance(Context context, int position){
        CJCXContentActivity.position = position;
        Intent intent = new Intent(context, CJCXContentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cjcx_item_content);
        findControl();
        getGrade();
    }

    private void getGrade(){
        Cursor cursor = DB.getInstance(this)
                .queryGrade("select * from Grade where id=?",
                        new String[]{position + ""});
        if (cursor.moveToFirst()){
            xn.setText(cursor.getString(cursor.getColumnIndex("xn")));
            xq.setText(cursor.getString(cursor.getColumnIndex("xq")));
            kcdm.setText(cursor.getString(cursor.getColumnIndex("kcdm")));
            kcmc.setText(cursor.getString(cursor.getColumnIndex("kcmc")));
            kcxz.setText(cursor.getString(cursor.getColumnIndex("kcxz")));
            kcgs.setText(cursor.getString(cursor.getColumnIndex("kcgs")));
            xf.setText(cursor.getString(cursor.getColumnIndex("xf")));
            jd.setText(cursor.getString(cursor.getColumnIndex("jd")));
            cj.setText(cursor.getString(cursor.getColumnIndex("cj")));
            bkcj.setText(cursor.getString(cursor.getColumnIndex("bkcj")));
            cxcj.setText(cursor.getString(cursor.getColumnIndex("cxcj")));
            kkxy.setText(cursor.getString(cursor.getColumnIndex("kkxy")));
        }
    }

    private void findControl(){
        xn = (TextView)findViewById(R.id.cjcx_content_xn);
        xq = (TextView)findViewById(R.id.cjcx_content_xq);
        kcdm = (TextView)findViewById(R.id.cjcx_content_kcdm);
        kcmc = (TextView)findViewById(R.id.cjcx_content_kcmc);
        kcxz = (TextView)findViewById(R.id.cjcx_content_kcxz);
        kcgs = (TextView)findViewById(R.id.cjcx_content_kcgs);
        xf = (TextView)findViewById(R.id.cjcx_content_xf);
        jd = (TextView)findViewById(R.id.cjcx_content_jd);
        cj = (TextView)findViewById(R.id.cjcx_content_cj);
        bkcj = (TextView)findViewById(R.id.cjcx_content_bkcj);
        cxcj = (TextView)findViewById(R.id.cjcx_content_cxcj);
        kkxy = (TextView)findViewById(R.id.cjcx_content_kkxy);
    }

}
