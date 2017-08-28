package com.ydl.imitatefdlq.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.HouseBean;
import com.ydl.imitatefdlq.entity.HouseBeanDao;
import com.ydl.imitatefdlq.entity.PictureBean;
import com.ydl.imitatefdlq.entity.PictureBeanDao;
import com.ydl.imitatefdlq.entity.RoomBean;
import com.ydl.imitatefdlq.entity.RoomBeanDao;
import com.ydl.imitatefdlq.widget.RoundImageView;

import java.util.List;

/**
 * Created by qweenhool on 2017/8/18.
 */

public class HousePropertyAdapter extends RecyclerView.Adapter<HousePropertyAdapter.HouseViewHolder> {

    private Context mContext;
    private List<HouseBean> mHouseBeanList;
    private LayoutInflater mInflater;

    //每一个对象代表一张表
    private HouseBeanDao houseBeanDao;
    private RoomBeanDao roomBeanDao;
    private PictureBeanDao pictureBeanDao;

    public HousePropertyAdapter(Context context, List<HouseBean> houseBeanList) {
        mContext = context;
        mHouseBeanList = houseBeanList;
        mInflater = LayoutInflater.from(mContext);
        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        houseBeanDao = daoSession.getHouseBeanDao();
        roomBeanDao = daoSession.getRoomBeanDao();
        pictureBeanDao = daoSession.getPictureBeanDao();

    }

    @Override
    public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_house_property, parent, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HouseViewHolder holder, int position) {

        //设置房产照片
        List<PictureBean> pictureBeanList = pictureBeanDao.queryBuilder()
                .where(PictureBeanDao.Properties.ForeignId.eq(mHouseBeanList.get(position).getId()))
                .list();
        if (pictureBeanList.size() != 0) {//有房产照片的话就设置照片
            holder.housePhoto.setImageURI(Uri.parse(pictureBeanList.get(0).getPath()));
        }
        //设置房产名
        holder.houseName.setText(mHouseBeanList.get(position).getHouseName());

        //设置房产信息
        List<RoomBean> roomBeanList = roomBeanDao.queryBuilder()
                .where(RoomBeanDao.Properties.HouseId.eq(mHouseBeanList.get(position).getId()))
                .list();
        if (roomBeanList.size() != 0) {
            //Todo 闲置功能尚待开发
            holder.totalRoomNumber.setText(roomBeanList.size() + "");
            holder.idleRoomNumber.setText(roomBeanList.size() + "");
            holder.llHouseInfo.setVisibility(View.VISIBLE);
            holder.tvHouseInfo.setVisibility(View.GONE);
        } else {
            holder.llHouseInfo.setVisibility(View.GONE);
            holder.tvHouseInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mHouseBeanList.size();
    }


    class HouseViewHolder extends RecyclerView.ViewHolder {
        private RoundImageView housePhoto;
        private TextView houseName;
        private LinearLayout llHouseInfo;
        private TextView totalRoomNumber;
        private TextView idleRoomNumber;
        private TextView tvHouseInfo;

        public HouseViewHolder(View itemView) {
            super(itemView);

            housePhoto = (RoundImageView) itemView.findViewById(R.id.riv_house_photo);
            houseName = (TextView) itemView.findViewById(R.id.tv_house_name);
            llHouseInfo = (LinearLayout) itemView.findViewById(R.id.ll_house_info);
            totalRoomNumber = (TextView) itemView.findViewById(R.id.tv_total_room_number);
            idleRoomNumber = (TextView) itemView.findViewById(R.id.tv_idle_room_number);
            tvHouseInfo = (TextView) itemView.findViewById(R.id.tv_no_room_yet);

        }
    }
}
