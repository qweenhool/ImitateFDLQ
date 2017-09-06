package com.ydl.imitatefdlq.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.adapter.RoomPhotoAdapter;
import com.ydl.imitatefdlq.adapter.SpacesItemDecoration;
import com.ydl.imitatefdlq.interfaze.OnItemClickListener;
import com.ydl.imitatefdlq.interfaze.OnItemLongClickListener;
import com.ydl.imitatefdlq.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomPhotoActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener, OnItemClickListener, OnItemLongClickListener {

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    @BindView(R.id.rl_add_room_photo_above)
    RelativeLayout rlAddRoomPhotoAbove;
    @BindView(R.id.rv_room_photo)
    RecyclerView rvRoomPhoto;
    @BindView(R.id.btn_add_room_photo)
    Button btnAddRoomPhoto;
    @BindView(R.id.ll_no_photo)
    LinearLayout llNoPhoto;

    private List<String> optionsList;
    private Uri imageUri;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private ArrayList<String> imagePath;
    private RoomPhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
                if (imagePath.size() != 0) {
                    Intent intent = new Intent();
                    intent.putExtra("imagePath", imagePath);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
    }

    private void initData() {
        optionsList = new ArrayList<>();
        optionsList.add("拍照");
        optionsList.add("从手机相册选择");

        imagePath = new ArrayList<>();

        ArrayList<String> imagePathFromIntent = getIntent().getStringArrayListExtra("image_path");
        if (imagePathFromIntent != null && imagePathFromIntent.size() != 0) {
            imagePath.addAll(imagePathFromIntent);

        }

        rvRoomPhoto.setLayoutManager(new GridLayoutManager(this, 4));
        rvRoomPhoto.addItemDecoration(new SpacesItemDecoration(4, 3, false));
        adapter = new RoomPhotoAdapter(RoomPhotoActivity.this, imagePath, rvRoomPhoto);
        rvRoomPhoto.setAdapter(adapter);

        adapter.setItemClickListener(this);
        adapter.setItemLongClickListener(this);

        if (imagePath.size() != 0) {
            adapter.notifyDataSetChanged();
            llNoPhoto.setVisibility(View.GONE);
            rvRoomPhoto.setVisibility(View.VISIBLE);
            rlAddRoomPhotoAbove.setVisibility(View.VISIBLE);

        } else {
            llNoPhoto.setVisibility(View.VISIBLE);
            rvRoomPhoto.setVisibility(View.GONE);
            rlAddRoomPhotoAbove.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (imagePath.size() != 0) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.flushCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务
        adapter.cancelAllTasks();
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
            public void onItemClick(final CharSequence charSequence, int i) {
                //为了解决跳转黑屏问题，采用延时执行
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                                /**
                                 * 图片多选
                                 * @param limit 最多选择图片张数的限制
                                 **/

                                takePhoto.onPickMultiple(6);
                            }
                        }
                    }
                }, 100);

            }
        })
                .setCancelable(true, true)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
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

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(final TResult result) {
        StyledDialog.buildMdLoading("图片上传中...").setActivity(RoomPhotoActivity.this).show();
        ArrayList<TImage> images = result.getImages();
        String originalPath;
        for (int i = 0; i < images.size(); i++) {
            originalPath = images.get(i).getOriginalPath();
            //用于返回给AddRoomNumberActivity，用来保存到数据库
            imagePath.add(0, originalPath);
        }
        adapter.notifyDataSetChanged();
        llNoPhoto.setVisibility(View.GONE);
        rvRoomPhoto.setVisibility(View.VISIBLE);
        rlAddRoomPhotoAbove.setVisibility(View.VISIBLE);
        StyledDialog.dismissLoading();

        //TODO 压缩后上传图片

    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onItemClick(View view, int position) {
        adapter.setSelectItem(position);
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        //长按事件
        adapter.setShowBox();
        //设置选中的项
        adapter.setSelectItem(position);
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onBackPressed() {
        //点击返回键，把选中的图片真实地址返回给AddRoomNumberActivity
        if (imagePath.size() != 0) {
            Intent intent = new Intent();
            intent.putExtra("imagePath", imagePath);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}
