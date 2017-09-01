package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.ui.base.BaseActivity;

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

    @Override
    protected int getContentView() {
        return R.layout.activity_add_room_number;
    }

    @Override
    protected void init(Bundle savedInstanceState) {


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

            }
        });

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
                Intent addRoomPhotoIntent = new Intent(this,RoomPhotoActivity.class);
                startActivityForResult(addRoomPhotoIntent,ROOM_PHOTO);
                break;
            case R.id.ll_add_room_config:
                Intent addRoomConfigIntent = new Intent(this,RoomConfigActivity.class);
                startActivityForResult(addRoomConfigIntent,ROOM_CONFIG);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ROOM_PHOTO:
                if(resultCode == RESULT_OK){
                    //TODO 返回选取的照片
                }
                break;
            case ROOM_CONFIG:
                if(resultCode == RESULT_OK){
                    //TODO 返回选取的房间配置
                }
        }

    }
}
