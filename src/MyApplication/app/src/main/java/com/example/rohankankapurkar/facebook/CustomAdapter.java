package com.example.rohankankapurkar.facebook;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NACHIKET on 5/11/2017.
 */

public class CustomAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<ListModel> mList;
    private static LayoutInflater inflater=null;


    CustomAdapter(ArrayList<ListModel> list, Context context) {
        mList = list;
        mContext = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder {
        TextView firstName;
        TextView lastName;
        TextView email;
        ImageView addFriendImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view = convertView;
        if(convertView==null) {
            view = inflater.inflate(R.layout.friend_list_item,parent,false);
            holder = new Holder();
            holder.firstName = (TextView) view.findViewById(R.id.ffname);
            holder.lastName = (TextView) view.findViewById(R.id.flname);
            holder.email = (TextView) view.findViewById(R.id.femail);
            holder.addFriendImage = (ImageView) view.findViewById(R.id.addFriend);
            view.setTag(holder);

            /******** Set Item Click Listner for LayoutInflater for each row *******/
            holder.addFriendImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /******** call your sendRequest function *******/
                    sendFriendRequest();
                }
            });

        } else {
            holder = (Holder) view.getTag();
        }
        holder.firstName.setText(mList.get(position).getFriendFirstName());
        holder.lastName.setText(mList.get(position).getFriendLastName());
        holder.email.setText(mList.get(position).getFriendEmail());

        return view;
    }

    private void sendFriendRequest() {
        /******** Make API call for your friendRequest logic *******/
        Log.v("NACHIKET", "=====INSIDE sendFriendRequest=====");

        /*sendFriendRequest =  new sendFriendRequestTask();
        sendFriendRequest.execute((Void) null);*/
        ((FriendListActivity) mContext).sendFriendRequest();
    }
}
