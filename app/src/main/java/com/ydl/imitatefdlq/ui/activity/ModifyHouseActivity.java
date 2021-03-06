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
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
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
import com.ydl.imitatefdlq.entity.RoomBean;
import com.ydl.imitatefdlq.entity.RoomBeanDao;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.util.BitmapUtil;
import com.ydl.imitatefdlq.util.CacheTask;
import com.ydl.imitatefdlq.util.EditTextUtil;
import com.ydl.imitatefdlq.util.LruCacheUtil;
import com.ydl.imitatefdlq.widget.RoundImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyHouseActivity extends BaseActivity {

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
    private List<String> optionList;//选择照片获取方式
    private File pictures;//Pictures目录
    private File file;//fdlq.jpg
    private Uri imageUri;//拍照返回的uri
    private String imagePath;//选取相册照片后返回的照片路径
    private String picUUID;//缓存图片的名称
    private String houseId;//房间id

    //每一个对象代表一张表
    private HouseBeanDao houseBeanDao;
    private PictureBeanDao pictureBeanDao;
    private RoomBeanDao roomBeanDao;

    private List<HouseBean> houseBeanList;
    private List<PictureBean> pictureBeanList;

    @Override
    protected int getContentView() {
        return R.layout.activity_modify_house;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        EditTextUtil.clearButtonListener(etHouseName, ivClear);

        initData();

        initOptionPicker();

        setTitle("修改房产");

        setTopLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        setTopRightButton("保存", 0, new OnClickListener() {
            @Override
            public void onClick() {
                if (TextUtils.isEmpty(etHouseName.getText().toString().trim())) {
                    StyledDialog.buildIosAlert("提醒", "房产名不能为空", new MyDialogListener() {
                        @Override
                        public void onFirst() {

                        }

                        @Override
                        public void onSecond() {

                        }
                    }).show();
                } else {
                    //Todo 点击保存，同步服务器

                    //同步house表的内容
                    HouseBean houseBean = houseBeanList.get(0);
                    houseBean.setHouseName(etHouseName.getText().toString());
                    houseBean.setHouseType(tvHouseType.getText().toString());
                    houseBeanDao.update(houseBean);
                    //同步picture表的内容
                    List<PictureBean> pictureBeanList = pictureBeanDao.queryBuilder()
                            .where(PictureBeanDao.Properties.ForeignId.eq(houseId))
                            .list();
                    if (!optionList.contains("删除")) {//说明用户删除了房产照片
                        if (pictureBeanList.size() != 0) {//如果picture表中原来就有房产照片
                            pictureBeanDao.deleteByKey(pictureBeanList.get(0).getId());//那么删除房产照片
                        }
                    } else if (picUUID != null) {//说明用户更换了房产照片
                        if (pictureBeanList.size() != 0) {//如果picture表中原来就有房产照片
                            pictureBeanDao.deleteByKey(pictureBeanList.get(0).getId());//先删除旧的房产照片
                        }
                        //再把新的照片存进去
                        PictureBean pictureBean = new PictureBean();
                        pictureBean.setId(UUID.randomUUID().toString());
                        pictureBean.setPath(pictures.getAbsolutePath() + "/" + picUUID + ".jpg");
                        pictureBean.setForeignId(houseId);
                        pictureBean.setOrderNumber(new Date());
                        pictureBean.setDataUpload(0);
                        pictureBean.setUploadUrl(null);//Todo 上传服务器地址
                        pictureBean.setSortNo(0);
                        pictureBeanDao.insert(pictureBean);
                    }
                    finish();

                }
            }
        });
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
                //TODO 收款账户变更
                break;
            case R.id.tv_delete:
                deleteHouse();
                break;
        }
    }

    private void deleteHouse() {
        StyledDialog.buildIosAlert("删除确认", "删除房产将一并删除其所有的房号，租客及账单，您确定要删除吗？", new MyDialogListener() {
            @Override
            public void onFirst() {
                //Todo 联网删除服务器数据

                //删除house表中这一行数据
                houseBeanDao.deleteByKey(houseId);
                //删除picture表中house对应照片的数据
                List<PictureBean> housePictureBeanList = pictureBeanDao.queryBuilder()
                        .where(PictureBeanDao.Properties.ForeignId.
                                eq(houseId))
                        .list();
                if (pictureBeanList.size() != 0) {
                    pictureBeanDao.deleteByKey(pictureBeanList.get(0).getId());
                }
                //删除room表中跟房间id相关的房号
                List<RoomBean> roomBeanList = roomBeanDao.queryBuilder()
                        .where(RoomBeanDao.Properties.HouseId.
                                eq(houseId))
                        .list();
                if (roomBeanList.size() != 0) {//房间不为空
                    String roomId;
                    for (int i = 0; i < roomBeanList.size(); i++) {
                        roomId = roomBeanList.get(i).getId();
                        roomBeanDao.deleteByKey(roomId);//删除相应的房间
                        List<PictureBean> roomPictureBeanList = pictureBeanDao.queryBuilder()
                                .where(PictureBeanDao.Properties.ForeignId.eq(roomId))
                                .list();
                        //删除picture表中room相关的照片数据
                        if (roomPictureBeanList.size() != 0) {//第i个房间照片不为空
                            for (int j = 0; j < roomPictureBeanList.size(); j++) {
                                pictureBeanDao.deleteByKey(roomPictureBeanList.get(j).getId());//删除第i个房间的每一张照片
                            }
                        }
                    }
                }
                Intent intent = new Intent(ModifyHouseActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("home_property", "home_property");
                startActivity(intent);
            }

            @Override
            public void onSecond() {

            }
        }).show();
    }

    private void initData() {
        optionList = new ArrayList<>();
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
            imageUri = FileProvider.getUriForFile(ModifyHouseActivity.this,
                    "com.ydl.imitatefdlq.fileprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }
        //获取数据库文件
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        houseBeanDao = daoSession.getHouseBeanDao();
        pictureBeanDao = daoSession.getPictureBeanDao();
        roomBeanDao = daoSession.getRoomBeanDao();

        houseId = getIntent().getStringExtra("house_id");
        houseBeanList = houseBeanDao.queryBuilder()
                .where(HouseBeanDao.Properties.Id.eq(houseId))
                .list();
        //设置房产名称
        etHouseName.setText(houseBeanList.get(0).getHouseName());
        //设置EditText光标在最后
        etHouseName.requestFocus();
        etHouseName.setSelection(houseBeanList.get(0).getHouseName().length());
        //设置房产类型
        tvHouseType.setText(houseBeanList.get(0).getHouseType());
        //获取房产照片
        pictureBeanList = pictureBeanDao.queryBuilder()
                .where(PictureBeanDao.Properties.ForeignId.eq(houseId))
                .list();
        if (pictureBeanList.size() != 0) {
            Bitmap bitmap = LruCacheUtil.getInstance().getBitmapFromMemoryCache(pictureBeanList.get(0).getPath());
            if (bitmap == null) {//内存中没有缓存
                ivHousePhoto.setImageResource(R.drawable.ic_add_photo);
            } else {//内存中有缓存
                ivHousePhoto.setImageBitmap(bitmap);
            }
            optionList.add("拍照");
            optionList.add("从手机相册选择");
            optionList.add("删除");
        } else {
            ivHousePhoto.setImageResource(R.drawable.ic_add_photo);
            optionList.add("拍照");
            optionList.add("从手机相册选择");
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

    private void initStyledDialog() {
        StyledDialog.buildIosSingleChoose(optionList, new MyItemDialogListener() {
            @Override
            public void onItemClick(final CharSequence charSequence, int i) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (charSequence.toString()) {
                            case "拍照":
                                if (ContextCompat.checkSelfPermission(ModifyHouseActivity.this,
                                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {//不同意就弹权限框
                                    ActivityCompat.requestPermissions(ModifyHouseActivity.this,
                                            new String[]{Manifest.permission.CAMERA}, 1);
                                } else {//同意拍照就打开摄像头
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    startActivityForResult(intent, TAKE_PHOTO);
                                }
                                break;
                            case "从手机相册选择":
                                if (ContextCompat.checkSelfPermission(ModifyHouseActivity.this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//不同意就弹权限框
                                    ActivityCompat.requestPermissions(ModifyHouseActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                                } else {//同意就打开相册
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, CHOOSE_PHOTO);
                                }
                                break;
                            case "删除":
                                ivHousePhoto.setImageResource(R.drawable.ic_add_photo);
                                picUUID = null;
                                if (optionList.contains("删除")) {
                                    optionList.remove("删除");
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }, 100);
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
                    StyledDialog.buildLoading("请稍后").setCancelable(false, false).show();
                    //1.Picture下生成拍照原图fdlq.jpg (默认生成)，imageUri和file都包含这张图片的地址信息
                    //2.压缩拍照原图fdlq.jpg，长宽像素都为原来的一半，并生成uuid.jpg
                    picUUID = UUID.randomUUID().toString();
                    File file = BitmapUtil.saveBitmapToFile(this.file, pictures.getPath() + "/" + picUUID + ".jpg");
                    //3.xBitmapCache缓存一张,名字为MD5Encoder.encode(uuid)。同时在内存中也缓存一张
                    //4.将拍照后的图片设置到imageView
                    new CacheTask(ModifyHouseActivity.this, ivHousePhoto, R.drawable.ic_add_photo)
                            .execute(file.getAbsolutePath());

                    if (!optionList.contains("删除")) {
                        optionList.add("删除");
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    StyledDialog.buildLoading("请稍后").setCancelable(false, false).show();
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                    if (!optionList.contains("删除")) {
                        optionList.add("删除");
                    }
                    picUUID = UUID.randomUUID().toString();
                    //1.Picture下缓存一张
                    String cachePath = pictures.getPath() + File.separator + picUUID + ".jpg";
                    copyFile(imagePath, cachePath);
                    //2.xBitmapCache缓存一张,名字为MD5Encoder.encode(uuid)。同时在内存中也缓存一张
                    //3.将拍照后的图片设置到imageView
                    new CacheTask(ModifyHouseActivity.this, ivHousePhoto, R.drawable.ic_add_photo)
                            .execute(cachePath);
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
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(srcPath);
            fos = new FileOutputStream(desPath);
            byte[] b = new byte[1024];
            int len;
            while ((len = fis.read(b)) != -1) {
                fos.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {//先关闭
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            if (EditTextUtil.isShouldHideInput(view, ev)) {
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
