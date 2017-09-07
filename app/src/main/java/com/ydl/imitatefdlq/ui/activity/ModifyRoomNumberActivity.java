package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.PictureBean;
import com.ydl.imitatefdlq.entity.PictureBeanDao;
import com.ydl.imitatefdlq.entity.RoomBean;
import com.ydl.imitatefdlq.entity.RoomBeanDao;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.util.BitmapUtil;
import com.ydl.imitatefdlq.util.EditTextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyRoomNumberActivity extends BaseActivity {

    @BindView(R.id.tv_house_name)
    TextView tvHouseName;
    @BindView(R.id.et_room_name)
    EditText etRoomName;
    @BindView(R.id.iv_room_photo)
    ImageView ivRoomPhoto;
    @BindView(R.id.tv_room_amount)
    TextView tvRoomAmount;
    @BindView(R.id.rl_add_room_photo)
    RelativeLayout rlAddRoomPhoto;
    @BindView(R.id.ll_add_room_config)
    LinearLayout llAddRoomConfig;
    @BindView(R.id.rv_room_config)
    RecyclerView rvRoomConfig;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.iv_clear)
    ImageView ivClear;

    private String roomId;
    private String houseName;
    private String roomName;

    private RoomBeanDao roomBeanDao;
    private PictureBeanDao pictureBeanDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_modify_room_number;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        initData();

        setTitle("修改房号");

        setTopLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        setTopRightButton("保存", 0, new OnClickListener() {
            @Override
            public void onClick() {

            }
        });
    }

    private void initData() {
        EditTextUtil.clearButtonListener(etRoomName, ivClear);

        roomId = getIntent().getStringExtra("room_id");
        houseName = getIntent().getStringExtra("house_name");

        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        roomBeanDao = daoSession.getRoomBeanDao();
        pictureBeanDao = daoSession.getPictureBeanDao();

        List<RoomBean> roomBeanList = roomBeanDao.queryBuilder()
                .where(RoomBeanDao.Properties.Id.eq(roomId))
                .list();
        //设置房产名
        tvHouseName.setText(houseName);
        //设置房号
        roomName = roomBeanList.get(0).getRoomName();
        etRoomName.setText(roomName);
        //把EditText的光标在最后
        etRoomName.requestFocus();
        etRoomName.setSelection(roomName.length());
        //设置照片
        List<PictureBean> pictureBeanList = pictureBeanDao.queryBuilder()
                .where(PictureBeanDao.Properties.ForeignId.eq(roomId))
                .orderDesc(PictureBeanDao.Properties.SortNo)
                .list();
        if (pictureBeanList.size() != 0) {
            //TODO 图片压缩到控件大小显示的时候会导致有点延时
            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromPath(pictureBeanList.get(0).getPath(), 50, 50);
            if (bitmap != null) {
                ivRoomPhoto.setImageBitmap(bitmap);
            }
            tvRoomAmount.setText(pictureBeanList.size() + "张");
        }
        //TODO 设置房号配置
    }

    @OnClick({R.id.rl_add_room_photo, R.id.ll_add_room_config, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_add_room_photo:

                break;
            case R.id.ll_add_room_config:

                break;
            case R.id.tv_delete:
                StyledDialog.buildIosAlert("删除确认", "您确定要删除" + houseName + "-" + roomName +
                        ",同时将删除其全部租客及账单", new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        //删除该房间
                        roomBeanDao.deleteByKey(roomId);
                        //删除该房间照片
                        List<PictureBean> pictureBeanList = pictureBeanDao.queryBuilder()
                                .where(PictureBeanDao.Properties.ForeignId.eq(roomId))
                                .list();
                        if (pictureBeanList.size() != 0) {
                            for (int i = 0; i < pictureBeanList.size(); i++) {
                                pictureBeanDao.deleteByKey(pictureBeanList.get(i).getId());
                            }
                        }
                        //TODO 删除房号配置
                        //TODO 删除相应的租客
                        Intent roomNumberIntent = new Intent(ModifyRoomNumberActivity.this, RoomNumberActivity.class);
                        roomNumberIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(roomNumberIntent);
                    }

                    @Override
                    public void onSecond() {

                    }
                }).show();

                break;
        }
    }
}
