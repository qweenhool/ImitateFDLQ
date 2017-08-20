package com.ydl.imitatefdlq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.interfaze.OnItemClickListener;

/**
 * Created by qweenhool on 2017/8/18.
 */

public class RoomNumberAdapter extends RecyclerView.Adapter<RoomNumberAdapter.RoomNumberViewHolder> {

    private Context mContext;
    private String[] mRoomNumberArr;
    private LayoutInflater mInflater;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RoomNumberAdapter(Context context, String[] roomNumberArr) {
        mContext = context;
        mRoomNumberArr = roomNumberArr;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RoomNumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_room_number, parent, false);
        return new RoomNumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomNumberViewHolder holder, int position) {
        holder.roomNumber.setText(mRoomNumberArr[position]);
        //租客页面还没搞定，先这样！
        holder.roomInfo.setText("闲置");
        holder.roomInfo.setTextColor(Color.parseColor("#ff8330"));
    }

    @Override
    public int getItemCount() {
        return mRoomNumberArr.length;
    }



    class RoomNumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView roomNumber;
        private TextView roomInfo;

        public RoomNumberViewHolder(View itemView) {
            super(itemView);

            roomNumber = (TextView) itemView.findViewById(R.id.tv_room_number);
            roomInfo = (TextView) itemView.findViewById(R.id.tv_room_info);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getLayoutPosition());
        }
    }
}
