package com.ydl.imitatefdlq.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.AddRoomBean;

/**
 * Created by qweenhool on 2017/8/16.
 */

public class AddRoomAdapter extends BaseQuickAdapter<AddRoomBean, BaseViewHolder> {


    public AddRoomAdapter() {
        super(R.layout.item_null);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddRoomBean item) {

        helper.addOnClickListener(R.id.iv_remove_room);
    }
}
