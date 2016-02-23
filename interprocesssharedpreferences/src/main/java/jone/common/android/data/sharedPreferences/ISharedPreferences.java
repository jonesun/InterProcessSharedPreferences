package jone.common.android.data.sharedPreferences;

import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Created by jone.sun on 2016/2/17.
 */
public abstract class ISharedPreferences extends Observable {
    public abstract boolean contains(@Nullable String key);

    public abstract boolean getBoolean(@Nullable String key, boolean defValue);

    public abstract float getFloat(@Nullable String key, float defValue);

    public abstract int getInt(@Nullable String key, int defValue);

    public abstract long getLong(@Nullable String key, long defValue);

    public abstract String getString(@Nullable String key, @Nullable String defValue);

    public abstract Set<String> getStringSet(@Nullable String key, @Nullable Set<String> defValue);

    public abstract Map<String, ?> getAll();

    public abstract void putBoolean(@Nullable String key, boolean value);

    public abstract void putFloat(@Nullable String key, float value);

    public abstract void putInt(@Nullable String key, int value);

    public abstract void putLong(@Nullable String key, long value);

    public abstract void putString(@Nullable String key, @Nullable String value);

    public abstract void putStringSet(@Nullable String key, @Nullable Set<String> value);

    public abstract void remove(@Nullable String key);

    public abstract void clear();

    public void registerOnSharedPreferenceChangeListener(@Nullable OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        if (onSharedPreferenceChangeListener == null) {
            return;
        }
        addObserver(onSharedPreferenceChangeListener);
    }

    public void unregisterOnSharedPreferenceChangeListener(@Nullable OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        if (onSharedPreferenceChangeListener == null) {
            return;
        }
        deleteObserver(onSharedPreferenceChangeListener);
    }

    public static abstract class OnSharedPreferenceChangeListener implements Observer {
        public abstract void onSharedPreferenceChanged(ISharedPreferences sharedPreferences, String key);

        @Override
        public void update(Observable observable, Object data) {
            onSharedPreferenceChanged((ISharedPreferences) observable, (String) data);
        }
    }
}
