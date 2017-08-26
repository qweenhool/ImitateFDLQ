package com.ydl.imitatefdlq.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.HouseBean;
import com.ydl.imitatefdlq.entity.HouseBeanDao;
import com.ydl.imitatefdlq.entity.PictureBean;
import com.ydl.imitatefdlq.entity.PictureBeanDao;
import com.ydl.imitatefdlq.entity.RoomBean;
import com.ydl.imitatefdlq.entity.RoomBeanDao;
import com.ydl.imitatefdlq.ui.fragment.AddRoomFragment;
import com.ydl.imitatefdlq.ui.fragment.BatchAddRoomFragment;
import com.ydl.imitatefdlq.util.EditTextUtils;
import com.ydl.imitatefdlq.util.StatusBarCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddHouseActivity extends AppCompatActivity {


    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.tb_add_house)
    Toolbar tbAddHouse;
    @BindView(R.id.et_add_house_name)
    EditText etAddHouseName;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.tv_house_type)
    TextView tvHouseType;
    @BindView(R.id.ll_house_type)
    LinearLayout llHouseType;
    @BindView(R.id.iv_house_photo)
    ImageView ivHousePhoto;
    @BindView(R.id.ll_house_photo)
    LinearLayout llHousePhoto;
    @BindView(R.id.tv_account_info)
    TextView tvAccountInfo;
    @BindView(R.id.ll_receive_account)
    LinearLayout llReceiveAccount;
    @BindView(R.id.cb_add_house_number)
    CheckBox cbAddHouseNumber;
    @BindView(R.id.fl_add_room_number)
    FrameLayout flAddRoomNumber;

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    private String imagePath;
    private Uri imageUri;
    private String[] houseTypeArr;
    private OptionsPickerView opvHouseType;
    private List<String> housePhotoList;
    private AddRoomFragment addRoomFragment;
    private BatchAddRoomFragment batchAddRoomFragment;
    //批量添加房号开关
    private boolean isBatchAddRoom;

    //每一个对象代表一张表
    private HouseBeanDao houseBeanDao;
    private RoomBeanDao roomBeanDao;
    private PictureBeanDao pictureBeanDao;

    private HouseBean houseBean;
    private RoomBean roomBean;
    private PictureBean pictureBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorStatusbar));
        EditTextUtils.clearButtonListener(etAddHouseName, ivClear);

        initData();

        initToolbar();
        //构建条件选择器，此处为房产类型的选择
        initOptionPicker();

        initFragment(savedInstanceState);


    }

    private void initData() {
        housePhotoList = new ArrayList<>();
        housePhotoList.add("拍照");
        housePhotoList.add("从手机相册选择");

        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        houseBeanDao = daoSession.getHouseBeanDao();
        roomBeanDao = daoSession.getRoomBeanDao();
        pictureBeanDao = daoSession.getPictureBeanDao();

        houseBean = new HouseBean();
        pictureBean = new PictureBean();

    }

    private void initFragment(Bundle savedInstanceState) {
//        if (savedInstanceState == null) {
//            addRoomFragment = new AddRoomFragment();
//            batchAddRoomFragment = new BatchAddRoomFragment();
//        }else {
//            //Todo 先存，再取
//        }
        addRoomFragment = new AddRoomFragment();
        batchAddRoomFragment = new BatchAddRoomFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_add_room_number, addRoomFragment, addRoomFragment.getClass().getSimpleName())
                .add(R.id.fl_add_room_number, batchAddRoomFragment, batchAddRoomFragment.getClass().getSimpleName())
                .hide(batchAddRoomFragment)
                .show(addRoomFragment)
                .commit();
    }

    private void initStyledDialog() {
        StyledDialog.buildIosSingleChoose(housePhotoList, new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence charSequence, int i) {
                if ("拍照".equals(charSequence)) {
                    File file = new File(getExternalCacheDir(), "temp_house_image.jpg");

                    try {
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        imageUri = FileProvider.getUriForFile(AddHouseActivity.this,
                                "com.ydl.imitatefdlq.fileprovider", file);
                    } else {
                        imageUri = Uri.fromFile(file);
                    }
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PHOTO);

                } else if ("从手机相册选择".equals(charSequence)) {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, CHOOSE_PHOTO);

                } else if ("删除".equals(charSequence)) {
                    ivHousePhoto.setImageResource(R.drawable.ic_add_photo);
//                    housePhotoArr = new String[]{"拍照", "从手机相册选择"};
                    if (housePhotoList.contains("删除")) {
                        housePhotoList.remove("删除");
                    }
                }
            }
        })
                .setCancelable(true, true)
                .show();
    }

    private void initOptionPicker() {
        houseTypeArr = new String[]{"住宅/小区/公寓", "商铺/门市房", "厂房/车间", "仓库/车库/停车位", "写字楼/办公室"};
        //开源库地址：https://github.com/Bigkoo/Android-PickerView
        opvHouseType = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tvHouseType.setText(houseTypeArr[options1]);
            }
        })
                .setTextColorCenter(Color.parseColor("#5287E7"))//滚轮文字颜色设置
                .setLineSpacingMultiplier(1.8f)//滚轮间距设置（1.2-2.0倍，此为文字高度的间距倍数）
                .build();
        opvHouseType.setPicker(Arrays.asList(houseTypeArr));
    }

    private void initToolbar() {
        setSupportActionBar(tbAddHouse);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        tvToolbarName.setText("添加房产");
        tbAddHouse.setNavigationIcon(R.drawable.back);
        tbAddHouse.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (TextUtils.isEmpty(etAddHouseName.getText().toString().trim())) {
                StyledDialog.buildIosAlert("提醒", "房产名不能为空", new MyDialogListener() {
                    @Override
                    public void onFirst() {

                    }

                    @Override
                    public void onSecond() {

                    }
                }).show();
            } else {
                saveData();
                //来个ios样式的loading
                StyledDialog.buildLoading("请稍后").show();
                //Todo,此处应该同步服务器
                Intent roomNumberIntent = new Intent(this, RoomNumberActivity.class);
                roomNumberIntent.putExtra("house_id", houseBean.getId());
                startActivity(roomNumberIntent);
                finish();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //将房产数据存到数据库中
    private void saveData() {

        //往house表中存数据
        String uuid = UUID.randomUUID().toString();
        houseBean.setId(uuid);
        houseBean.setDataUpload(0);
        houseBean.setHouseName(etAddHouseName.getText().toString());
        houseBean.setHouseType(tvHouseType.getText().toString());
        houseBean.setOrderNumber(new Date());
        houseBean.setUseFeeTemplate(1);
        houseBeanDao.insert(houseBean);
        //Todo,往picture表中存数据

        //往room表中存数据
        //第一种情况，默认添加房号
        LinearLayout container = (LinearLayout) getSupportFragmentManager()
                .findFragmentByTag(AddRoomFragment.class.getSimpleName())
                .getView()
                .findViewById(R.id.ll_container);
        EditText et;
        int n = 1;
        for (int i = 0; i < container.getChildCount(); i++) {
            et = (EditText) container.getChildAt(container.getChildCount()-i-1).findViewById(R.id.et_add_room_number);
            if (!TextUtils.isEmpty(et.getText().toString().trim())) {
                roomBean = new RoomBean();
                roomBean.setHouseId(uuid);
                roomBean.setDataUpload(0);
                roomBean.setId(UUID.randomUUID().toString());
                roomBean.setRoomName(et.getText().toString().trim());
                roomBean.setOrderNumber(n++);
                roomBeanDao.insert(roomBean);
            }
        }
        //Todo,第二种情况，批量添加房号

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ivHousePhoto.setImageBitmap(bitmap);
                        if (!housePhotoList.contains("删除")) {
                            housePhotoList.add("删除");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                    if (!housePhotoList.contains("删除")) {
                        housePhotoList.add("删除");
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ivHousePhoto.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @OnClick({R.id.ll_house_type, R.id.ll_house_photo, R.id.ll_receive_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_house_type:
                opvHouseType.show();
                break;
            case R.id.ll_house_photo:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    initStyledDialog();
                }
                break;
            case R.id.ll_receive_account:
                Intent accountIntent = new Intent(this, SelectAccountActivity.class);
                startActivity(accountIntent);
                break;
        }
    }

    @OnClick(R.id.cb_add_house_number)
    public void onViewClicked() {
        if (!isBatchAddRoom) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//添加转换动画
                    .show(batchAddRoomFragment)
                    .hide(addRoomFragment)
                    .commit();
            isBatchAddRoom = true;
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .show(addRoomFragment)
                    .hide(batchAddRoomFragment)
                    .commit();
            isBatchAddRoom = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initStyledDialog();
                } else {
                    Toast.makeText(this, "你拒绝了该权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
