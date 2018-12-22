package com.example.a18199.a16211160204niewei.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;


import com.example.a18199.a16211160204niewei.News.DatabaseNews;
import com.example.a18199.a16211160204niewei.News.NewsThread;
import com.example.a18199.a16211160204niewei.News.RecyclerViewAdapter;
import com.example.a18199.a16211160204niewei.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class SearchResultActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private int page;
    private List<DatabaseNews> list_news;
    private RefreshLayout mRefreshLayout;
    private String title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        setTheme(R.style.Default_TextSize_Big);

        searchView = findViewById(R.id.search_news);
        recyclerView = findViewById(R.id.listView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
                title = query;
                LoadMoreData(1, title);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(SearchResultActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this, LinearLayoutManager.VERTICAL, false));
        mRefreshLayout = findViewById(R.id.refreshLayout);
        list_news = new ArrayList<>();
        adapter = new RecyclerViewAdapter(SearchResultActivity.this, list_news);
        recyclerView.setAdapter(adapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (title != null && list_news.size() != 0) {
                    LoadMoreData(1, title);
                }
                mRefreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (title != null && list_news.size() != 0) {
                    LoadMoreData(2, title);
                }
                mRefreshLayout.finishLoadMore(1000);//传入false表示加载失败
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    List<DatabaseNews> list = (List<DatabaseNews>) msg.getData().getSerializable("list");
                    if (list.size() == 0) {
                        Toast.makeText(SearchResultActivity.this, "找不到相关新闻", LENGTH_LONG);
                        break;
                    }
                    if (page == 1) {
                        list_news.clear();
                    }
                    list_news.addAll(list);
                    refresh();
                    break;
            }
            return false;
        }
    });

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    private void LoadMoreData(int i, String mTitle) {
        if (i == 1) {
            page = 1;
        } else {
            page++;
        }
        if (mTitle != null) {
            NewsThread newsThread = new NewsThread(handler, String.valueOf(page), mTitle, "");
            newsThread.start();
        }
    }

    public List<DatabaseNews> getAll(String title) {
        return LitePal.where("channelName = ?", title).find(DatabaseNews.class);
    }
}
