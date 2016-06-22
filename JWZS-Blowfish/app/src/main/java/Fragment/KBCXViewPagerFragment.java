package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Util.LogUtil;
import cyj.Activity.R;

//课表查询Fragment类
public class KBCXViewPagerFragment extends Fragment {

    private RecyclerView recyclerView;
    private KBCXRecycleAdapter kbcxRecycleAdapter;
    private View view;
    private int day;

    //获取Fragment实例
    public static KBCXViewPagerFragment getInstance(int day){
        Bundle bundle = new Bundle();
        bundle.putInt("day", day);
        KBCXViewPagerFragment kbcxViewPagerFragment = new KBCXViewPagerFragment();
        kbcxViewPagerFragment.setArguments(bundle);
        return kbcxViewPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        day = getArguments().getInt("day");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.kbcx_recycleview,
                    container, false);
            recyclerView = (RecyclerView)view.findViewById(R.id.kbcx_recycle_view);
            kbcxRecycleAdapter = new KBCXRecycleAdapter(getActivity(), day);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(kbcxRecycleAdapter);
        }
        return view;
    }

}
