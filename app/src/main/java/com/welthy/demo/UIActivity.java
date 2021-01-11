package com.welthy.demo;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.welthy.demo.views.WaterWaveView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UIActivity extends AppCompatActivity {

    @BindView(R.id.bei_saier)
    WaterWaveView mBeiSaier;

    @BindView(R.id.btn_startAnim)
    Button btnStartAnim;
    @BindView(R.id.btn_stopAnim)
    Button btnStopAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_layout);
        ButterKnife.bind(this);

        btnStartAnim.setOnClickListener((l) -> {
            mBeiSaier.startAnim();
        });

        btnStopAnim.setOnClickListener((l) -> {
            mBeiSaier.stopAnim();
        });
    }
}
