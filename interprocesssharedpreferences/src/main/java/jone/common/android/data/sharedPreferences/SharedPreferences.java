package jone.common.android.data.sharedPreferences;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * SharedPreferences 简化android库的操作
 * Created by jone.sun on 2016/2/17.
 */
public class SharedPreferences extends ISharedPreferences {
    private android.content.SharedPreferences sharedPreferences;

    private android.content.SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListenerWithAndroid = new android.content.SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(android.content.SharedPreferences sharedPreferences, String key) {
            setChanged();
            //只有在setChange()被调用后，notifyObservers()才会去调用update()，否则什么都不干。
            notifyObservers(key);
        }
    };

    public SharedPreferences(Context context) {
        sharedPreferences = getSharedPreferences(context);
    }

    public SharedPreferences(Context context, @Nullable String name) {
        sharedPreferences = getSharedPreferences(context, name);
    }

    public SharedPreferences(Context context, @Nullable String name, int mode) {
        sharedPreferences = getSharedPreferences(context, name, mode);
    }

    @Override
    public boolean contains(@Nullable String key) {
        return sharedPreferences.contains(key);
    }

    @Override
    public boolean getBoolean(@Nullable String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    @Override
    public float getFloat(@Nullable String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    @Override
    public int getInt(@Nullable String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    @Override
    public long getLong(@Nullable String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    @Override
    public String getString(@Nullable String key, @Nullable String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    @Override
    public Set<String> getStringSet(@Nullable String key, @Nullable Set<String> defValue) {
        return sharedPreferences.getStringSet(key, defValue);
    }

    @Override
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    @Override
    public void putBoolean(@Nullable String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    @Override
    public void putFloat(@Nullable String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    @Override
    public void putInt(@Nullable String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    @Override
    public void putLong(@Nullable String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    @Override
    public void putString(@Nullable String key, @Nullable String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    @Override
    public void putStringSet(@Nullable String key, @Nullable Set<String> value) {
        sharedPreferences.edit().putStringSet(key, value).apply();
    }

    @Override
    public void remove(@Nullable String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    @Override
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }


    @Override
    public void registerOnSharedPreferenceChangeListener(@Nullable OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        super.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListenerWithAndroid);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(@Nullable OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        super.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListenerWithAndroid);
    }

    private android.content.SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    private android.content.SharedPreferences getSharedPreferences(Context context, String name) {
        return getSharedPreferences(context, name, Context.MODE_PRIVATE);
    }

    private android.content.SharedPreferences getSharedPreferences(Context context, String name, int mode) {
        return context.getApplicationContext().getSharedPreferences(name, mode);
    }

}
