package com.storerepublic.wifaapp.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.storerepublic.wifaapp.R;
import com.storerepublic.wifaapp.helper.BlogCommentOnclicklinstener;
import com.storerepublic.wifaapp.modelsList.blogCommentsModel;
import com.storerepublic.wifaapp.utills.SettingsMain;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemBlogCommentListAdapter extends RecyclerView.Adapter<ItemBlogCommentListAdapter.CustomViewHolder> {

    private List<blogCommentsModel> feedItemList;
    private Context mContext;
    private BlogCommentOnclicklinstener oNItemClickListener;
    SettingsMain settingsMain;

    public ItemBlogCommentListAdapter(Context context, List<blogCommentsModel> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        settingsMain=new SettingsMain(context);
    }

    @Override
    public ItemBlogCommentListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comnt_list, null);
        return new ItemBlogCommentListAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemBlogCommentListAdapter.CustomViewHolder customViewHolder, int i) {
        final blogCommentsModel feedItem = feedItemList.get(i);

        customViewHolder.name.setText(feedItemList.get(i).getName());
        customViewHolder.message.setText(feedItemList.get(i).getMessage());
        customViewHolder.date.setText(feedItemList.get(i).getDate());
        customViewHolder.reply.setText(feedItemList.get(i).getReply());

        if (settingsMain.getAppOpen())
        {
            customViewHolder.reply.setVisibility(View.GONE);
        }
        else
        customViewHolder.reply.setVisibility(feedItem.isCanReply() ? View.VISIBLE : View.GONE);

        if (!TextUtils.isEmpty(feedItem.getImage())) {
            Picasso.with(mContext).load(feedItem.getImage())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(customViewHolder.imageView);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oNItemClickListener.onItemClick(feedItem);
            }
        };

        customViewHolder.reply.setOnClickListener(listener);

        if (feedItemList.get(i).getHasReplyList()) {
            ItemBlogCommentReplyAdapter itemSendRecMesageAdapter = new ItemBlogCommentReplyAdapter(mContext, feedItemList.get(i).getListitemsiner());
            if (feedItemList.get(i).getListitemsiner().size() > 0)
                customViewHolder.recyclerView.setAdapter(itemSendRecMesageAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, message, date, reply;

        RecyclerView recyclerView;

        CustomViewHolder(View view) {
            super(view);

            this.imageView = view.findViewById(R.id.image_view);

            this.name = view.findViewById(R.id.text_viewName);
            this.message = view.findViewById(R.id.prices);
            this.date = view.findViewById(R.id.loginTime);
            this.reply = view.findViewById(R.id.verified);

            this.recyclerView = view.findViewById(R.id.sublist);

            LinearLayoutManager MyLayoutManager = new LinearLayoutManager(mContext);
            MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(MyLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);

        }
    }

    public BlogCommentOnclicklinstener getOnItemClickListener() {
        return oNItemClickListener;
    }

    public void setOnItemClickListener(BlogCommentOnclicklinstener onItemClickListener) {
        this.oNItemClickListener = onItemClickListener;
    }
}
