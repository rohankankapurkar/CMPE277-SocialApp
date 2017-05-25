package com.example.rohankankapurkar.facebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NACHIKET on 5/13/2017.
 */

public class GetFriendAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ListModel> mList;
    private static LayoutInflater inflater=null;

    GetFriendAdapter(ArrayList<ListModel> list, Context context) {
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
        TextView Name;
        TextView email;
        TextView statusButton0;
        TextView statusButton1;
        TextView statusButton2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        GetFriendAdapter.Holder holder = null;

        if(convertView==null) {
            view = inflater.inflate(R.layout.friend_view,parent,false);
            holder = new GetFriendAdapter.Holder();
            holder.Name = (TextView) view.findViewById(R.id.friend_name_tv);
            holder.email = (TextView) view.findViewById(R.id.friend_email_tv);
            holder.statusButton0 = (TextView) view.findViewById(R.id.button_0);
            holder.statusButton1 = (TextView) view.findViewById(R.id.button_1);
            holder.statusButton2 = (TextView) view.findViewById(R.id.button_2);
            view.setTag(holder);
        } else {
            holder = (GetFriendAdapter.Holder) view.getTag();
        }
        holder.Name.setText(mList.get(position).getFriendFirstName()+" "+mList.get(position).getFriendLastName() );
        holder.email.setText(mList.get(position).getFriendEmail());
        holder.statusButton0.setVisibility(View.INVISIBLE);
        holder.statusButton1.setVisibility(View.INVISIBLE);
        holder.statusButton2.setVisibility(View.INVISIBLE);
        switch ( mList.get(position).getStatus()) {
            case 0:
                holder.statusButton0.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.statusButton1.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.statusButton2.setVisibility(View.VISIBLE);
                break;
        }
        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        switch ( mList.get(position).getStatus()) {
            case 2:
                return true;
            default:
                return false;
        }
    }
}
