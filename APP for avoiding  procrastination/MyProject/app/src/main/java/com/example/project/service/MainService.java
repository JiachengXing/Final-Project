package com.example.project.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.utils.CountDownTimer;
import com.example.project.utils.SPUtils;
import com.example.project.utils.TimeFormatUtil;

import java.util.concurrent.TimeUnit;


public class MainService extends Service implements View.OnTouchListener, CountDownTimer.OnCountDownTickListener {

  private static final String TAG = MainService.class.getSimpleName();

  private WindowManager windowManager;

  private View floatyView;

  private TextView mTextCountDown;

  private CountDownTimer mTimer;

  @Override
  public IBinder onBind(Intent intent) {

    return null;
  }

  @Override
  public void onCreate() {

    super.onCreate();

    windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

    addOverlayView();
  }

  private void addOverlayView() {

    final LayoutParams params;
    int layoutParamsType;

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      layoutParamsType = LayoutParams.TYPE_APPLICATION_OVERLAY;
    }
    else {
      layoutParamsType = LayoutParams.TYPE_PHONE;
    }

    params = new LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT,
        layoutParamsType,
        0,
        PixelFormat.OPAQUE);

    params.gravity = Gravity.CENTER | Gravity.START;
    params.x = 0;
    params.y = 0;

    FrameLayout interceptorLayout = new FrameLayout(this) {

      @Override
      public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

          if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            Log.v(TAG, "BACK Button Pressed");

            return true;
          }
        }

        return super.dispatchKeyEvent(event);
      }
    };

    LayoutInflater inflater = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

    if (inflater != null) {
      floatyView = inflater.inflate(R.layout.activity_focused, interceptorLayout);
      mTextCountDown = (TextView)floatyView.findViewById(R.id.text_count_down);
      floatyView.findViewById(R.id.btn_unlock).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onDestroy();
        }
      });

      long millsInTotal = TimeUnit.MINUTES.toMillis(getMinutesInTotal());

      mTimer = new CountDownTimer(millsInTotal);
      mTimer.setOnChronometerTickListener(this);
      mTimer.start();
      floatyView.setOnTouchListener(this);
      windowManager.addView(floatyView, params);
    }
    else {
      Log.e("main service", "Layout Inflater Service is null; can't inflate and display R.layout.activity_focused");
    }
  }

  public int getMinutesInTotal() {
    return  (int) SPUtils
            .get(this,"pref_key_short_break", 5);
  }

  private void updateText(long millisUntilFinished) {
    mTextCountDown.setText(TimeFormatUtil.formatTime(millisUntilFinished));
  }

  @Override
  public void onDestroy() {

    super.onDestroy();

    if (floatyView != null) {

      windowManager.removeView(floatyView);

      floatyView = null;
    }
  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent) {
    view.performClick();

    Log.v(TAG, "onTouch...");

    // Kill service
//    onDestroy();

    return true;
  }

  @Override
  public void onCountDownTick(long millisUntilFinished) {
    updateText(millisUntilFinished);

  }

  @Override
  public void onCountDownFinish() {
    onDestroy();

  }
}
