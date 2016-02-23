package jone.common.android.data.sharedPreferences;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * 跨进程的SharedPreferences
 * 需在AndroidManifest注册ContentProvider {@link InterProcessContentProvider}
 * Created by jone.sun on 2016/2/17.
 */
public class InterProcessSharedPreferences extends ISharedPreferences {
    private static final String TAG = InterProcessSharedPreferences.class.getSimpleName();
    private static InterProcessSharedPreferences instance;
    private Context context;
    private ContentResolver contentResolver;
    private Uri URL;

    private ContentObserver contentObserver = new ContentObserver(new Handler()) {
        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            //Log.e(TAG, "onChange: " + selfChange + " " + uri);
            setChanged();
            //只有在setChange()被调用后，notifyObservers()才会去调用update()，否则什么都不干。
            notifyObservers(uri.getPath().replace("/sp/", ""));
        }
    };

    public static InterProcessSharedPreferences getInstance(Application context) {
        if (instance == null) {
            instance = new InterProcessSharedPreferences(context);
        }
        return instance;
    }

    private InterProcessSharedPreferences(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
        URL = InterProcessContentProvider.SP_URI(context);
    }

    @Override
    public boolean contains(@Nullable String key) {
        return false;
    }

    @Override
    public boolean getBoolean(@Nullable String key, boolean defValue) {
        boolean result = defValue;
        String resultStr = getResult(context, key, defValue);
        if (resultStr != null) {
            result = Boolean.parseBoolean(resultStr);
        }
        return result;
    }

    @Override
    public float getFloat(@Nullable String key, float defValue) {
        float result = defValue;
        String resultStr = getResult(context, key, defValue);
        if (resultStr != null) {
            result = Float.parseFloat(resultStr);
        }
        return result;
    }

    @Override
    public int getInt(@Nullable String key, int defValue) {
        int result = defValue;
        String resultStr = getResult(context, key, defValue);
        if (resultStr != null) {
            result = Integer.parseInt(resultStr);
        }
        return result;
    }

    @Override
    public long getLong(@Nullable String key, long defValue) {
        long result = defValue;
        String resultStr = getResult(context, key, defValue);
        if (resultStr != null) {
            result = Long.parseLong(resultStr);
        }
        return result;
    }

    @Override
    public String getString(@Nullable String key, @Nullable String defValue) {
        String result = defValue;
        String resultStr = getResult(context, key, defValue);
        if (resultStr != null) {
            result = resultStr;
        }
        return result;
    }

    @Override
    public Map<String, ?> getAll() {
        throw new RuntimeException("now not support getAll()!!!");
    }

    @Override
    public void putBoolean(@Nullable String key, boolean value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        contentResolver.insert(URL, contentValues);
    }

    @Override
    public void putFloat(@Nullable String key, float value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        contentResolver.insert(URL, contentValues);
    }

    @Override
    public void putInt(@Nullable String key, int value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        contentResolver.insert(URL, contentValues);
    }

    @Override
    public void putLong(@Nullable String key, long value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        contentResolver.insert(URL, contentValues);
    }

    @Override
    public void putString(@Nullable String key, @Nullable String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        contentResolver.insert(URL, contentValues);
    }

    @Override
    public void remove(@Nullable String key) {
        contentResolver.delete(URL, key, null);
    }

    @Override
    public void clear() {
        contentResolver.delete(URL, null, new String[]{});
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        super.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        contentResolver.registerContentObserver(Uri.parse(InterProcessContentProvider.STR_WILDCARD_SP_URI(context)),
                true, contentObserver);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        super.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        contentResolver.unregisterContentObserver(contentObserver);
    }

    @Override
    public void putStringSet(String key, @Nullable Set<String> value) {
        throw new RuntimeException("now not support putStringSet()!!!");
    }

    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValue) {
        throw new RuntimeException("now not support getStringSet()!!!");
    }

    private <T> String getResult(Context context, String key, T defaultValue) {
        String result = null;
        String strings[] = null;
        if (defaultValue != null) {
            strings = new String[]{defaultValue.getClass().getCanonicalName()};
        }
        Cursor cursor = context.getContentResolver().query(URL,
                null,
                key,
                strings,
                String.valueOf(defaultValue));
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(0);
            cursor.close();
        }
        if (result == null || result.equals("null")) {
            result = null;
        }
        return result;
    }
}
