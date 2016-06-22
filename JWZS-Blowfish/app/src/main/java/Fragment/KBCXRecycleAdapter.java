package Fragment;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import Database.DB;
import Model.CourseModel;
import Util.HttpUtil;
import cyj.Activity.R;

//课表查询RecycleAdapter类
public class KBCXRecycleAdapter extends RecyclerView.Adapter<KBCXRecycleAdapter.MyViewHolder> {

    private static Context context;

    //存放课程实例
    private List<CourseModel> datas;

    private int day = 1;

    public KBCXRecycleAdapter(Context context, int day) {
        this.context = context;
        this.day = day;
        initData();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(
                LayoutInflater.from(context)
                .inflate(R.layout.kbcx_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.course_info.setText(datas.get(position).getCourseInfo());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void initData(){
        datas = new ArrayList<>();
        Cursor cursor = DB.getInstance(context)
                .query("select * from Course where number=? and col=? order by id asc", new String[]{HttpUtil.getXSXH(), day+""});
        if (cursor.moveToFirst()){
            do {
                CourseModel courseModel = new CourseModel();
                String rowspan = cursor.getString(cursor.getColumnIndex("rowspan"));
                courseModel.setRowspan(rowspan);
                courseModel.setCourseInfo(cursor.getString(cursor.getColumnIndex("courseInfo")));
                if (!rowspan.equals("0"))
                    datas.add(courseModel);
            }while (cursor.moveToNext());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        //课程信息
        private TextView course_info;

        public MyViewHolder(View itemView) {
            super(itemView);
            course_info = (TextView)itemView.findViewById(R.id.recycle_info);
        }
    }
}
