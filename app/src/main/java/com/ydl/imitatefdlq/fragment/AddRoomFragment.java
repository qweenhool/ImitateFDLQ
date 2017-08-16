package com.ydl.imitatefdlq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.adapter.AddRoomAdapter;
import com.ydl.imitatefdlq.entity.AddRoomBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qweenhool on 2017/8/15.
 */

public class AddRoomFragment extends Fragment  {

    @BindView(R.id.rv_add_room)
    RecyclerView rvAddRoom;
    Unbinder unbinder;

    private AddRoomAdapter mAdapter;
    private List<AddRoomBean> beanList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_room, container, false);
        unbinder = ButterKnife.bind(this, view);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        rvAddRoom.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AddRoomAdapter();
        mAdapter.addHeaderView(getHeaderView());
        rvAddRoom.setAdapter(mAdapter);
    }

    private View getHeaderView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_header, (ViewGroup) rvAddRoom.getParent(), false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(getContext()).inflate(R.layout.item_remove_header, (ViewGroup) rvAddRoom.getParent(), false);
                mAdapter.addHeaderView(view,0);
                ImageView removeRoom = (ImageView) view.findViewById(R.id.iv_remove_room);
                removeRoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.removeHeaderView(view);
                    }
                });
                EditText editText = (EditText) view.findViewById(R.id.et_add_room_number);
                String trim = editText.getText().toString().trim();

            }
        });

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
