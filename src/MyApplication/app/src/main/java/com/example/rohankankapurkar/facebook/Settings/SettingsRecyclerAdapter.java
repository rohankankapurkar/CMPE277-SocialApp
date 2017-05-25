package com.example.rohankankapurkar.facebook.Settings;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rohankankapurkar.facebook.R;

public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.ViewHolder> {
    private String userName;

    public SettingsRecyclerAdapter(String userName) {
        this.userName = userName;
    }

    private String[] titles = {"Privacy",
            "Notifications"};

    private int[] images = { R.drawable.ic_privacy,
            R.drawable.ic_notification };


    class ViewHolder extends RecyclerView.ViewHolder {
        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;


        public ViewHolder(final View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    final Intent intent;
                    int position = getAdapterPosition();
/*
                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
*/
                    if(position==0)//privacy
                    {
                        intent=new Intent(itemView.getContext(),PrivacySettings.class);
                        intent.putExtra("userName", userName);

                        itemView.getContext().startActivity(intent);

                    }
                    else if(position==1)//notifications
                    {
                        intent=new Intent(itemView.getContext(),NotificationSettings.class);
                        intent.putExtra("userName",userName);
                        itemView.getContext().startActivity(intent);

                    }


                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.settings_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles[i]);
        viewHolder.itemImage.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}