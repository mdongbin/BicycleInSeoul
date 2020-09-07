package com.example.bicycleinseoul;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

public class AlarmFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        EasyCountDownTextview easyCountDownTextview = (EasyCountDownTextview) view.findViewById(R.id.easyCountDownTextview);

        easyCountDownTextview.setTime(1,1, 30, 15);// setTime(days, hours, minute, second)
        easyCountDownTextview.setTextSize(30);
        easyCountDownTextview.startTimer();
//        EasyCountDownTextview countDownTextview = (EasyCountDownTextview) view.findViewById(R.id.easyCountDownTextview);
//        easyCountDownTextview.setTime(1, 1, 1, 1);

        easyCountDownTextview.setOnTick(new CountDownInterface() {
            @Override
            public void onTick(long time) {
                Log.e("#", "tick!");
            }

            @Override
            public void onFinish() {
                Log.e("@", "finish!");

            }
        });

        return view;
    }
}
