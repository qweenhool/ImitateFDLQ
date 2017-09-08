package com.ydl.imitatefdlq.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.interfaze.OnItemClickListener;
import com.ydl.imitatefdlq.interfaze.OnItemLongClickListener;
import com.ydl.imitatefdlq.util.BitmapUtil;
import com.ydl.imitatefdlq.util.DiskLruCacheUtil;
import com.ydl.imitatefdlq.util.LruCacheUtil;
import com.ydl.imitatefdlq.util.MD5Encoder;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by qweenhool on 2017/9/4.
 */

public class RoomPhotoAdapter extends RecyclerView.Adapter<RoomPhotoAdapter.RoomPhotoViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private ArrayList<String> imagePath;
    private Context context;

    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    private Set<CacheTask> taskCollection;

    private RecyclerView recyclerView;
    /**
     * 图片硬盘缓存核心类。
     */
    private DiskLruCache diskLruCache;

    //是否显示单选框,默认false
    private boolean isShowBox = false;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();

    public RoomPhotoAdapter(Context context, ArrayList<String> imagePath, RecyclerView recyclerView) {
        this.context = context;
        this.imagePath = imagePath;
        this.recyclerView = recyclerView;

        taskCollection = new HashSet<>();
        diskLruCache = DiskLruCacheUtil.getDiskLruCache(context);

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
        String imageUrl = imagePath.get(position);
        holder.ivRoomPhoto.setTag(imageUrl);
        holder.ivRoomPhoto.setImageResource(R.drawable.ic_room_photo);
        showBitmap(holder.ivRoomPhoto, imageUrl);
        //长按显示/隐藏
        if (isShowBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        //设置Tag
        holder.itemView.setTag(position);
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
    }

    @Override
    public int getItemCount() {
        return imagePath.size();
    }

    class RoomPhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRoomPhoto;
        CheckBox checkBox;

        public RoomPhotoViewHolder(View itemView) {
            super(itemView);

            ivRoomPhoto = (ImageView) itemView.findViewById(R.id.iv_room_photo);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
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

    private void showBitmap(ImageView imageView, String imageUrl) {
        try {
            Bitmap bitmap = LruCacheUtil.getInstance().getBitmapFromMemoryCache(imageUrl);
            if (bitmap == null) {
                CacheTask task = new CacheTask();
                taskCollection.add(task);
                task.execute(imageUrl);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化map集合,默认为不选中
    private void initMap() {
        for (int i = 0; i < imagePath.size(); i++) {
            map.put(i, false);
        }
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

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        if (taskCollection != null) {
            for (CacheTask task : taskCollection) {
                task.cancel(false);
            }
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

    class CacheTask extends AsyncTask<String, Void, Bitmap> {

        private String imageUrl;

        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
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
                    bitmap = BitmapUtil.decodeSampledBitmapFromFD(fileDescriptor, 90, 90);
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
            ImageView imageView = (ImageView) recyclerView.findViewWithTag(imageUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);
        }
    }
}
