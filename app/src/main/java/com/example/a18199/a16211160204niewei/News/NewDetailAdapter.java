package com.example.a18199.a16211160204niewei.News;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a18199.a16211160204niewei.Activity.WebViewActivity;
import com.example.a18199.a16211160204niewei.R;

import java.util.List;

public class NewDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsDetail> list;
    private Context context;

    public NewDetailAdapter(Context context, List<NewsDetail> list) {
        super();
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(context).inflate(R.layout.item_no_pic, null);
        ViewHolder viewHolder = new ViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).title.setText(list.get(position).getTitle());
        String date = list.get(position).getDate();
        String source = list.get(position).getSource();
        ((ViewHolder) holder).res.setText(String.valueOf(source + "    " + date));
        final String id = list.get(position).getChannid();
        ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("id", id);
                ((Activity) context).startActivityForResult(intent, 200);
                ((Activity) context).overridePendingTransition(R.anim.anim, R.anim.in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView res;

        public ViewHolder(View root) {
            super(root);
            title = root.findViewById(R.id.textView_no_title);
            res = root.findViewById(R.id.textView_no_res);
        }
    }
}
