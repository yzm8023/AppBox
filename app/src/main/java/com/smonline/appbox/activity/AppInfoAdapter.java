package com.smonline.appbox.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smonline.appbox.R;

import java.util.List;

/**
 * Created by yzm on 17-12-1.
 */

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.ViewHolder>{

    private Context mContext;
    private List<AppInfo> mApps;
    private OnItemClickListener mListener;

    public AppInfoAdapter(Context context){
        this.mContext = context;
    }

    public void setInstalledApps(List<AppInfo> apps){
        this.mApps = apps;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @Override
    public AppInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.app_repo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppInfoAdapter.ViewHolder holder, int position) {
        AppInfo appInfo = mApps.get(position);
        if(appInfo != null){
            Drawable icon = appInfo.getAppIcon();
            CharSequence title = appInfo.getAppName();
            holder.appIcon.setImageDrawable(icon);
            holder.appTitle.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return mApps != null ? mApps.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView appIcon;
        TextView appTitle;

        public ViewHolder(View viewItem){
            super(viewItem);
            appIcon = (ImageView) viewItem.findViewById(R.id.app_icon_img);
            appTitle = (TextView) viewItem.findViewById(R.id.app_title_txt);
            viewItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(getLayoutPosition(), mApps.get(getLayoutPosition()));
        }
    }
}
