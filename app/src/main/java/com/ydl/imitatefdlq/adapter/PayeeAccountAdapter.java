package com.ydl.imitatefdlq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.PayeeAccountBean;
import com.ydl.imitatefdlq.widget.RoundImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qweenhool on 2017/8/18.
 */

public class PayeeAccountAdapter extends RecyclerView.Adapter<PayeeAccountAdapter.AccountViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<PayeeAccountBean> mPayeeAccountBeanList;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();

    public PayeeAccountAdapter(Context context, List<PayeeAccountBean> payeeAccountBeanList) {
        mContext = context;
        mPayeeAccountBeanList = payeeAccountBeanList;
        mInflater = LayoutInflater.from(mContext);
        initMap();

    }

    //初始化map集合,默认为不选中
    private void initMap() {
        for (int i = 0; i < mPayeeAccountBeanList.size(); i++) {
            map.put(i, false);
        }
    }

    public Map<Integer, Boolean> getMap() {
        return map;
    }

    //点击item选中CheckBox
    public void setSelectItem(int position) {
        //对当前状态取反
        if (map.get(position)) {
            map.put(position, false);
        } else {
            map.put(position, true);
        }
        notifyItemChanged(position);
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_payee_account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, final int position) {
        PayeeAccountBean payeeAccountBean = mPayeeAccountBeanList.get(position);
        switch (payeeAccountBean.getAccountType()) {
            case 1://银行卡
                Log.e("PayeeAccountAdapter", "银行卡 == ");
                holder.ivType.setImageResource(R.drawable.unionpay);
                holder.tvType.setText(payeeAccountBean.getDepositBank());
                holder.tvCardholder.setText(payeeAccountBean.getCardHolder());
                holder.tvCardNumber.setText(payeeAccountBean.getCardNumber());
                if (!TextUtils.isEmpty(payeeAccountBean.getDepositBranch())) {
                    holder.tvDepositBranch.setText(payeeAccountBean.getDepositBranch());
                    holder.tvDepositBranch.setVisibility(View.VISIBLE);
                } else {
                    holder.tvDepositBranch.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(payeeAccountBean.getRemark())) {
                    holder.tvRemark.setText(payeeAccountBean.getRemark());
                    holder.tvRemark.setVisibility(View.VISIBLE);
                    holder.viewLine.setVisibility(View.VISIBLE);
                } else {
                    holder.tvRemark.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                }
                holder.tvCardNumber.setVisibility(View.VISIBLE);
                holder.tvCardholder.setVisibility(View.VISIBLE);
                break;
            case 2://微信
                Log.e("PayeeAccountAdapter", "微信 == ");
                holder.ivType.setImageResource(R.drawable.wechat);
                holder.tvType.setText(payeeAccountBean.getAccountName());
                holder.tvCardNumber.setText(payeeAccountBean.getWechatAccount());
                if (!TextUtils.isEmpty(payeeAccountBean.getRemark())) {
                    holder.tvRemark.setText(payeeAccountBean.getRemark());
                    holder.tvRemark.setVisibility(View.VISIBLE);
                    holder.viewLine.setVisibility(View.VISIBLE);
                } else {
                    holder.tvRemark.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                }
                holder.tvCardNumber.setVisibility(View.VISIBLE);
                holder.tvCardholder.setVisibility(View.GONE);
                holder.tvDepositBranch.setVisibility(View.GONE);
                break;
            case 3://支付宝
                Log.e("PayeeAccountAdapter", "支付宝 == ");
                holder.ivType.setImageResource(R.drawable.alipay);
                holder.tvType.setText(payeeAccountBean.getAccountName());
                holder.tvCardNumber.setText(payeeAccountBean.getAlipayAccount());
                if (!TextUtils.isEmpty(payeeAccountBean.getRemark())) {
                    holder.tvRemark.setText(payeeAccountBean.getRemark());
                    holder.tvRemark.setVisibility(View.VISIBLE);
                    holder.viewLine.setVisibility(View.VISIBLE);
                } else {
                    holder.tvRemark.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                }
                holder.tvCardNumber.setVisibility(View.VISIBLE);
                holder.tvCardholder.setVisibility(View.GONE);
                holder.tvDepositBranch.setVisibility(View.GONE);
                break;
            case 4://其他
                Log.e("PayeeAccountAdapter", "其他 == ");
                holder.ivType.setImageResource(R.drawable.other);
                holder.tvType.setText(payeeAccountBean.getAccountName());
                holder.tvRemark.setText(payeeAccountBean.getRemark());
                holder.tvCardholder.setVisibility(View.GONE);
                holder.tvDepositBranch.setVisibility(View.GONE);
                holder.tvCardNumber.setVisibility(View.GONE);
                holder.viewLine.setVisibility(View.VISIBLE);
                holder.tvRemark.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        //设置Tag
        holder.itemView.setTag(position);
        //设置checkBox改变监听
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //用map集合保存
                map.put(position, isChecked);
            }
        });
        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
        }
        holder.checkBox.setChecked(map.get(position));

    }

    @Override
    public int getItemCount() {
        return mPayeeAccountBeanList.size();
    }

    class AccountViewHolder extends RecyclerView.ViewHolder{
        private RoundImageView ivType;
        private TextView tvType;
        private TextView tvDepositBranch;
        private TextView tvCardholder;
        private TextView tvCardNumber;
        private TextView tvRemark;
        private View viewLine;
        private CheckBox checkBox;

        public AccountViewHolder(View itemView) {
            super(itemView);

            ivType = (RoundImageView) itemView.findViewById(R.id.iv_type);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvDepositBranch = (TextView) itemView.findViewById(R.id.tv_deposit_branch);
            tvCardholder = (TextView) itemView.findViewById(R.id.tv_cardholder);
            tvCardNumber = (TextView) itemView.findViewById(R.id.tv_card_number);
            tvRemark = (TextView) itemView.findViewById(R.id.tv_remark);
            viewLine = itemView.findViewById(R.id.view_line);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_box);

        }
    }


}
