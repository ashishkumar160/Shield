package com.iohertz.ashish.shield.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iohertz.ashish.shield.Model.Log;
import com.iohertz.ashish.shield.R;

import java.util.List;

/**
 * Created by ashish on 6/3/17.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private Context mContext;
    private List<Log> logList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView log_image;
        public TextView log_text;
        public TextView log_time;

        public ViewHolder(View view) {
            super(view);
            log_image = (ImageView) view.findViewById(R.id.log_image);
            log_text = (TextView) view.findViewById(R.id.log_textview);
            log_time = (TextView) view.findViewById(R.id.log_textview_time);
        }
    }

    public LogAdapter(Context mContext,List<Log> logList) {
        this.mContext = mContext;
        this.logList = logList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log log = logList.get(position);
        holder.log_text.setText(log.getLog());
        holder.log_time.setText(log.getLogtime());
        Glide.with(mContext).load(log.getThumbnail()).into(holder.log_image);
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }
}
