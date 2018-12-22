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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.a18199.a16211160204niewei.R;
import com.example.a18199.a16211160204niewei.Activity.WebViewActivity;
import com.facebook.drawee.view.SimpleDraweeView;

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
            ((ViewHolderA) holder).res.setText(String.valueOf("  " + source + "    " + date));
            ((ViewHolderA) holder).itemView.setTag(list.get(position).getLink());
            ((ViewHolderA) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("id", id);
                    Log.d("ID", "onBindViewHolder: " + id);
                    ((Activity) context).startActivityForResult(intent, 200);
                }
            });
        } else {
            ((ViewHolderB) holder).title.setText(list.get(position).getTitle());
            String date = list.get(position).getDate();
            String source = list.get(position).getSource();
            ((ViewHolderB) holder).res.setText(String.valueOf("  " + source + "    " + date));
            Uri url = Uri.parse(list.get(position).getImageurls());
            //loadIntoUseFitWidth(context, list.get(position).getImageurls(), R.drawable.loading, ((ViewHolderB) holder).iv);
            ((ViewHolderB) holder).iv.setImageURI(url);
            ((ViewHolderB) holder).itemView.setTag(list.get(position).getLink());
            ((ViewHolderB) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("id", id);
                    Log.d("ID", "onBindViewHolder: " + id);
                    ((Activity) context).startActivityForResult(intent, 200);
                }
            });
        }
    }

    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        if (params.height > 360) {
                            params.height = 360;
                        }
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public int getItemViewType(int position) {
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
