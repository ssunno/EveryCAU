package univ.cau.ssunno.everycau;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import univ.cau.ssunno.everycau.meals.MealsFragment;
import univ.cau.ssunno.everycau.schedule.ScheduleFragment;

/*
*  메인 탭 액티비티
*  내부 프래그먼트 관리
*  뷰페이저
* */
public class MainTabActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TextView toolbarTitle;
    private ViewPager mViewPager;
    private ArrayList<ImageView> navigationCircleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up the TabLayout
        initTabLayout();

        // Set up the Navigation circle
        initNaviCircle();

        toolbarTitle = (TextView)findViewById(R.id.toolbar_title);
        toolbarTitle.setText(mSectionsPagerAdapter.getPageTitle(0));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<BaseTabFragment> fragments = null;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(new MealsFragment());
            fragments.add(new ScheduleFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getFragmentName();
        }
    }

    private void initTabLayout(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setVisibility(View.GONE);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                toolbarTitle.setText(tab.getText());
                navigationCircleList.get(tab.getPosition()).setImageResource(R.drawable.navigation_selected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                navigationCircleList.get(tab.getPosition()).setImageResource(R.drawable.navigation_default);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initNaviCircle() {
        LinearLayout toolbarNavigationLayout = (LinearLayout)findViewById(R.id.toolbar_navigation_layout);
        navigationCircleList = new ArrayList<>();
        for ( int i = 0 ; i < mSectionsPagerAdapter.getCount() ; i ++ ) {
            ImageView navigationCircle = generateNaviCircle();
            navigationCircleList.add(navigationCircle);
            toolbarNavigationLayout.addView(navigationCircle);
        }
        navigationCircleList.get(0).setImageResource(R.drawable.navigation_selected);
    }

    private ImageView generateNaviCircle() {
        ImageView circle = new ImageView(getApplicationContext());
        circle.setImageResource(R.drawable.navigation_default);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int padding = (int)metrics.density * 2;
        circle.setPadding(padding, padding * 2, padding, padding);
        return circle;
    }
}
