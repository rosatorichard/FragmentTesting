package com.batchmates.android.fragmenttesting;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Android on 7/2/2017.
 */

public class CountThreader extends Thread{

    private int counter=0;
    TextView textView;
    private boolean running=false;
    private Main2Activity mainthread;


    Handler handler=new Handler(Looper.getMainLooper());
    public CountThreader(TextView text,boolean running,Main2Activity mainthread, int counter) {
        this.running = running;
        this.textView= text;
        this.mainthread=mainthread;
        this.counter=counter;
    }


    public void changeRunning()
    {
        running=false;
    }


    @Override
    public void run() {


        while(running)
        {
            if (Thread.currentThread().isInterrupted())
            {

            }
//            counter++;
            try {
                java.lang.Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d("Thread", "run: "+counter);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("WTF", "run: made it "+textView.getText());
                    textView.setText(String.valueOf(counter));
                    mainthread.update(counter);


                }
            });
            counter++;
//
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("Increment counter", "run: Made it");
//                    textView.setText(String.valueOf(counter));
//                }
//            });
        }
    }
}
