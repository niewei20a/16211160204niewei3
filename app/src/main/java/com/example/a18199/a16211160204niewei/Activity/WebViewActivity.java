package com.example.a18199.a16211160204niewei.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.a18199.a16211160204niewei.News.NewsDetail;
import com.example.a18199.a16211160204niewei.News.ThreadGetNewContent;
import com.example.a18199.a16211160204niewei.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class WebViewActivity extends AppCompatActivity {
    private Handler handler;
    private WebView webView;
    private String id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        webView = findViewById(R.id.webview);

        Connector.getDatabase();
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings = webView.getSettings();
        //主要是这句
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        List<NewsDetail> newsList= LitePal.where("channid = ?", id).find(NewsDetail.class);
                        if(newsList.size()==0){

                        }else {
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
        Log.d("new web", "onCreate:res " + id);
        if (id != null && id != "") {
            ThreadGetNewContent thread = new ThreadGetNewContent(id, handler);
            thread.start();
        } else {
            Toast.makeText(WebViewActivity.this, "---" + id + "---" + id, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(WebViewActivity.this, "---" + requestCode + "---" + resultCode, Toast.LENGTH_LONG).show();
        if (requestCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    Intent intent = getIntent();
                    Bundle bundle = intent.getBundleExtra("BUNDLE_WEATHER_INFO");
                    String msg = bundle.getString("id");
                    ThreadGetNewContent thread = new ThreadGetNewContent(msg, handler);
                    thread.start();
                    Log.d("new web", "onCreate:res " + msg);
                    break;
                default:
                    break;
            }
        }
    }

    private void load(NewsDetail newsDetail) {
        String content  = newsDetail.getContent();
        String date = newsDetail.getDate();
        String source = newsDetail.getSource();
        String tilte=newsDetail.getTitle();
        String titleHtml = "<h1 class=" + "main_title" + ">" + tilte + " </h1>";
        String style = "<style type=" + "text/css" + ">.main_title{text-align:center}</style>";
        String html = "<html><header>" + style + " </header>" + titleHtml + "" + content + "</body></html>";
        Log.d("HTML", "load: " + html);
        html = getNewContent(html);
        webView.loadData(html, "text/html", "uft-8");
    }

    public static String getNewContent(String htmltext) {
        try {
            Document doc = Jsoup.parse(htmltext);
            Log.d("HTML", "load: " + doc);
            doc.getElementsByAttributeValue("src", "http://static.ws.126.net/cnews/css13/img/end_news.png").remove();
            Elements p = doc.getElementsByTag("p");
            p.get(0).remove();
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