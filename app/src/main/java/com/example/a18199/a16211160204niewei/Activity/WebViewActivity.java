package com.example.a18199.a16211160204niewei.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.a18199.a16211160204niewei.News.NewsDetail;
import com.example.a18199.a16211160204niewei.News.ThreadGetNewContent;
import com.example.a18199.a16211160204niewei.R;
import com.example.a18199.a16211160204niewei.Utils.SPUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.view.View.SCROLLBARS_OUTSIDE_OVERLAY;

public class WebViewActivity extends AppCompatActivity {
    private Handler handler;
    private WebView webView;
    private String id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        webView = findViewById(R.id.webview);
        init();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        List<NewsDetail> newsList = LitePal.where("channid = ?", id).find(NewsDetail.class);
                        if (newsList.size() == 0) {

                        } else {
                            NewsDetail newsDetail = newsList.get(0);
                            load(newsDetail);
                        }
                        break;
                }
                return false;
            }
        });
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id != null && !id.equals("")) {
            ThreadGetNewContent thread = new ThreadGetNewContent(id, handler, false);
            thread.start();
        } else {
            Toast.makeText(WebViewActivity.this, "---" + id + "---" + id, Toast.LENGTH_LONG).show();
        }
    }
    protected void onDestroy() {
        webView.removeAllViews();
        webView.destroy();
        webView=null;
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
    public void init() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setBlockNetworkImage(false);//解除数据阻止
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);//加载图片
        webSettings.setDomStorageEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);//设置渲染优先级
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                webView.setEnabled(false);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webView.setEnabled(true);
                super.onPageStarted(view, url, favicon);
            }
        });
        if (SPUtils.getData("font", "").equals("large")) {
            webSettings.setTextSize(WebSettings.TextSize.LARGER);
        } else if (SPUtils.getData("font", "").equals("small")) {
            webSettings.setTextSize(WebSettings.TextSize.SMALLER);
        } else {
            webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        }
    }

    private void load(NewsDetail newsDetail) {
        String content = newsDetail.getContent();

        String date = newsDetail.getDate();
        String source = newsDetail.getSource();
        String tilte = newsDetail.getTitle();
        String titleHtml = "<h2 class=\"main_title\">" + tilte + " </h2>";
        String style = "<style type=\"text/css\">.main_title{font-weight:bold;}</style>";
        String source_html = "<p>" + source + "  " + date + "</p>";
        if (SPUtils.getData("theme", "").equals("night")) {
            //webView.loadUrl("javascript:load_night()");
            style += loadJs();
        } else {
            //webView.loadUrl("javascript:load_day()");
        }
        String html = "<html><header>" + style + " </header><body class=\"night\">" + titleHtml + source_html + content + "</body></html>";
        html = getNewContent(html);
        Log.d("HTML", "load: " + html);
        //webView.loadData(html,"text/html; charset=UTF-8", null);
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    public String loadJs() {
        InputStream mIs;
        String wholeJS = null;
        try {
            mIs = getResources().getAssets().open("night.css");
            if (mIs != null) {
                byte buff[] = new byte[1024];
                ByteArrayOutputStream fromFile = new ByteArrayOutputStream();
                do {
                    int numread;
                    numread = mIs.read(buff);
                    if (numread <= 0) {
                        break;
                    }
                    fromFile.write(buff, 0, numread);
                } while (true);
                wholeJS = fromFile.toString();
            } else {
                Toast.makeText(WebViewActivity.this, "js加载失败", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wholeJS;
    }

    public static String getNewContent(String htmltext) {
        try {
            Document doc = Jsoup.parse(htmltext);
            doc.getElementsByAttributeValue("src", "http://static.ws.126.net/cnews/css13/img/end_news.png").remove();
            doc.getElementsByAttributeValue("src", "http://www.chinanews.com/fileftp/2018/12/2018-12-17/U194P4T47D43466F980DT20181217094708.jpg").remove();
            if (SPUtils.getData("picture", "have").equals("no")) {
                doc.getElementsByTag("img").remove();
            }
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%").attr("height", "auto");
            }
            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }
}
