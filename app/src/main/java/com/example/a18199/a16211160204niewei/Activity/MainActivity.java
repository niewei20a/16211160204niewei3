package com.example.a18199.a16211160204niewei.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.a18199.a16211160204niewei.R;
import com.example.a18199.a16211160204niewei.Tab.FragmentAdapter;
import com.example.a18199.a16211160204niewei.Tab.TabFragment;
import com.example.a18199.a16211160204niewei.Utils.SPUtils;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.support.design.widget.TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        Resources res = getResources();
        String[] city = res.getStringArray(R.array.Channel);

        List<TabFragment> list_tab = new ArrayList<>(city.length);
        List<String> list_title = new ArrayList<>(city.length);
        for (int i = 0; i < city.length; i++) {
            String city_array = city[i];
            tabLayout.addTab(tabLayout.newTab().setText(city_array));
            list_tab.add(TabFragment.newInstance(city_array));
            list_title.add(city_array);
        }
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), list_tab, list_title);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-event-name"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if (message.equals("recreate")) {
                Log.d("receivermessage", "Got message: 接受了" + message);
                recreate();
            }
        }
    };

    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public void init() {

        String font_size = SPUtils.getData("font", "");
        switch (font_size) {
            case "medium":
                setTheme(R.style.Default_TextSize_Middle);
                break;
            case "small":
                setTheme(R.style.Default_TextSize_Small);
                break;
            case "large":
                setTheme(R.style.Default_TextSize_Big);
                break;
            default:
                setTheme(R.style.Default_TextSize_Middle);
                break;
        }
        String day_night = SPUtils.getData("theme", "");
        if (day_night.equals("day") || day_night.equals("")) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == R.id.ab_search) {
            startActivity(new Intent(MainActivity.this, SearchResultActivity.class));
            overridePendingTransition(R.anim.anim, R.anim.in);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.serach, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            overridePendingTransition(R.anim.anim, R.anim.in);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
