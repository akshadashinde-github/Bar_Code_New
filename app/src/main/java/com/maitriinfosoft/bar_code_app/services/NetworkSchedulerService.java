package com.maitriinfosoft.bar_code_app.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.maitriinfosoft.bar_code_app.connectivity.ConnectivityReceiver;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkSchedulerService extends JobService implements ConnectivityReceiver.ConnectivityReceiverListener{

    public static final String TAG="NetworkSchedulerService";
    ConnectivityReceiver connectivityReceiver;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"Service created");
        connectivityReceiver=new ConnectivityReceiver();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG,"onStartJob");
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityReceiver,intentFilter);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG,"onStopJob");
        unregisterReceiver(connectivityReceiver);
        return false;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        String message=isConnected ? "Connected to internet!" : "No connected to internet";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
