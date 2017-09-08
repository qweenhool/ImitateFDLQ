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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.ui.fragment.AddRoomFragment;
import com.ydl.imitatefdlq.ui.fragment.BatchAddRoomFragment;
import com.ydl.imitatefdlq.util.BitmapUtil;
import com.ydl.imitatefdlq.util.CacheTask;
import com.ydl.imitatefdlq.util.DiskLruCacheUtil;
import com.ydl.imitatefdlq.util.EditTextUtil;

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

public class AddHouseActivity extends BaseActivity {

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

    private Uri imageUri;//拍照返回的uri
    private File pictures;//Pictures目录
    private File file;//fdlq.jpg
    private String imagePath;//选取相册照片后返回的照片路径
    private String picUUID;//缓存图片的名称
    private String[] houseTypeArr;
    private OptionsPickerView opvHouseType;
    private List<String> optionList;//选择照片获取方式
    private AddRoomFragment addRoomFragment;
    private BatchAddRoomFragment batchAddRoomFragment;
    private boolean isBatchAddRoom;//批量添加房号开关
    private DiskLruCache diskLruCache;

    //每一个对象代表一张表
    private HouseBeanDao houseBeanDao;
    private RoomBeanDao roomBeanDao;
    private PictureBeanDao pictureBeanDao;

    private HouseBean houseBean;
    private RoomBean roomBean;
    private PictureBean pictureBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_house;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        EditTextUtil.clearButtonListener(etAddHouseName, ivClear);

        initData();
        //构建条件选择器，此处为房产类型的选择
        initOptionPicker();

        initFragment(savedInstanceState);

        setTitle("添加房产");

        setTopLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        setTopRightButton("保存", 0, new OnClickListener() {
            @Override
            public void onClick() {
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
                    Intent roomNumberIntent = new Intent(AddHouseActivity.this, RoomNumberActivity.class);
                    roomNumberIntent.putExtra("house_id", houseBean.getId());
                    startActivity(roomNumberIntent);
                    finish();
                }
            }
        });

    }

    @OnClick({R.id.ll_house_type, R.id.ll_house_photo, R.id.ll_receive_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_house_type:
                opvHouseType.show();
                break;
            case R.id.ll_house_photo:
                showStyledDialog();
                break;
            case R.id.ll_receive_account:
                Intent accountIntent = new Intent(this, PayeeAccountActivity.class);
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

    private void initData() {
        optionList = new ArrayList<>();
        optionList.add("拍照");
        optionList.add("从手机相册选择");
        diskLruCache = DiskLruCacheUtil.getDiskLruCache(this);
        //创建缓存目录 cache/xBitmapCache
        File xBitmapCache = new File(getExternalCacheDir().getPath() + "/xBitmapCache");
        if (!xBitmapCache.exists()) {
            xBitmapCache.mkdirs();
        }
        //创建缓存目录 cache/.nomedia
        File noMedia = new File(getExternalCacheDir().getPath() + "/.nomedia");
        if (!noMedia.exists()) {
            try {
                noMedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //创建缓存目录 files/DCIM
        File dcim = new File(getExternalFilesDir(null).getPath() + "/DCIM");
        if (!dcim.exists()) {
            dcim.mkdirs();
        }
        //创建缓存目录 files/Pictures
        pictures = new File(getExternalFilesDir(null).getPath() + "/Pictures");
        if (!pictures.exists()) {
            pictures.mkdirs();
        }

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
            imageUri = FileProvider.getUriForFile(AddHouseActivity.this,
                    "com.ydl.imitatefdlq.fileprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }
        //获取数据库文件
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        houseBeanDao = daoSession.getHouseBeanDao();
        roomBeanDao = daoSession.getRoomBeanDao();
        pictureBeanDao = daoSession.getPictureBeanDao();
        //获取bean对象
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

    private void showStyledDialog() {
        StyledDialog.buildIosSingleChoose(optionList, new MyItemDialogListener() {
            @Override
            public void onItemClick(final CharSequence charSequence, int i) {
                //为了解决跳转黑屏问题，采用延时执行
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (charSequence.toString()) {
                            case "拍照":
                                if (ContextCompat.checkSelfPermission(AddHouseActivity.this,
                                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {//不同意就弹权限框
                                    ActivityCompat.requestPermissions(AddHouseActivity.this,
                                            new String[]{Manifest.permission.CAMERA}, 1);
                                } else {//同意拍照就打开摄像头
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    startActivityForResult(intent, TAKE_PHOTO);
                                }
                                break;
                            case "从手机相册选择":
                                if (ContextCompat.checkSelfPermission(AddHouseActivity.this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//不同意就弹权限框
                                    ActivityCompat.requestPermissions(AddHouseActivity.this,
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
        //往picture表中存数据
        if (picUUID != null) {//不为空说明用户点击了成功拍照或者从图库选择了一张图片
            pictureBean.setId(UUID.randomUUID().toString());
            pictureBean.setPath(pictures.getAbsolutePath() + "/" + picUUID + ".jpg");
            pictureBean.setForeignId(uuid);
            pictureBean.setOrderNumber(new Date());
            pictureBean.setDataUpload(0);
            pictureBean.setUploadUrl(null);//Todo 上传服务器地址
            pictureBean.setSortNo(0);
            pictureBeanDao.insert(pictureBean);
        }
        //往room表中存数据
        //第一种情况，默认添加房号
        LinearLayout container = (LinearLayout) getSupportFragmentManager()
                .findFragmentByTag(AddRoomFragment.class.getSimpleName())
                .getView()
                .findViewById(R.id.ll_container);
        EditText et;
        int n = 1;
        for (int i = 0; i < container.getChildCount(); i++) {
            et = (EditText) container.getChildAt(container.getChildCount() - i - 1).findViewById(R.id.et_add_room_number);
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
                if (resultCode == RESULT_OK) {//拍照成功后返回
                    StyledDialog.buildLoading("请稍后").show();
                    //1.Picture下生成拍照原图fdlq.jpg (默认生成)，imageUri和file都包含这张图片的地址信息
                    //TODO 2.压缩拍照原图fdlq.jpg，长宽像素都为原来的一半，并生成uuid.jpg
                    picUUID = UUID.randomUUID().toString();
                    File file = BitmapUtil.saveBitmapToFile(this.file, pictures.getPath() + "/" + picUUID + ".jpg");
                    //3.xBitmapCache缓存一张,名字为MD5Encoder.encode(uuid)。同时在内存中也缓存一张
                    //4.将拍照后的图片设置到imageView
                    new CacheTask(AddHouseActivity.this, ivHousePhoto, R.drawable.ic_add_photo)
                            .execute(file.getAbsolutePath());

                    if (!optionList.contains("删除")) {
                        optionList.add("删除");
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {//相册选取成功后返回
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
                    new CacheTask(AddHouseActivity.this, ivHousePhoto, R.drawable.ic_add_photo)
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
