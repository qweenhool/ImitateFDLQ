package com.ydl.imitatefdlq.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.activity.OtherActivity;
import com.ydl.imitatefdlq.activity.SearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;


/**
 * Created by qweenhool on 2017/8/4.
 */

public class FirstFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.bt_uncollected)
    Button btUncollected;
    @BindView(R.id.bt_unpayed)
    Button btUnpayed;
    @BindView(R.id.bt_payed)
    Button btPayed;
    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.tv_income_number)
    TextView tvIncomeNumber;
    @BindView(R.id.tv_expenditure_number)
    TextView tvExpenditureNumber;
    @BindView(R.id.tv_surplus_number)
    TextView tvSurplusNumber;
    @BindView(R.id.tv_upcoming)
    TextView tvUpcoming;
    @BindView(R.id.ll_upcoming)
    LinearLayout llUpcoming;
    @BindView(R.id.tv_renter)
    TextView tvRenter;
    @BindView(R.id.ll_renter)
    LinearLayout llRenter;
    @BindView(R.id.tv_owner)
    TextView tvOwner;
    @BindView(R.id.ll_owner)
    LinearLayout llOwner;
    @BindView(R.id.ll_bill)
    LinearLayout llBill;
    @BindView(R.id.ll_should_collect)
    LinearLayout llShouldCollect;
    @BindView(R.id.ll_should_pay)
    LinearLayout llShouldPay;
    @BindView(R.id.ll_idle_room)
    LinearLayout llIdleRoom;
    @BindView(R.id.ll_reminder)
    LinearLayout llReminder;
    @BindView(R.id.ll_water_meter)
    LinearLayout llWaterMeter;
    @BindView(R.id.ll_send_notice)
    LinearLayout llSendNotice;
    @BindView(R.id.refresh_layout)
    BGARefreshLayout refreshLayout;
    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;

    private Toolbar toolbar;
    private AppCompatActivity activity;
    private BGARefreshViewHolder viewHolder;
    private Unbinder unbinder;
    private static final String TAG = FirstFragment.class.getSimpleName();

    public FirstFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView---");
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        unbinder = ButterKnife.bind(this, view);

        //这句不添加home按钮点击无效
        setHasOptionsMenu(true);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_search);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        tvToolbarName.setText("首页");

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //给actionbar添加空标题，否则会一直显示应用标题
            actionBar.setTitle("");
        }

        refreshLayout = (BGARefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setDelegate(this);
        viewHolder = new BGANormalRefreshViewHolder(getContext(), false);
        viewHolder.setRefreshViewBackgroundColorRes(R.color.colorRefreshBackGround);
        refreshLayout.setRefreshViewHolder(viewHolder);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart---");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume---");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause---");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop---");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent searchIntent = new Intent(getContext(), SearchActivity.class);
                startActivity(searchIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                //模拟耗时操作
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                refreshLayout.endRefreshing();

            }
        }.execute();


    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_uncollected, R.id.bt_unpayed, R.id.bt_payed, R.id.iv_ad, R.id.ll_upcoming, R.id.ll_renter, R.id.ll_owner, R.id.ll_bill, R.id.ll_should_collect, R.id.ll_should_pay, R.id.ll_idle_room, R.id.ll_reminder, R.id.ll_water_meter, R.id.ll_send_notice})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.bt_uncollected:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_unpayed:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_payed:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_ad:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_upcoming:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_renter:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_owner:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_bill:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_should_collect:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_should_pay:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_idle_room:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_reminder:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_water_meter:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_send_notice:
                intent = new Intent(getContext(), OtherActivity.class);
                startActivity(intent);
                break;
        }
    }
}
