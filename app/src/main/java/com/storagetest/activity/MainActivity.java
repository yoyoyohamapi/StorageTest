package com.storagetest.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.storagetest.R;
import com.storagetest.entity.Baby;
import com.storagetest.entity.Powder;
import com.storagetest.entity.User;

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
                AVUser.logInInBackground("13880227627", "123456", new LogInCallback<User>() {
                    @Override
                    public void done(final User user, AVException e) {
                        AsyncTask task = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {
//                                user.hasBaby(MainActivity.this);
                                final Baby baby = new Baby();
                                Log.d("baby'id :", String.valueOf(baby.getUser()));
//                                baby.setStatistics(new Statistics());
//                                baby.setUser(user);
                                baby.setNickname("PipI");
                                baby.setBirthday("2014-02-08");
                                try {
                                    baby.saveInCloud();
                                    baby.saveInCache(MainActivity.this);
                                } catch (AVException e1) {
                                    e1.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
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
                AVQuery<Powder> query = AVQuery.getQuery(Powder.class);
                query.getInBackground("55b6329f00b0788037c9dff4", new GetCallback<Powder>() {
                    @Override
                    public void done(Powder powder, AVException e) {
                        AVFile file = powder.getLogo();
                        // 获取文件缩略图的URL
                        String url = file.getThumbnailUrl(false, 100, 100);
                        Toast.makeText(MainActivity.this, "文件缩略图URL：" + url, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
