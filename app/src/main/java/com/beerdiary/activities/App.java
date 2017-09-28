package com.beerdiary.activities;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jim on 9/18/2017.
 * This class is here just to provide the Application Context when needed
 */

public class App extends Application {

    private static Context appContext;
        @Override
        public void onCreate() {
            super.onCreate();
            appContext = this;
        }
        public static Context getAppContext() {
            return appContext;
        }
}
