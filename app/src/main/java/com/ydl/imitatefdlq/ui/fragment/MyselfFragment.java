package com.ydl.imitatefdlq.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.ui.activity.OtherActivity;
import com.ydl.imitatefdlq.ui.activity.PersonalInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qweenhool on 2017/8/4.
 */

public class MyselfFragment extends Fragment {

    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_autograph)
    TextView tvAutograph;
    @BindView(R.id.ll_personal_info)
    LinearLayout llPersonalInfo;
    @BindView(R.id.ll_online_collect_rents)
    LinearLayout llOnlineCollectRents;
    @BindView(R.id.ll_collect_account)
    LinearLayout llCollectAccount;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.ll_child_account)
    LinearLayout llChildAccount;
    @BindView(R.id.ll_custom_service_center)
    LinearLayout llCustomServiceCenter;
    @BindView(R.id.ll_system_notice)
    LinearLayout llSystemNotice;
    @BindView(R.id.ll_about)
    LinearLayout llAbout;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myself, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_personal_info, R.id.ll_online_collect_rents, R.id.ll_collect_account, R.id.ll_setting, R.id.ll_child_account, R.id.ll_custom_service_center, R.id.ll_system_notice, R.id.ll_about})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_personal_info:
                intent = new Intent(getContext(), PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_online_collect_rents:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_collect_account:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_setting:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_child_account:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_custom_service_center:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_system_notice:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_about:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
        }
    }
}
