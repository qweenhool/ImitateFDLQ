package com.ydl.imitatefdlq.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomPhotoActivity extends BaseActivity {

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    @BindView(R.id.rl_add_room_photo_above)
    RelativeLayout rlAddRoomPhotoAbove;
    @BindView(R.id.rv_room_photo)
    RecyclerView rvRoomPhoto;
    @BindView(R.id.btn_add_room_photo)
    Button btnAddRoomPhoto;

    private List<String> optionsList;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_room_photo;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        initData();

        setTitle("照片");

        setTopLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                //TODO 有照片的话就返回照片，没有就finish()
            }
        });
    }

    private void initData() {
        optionsList = new ArrayList<>();
        optionsList.add("拍照");
        optionsList.add("从手机相册选择");
    }

    @OnClick({R.id.rl_add_room_photo_above, R.id.btn_add_room_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_add_room_photo_above:
                showStyledDialog();
                break;
            case R.id.btn_add_room_photo:
                showStyledDialog();
                break;
        }
    }

    private void showStyledDialog() {
        StyledDialog.buildIosSingleChoose(optionsList, new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence charSequence, int i) {
                if ("拍照".equals(charSequence)) {
                    if (ContextCompat.checkSelfPermission(RoomPhotoActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {//不同意就弹权限框
                        ActivityCompat.requestPermissions(RoomPhotoActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 1);
                    } else {//同意拍照就打开摄像头
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                } else if ("从手机相册选择".equals(charSequence)) {
                    if (ContextCompat.checkSelfPermission(RoomPhotoActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//不同意就弹权限框
                        ActivityCompat.requestPermissions(RoomPhotoActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    } else {//同意就打开相册

                    }
                }
            }
        })
                .setCancelable(true, true)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PHOTO);
                } else {
                    Toast.makeText(this, "你拒绝了该权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, CHOOSE_PHOTO);
                } else {
                    Toast.makeText(this, "你拒绝了该权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


}
