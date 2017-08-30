package com.iohertz.ashish.shield.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iohertz.ashish.shield.Model.User;
import com.iohertz.ashish.shield.R;
import com.iohertz.ashish.shield.View.other.CircleTransform;

import java.util.List;

/**
 * Created by ashish on 6/3/17.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context mContext;
    private List<User> usersList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.user_name);
            thumbnail = (ImageView) view.findViewById(R.id.user_thumbnail);
        }
    }

    public UsersAdapter(Context mContext, List<User> usersList) {
        this.mContext = mContext;
        this.usersList = usersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.name.setText(user.getName());
        Glide.with(mContext).load(user.getThumbnail())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(mContext))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
