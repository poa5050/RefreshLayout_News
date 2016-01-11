package com.demo.flow.news;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.flow.engine.DataEngine;
import com.demo.flow.fragment.SwipeRecyclerViewFragment;
import com.demo.flow.util.Constants;
import com.demo.flow.util.ToastUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGAViewPager;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ViewAnimator.ViewAnimatorListener {

    private BGAViewPager mViewPager;

    @Bind(R.id.contentTitle)
    TextView mTitle;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    private ActionBarDrawerToggle drawerToggle;

    //@Bind(R.id.left_drawer)
    LinearLayout linearLayout;

    private ViewAnimator viewAnimator;

    private List<SlideMenuItem> list = new ArrayList<>();

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        /*
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer.setScrimColor(Color.TRANSPARENT);
        drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.drawer_open,R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(drawerToggle);
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, null, drawer, this);
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        mViewPager = getViewById(R.id.viewPager);
        mViewPager.setCurrentItem(0, false);
        DataEngine.contentActivity = getApplicationContext();
        setUpViewPager();
    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(ContentFragment.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(ContentFragment.BUILDING, R.drawable.icn_1);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(ContentFragment.BOOK, R.drawable.icn_2);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(ContentFragment.PAINT, R.drawable.icn_3);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(ContentFragment.CASE, R.drawable.icn_4);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(ContentFragment.SHOP, R.drawable.icn_5);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(ContentFragment.PARTY, R.drawable.icn_6);
        list.add(menuItem6);
        SlideMenuItem menuItem7 = new SlideMenuItem(ContentFragment.MOVIE, R.drawable.icn_7);
        list.add(menuItem7);
    }

    private void setUpViewPager() {
        mViewPager.setAllowUserScrollable(false);
        mViewPager.setAdapter(new ContentViewPagerAdapter(getSupportFragmentManager(), this));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        //tongbu(item,true);
        refershView(item.getTitle().toString());
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        return null;
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawer.closeDrawers();
    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

    private static class ContentViewPagerAdapter extends FragmentPagerAdapter {
        private Class[] mFragments = new Class[]{SwipeRecyclerViewFragment.class};
        private Context mContext;

        public ContentViewPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(mContext, mFragments[position].getName());
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //tongbu(item,false);
        item.setChecked(true);
        refershView(item.getTitle().toString());
        return super.onOptionsItemSelected(item);
    }

    private void refershView(String title){
        Constants.type = title;
        if(mTitle != null) {
            mTitle.setText(Constants.type + "新闻");
        }
        mViewPager.setCurrentItem(0, false);
        setUpViewPager();
    }

    private void tongbu(MenuItem item,boolean isNav){
        Log.e("news",item == null?"1":"2");
        int checkId = R.id.action_guonei;
        if(isNav){
            switch (item.getItemId()) {
                case R.id.item_guonei:
                    checkId = R.id.action_guonei;
                    break;
                case R.id.item_guoji:
                    checkId = R.id.action_guonei;
                    break;
                case R.id.item_gaoxiao:
                    checkId = R.id.action_gaoxiao;
                    break;
                case R.id.item_jushi:
                    checkId = R.id.action_jushi;
                    break;
                case R.id.item_keji:
                    checkId = R.id.action_keji;
                    break;
                case R.id.item_shehui:
                    checkId = R.id.action_shehui;
                    break;
                case R.id.item_tiyu:
                    checkId = R.id.action_tiyu;
                    break;
                case R.id.item_zhengfa:
                    checkId = R.id.action_zhengfa;
                    break;
                case R.id.item_yule:
                    checkId = R.id.action_yule;
                    break;
            }
        }else{
            switch (item.getItemId()) {
                case R.id.action_guonei:
                    checkId = R.id.item_guonei;
                    break;
                case R.id.action_guoji:
                    checkId = R.id.item_guonei;
                    break;
                case R.id.action_gaoxiao:
                    checkId = R.id.item_gaoxiao;
                    break;
                case R.id.action_jushi:
                    checkId = R.id.item_jushi;
                    break;
                case R.id.action_keji:
                    checkId = R.id.item_keji;
                    break;
                case R.id.action_shehui:
                    checkId = R.id.item_shehui;
                    break;
                case R.id.action_tiyu:
                    checkId = R.id.item_tiyu;
                    break;
                case R.id.action_zhengfa:
                    checkId = R.id.item_zhengfa;
                    break;
                case R.id.action_yule:
                    checkId = R.id.item_yule;
                    break;
            }
        }
        ((MenuItem)findViewById(checkId)).setChecked(true);
    }

    // 上一次按下Back键的时间
    long mLastBackTime = 0;
    // 两次Back之间的有效时间间隔，2秒
    long mBackInterval = 2 * 1000;

    // 关闭软键盘
    public void hideSoftInput() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        */

        // 关闭软键盘
        hideSoftInput();

        if((System.currentTimeMillis() - mLastBackTime) > mBackInterval){
            // 记录本次按键时间
            mLastBackTime = System.currentTimeMillis();
            // 提示
            ToastUtil.show("再按一次退出");
        } else {
            // 取消提示
            ToastUtil.cancel();
            super.onBackPressed();
        }
    }
}
