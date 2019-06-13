package com.creative.servicetesting;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
public class LocalWordService extends Service {
    private final IBinder mBinder = new MyBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;
    private List<String> resultList = new ArrayList<String>();
    private int counter = 1;

    private Thread mThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("DEBUG", "on start command called");
       // addResultValues();
        findInBackground();
        return Service.START_STICKY;
    }


    private void findInBackground(){
        mThread = new Thread(new Runnable() {
            boolean abort = false;

            public void run() {

                while (!abort) {


                    try {



                        if(serviceCallbacks != null){
                            serviceCallbacks.doSomething();
                        }else{
                            Log.d("DEBUG","now i am in background haha");
                        }

                        Thread.sleep(5000);


                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        abort = true;
                        e.printStackTrace();
                    }

                }

            }
        });
        mThread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("DEBUG", "on bind called");
        //addResultValues();
        return mBinder;
    }

    public class MyBinder extends Binder {
        LocalWordService getService() {
            return LocalWordService.this;
        }
    }

    public List<String> getWordList() {
        return resultList;
    }

    private void addResultValues() {
        Random random = new Random();
        List<String> input = Arrays.asList("Linux", "Android","iPhone","Windows7" );
        resultList.add(input.get(random.nextInt(3)) + " " + counter++);
        if (counter == Integer.MAX_VALUE) {
            counter = 0;
        }
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    public interface ServiceCallbacks {
        void doSomething();
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        //Log.v("STOP_SERVICE", "DONE");
        if(mThread != null)
            mThread.interrupt();
        //stopSelf();
    }
}