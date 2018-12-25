package com.example.a18199.a16211160204niewei.News;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.show.api.ShowApiRequest;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ThreadGetNewContent extends Thread {
    private String id;
    private Handler handler;
    private Boolean isAll;

    public ThreadGetNewContent(String id, Handler handler, Boolean isAll) {
        super();
        this.id = id;
        this.handler = handler;
        this.isAll = isAll;
    }

    @Override
    public void run() {
        List<NewsDetail> newsList = LitePal.where("channid = ?", id).find(NewsDetail.class);
        if (newsList.size() == 0 || isAll) {
            String res;
            if (isAll) {
                res = new ShowApiRequest("http://route.showapi.com/109-35", "83588", "b81ffec323a743aa80bcd3eb3dfcce9d")
                        .addTextPara("channelId", "")
                        .addTextPara("channelName", "")
                        .addTextPara("title", "")
                        .addTextPara("page", "1")
                        .addTextPara("needContent", "0")
                        .addTextPara("needHtml", "1")
                        .addTextPara("needAllList", "0")
                        .addTextPara("maxResult", "100")
                        .addTextPara("id", "")
                        .post();
            } else {
                res = new ShowApiRequest("http://route.showapi.com/109-35", "83588", "b81ffec323a743aa80bcd3eb3dfcce9d")
                        .addTextPara("channelId", "")
                        .addTextPara("channelName", "")
                        .addTextPara("title", "")
                        .addTextPara("page", "1")
                        .addTextPara("needContent", "0")
                        .addTextPara("needHtml", "1")
                        .addTextPara("needAllList", "0")
                        .addTextPara("maxResult", "1")
                        .addTextPara("id", id)
                        .post();
            }
            Log.d("GETALLNEWS", "run: "+isAll);
            try {
                JSONObject jsonObject = JSON.parseObject(res);
                if (jsonObject.getInteger("showapi_res_code") != 0) {
                } else {
                    JSONObject js = jsonObject.getJSONObject("showapi_res_body");
                    JSONObject json = js.getJSONObject("pagebean");
                    JSONArray jsonlist = json.getJSONArray("contentlist");
                    for (int i = 0; i < jsonlist.size(); i++) {
                        JSONObject j = (JSONObject) jsonlist.get(i);
                        NewsDetail nd = new NewsDetail();
                        nd.setContent(j.getString("html"));
                        nd.setChannid(j.getString("id"));
                        Date date = j.getDate("pubDate");
                        String da = new SimpleDateFormat("MM-dd hh:mm:ss").format(date);
                        nd.setDate(da);
                        nd.setSource(j.getString("source"));
                        nd.setTitle(j.getString("title"));
                        if (nd.save()) {
                            Log.d("SAVE", "存储成功" + nd);
                        } else {
                            Log.d("SAVE", "存储失败" + nd);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Message message = handler.obtainMessage();//获取message
        Message message = new Message();
        message.what = 1;
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("html", html);
//        message.setData(bundle);
        handler.sendMessage(message);//发送message
    }
}
