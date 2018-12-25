package com.example.a18199.a16211160204niewei.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.a18199.a16211160204niewei.News.NewDetailAdapter;
import com.example.a18199.a16211160204niewei.News.NewsDetail;
import com.example.a18199.a16211160204niewei.News.RecyclerViewAdapter;
import com.example.a18199.a16211160204niewei.News.ThreadGetNewContent;
import com.example.a18199.a16211160204niewei.R;
import com.example.a18199.a16211160204niewei.Tab.SettingFragment;
import com.example.a18199.a16211160204niewei.Tab.TabFragment;
import com.example.a18199.a16211160204niewei.Utils.SPUtils;
import com.example.a18199.a16211160204niewei.Utils.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RefreshLayout mRefreshLayout;
    private List<NewsDetail> list;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_download);
        init();

        recyclerView = findViewById(R.id.Download_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRefreshLayout = findViewById(R.id.Download_refreshLayout);
        list = LitePal.findAll(NewsDetail.class);

        NewDetailAdapter adapter = new NewDetailAdapter(this, list);
        Toolbar toolbar = findViewById(R.id.Download_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("离线阅读");

        Button button = findViewById(R.id.Download_button);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    list = LitePal.findAll(NewsDetail.class);
                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() >= 100) {

                    ToastUtil.showShortString(DownloadActivity.this, "离线新闻已下载");
                } else {
                    ThreadGetNewContent td = new ThreadGetNewContent("", handler, true);
                    td.start();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            case "big":
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
}
