package com.example.rohankankapurkar.facebook.Album;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by keke on 5/21/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private  ArrayList<String> urlList;

    public ImageAdapter(Context mContext, ArrayList<String> urlList) {
        this.mContext = mContext;
        this.urlList = urlList;
    }

    public int getCount() {
        return urlList.size();
    }

    public Object getItem(int position) {
        return urlList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(600, 600));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(urlList.get(position)).into(imageView);
//        Log.i("keke", "getView:list "+urlList);
        return imageView;
    }
}
