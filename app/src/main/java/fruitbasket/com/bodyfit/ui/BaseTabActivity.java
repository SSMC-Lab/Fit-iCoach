package fruitbasket.com.bodyfit.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseTabActivity extends FragmentActivity {

    private Map<String, Fragment> mFragMap;
    private ArrayList<View> mViewList;
    private ArrayList<Fragment> mFragList;
    private PagerAdapter mPagerAdapter;
    private View mLastTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewList = new ArrayList<View>();
        mFragList = new ArrayList<Fragment>();
        mFragMap = new HashMap<String, Fragment>();

    }

    protected void setCurrentTab(int position) {
        View tab = mViewList.get(position);
        if (tab != null) {
            if (mLastTab != null) {
                mLastTab.setSelected(false);
            }
            tab.setSelected(true);
            mLastTab = tab;
            ViewPager vp = getViewPager();
            if (vp != null) {
                vp.setCurrentItem(position);
            }

        }
    }

    protected ViewPager getViewPager() {
        return null;
    }

    abstract protected TabWidget getTabWidget();

    /*abstract protected View getLeftContext();

    abstract protected View getRighttContext();*/

    protected void addTab(View tab, Fragment fragment, String str) {
        if (tab == null || fragment == null || str == null) {
            return;
        }

        ViewPager vp = getViewPager();
        if (vp != null) {
            if (vp.getAdapter() == null) {
                vp.setAdapter(mPagerAdapter);
                vp.setOnPageChangeListener(new ViewPageChangeListener());
            }
        }
        TabWidget tb = getTabWidget();
        if (tb != null) {
            tab.setTag(str);
            tb.addView(tab);
            mViewList.add(tab);
            tab.setOnClickListener(mTabClickListener);
        }
        if (mFragMap != null) {
            mFragMap.put(str, fragment);
            mFragList.add(fragment);
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    protected void addTab(View tab, Fragment fragment, String str, int index) {
        if (tab == null || fragment == null || str == null) {
            return;
        }
        TabWidget tb = getTabWidget();
        if (tb != null) {
            tb.addView(tab, index);
            tab.setOnClickListener(mTabClickListener);
        }
        if (tb != null) {
            tab.setTag(str);
            tb.addView(tab);
            mViewList.add(index, tab);
            tab.setOnClickListener(mTabClickListener);
        }
        if (mFragMap != null) {
            mFragMap.put(str, fragment);
        }
    }


    private View.OnClickListener mTabClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mLastTab != null) {
                mLastTab.setSelected(false);
            }
            String str = (String) v.getTag();
            if (str != null) {
                Fragment frag = mFragMap.get(str);
                if (frag != null) {
                    ViewPager vp = getViewPager();
                    if (vp != null) {
                        vp.setCurrentItem(mFragList.indexOf(frag), true);
                    }
                }
            }
            mLastTab = v;
            v.setSelected(true);
        }

    };

    private class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragList.get(position);
        }

        @Override
        public int getCount() {
            return mFragList.size();
        }
    }

    private class ViewPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int index) {

        }

        @Override
        public void onPageScrolled(int index, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int index) {
            setCurrentTab(index);
        }

    }

}
