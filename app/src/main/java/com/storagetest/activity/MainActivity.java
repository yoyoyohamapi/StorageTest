package com.storagetest.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.storagetest.R;
import com.storagetest.entity.Baby;
import com.storagetest.entity.User;
import com.storagetest.utils.ACache;

/**
 * MainActivity
 * Desc:
 * Date: 2015/8/9
 * Time: 23:24
 * Created by: Wooxxx
 */
public class MainActivity extends Activity {
    private Button saveBtn, getBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
    }

    private void initViews() {
        saveBtn = (Button) findViewById(R.id.saveBtn);
        getBtn = (Button) findViewById(R.id.getBtn);
    }

    ;

    private void setListeners() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVUser.logInInBackground("18581867753", "123456", new LogInCallback<User>() {
                    @Override
                    public void done(final User user, AVException e) {
                        AsyncTask task = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                user.hasBaby(MainActivity.this);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                Toast.makeText(MainActivity.this, "Save success", Toast.LENGTH_SHORT).show();
                                super.onPostExecute(o);
                            }
                        };

                        task.execute();
                    }
                }, User.class);
            }
        });

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ACache aCache = ACache.get(MainActivity.this);
                String json = aCache.getAsString(Baby.CACHE_KEY);
                Log.d("JSON ARR", json);
                Baby baby = JSON.parseObject(json, Baby.class);
                Log.d("Baby created at:", String.valueOf(baby.getCreatedAt()));
            }
        });
    }
}
