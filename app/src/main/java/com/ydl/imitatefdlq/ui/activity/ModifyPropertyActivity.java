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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.ydl.imitatefdlq.util.BitmapUtil;
import com.ydl.imitatefdlq.util.EditTextUtils;
import com.ydl.imitatefdlq.util.StatusBarCompat;
import com.ydl.imitatefdlq.widget.RoundImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPropertyActivity extends AppCompatActivity {

    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_house_name)
    EditText etHouseName;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.tv_house_type)
    TextView tvHouseType;
    @BindView(R.id.ll_house_type)
    LinearLayout llHouseType;
    @BindView(R.id.iv_house_photo)
    RoundImageView ivHousePhoto;
    @BindView(R.id.ll_house_photo)
    LinearLayout llHousePhoto;
    @BindView(R.id.tv_account_info)
    TextView tvAccountInfo;
    @BindView(R.id.ll_receive_account)
    LinearLayout llReceiveAccount;
    @BindView(R.id.ll_block)
    LinearLayout llBlock;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    private String[] houseTypeArr;
    private OptionsPickerView opvHouseType;
    private List<String> housePhotoList;
    private File pictures;
    private File file;
    private Uri imageUri;
    private String picUUID;
    private String imagePath;


    //每一个对象代表一张表
    private HouseBeanDao houseBeanDao;
    private PictureBeanDao pictureBeanDao;

    private List<HouseBean> houseBeanList;
    private List<PictureBean> pictureBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_property);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorStatusbar));
        EditTextUtils.clearButtonListener(etHouseName, ivClear);

        initToolbar();

        initData();

        initOptionPicker();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        tvToolbarName.setText("修改房产");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {
        housePhotoList = new ArrayList<>();
        pictures = new File(getExternalFilesDir(null).getPath() + "/Pictures");
        //原始照片，初始化imageUri
        file = new File(pictures, "fdlq.jpg");

        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(ModifyPropertyActivity.this,
                    "com.ydl.imitatefdlq.fileprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }
        //获取数据库文件
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        houseBeanDao = daoSession.getHouseBeanDao();
        pictureBeanDao = daoSession.getPictureBeanDao();

        Intent intent = getIntent();
        String houseId = intent.getStringExtra("house_id");
        houseBeanList = houseBeanDao.queryBuilder()
                .where(HouseBeanDao.Properties.Id.eq(houseId))
                .list();
        //设置房产名称
        etHouseName.setText(houseBeanList.get(0).getHouseName());
        //设置房产类型
        tvHouseType.setText(houseBeanList.get(0).getHouseType());
        //设置EditText光标在最后
        etHouseName.requestFocus();
        etHouseName.setSelection(houseBeanList.get(0).getHouseName().length());
        //获取房产照片
        pictureBeanList = pictureBeanDao.queryBuilder()
                .where(PictureBeanDao.Properties.ForeignId.eq(houseBeanList.get(0).getId()))
                .list();
        if (pictureBeanList.size() != 0) {
            ivHousePhoto.setImageURI(Uri.parse(pictureBeanList.get(0).getPath()));
            housePhotoList.add("拍照");
            housePhotoList.add("从手机相册选择");
            housePhotoList.add("删除");
        } else {
            housePhotoList.add("拍照");
            housePhotoList.add("从手机相册选择");
        }
        //Todo 获取收款账户
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (TextUtils.isEmpty(etHouseName.getText().toString().trim())) {
                StyledDialog.buildIosAlert("提醒", "房产名不能为空", new MyDialogListener() {
                    @Override
                    public void onFirst() {

                    }

                    @Override
                    public void onSecond() {

                    }
                }).show();
            } else {//没点击保存之前，表的内容不会变
                //Todo 点击保存，同步服务器

                //修改house表的内容
                HouseBean houseBean = houseBeanList.get(0);
                houseBean.setHouseName(etHouseName.getText().toString());
                houseBean.setHouseType(tvHouseType.getText().toString());
                houseBeanDao.update(houseBean);
                //修改picture表的内容
                if (pictureBeanList.size() != 0) {//原来有房产照片
                    PictureBean pictureBean = pictureBeanList.get(0);
                    if (picUUID != null) {//不为空表示用户选择了新照片
                        pictureBean.setPath(pictures.getAbsolutePath() + "/" + picUUID + ".jpg");
                        pictureBean.setOrderNumber(new Date());
                        pictureBean.setUploadUrl(null);//Todo 上传服务器地址
                        pictureBeanDao.update(pictureBean);
                    } else if (!housePhotoList.contains("删除")) {//表示用户删除了照片，直接更新数据库
                        pictureBeanDao.deleteByKey(pictureBean.getId());
                        //Todo 同时告诉服务器照片被删了
                    }
                } else {//原来没有房产照片
                    PictureBean pictureBean = new PictureBean();
                    pictureBean.setId(UUID.randomUUID().toString());
                    pictureBean.setPath(pictures.getAbsolutePath() + "/" + picUUID + ".jpg");
                    pictureBean.setForeignId(houseBean.getId());
                    pictureBean.setOrderNumber(new Date());
                    pictureBean.setDataUpload(0);
                    pictureBean.setUploadUrl(null);//Todo 上传服务器地址
                    pictureBean.setSortNo(0);
                    pictureBeanDao.insert(pictureBean);
                }
                finish();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ll_house_type, R.id.ll_house_photo, R.id.ll_receive_account, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_house_type:
                opvHouseType.show();
                break;
            case R.id.ll_house_photo://点击房产照片
                initStyledDialog();
                break;
            case R.id.ll_receive_account:
                break;
            case R.id.tv_delete:
                StyledDialog.buildIosAlert("删除确认", "删除房产将一并删除其所有的房号，租客及账单，您确定要删除吗？", new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        Toast.makeText(ModifyPropertyActivity.this, "确认", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSecond() {
                        Toast.makeText(ModifyPropertyActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                break;
        }
    }

    private void initStyledDialog() {
        StyledDialog.buildIosSingleChoose(housePhotoList, new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence charSequence, int i) {
                if ("拍照".equals(charSequence)) {
                    if (ContextCompat.checkSelfPermission(ModifyPropertyActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {//不同意就弹权限框
                        ActivityCompat.requestPermissions(ModifyPropertyActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 1);
                    } else {//同意拍照就打开摄像头
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                } else if ("从手机相册选择".equals(charSequence)) {
                    if (ContextCompat.checkSelfPermission(ModifyPropertyActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//不同意就弹权限框
                        ActivityCompat.requestPermissions(ModifyPropertyActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    } else {//同意就打开相册
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        intent.setType("image/*");
                        startActivityForResult(intent, CHOOSE_PHOTO);
                    }
                } else if ("删除".equals(charSequence)) {
                    ivHousePhoto.setImageResource(R.drawable.ic_add_photo);
                    picUUID = null;
                    if (housePhotoList.contains("删除")) {
                        housePhotoList.remove("删除");
                    }
                }
            }
        })
                .setCancelable(true, true)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    StyledDialog.buildLoading("请稍后").show();
                    //Todo 同时在Android/data/包名/cache/xBitmapCache下也缓存一张压缩过的（一长串数字.0），有何用？
                    picUUID = UUID.randomUUID().toString();
                    File compressedFile = BitmapUtil.saveBitmapToFile(file, pictures.getPath() + "/" + picUUID + ".jpg");
                    Bitmap bitmap = BitmapFactory.decodeFile(compressedFile.getAbsolutePath());
                    ivHousePhoto.setImageBitmap(bitmap);
                    if (!housePhotoList.contains("删除")) {
                        housePhotoList.add("删除");
                    }
                    StyledDialog.dismissLoading();
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    StyledDialog.buildLoading("请稍后").show();
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
                    picUUID = UUID.randomUUID().toString();
                    //把imagePath所在的图片复制到Android/data/包名/files/Pictures目录下并更名为pictures.getPath() + "/" + picUUID + ".jpg"
                    copyFile(imagePath, pictures.getPath() + "/" + picUUID + ".jpg");
                    StyledDialog.dismissLoading();
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

    public void copyFile(String srcPath, String desPath) {
        try {
            int byteSum = 0;
            int byteRead = 0;
            File srcFile = new File(srcPath);
            if (srcFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(srcPath); //读入原文件,读入到代码中
                FileOutputStream fs = new FileOutputStream(desPath);
                byte[] buffer = new byte[1444];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    byteSum += byteRead; //字节数 文件大小
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (EditTextUtils.isShouldHideInput(view, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


}
