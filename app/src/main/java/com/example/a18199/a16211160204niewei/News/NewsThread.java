package com.example.a18199.a16211160204niewei.News;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.show.api.ShowApiRequest;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsThread extends Thread {
    private Handler handler;
    private String pages;
    private String tilte;
    public static final String TYPE = "CONTENT_OF_NEWS";
    private String channelName;

    public NewsThread(Handler handler, String pages, String title, String channelName) {
        this.handler = handler;
        this.pages = pages;
        this.tilte = title;
        this.channelName = channelName;
    }
    @Override
    public void run() {
        ArrayList<DatabaseNews> list = new ArrayList<>();
        String res;
        if (pages.equals("1") && (!channelName.equals(""))) {
            LitePal.deleteAll(DatabaseNews.class, "channelName = ?", channelName);
        }
        res = new ShowApiRequest("http://route.showapi.com/109-35", "83588", "b81ffec323a743aa80bcd3eb3dfcce9d")
                .addTextPara("channelId", "")
                .addTextPara("channelName", channelName)
                .addTextPara("title", tilte)
                .addTextPara("page", pages)
                .addTextPara("needContent", "0")
                .addTextPara("needHtml", "0")
                .addTextPara("needAllList", "0")
                .addTextPara("maxResult", "20")
                .addTextPara("id", "")
                .post();
        try {
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getInteger("showapi_res_code") != 0) {
            } else {
                JSONObject js = jsonObject.getJSONObject("showapi_res_body");
                JSONObject json = js.getJSONObject("pagebean");
                JSONArray jsonlist = json.getJSONArray("contentlist");
                for (int i = 0; i < jsonlist.size(); i++) {
                    JSONObject j = (JSONObject) jsonlist.get(i);
                    DatabaseNews np = new DatabaseNews();
                    Date date = j.getDate("pubDate");
                    String da = new SimpleDateFormat("MM-dd hh:mm").format(date);
                    np.setDate(da);
                    np.setHavePic(j.getBoolean("havePic"));
                    np.setLink(j.getString("link"));
                    np.setSource(j.getString("source"));
                    np.setTitle(j.getString("title"));
                    np.setChannelId(j.getString("id"));
                    np.setChannelName(channelName);
                    if (j.getBoolean("havePic")) {
                        JSONArray jl = j.getJSONArray("imageurls");
                        JSONObject jurl = (JSONObject) jl.get(0);
                        np.setImageurls(jurl.getString("url"));
                    }
                    if (tilte.equals("")) {
                        np.save();
                    }
                    list.add(np);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message message = handler.obtainMessage();//获取message
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", list);
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
