package com.pluu.webtoon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pluu.webtoon.R;
import com.pluu.webtoon.item.ChatView;
import com.pluu.webtoon.item.DetailView;
import com.pluu.webtoon.ui.view.AspectRatioImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Detail, Chatting Adapter
 * Created by PLUUSYSTEM-NEW on 2016-01-24.
 */
public class DetailChatAdapter extends RecyclerView.Adapter<DetailChatAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<DetailView> list;
    private final int profileSize;

    public DetailChatAdapter(Context context, int profileSize) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = new ArrayList<>();
        this.profileSize = profileSize;
    }

    public void setList(List<DetailView> list) {
        this.list.addAll(list);
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.view_chatting_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DetailView item = list.get(position);

        holder.layoutNotice.setVisibility(View.GONE);
        holder.rightLayout.setVisibility(View.GONE);
        holder.leftLayout.setVisibility(View.GONE);
        holder.empty.setVisibility(View.GONE);

        final ChatView chatValue = item.getChatValue();

        switch (item.getType()) {
            case CHAT_NOTICE:
                holder.layoutNotice.setVisibility(View.VISIBLE);
                holder.noticeImageLayout.setVisibility(View.GONE);
                holder.text1.setVisibility(View.VISIBLE);
                holder.text1.setText(chatValue.getText());
                break;
            case CHAT_NOTICE_IMAGE:
                holder.layoutNotice.setVisibility(View.VISIBLE);
                holder.text1.setVisibility(View.GONE);
                holder.noticeImageLayout.setVisibility(View.VISIBLE);

                if (chatValue.getHRatio() != 0) {
                    holder.noticeImageView.sethRatio(chatValue.getHRatio());
                    Glide.with(context)
                            .load(chatValue.getImgUrl())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(holder.noticeImageView);
                } else {
                    Glide.with(context)
                            .load(chatValue.getImgUrl())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    float hRatio = (float) resource.getHeight() / resource.getWidth();
                                    chatValue.setHRatio(hRatio);
                                    holder.noticeImageView.sethRatio(hRatio);
                                    holder.noticeImageView.setImageBitmap(resource);
                                }
                            });
                }
                break;
            case CHAT_LEFT:
                loadProfileImage(holder.leftProfileImageView, chatValue.getImgUrl());
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.leftNameTextView.setText(chatValue.getName());
                holder.leftMessageTextView.setText(chatValue.getText());
                break;
            case CHAT_RIGHT:
                loadProfileImage(holder.rightProfileImageView, chatValue.getImgUrl());
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.rightNameTextView.setText(chatValue.getName());
                holder.rightMessageTextView.setText(chatValue.getText());
                break;
            case CHAT_EMPTY:
                holder.empty.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void loadProfileImage(ImageView view, String url) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(profileSize, profileSize)
                .centerCrop()
                .placeholder(R.drawable.transparent_background)
                .into(view);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(android.R.id.text1)
        TextView text1;
        @Bind(R.id.noticeImageView)
        AspectRatioImageView noticeImageView;
        @Bind(R.id.noticeVideoView)
        ImageView noticeVideoView;
        @Bind(R.id.noticeImageLayout)
        RelativeLayout noticeImageLayout;
        @Bind(R.id.layoutNotice)
        LinearLayout layoutNotice;
        @Bind(R.id.rightProfileImageView)
        ImageView rightProfileImageView;
        @Bind(R.id.rightProfileImageLayout)
        RelativeLayout rightProfileImageLayout;
        @Bind(R.id.rightNameTextView)
        TextView rightNameTextView;
        @Bind(R.id.rightMessageTextView)
        TextView rightMessageTextView;
        @Bind(R.id.rightLayout)
        LinearLayout rightLayout;
        @Bind(R.id.leftProfileImageView)
        ImageView leftProfileImageView;
        @Bind(R.id.leftProfileImageLayout)
        RelativeLayout leftProfileImageLayout;
        @Bind(R.id.leftNameTextView)
        TextView leftNameTextView;
        @Bind(R.id.leftMessageTextView)
        TextView leftMessageTextView;
        @Bind(R.id.leftLayout)
        LinearLayout leftLayout;
        @Bind(R.id.emptyView)
        View empty;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
