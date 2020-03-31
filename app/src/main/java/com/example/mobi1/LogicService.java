package com.example.mobi1;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class LogicService extends Service {

    public class LocalBinder extends Binder {
        LogicService getService(){
            return LogicService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public double add(double num1, double num2){
        return num1+num2;
    }

    public double sub(double num1, double num2){
        return num1-num2;
    }

    public double mul(double num1, double num2){
        return num1*num2;
    }

    public double div(double num1, double num2){
        return num1/num2;
    }
}
