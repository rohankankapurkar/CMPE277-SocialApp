package com.example.rohankankapurkar.facebook.Album;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rohankankapurkar.facebook.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> urlList;
    private ArrayList<String> titleList;
    private String userName;

    public RecyclerAdapter(Context mContext,ArrayList<String> titleList,ArrayList<String> urlList,String userName) {
        this.mContext = mContext;
        this.urlList = urlList;
        this.titleList = titleList;
        this.userName=userName;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
        private final Context recContext;
        public ViewHolder( View itemView) {
            super(itemView);
            recContext=itemView.getContext();
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent;
                    int position = getAdapterPosition();
                    if (position == 0)//add album
                    {
                        Snackbar.make(v, "Add new album", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        intent=new Intent(recContext,AddAlbumDelatils.class);
                        intent.putExtra("userName",getUserName());
                        recContext.startActivity(intent);
                    }
                    else{
                        Snackbar.make(v, "Click detected on item " + position, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        intent = new Intent(recContext, ViewSelectedAlbumPage.class);
                        intent.putExtra("albumName", titleList.get(position));
                        intent.putExtra("userName",getUserName());
                        recContext.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.album_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titleList.get(i));
        if(i==0)//add album
        {
            viewHolder.itemImage.setImageResource(R.drawable.add);
        }
        else {
            Picasso.with(mContext).load(urlList.get(i)).into(viewHolder.itemImage);
        }
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public String getUserName(){
        String uName=userName.replace('.','!');
        return uName;
    }
}