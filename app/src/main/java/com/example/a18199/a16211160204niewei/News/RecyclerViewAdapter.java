package com.example.a18199.a16211160204niewei.News;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a18199.a16211160204niewei.R;
import com.example.a18199.a16211160204niewei.Activity.WebViewActivity;
import com.example.a18199.a16211160204niewei.Utils.SPUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DatabaseNews> list;
    private Context context;

    public enum Item_Type {
        RECYCLEVIEW_ITEM_TYPE_1,
        RECYCLEVIEW_ITEM_TYPE_2,
        RECYCLEVIEW_ITEM_TYPE_3
    }

    public RecyclerViewAdapter(Context context, List<DatabaseNews> list) {
        super();
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == Item_Type.RECYCLEVIEW_ITEM_TYPE_1.ordinal()) {
            View mView = LayoutInflater.from(context).inflate(R.layout.item_no_pic, null);
            ViewHolderA viewHolder = new ViewHolderA(mView);
            return viewHolder;
        } else {
            View mView = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
            ViewHolderB viewHolder = new ViewHolderB(mView);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final String id = list.get(position).getChannelId();
        if (holder instanceof ViewHolderA) {
            ((ViewHolderA) holder).title.setText(list.get(position).getTitle());
            String date = list.get(position).getDate();
            String source = list.get(position).getSource();
            ((ViewHolderA) holder).res.setText(String.valueOf(source + "    " + date));
            ((ViewHolderA) holder).itemView.setTag(list.get(position).getLink());
            ((ViewHolderA) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.alpha_out, R.anim.alpha_in);
                }
            });
        } else {
            ((ViewHolderB) holder).title.setText(list.get(position).getTitle());
            String date = list.get(position).getDate();
            String source = list.get(position).getSource();
            ((ViewHolderB) holder).res.setText(String.valueOf(source + "    " + date));
            Uri url = Uri.parse(list.get(position).getImageurls());
            int width = 130, height = 100;
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(url)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setOldController(((ViewHolderB) holder).iv.getController()).setImageRequest(request)
                    .build();
            ((ViewHolderB) holder).iv.setController(controller);
            ((ViewHolderB) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.alpha_out, R.anim.alpha_in);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public int getItemViewType(int position) {
        String havepic = SPUtils.getData("picture", "");
        if (havepic.equals("no")) {
            return Item_Type.RECYCLEVIEW_ITEM_TYPE_1.ordinal();
        }
        if (list.get(position).isHavePic()) {
            if (list.get(position).getImageurls().equals("http://static.ws.126.net/cnews/css13/img/end_news.png")) {
                return Item_Type.RECYCLEVIEW_ITEM_TYPE_1.ordinal();
            } else {
                return Item_Type.RECYCLEVIEW_ITEM_TYPE_2.ordinal();
            }
        } else {
            return Item_Type.RECYCLEVIEW_ITEM_TYPE_1.ordinal();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderA extends RecyclerView.ViewHolder {
        TextView title;
        TextView res;

        public ViewHolderA(View root) {
            super(root);
            title = root.findViewById(R.id.textView_no_title);
            res = root.findViewById(R.id.textView_no_res);
        }
    }

    class ViewHolderB extends RecyclerView.ViewHolder {
        TextView title;
        TextView res;
        SimpleDraweeView iv;

        public ViewHolderB(View root) {
            super(root);
            title = root.findViewById(R.id.textView_title);
            res = root.findViewById(R.id.textView_res);
            iv = root.findViewById(R.id.imageView_pic);
        }
    }
}
