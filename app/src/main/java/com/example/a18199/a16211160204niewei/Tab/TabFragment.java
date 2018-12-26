package com.example.a18199.a16211160204niewei.Tab;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a18199.a16211160204niewei.Activity.MainActivity;
import com.example.a18199.a16211160204niewei.News.DatabaseNews;
import com.example.a18199.a16211160204niewei.News.NewsThread;
import com.example.a18199.a16211160204niewei.News.RecyclerViewAdapter;
import com.example.a18199.a16211160204niewei.R;
import com.example.a18199.a16211160204niewei.Utils.NetworkUtils;
import com.example.a18199.a16211160204niewei.Utils.ToastUtil;
import com.example.a18199.a16211160204niewei.other.ScrollAwareFABBehavior;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;


import java.util.Collection;
import java.util.List;


public class TabFragment extends Fragment {
    private RecyclerView recyclerView;
    private String mTitle;
    private Context mContext;
    private int page = 1;
    private List<DatabaseNews> list_news;
    private RecyclerViewAdapter adapter;
    private RefreshLayout mRefreshLayout;
    private View rootView;
    private static String TAG = "TABFRAGMENT1";

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (getArguments() != null) {
            mTitle = getArguments().getString("title");
        }
    }

    public static TabFragment newInstance(String title) {
        TabFragment f = new TabFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.recycler_layout, container, false);
        }
        return rootView;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.listView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int orientation = LinearLayoutManager.VERTICAL;
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, orientation, false));
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        if (mRefreshLayout == null) {
            mRefreshLayout = view.findViewById(R.id.refreshLayout);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
                mRefreshLayout.autoRefresh();
            }
        });
        list_news = getAll(mTitle);

        if (list_news.size() == 0) {
            mRefreshLayout.autoRefresh();
        }

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

    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        rootView = null;
        super.onDestroy();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    List<DatabaseNews> list_get = (List<DatabaseNews>) msg.getData().getSerializable("list");
                    if (list_get.size() != 0) {
                        if (list_get.get(0).getChannelId().equals(list_news.get(0).getChannelId())) {
                            mRefreshLayout.finishRefresh(false);
                            ToastUtil.showShortString(getActivity(), "无可用更新！等等再刷新吧！");
                            break;
                        }
                        if (page == 1) {
                            list_news.clear();
                        }
                        list_news.addAll((List<DatabaseNews>) msg.getData().getSerializable("list"));
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadMore();
                        refresh();
                    } else {
                        ToastUtil.showShortString(getActivity(), "刷新失败");
                    }
                    break;
            }
            return false;
        }
    });

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    private void LoadMoreData(int i) {
        if (NetworkUtils.isNetWorkAvailable(getContext())) {
            if (i == 1) {
                page = 1;
            } else {
                page++;
            }
            if (mTitle != null) {
                NewsThread newsThread = new NewsThread(handler, String.valueOf(page), "", mTitle);
                newsThread.start();
            }
        } else {
            Toast.makeText(getContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    public List<DatabaseNews> getAll(String title) {
        if (title != null) {
            return LitePal.where("channelName = ?", title).find(DatabaseNews.class);
        }

        return list_news;
    }
}
