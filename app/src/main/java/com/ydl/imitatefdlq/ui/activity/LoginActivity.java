package com.ydl.imitatefdlq.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ydl.imitatefdlq.Config;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.ResultJson;
import com.ydl.imitatefdlq.util.Base64;
import com.ydl.imitatefdlq.util.NetworkState;
import com.ydl.imitatefdlq.util.StatusBarCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_register_account)
    TextView tvRegisterAccount;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private SharedPreferences sp;
    private String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorStatusbar));

        initData();

    }

    private void initData() {
        sp = getSharedPreferences("Token", MODE_PRIVATE);
    }

    @OnClick({R.id.tv_forget_password, R.id.tv_register_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_password:

                break;
            case R.id.tv_register_account:
                //跳转到注册页面
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (NetworkState.networkConnected(this)) {
            //6.0以后需要加运行时权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            } else {
//                login();
                Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(this, "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    login();
                } else {
                    Toast.makeText(this, "你拒绝了该权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void login() {
        imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
            StyledDialog.buildLoading("登录中").show();
            String loginInfo = imei + "#" + account + "#" + password;
            OkGo.<String>post(Config.LOGIN_URL)
                    .tag(this)
                    .params(Config.ACCESS_KEY, Base64.getBase64(loginInfo))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {//UI线程
                            ResultJson resultJson = new Gson().fromJson(response.body(), ResultJson.class);
                            switch (resultJson.getErrorCode()) {
                                case 0://成功，返回token
                                    StyledDialog.dismissLoading();
                                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                    String data = resultJson.getData();
                                    String token = Base64.getBase64(data);
                                    sp.edit().putString("token", token).apply();
                                    //启动同步页面
                                    Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 1://服务器异常
                                    StyledDialog.dismissLoading();
                                    String errorString1 = resultJson.getErrorString();
                                    Toast.makeText(LoginActivity.this, errorString1, Toast.LENGTH_SHORT).show();
                                    break;
                                case 2://用户名不存在
                                    StyledDialog.dismissLoading();
                                    String errorString2 = resultJson.getErrorString();
                                    Toast.makeText(LoginActivity.this, errorString2, Toast.LENGTH_SHORT).show();
                                    break;
                                case 3://密码不正确
                                    StyledDialog.dismissLoading();
                                    String errorString3 = resultJson.getErrorString();
                                    Toast.makeText(LoginActivity.this, errorString3, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;

                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Toast.makeText(LoginActivity.this, "服务器不可用，请稍后重试", Toast.LENGTH_SHORT).show();
                            StyledDialog.dismissLoading();
                        }
                    });
        } else {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        Log.e(this.getClass().getSimpleName(), "onDestroy");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
