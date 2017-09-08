package com.ydl.imitatefdlq.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hss01248.dialog.StyledDialog;
import com.jakewharton.disklrucache.DiskLruCache;
import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.HouseBean;
import com.ydl.imitatefdlq.entity.HouseBeanDao;
import com.ydl.imitatefdlq.entity.PictureBean;
import com.ydl.imitatefdlq.entity.PictureBeanDao;
import com.ydl.imitatefdlq.entity.RoomBean;
import com.ydl.imitatefdlq.entity.RoomBeanDao;
import com.ydl.imitatefdlq.util.BitmapUtil;
import com.ydl.imitatefdlq.util.DiskLruCacheUtil;
import com.ydl.imitatefdlq.util.LruCacheUtil;
import com.ydl.imitatefdlq.util.MD5Encoder;
import com.ydl.imitatefdlq.widget.RoundImageView;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by qweenhool on 2017/8/18.
 */

public class HousePropertyAdapter extends RecyclerView.Adapter<HousePropertyAdapter.HouseViewHolder> {

    private Context mContext;
    private List<HouseBean> mHouseBeanList;
    private LayoutInflater mInflater;

    private DiskLruCache diskLruCache;

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

        diskLruCache = DiskLruCacheUtil.getDiskLruCache(context);

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
            String imageUrl = pictureBeanList.get(0).getPath();
            showBitmap(holder.housePhoto, imageUrl);
        } else {
            holder.housePhoto.setImageResource(R.drawable.room_info);
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

    private void showBitmap(ImageView imageView, String imageUrl) {
        try {
            Bitmap bitmap = LruCacheUtil.getInstance().getBitmapFromMemoryCache(imageUrl);
            if (bitmap == null) {//内存中没有从硬盘缓存读取
                CacheTask task = new CacheTask(imageView);
                task.execute(imageUrl);
            } else {//内存中有就直接从内存读取
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将缓存记录同步到journal文件中。
     */
    public void flushCache() {
        if (diskLruCache != null) {
            try {
                diskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class CacheTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public CacheTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            FileInputStream fileInputStream = null;
            FileDescriptor fileDescriptor = null;
            DiskLruCache.Snapshot snapshot;
            try {
                // 生成图片URL对应的key
                String key = MD5Encoder.encode(imageUrl);
                snapshot = diskLruCache.get(key);
                if (snapshot == null) {
                    // 如果没有找到对应的缓存，则准备将imageUrl的图片写入缓存，默认缓存目录为xBitmapCache
                    DiskLruCache.Editor editor = diskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        DiskLruCacheUtil.cacheFile(imageUrl, outputStream);
                        editor.commit();
                    }
                    snapshot = diskLruCache.get(key);
                }
                if (snapshot != null) {
                    fileInputStream = (FileInputStream) snapshot.getInputStream(0);
                    fileDescriptor = fileInputStream.getFD();
                }
                // 将缓存数据解析成Bitmap对象
                Bitmap bitmap = null;
                if (fileInputStream != null) {
                    bitmap = BitmapUtil.decodeSampledBitmapFromFD(fileDescriptor, 50, 50);
                }
                if (bitmap != null) {
                    // 将Bitmap对象添加到内存缓存当中
                    LruCacheUtil.getInstance().addBitmapToMemoryCache(imageUrl, bitmap);
                }
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileDescriptor == null && fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }else {
                imageView.setImageResource(R.drawable.room_info);
            }
            StyledDialog.dismissLoading();
        }
    }
}
