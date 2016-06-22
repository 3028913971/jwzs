package Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import Util.LogUtil;

//课表查询ViewPagerAdapter类
public class KBCXViewPagerAdapter extends FragmentPagerAdapter {

    //存放viewpager fragment
    private List<Fragment> fragmentList = new ArrayList<>();

    //存放viewpager标题
    private List<String> titleList = new ArrayList<>();

    public KBCXViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

}
