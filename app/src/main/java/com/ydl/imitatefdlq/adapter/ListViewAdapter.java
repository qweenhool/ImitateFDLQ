package com.ydl.imitatefdlq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ydl.imitatefdlq.R;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ListViewAdapter extends BaseAdapter implements View.OnClickListener {

    private static final int UPCOMING = 0;
    private static final int ACCOUNT_BOOK = 1;
    private static final int EXTRA = 2;
    private Context mContext;
    private LayoutInflater inflater;

    public ListViewAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        switch (getItemViewType(position)){
            case UPCOMING:
                view = inflater.inflate(R.layout.item_first, parent, false);
                LinearLayout owner = (LinearLayout) view.findViewById(R.id.ll_owner);
                LinearLayout renter = (LinearLayout) view.findViewById(R.id.ll_renter);
                LinearLayout upcoming = (LinearLayout) view.findViewById(R.id.ll_upcoming);

                owner.setOnClickListener(this);
                renter.setOnClickListener(this);
                upcoming.setOnClickListener(this);
                return view;
            case ACCOUNT_BOOK:
               view = inflater.inflate(R.layout.item_second, parent, false);
                return view;
            case EXTRA:
                view = inflater.inflate(R.layout.item_third, parent, false);
                return view;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return UPCOMING;
            case 1:
                return ACCOUNT_BOOK;
            case 2:
                return EXTRA;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll_owner:
                Toast.makeText(mContext,"我被点击了",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_renter:
                Toast.makeText(mContext,"我被点击了",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_upcoming:
                Toast.makeText(mContext,"我被点击了",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
