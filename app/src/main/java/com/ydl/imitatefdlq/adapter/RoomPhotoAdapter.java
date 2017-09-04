package com.ydl.imitatefdlq.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.interfaze.OnItemClickListener;
import com.ydl.imitatefdlq.interfaze.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qweenhool on 2017/9/4.
 */

public class RoomPhotoAdapter extends RecyclerView.Adapter<RoomPhotoAdapter.RoomPhotoViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private ArrayList<String> result;
    private Context context;

    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    //是否显示单选框,默认false
    private boolean isShowBox = false;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();

    public RoomPhotoAdapter(Context context, ArrayList<String> result) {
        this.context = context;
        this.result = result;
        initMap();
    }

    @Override
    public RoomPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_photo, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new RoomPhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomPhotoViewHolder holder, final int position) {
        holder.ivRoomPhoto.setImageURI(Uri.parse(result.get(position)));

        //长按显示/隐藏
        if (isShowBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        //设置Tag
        holder.view.setTag(position);
        //设置checkBox改变监听
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //用map集合保存
                map.put(position, isChecked);
            }
        });
        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
        }
        holder.checkBox.setChecked(map.get(position));
        Log.e("RoomPhotoAdapter", "onBindViewHolder --> " );
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            //注意这里使用getTag方法获取数据
            itemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        //不管显示隐藏，清空状态
        initMap();
        return itemLongClickListener != null && itemLongClickListener.onItemLongClick(v, (Integer) v.getTag());
    }

    class RoomPhotoViewHolder extends RecyclerView.ViewHolder{

        ImageView ivRoomPhoto;
        CheckBox checkBox;
        View view;

        public RoomPhotoViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ivRoomPhoto = (ImageView) itemView.findViewById(R.id.iv_room_photo);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb);
        }
    }

    //初始化map集合,默认为不选中
    private void initMap() {
        for (int i = 0; i < result.size(); i++) {
            map.put(i, false);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    //设置是否显示CheckBox
    public void setShowBox() {
        //取反
        isShowBox = !isShowBox;
    }

    //点击item选中CheckBox
    public void setSelectItem(int position) {
        //对当前状态取反
        if (map.get(position)) {
            map.put(position, false);
        } else {
            map.put(position, true);
        }
        notifyItemChanged(position);
    }

    //返回集合给MainActivity
    public Map<Integer, Boolean> getMap() {
        return map;
    }
}
