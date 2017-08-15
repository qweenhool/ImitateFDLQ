package com.ydl.imitatefdlq.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class HomePageFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

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
    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.tb_home_page)
    Toolbar tbHomePage;
    @BindView(R.id.rl_home_page)
    BGARefreshLayout rlHomePage;

    private AppCompatActivity activity;
    private BGARefreshViewHolder viewHolder;
    private Unbinder unbinder;
    private static final String TAG = HomePageFragment.class.getSimpleName();

    public HomePageFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        unbinder = ButterKnife.bind(this, view);

        setToolbar();

        setRefreshLayout();

        return view;
    }

    private void setRefreshLayout() {
        rlHomePage.setDelegate(this);
        viewHolder = new BGANormalRefreshViewHolder(getContext(), false);
        viewHolder.setRefreshViewBackgroundColorRes(R.color.colorRefreshBackGround);
        rlHomePage.setRefreshViewHolder(viewHolder);

    }

    private void setToolbar() {
        //这句不添加home按钮点击无效
        setHasOptionsMenu(true);

        tbHomePage.setNavigationIcon(R.drawable.ic_search);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(tbHomePage);
        tvToolbarName.setText("首页");

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //给actionbar添加空标题，否则会一直显示应用标题
            actionBar.setTitle("");
        }

        tbHomePage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });
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
