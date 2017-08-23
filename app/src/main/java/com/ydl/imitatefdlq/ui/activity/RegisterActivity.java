package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.login_services)
    TextView loginServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorStatusbar));

    }

    @OnClick(R.id.btn_register)
    public void onViewClicked() {
        if (NetworkState.networkConnected(this)) {
            String account = etAccount.getText().toString();
            String password = etPassword.getText().toString();
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                StyledDialog.buildLoading("注册中").show();
                String info = account + "#" + password;
                OkGo.<String>post(Config.REG_URL)
                        .tag(this)
                        .params(Config.ACCESS_KEY, Base64.getBase64(info))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                ResultJson resultJson = new Gson().fromJson(response.body(), ResultJson.class);
                                switch (resultJson.getErrorCode()) {
                                    case 0://成功
                                        String data = resultJson.getData();
                                        StyledDialog.dismissLoading();
                                        Toast.makeText(RegisterActivity.this, data, Toast.LENGTH_SHORT).show();
                                        //Todo 跳转到主界面
                                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        break;
                                    case 1://服务器异常
                                        String errorString1 = resultJson.getErrorString();
                                        StyledDialog.dismissLoading();
                                        Toast.makeText(RegisterActivity.this, errorString1, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2://用户名格式不对
                                        String errorString2 = resultJson.getErrorString();
                                        StyledDialog.dismissLoading();
                                        Toast.makeText(RegisterActivity.this, errorString2, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3://密码格式不对
                                        String errorString3 = resultJson.getErrorString();
                                        StyledDialog.dismissLoading();
                                        Toast.makeText(RegisterActivity.this, errorString3, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 4://用户已存在
                                        String errorString4 = resultJson.getErrorString();
                                        StyledDialog.dismissLoading();
                                        Toast.makeText(RegisterActivity.this, errorString4, Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                                Toast.makeText(RegisterActivity.this, "服务器不可用，请稍后重试", Toast.LENGTH_SHORT).show();
                                StyledDialog.dismissLoading();
                            }
                        });
            } else {
                Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出的时候取消请求
        OkGo.getInstance().cancelTag(this);
    }
}
