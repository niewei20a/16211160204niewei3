package com.example.a18199.a16211160204niewei.Tab;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a18199.a16211160204niewei.Activity.MainActivity;
import com.example.a18199.a16211160204niewei.News.DatabaseNews;
import com.example.a18199.a16211160204niewei.News.NewsThread;
import com.example.a18199.a16211160204niewei.News.RecyclerViewAdapter;
import com.example.a18199.a16211160204niewei.R;
import com.example.a18199.a16211160204niewei.Utils.NetworkUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;


import java.util.Collection;
import java.util.List;


public class TabFragment extends Fragment {
    private RecyclerView recyclerView;
    private String mTitle;
    private Context mContext;
    private int page;
    private List<DatabaseNews> list_news;
    private RecyclerViewAdapter adapter;
    private RefreshLayout mRefreshLayout;
    private static String TAG ="TabFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (getArguments() != null) {
            mTitle = getArguments().getString("title");
        }
        Log.d(TAG, "onCreate: Fargment "+mTitle+" 创建 ");
        page = 1;
        list_news=getAll(mTitle);
    }

    public static TabFragment newInstance(String title) {
        TabFragment f = new TabFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Fargment "+mTitle+" 创建 ");
        return inflater.inflate(R.layout.recycler_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: Fargment "+mTitle+" 创建 ");
        recyclerView = view.findViewById(R.id.listView);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        list_news = getAll(mTitle);
        if(list_news.size()==0){
            mRefreshLayout.autoRefresh();
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerViewAdapter(mContext, list_news);

        recyclerView.setAdapter(adapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                LoadMoreData(1);
                mRefreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                LoadMoreData(2);
                mRefreshLayout.finishLoadMore(1000);//传入false表示加载失败
            }
        });
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (page == 1) {
                        list_news.removeAll(list_news);
                        list_news.addAll((Collection<? extends DatabaseNews>) msg.getData().getSerializable("list"));
                    } else {
                        list_news.addAll((List<DatabaseNews>) msg.getData().getSerializable("list"));
                    }
                    break;
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadMore();
            refresh();
            return false;
        }
    });

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    private void LoadMoreData(int i) {
        if(NetworkUtils.isNetWorkAvailable(getContext())){
            if (i == 1) {
                page = 1;
            } else {
                page++;
            }
            if (mTitle != null) {
                NewsThread newsThread = new NewsThread(handler, String.valueOf(page),"", mTitle);
                newsThread.start();
            }
        }else {
            Toast.makeText(getContext(),"请检查网络连接",Toast.LENGTH_SHORT).show();
        }

    }

    public List<DatabaseNews> getAll(String title) {
        return LitePal.where("channelName = ?", title).find(DatabaseNews.class);
    }
}
