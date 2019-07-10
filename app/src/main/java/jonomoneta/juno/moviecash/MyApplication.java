package jonomoneta.juno.moviecash;

import android.app.Application;

import androidx.multidex.MultiDex;


/**
 * Created by Dilip on 19-09-2017.
 */

//TODO use smaller case letters for package name

public class MyApplication extends Application {
    public static final String TAG = MyApplication.class
            .getSimpleName();
    private static MyApplication mInstance;
    private PreferenceSettings mPreferenceSettings;

    public MyApplication() {

    }

    public static synchronized MyApplication getInstance() {
        if (mInstance == null) {
            mInstance = new MyApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public PreferenceSettings getPreferenceSettings() {
        if (mPreferenceSettings == null) {
            mPreferenceSettings = new PreferenceSettings(getApplicationContext());
        }
        return mPreferenceSettings;
    }


}
