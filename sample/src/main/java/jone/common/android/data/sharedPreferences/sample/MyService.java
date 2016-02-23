package jone.common.android.data.sharedPreferences.sample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import jone.common.android.data.sharedPreferences.ISharedPreferences;
import jone.common.android.data.sharedPreferences.InterProcessSharedPreferences;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    private InterProcessSharedPreferences interProcessSharedPreferences;

    ISharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new ISharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(ISharedPreferences sharedPreferences, String key) {
            Log.e(TAG, "interProcessSharedPreferences--onSharedPreferenceChanged>>key: " + key + " value: " + sharedPreferences.getString(key, "empty"));
        }
    };
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        interProcessSharedPreferences = InterProcessSharedPreferences.getInstance(getApplication());
        interProcessSharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        interProcessSharedPreferences.putString("testStr", TAG + "_testStrValue0_" + System.currentTimeMillis());
        Log.e(TAG, "interProcessSharedPreferences--save>>testStr: " + interProcessSharedPreferences.getString("testStr", "empty"));
//        interProcessSharedPreferences.putString("testStr1", TAG + "_testStrValue1_" + + System.currentTimeMillis());
//        Log.e(TAG, "interProcessSharedPreferences--save>>testStr1: " + interProcessSharedPreferences.getString("testStr1", "empty"));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //记得要解绑注册
        if(interProcessSharedPreferences != null){
            interProcessSharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }
    }
}
