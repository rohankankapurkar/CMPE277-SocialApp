package com.example.rohankankapurkar.facebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View vi = convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.friend_list_item,parent,false);
            holder = new Holder();
            holder.firstName = (TextView) vi.findViewById(R.id.ffname);
            holder.lastName = (TextView) vi.findViewById(R.id.flname);
            holder.email = (TextView) vi.findViewById(R.id.femail);
            vi.setTag(holder);
        } else {
            holder = (Holder) vi.getTag();
        }
        holder.firstName.setText(mList.get(position).getFriendFirstName());
        holder.lastName.setText(mList.get(position).getFriendLastName());
        holder.email.setText(mList.get(position).getFriendEmail());

        return vi;
    }
}
