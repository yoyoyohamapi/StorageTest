package com.storagetest;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVUser;
import com.storagetest.entity.Baby;
import com.storagetest.entity.User;
import com.storagetest.helper.LeanCloudHelper;
import com.storagetest.utils.ACache;

/**
 * App
 * Desc: 应用环境初始化
 * Team: InHand
 * User: Wooxxx
 * Date: 2015-03-05
 * Time: 10:37
 */
public class App extends Application {
    public static final String BABY_CACHE_KEY = "current_baby";
    public static Baby currentBaby = null;
    private static Context context = null;
    public static Typeface Typeface_arial;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化LeanCloud
        LeanCloudHelper.initLeanCloud(this);
        initCurrentBaby();
        context = getApplicationContext();
//        Typeface_arial = Typeface.createFromAsset(context.getAssets(), "ttf/arial.ttf");

    }


    public static User getCurrentUser() {
        return AVUser.cast(AVUser.getCurrentUser(), User.class);
    }

    public void initCurrentBaby() {
        if (currentBaby == null) {
            ACache aCache = ACache.get(this);
            String json = aCache.getAsString(Baby.CACHE_KEY);
            if (json == null)
                return;
            currentBaby = JSON.parseObject(json, Baby.class);
        }
    }

    /**
     * 获得当前宝宝
     *
     * @return 当前宝宝
     */
    public static Baby getCurrentBaby() {
        return currentBaby;
    }

    /**
     * 判断用户是否登陆
     *
     * @return 登陆与否
     */
    public static boolean logged() {
        if (AVUser.getCurrentUser() == null)
            return false;
        return true;
    }


    /**
     * 登出方法,会清除保存的宝宝信息
     */
    public void logOut() {
        ACache aCache = ACache.get(this);
        // 清除所有缓存
        aCache.clear();
        AVUser.logOut();
    }
}
