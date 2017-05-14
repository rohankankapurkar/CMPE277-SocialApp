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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
        /*ImageView addFriendImage;*/
        /*Spinner myFriendsOptionSpinner;*/
        Button addFriendButton;
        Button followFriendButton;
        Button messageButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //View superView = super.getView(position, convertView, parent);
        View view = convertView;
        Holder holder = null;

        if(convertView==null) {
            view = inflater.inflate(R.layout.friend_list_item,parent,false);
            holder = new Holder();
            holder.firstName = (TextView) view.findViewById(R.id.ffname);
            holder.lastName = (TextView) view.findViewById(R.id.flname);
            holder.email = (TextView) view.findViewById(R.id.femail);

            holder.addFriendButton = (Button) view.findViewById(R.id.addfriend);
            holder.followFriendButton = (Button) view.findViewById(R.id.followfriend);
            holder.messageButton = (Button) view.findViewById(R.id.message);

            view.setTag(holder);

            final Holder finalHolder = holder;
            holder.addFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("NACHIKET", finalHolder.email.getText().toString());
                    sendFriendRequest(finalHolder.email.getText().toString());
                }
            });

            holder.followFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followFriend();
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

    private void sendFriendRequest(String friend_req_sent_to) {
        /******** Make API call for your friendRequest logic *******/
        Log.v("NACHIKET", "=====INSIDE sendFriendRequest=====");
        ((FriendListActivity) mContext).sendFriendRequest(friend_req_sent_to);
    }

    private void followFriend() {
        /******** Make API call for your followFriend logic *******/
        Log.v("NACHIKET", "=====INSIDE followFriend=====");
        ((FriendListActivity) mContext).followFriend();
    }
}
