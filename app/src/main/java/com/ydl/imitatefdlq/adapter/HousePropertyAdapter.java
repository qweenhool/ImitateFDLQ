package com.ydl.imitatefdlq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.House;
import com.ydl.imitatefdlq.widget.RoundImageView;

import java.util.List;

/**
 * Created by qweenhool on 2017/8/18.
 */

public class HousePropertyAdapter extends RecyclerView.Adapter<HousePropertyAdapter.HouseViewHolder> {

    private Context mContext;
    private List<House> mHouseList;
    private LayoutInflater mInflater;
    private OnHouseItemClickListener listener;

    public void setOnItemClickListener(OnHouseItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnHouseItemClickListener {
        void onItemClick(int position);
    }

    public HousePropertyAdapter(Context context, List<House> houseList) {
        mContext = context;
        mHouseList = houseList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_house_property, parent, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HouseViewHolder holder, int position) {
        holder.houseName.setText(mHouseList.get(position).getName());
        String roomNumber = mHouseList.get(position).getRoomNumber();
        String[] temp = roomNumber.split(",");
        if (!TextUtils.isEmpty(roomNumber)) {
            holder.houseInfo.setText("共" + temp.length + "套住宅");
        } else {
            holder.houseInfo.setText("暂未添加房号");
        }
    }

    @Override
    public int getItemCount() {
        return mHouseList.size();
    }



    class HouseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RoundImageView photo;
        private TextView houseName;
        private TextView houseInfo;

        public HouseViewHolder(View itemView) {
            super(itemView);

            photo = (RoundImageView) itemView.findViewById(R.id.riv_house_photo);
            houseName = (TextView) itemView.findViewById(R.id.tv_house_name);
            houseInfo = (TextView) itemView.findViewById(R.id.tv_house_info);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getLayoutPosition());
        }
    }
}
