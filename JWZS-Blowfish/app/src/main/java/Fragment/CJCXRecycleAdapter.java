package Fragment;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import Database.DB;
import Model.GradeModel;
import Util.HttpUtil;
import Util.Utility;
import cyj.Activity.R;

//成绩查询的RecycleAdapter类
public class CJCXRecycleAdapter extends RecyclerView.Adapter<CJCXRecycleAdapter.MyViewHolder>{

    private Context context;
    private Utility.RecycleViewOnItemClick listener;

    //存放成绩实例
    private List<GradeModel> datas;

    public CJCXRecycleAdapter(Context context) {
        this.context = context;
        initData();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.cjcx_item, parent, false), listener
        );
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.cjcx_kcmc.setText(datas.get(position).getKcmc());
        holder.cjcx_xf.setText(datas.get(position).getXf());
        holder.cjcx_jd.setText(datas.get(position).getJd());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private void initData(){
        datas = new ArrayList<>();
        Cursor cursor = DB.getInstance(context)
                .query("select * from Grade where number=?", new String[]{HttpUtil.getXSXH()});
        if (cursor.moveToFirst()){
            do {
                GradeModel gradeModel = new GradeModel();
                gradeModel.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
                gradeModel.setKcmc(cursor.getString(cursor.getColumnIndex("kcmc")));
                gradeModel.setXf(cursor.getString(cursor.getColumnIndex("xf")));
                gradeModel.setJd(cursor.getString(cursor.getColumnIndex("jd")));
                datas.add(gradeModel);
            }while (cursor.moveToNext());
        }
    }

    public int getId(int position){
        return datas.get(position).getId();
    }

    public void setOnClick(Utility.RecycleViewOnItemClick listener){
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //课程名称、学分、绩点
        private TextView cjcx_kcmc, cjcx_xf, cjcx_jd;
        private Utility.RecycleViewOnItemClick listener;

        public MyViewHolder(View itemView, Utility.RecycleViewOnItemClick listener) {
            super(itemView);
            cjcx_kcmc = (TextView)itemView.findViewById(R.id.cjcx_kcmc_info);
            cjcx_xf = (TextView)itemView.findViewById(R.id.cjcx_xf_info);
            cjcx_jd = (TextView)itemView.findViewById(R.id.cjcx_jd_info);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v, getPosition());
            }
        }
    }
}
