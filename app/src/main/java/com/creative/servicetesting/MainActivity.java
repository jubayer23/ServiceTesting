package com.creative.servicetesting;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ServiceConnection, LocalWordService.ServiceCallbacks {

    private LocalWordService s;
    private boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isMyServiceRunning(LocalWordService.class)) {
            Log.d("DEBUG", " service started");
            Intent i = new Intent(this, LocalWordService.class);
            // potentially add data to the intent
            // i.putExtra("KEY1", "Value to be used by the service");
            startService(i);
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, LocalWordService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (bound) {
            s.setCallbacks(null); // unregister
            unbindService(this);
            bound = false;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        LocalWordService.MyBinder b = (LocalWordService.MyBinder) binder;
        s = b.getService();
        bound = true;
        s.setCallbacks(MainActivity.this);
        Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        s = null;
        bound = false;
    }

    @Override
    public void doSomething() {

        Log.d("DEBUG"," its calling fun");
    }
}
