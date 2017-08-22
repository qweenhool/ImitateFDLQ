package com.ydl.imitatefdlq.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.widget.RoundImageView;

/**
 * Created by qweenhool on 2017/8/21.
 */

public class CustomCursorAdapter extends SimpleCursorAdapter {

    private Context context;

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        SwipeMenuViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_house_property, parent, false);
            viewHolder = new SwipeMenuViewHolder();
            viewHolder.housePhoto = (RoundImageView) view.findViewById(R.id.riv_house_photo);
            viewHolder.housename = (TextView) view.findViewById(R.id.tv_house_name);
            viewHolder.roomInfo = (TextView) view.findViewById(R.id.tv_house_info);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (SwipeMenuViewHolder) view.getTag();
        }
//        viewHolder.housename.setText();
//        viewHolder.roomInfo.setText();

        return view;
    }

    class SwipeMenuViewHolder {

        RoundImageView housePhoto;
        TextView housename;
        TextView roomInfo;

    }
}
