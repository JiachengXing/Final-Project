package com.example.project.widget;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

public class MyClickListener implements View.OnTouchListener {

    private static int timeout=400;
    private int clickCount = 0;
    private Handler handler;
    private MyClickCallBack myClickCallBack;
    public interface MyClickCallBack{
        void oneClick();
        void doubleClick();

    }


    public MyClickListener(MyClickCallBack myClickCallBack) {
        this.myClickCallBack = myClickCallBack;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            clickCount++;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (clickCount == 1) {
                        myClickCallBack.oneClick();
                    }else if(clickCount==2){
                        myClickCallBack.doubleClick();
                    }
                    handler.removeCallbacksAndMessages(null);
                    clickCount = 0;
                }
            },timeout);
        }
        return false;
    }
}
