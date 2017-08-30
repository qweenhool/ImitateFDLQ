package com.ydl.imitatefdlq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.PayeeAccountBean;
import com.ydl.imitatefdlq.entity.PayeeAccountBeanDao;

import java.util.List;

/**
 * Created by qweenhool on 2017/8/18.
 */

public class PayeeAccountAdapter extends RecyclerView.Adapter<PayeeAccountAdapter.AccountViewHolder> {

    private Context mContext;
    private List<PayeeAccountBean> mPayeeAccountBeanList;
    private LayoutInflater mInflater;

    //每一个对象代表一张表
    private PayeeAccountBeanDao payeeAccountBeanDao;

    public PayeeAccountAdapter(Context context, List<PayeeAccountBean> payeeAccountBeanList) {
        mContext = context;
        mPayeeAccountBeanList = payeeAccountBeanList;
        mInflater = LayoutInflater.from(mContext);
        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        payeeAccountBeanDao = daoSession.getPayeeAccountBeanDao();

    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_payee_account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        PayeeAccountBean payeeAccountBean = mPayeeAccountBeanList.get(position);
        switch (payeeAccountBean.getAccountType()){
            case 1://银行卡
                holder.ivType.setImageResource(R.drawable.unionpay);
                holder.tvType.setText(payeeAccountBean.getAccountName());
                holder.tvCardholder.setText(payeeAccountBean.getCardHolder());
                holder.tvCardNumber.setText(payeeAccountBean.getCardNumber());
                if(!TextUtils.isEmpty(payeeAccountBean.getDepositBranch())){
                    holder.tvDepositBranch.setText(payeeAccountBean.getDepositBranch());
                }else {
                    holder.tvDepositBranch.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(payeeAccountBean.getRemark())){
                    holder.tvRemark.setText(payeeAccountBean.getRemark());
                    holder.viewLine.setVisibility(View.VISIBLE);
                    holder.tvRemark.setVisibility(View.VISIBLE);
                }else {
                    holder.tvRemark.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                }
                break;
            case 2://微信
                holder.ivType.setImageResource(R.drawable.wechat);
                holder.tvType.setText(payeeAccountBean.getAccountName());
                holder.tvCardNumber.setText(payeeAccountBean.getWechatAccount());
                if(!TextUtils.isEmpty(payeeAccountBean.getRemark())){
                    holder.tvRemark.setText(payeeAccountBean.getRemark());
                    holder.viewLine.setVisibility(View.VISIBLE);
                    holder.tvRemark.setVisibility(View.VISIBLE);
                }else {
                    holder.tvRemark.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                }
                holder.tvCardholder.setVisibility(View.GONE);
                holder.tvDepositBranch.setVisibility(View.GONE);
                break;
            case 3://支付宝
                holder.ivType.setImageResource(R.drawable.alipay);
                holder.tvType.setText(payeeAccountBean.getAccountName());
                holder.tvCardNumber.setText(payeeAccountBean.getAlipayAccount());
                if(!TextUtils.isEmpty(payeeAccountBean.getRemark())){
                    holder.tvRemark.setText(payeeAccountBean.getRemark());
                    holder.viewLine.setVisibility(View.VISIBLE);
                    holder.tvRemark.setVisibility(View.VISIBLE);
                }else {
                    holder.tvRemark.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                }
                holder.tvCardholder.setVisibility(View.GONE);
                holder.tvDepositBranch.setVisibility(View.GONE);
                break;
            case 4://其他
                holder.ivType.setImageResource(R.drawable.other);
                holder.tvType.setText(payeeAccountBean.getRemark());
                holder.tvCardholder.setVisibility(View.GONE);
                holder.tvDepositBranch.setVisibility(View.GONE);
                holder.tvCardNumber.setVisibility(View.GONE);
                break;
            default:
                break;
        }


    }

    @Override
    public int getItemCount() {
        return mPayeeAccountBeanList.size();
    }


    class AccountViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivType;
        private TextView tvType;
        private TextView tvDepositBranch;
        private TextView tvCardholder;
        private TextView tvCardNumber;
        private TextView tvRemark;
        private View viewLine;

        public AccountViewHolder(View itemView) {
            super(itemView);

            ivType = (ImageView) itemView.findViewById(R.id.iv_type);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvDepositBranch = (TextView) itemView.findViewById(R.id.tv_deposit_branch);
            tvCardholder = (TextView) itemView.findViewById(R.id.tv_cardholder);
            tvCardNumber = (TextView) itemView.findViewById(R.id.tv_card_number);
            tvRemark = (TextView) itemView.findViewById(R.id.tv_remark);
            viewLine = itemView.findViewById(R.id.view_line);

        }
    }
}
