package jone.common.android.data.sharedPreferences.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jone.common.android.data.sharedPreferences.ISharedPreferences;
import jone.common.android.data.sharedPreferences.InterProcessSharedPreferences;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText edit_value;
    private Button btn_save_data, btn_start_service;
    private TextView txt_info;
    private InterProcessSharedPreferences interProcessSharedPreferences;

    ISharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new ISharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(ISharedPreferences sharedPreferences, String key) {
            Log.e(TAG, "interProcessSharedPreferences--onSharedPreferenceChanged>>key: " + key + " value: " + sharedPreferences.getString(key, "empty"));
            if(txt_info != null){
                txt_info.append(key + " 发生改变: " + sharedPreferences.getString(key, "empty") + "\r\n\r\n");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_value = (EditText) findViewById(R.id.edit_value);
        btn_save_data = (Button) findViewById(R.id.btn_save_data);
        btn_start_service = (Button) findViewById(R.id.btn_start_service);
        txt_info = (TextView) findViewById(R.id.txt_info);

        //初始化
        interProcessSharedPreferences = InterProcessSharedPreferences.getInstance(getApplication());

        //如果不需要监听值改变则不需要设置
        interProcessSharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);

        txt_info.setText("跨进程SharedPreferences初始化成功!!!\r\n");

        btn_save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interProcessSharedPreferences.putString("testStr", edit_value.getText().toString());
                Log.e(TAG, "interProcessSharedPreferences--save>>testStr: " + interProcessSharedPreferences.getString("testStr", "empty"));
            }
        });


        btn_start_service.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               txt_info.append("启动服务成功!!!" + "\r\n");
               startService(new Intent(MainActivity.this, MyService.class));
           }
       });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得要解绑注册
        if(interProcessSharedPreferences != null && onSharedPreferenceChangeListener != null){
            interProcessSharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }
    }
}
