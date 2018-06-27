package com.example.xiaojiasoundbox;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//public class MainActivity extends AppCompatActivity implements View.OnClickListener{
public class MainActivity extends FragmentActivity implements View.OnClickListener{
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private TextView mTextViewControlCenter;
    private TextView mTextViewSearch;

    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new DiscoverFragment());
        mFragmentList.add(new ChatFragment());
        mFragmentList.add(new DeviceFragment());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mFragmentList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(2);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText(R.string.discover);
        mTabLayout.getTabAt(1).setText(R.string.chat);
        mTabLayout.getTabAt(2).setText(R.string.device);

        mTextViewControlCenter = (TextView) findViewById(R.id.controlcenter);
        mTextViewControlCenter.setClickable(true);
        mTextViewControlCenter.setOnClickListener(this);

        mTextViewSearch = (TextView) findViewById(R.id.search);
        mTextViewSearch.setClickable(true);
        mTextViewSearch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.controlcenter:{
                Toast.makeText(MainActivity.this, R.string.reserve, Toast.LENGTH_SHORT).show();
            }
            break;

            case R.id.search:{
                Toast.makeText(MainActivity.this, R.string.reserve, Toast.LENGTH_SHORT).show();
            }
                break;

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> FragmentList) {
            super(fm);
            this.mFragmentList = FragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
