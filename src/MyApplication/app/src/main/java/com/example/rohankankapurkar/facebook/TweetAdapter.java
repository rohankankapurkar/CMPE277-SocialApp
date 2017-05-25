package com.example.rohankankapurkar.facebook;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rohankankapurkar on 5/25/17.
 */

public class TweetAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<TweetPojo> tweet;

    public TweetAdapter(Context context, List<TweetPojo> feedItems) {
        this.mContext = context;
        this.tweet = feedItems;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tweet.size();
    }

    @Override
    public Object getItem(int location) {
        return tweet.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder {
        TextView from;
        TextView time;
        TextView post;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //View superView = super.getView(position, convertView, parent);
        View view = convertView;
        TweetAdapter.Holder holder = null;

        if(convertView==null) {
            view = inflater.inflate(R.layout.tweet_view_layout,parent,false);
            holder = new Holder();
            holder.from = (TextView) view.findViewById(R.id.tweet_name_tv);
            holder.time = (TextView) view.findViewById(R.id.tweet_time_tv);
            holder.post = (TextView) view.findViewById(R.id.tweet_message);
            view.setTag(holder);
        } else {
            holder = (TweetAdapter.Holder) view.getTag();
        }
        holder.from.setText(tweet.get(position).getName());
        holder.time.setText(tweet.get(position).getTime());
        holder.post.setText(tweet.get(position).getTweet());
        return view;
    }
}
