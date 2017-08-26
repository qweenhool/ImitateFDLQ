package com.ydl.imitatefdlq.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ydl.imitatefdlq.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qweenhool on 2017/8/15.
 */

public class AddRoomFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.ll_add_room)
    LinearLayout llAddRoom;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_room, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ll_add_room)
    public void onViewClicked() {

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.item_remove_header,llContainer,false);
        //点击添加房号添加一个view
        llContainer.addView(view, llContainer.getChildCount());
        //点击红色删除图标直接删除本行
        view.findViewById(R.id.iv_remove_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContainer.removeView(view);
            }
        });

    }

}
