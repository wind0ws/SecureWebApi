package com.threshold.webapiauth;

import android.app.Application;
import android.util.Log;

import timber.log.Timber;

/**
 * Application of App,Init some Global instance
 * Created by Threshold on 2016/1/14.
 */
public class WebApiApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initTimber();
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

           // FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                   // FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                   // FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }
}
