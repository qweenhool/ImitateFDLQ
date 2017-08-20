package com.ydl.imitatefdlq.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ydl.imitatefdlq.R;

/**
 * Created by qweenhool on 2017/8/20.
 */

public class BatchAddRoomFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_batch_add_room, container, false);
        return view;
    }
}
