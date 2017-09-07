package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.PictureBean;
import com.ydl.imitatefdlq.entity.PictureBeanDao;
import com.ydl.imitatefdlq.entity.RoomBean;
import com.ydl.imitatefdlq.entity.RoomBeanDao;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.util.BitmapUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddRoomNumberActivity extends BaseActivity {

    private static final int ROOM_PHOTO = 1;
    private static final int ROOM_CONFIG = 2;
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

    private ArrayList<String> imagePath;
    private RoomBeanDao roomBeanDao;
    private PictureBeanDao pictureBeanDao;


    @Override
    protected int getContentView() {
        return R.layout.activity_add_room_number;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        initData();

        setTitle("添加房号");

        setTopLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        setTopRightButton("保存", 0, new OnClickListener() {
            @Override
            public void onClick() {
                if (!TextUtils.isEmpty(etRoomName.getText().toString().trim())) {
                    String uuid = UUID.randomUUID().toString();
                    RoomBean roomBean = new RoomBean();
                    List<RoomBean> list = roomBeanDao.queryBuilder()
                            .orderDesc(RoomBeanDao.Properties.OrderNumber)
                            .list();
                    if(list.size()!=0){
                        roomBean.setOrderNumber(list.get(0).getOrderNumber()+1);
                    }else {
                        roomBean.setOrderNumber(1);
                    }
                    roomBean.setId(uuid);
                    roomBean.setDataUpload(0);
                    roomBean.setHouseId(getIntent().getStringExtra("house_id"));
                    roomBean.setRoomName(etRoomName.getText().toString());
                    roomBeanDao.insert(roomBean);

                    if (imagePath != null && imagePath.size() != 0) {
                        for (int i = 0; i < imagePath.size(); i++) {
                            PictureBean pictureBean = new PictureBean();
                            pictureBean.setId(UUID.randomUUID().toString());
                            pictureBean.setPath(imagePath.get(i));
                            pictureBean.setForeignId(uuid);
                            pictureBean.setOrderNumber(new Date());
                            pictureBean.setDataUpload(0);
                            pictureBean.setUploadUrl(null);
                            pictureBeanDao.insert(pictureBean);
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("room_id",uuid);
                    setResult(RESULT_OK,intent);
                    finish();

                } else {
                    Toast.makeText(AddRoomNumberActivity.this, "房号不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initData() {
        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        roomBeanDao = daoSession.getRoomBeanDao();
        pictureBeanDao = daoSession.getPictureBeanDao();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_add_room_photo, R.id.ll_add_room_config})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_add_room_photo:
                Intent addRoomPhotoIntent = new Intent(this, RoomPhotoActivity.class);
                if(imagePath!=null){
                    addRoomPhotoIntent.putExtra("image_path",imagePath);
                }
                startActivityForResult(addRoomPhotoIntent, ROOM_PHOTO);
                break;
            case R.id.ll_add_room_config:
                Intent addRoomConfigIntent = new Intent(this, RoomConfigActivity.class);
                startActivityForResult(addRoomConfigIntent, ROOM_CONFIG);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ROOM_PHOTO:
                if (resultCode == RESULT_OK) {
                    //TODO 返回选取的照片

                    imagePath = data.getStringArrayListExtra("imagePath");
                    String firstImagePath = imagePath.get(0);
                    //只显示第一张图片
                    ivRoomPhoto.setImageBitmap(BitmapUtil.decodeSampledBitmapFromPath(firstImagePath, 50, 50));
                    tvRoomAmount.setText(imagePath.size() + "张");
                }
                break;
            case ROOM_CONFIG:
                if (resultCode == RESULT_OK) {
                    //TODO 返回选取的房间配置
                }
        }

    }

}
