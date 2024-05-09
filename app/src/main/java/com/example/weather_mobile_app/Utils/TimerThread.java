package com.example.weather_mobile_app.Utils;

import android.os.Looper;

import com.example.weather_mobile_app.AppConfig;
import com.example.weather_mobile_app.MainActivity;

public class TimerThread extends Thread {

    public TimerThread() {}

    public void run() {
        Looper.prepare();
        while (!Thread.currentThread().isInterrupted()) {
            while (AppConfig.threadTimer > 0) {
                AppConfig.threadTimer--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            if (Thread.currentThread().isInterrupted()) break;
            MainActivity.getMainActivity().getAPIData();
            AppConfig.prepareTimer();
        }
    }
}
