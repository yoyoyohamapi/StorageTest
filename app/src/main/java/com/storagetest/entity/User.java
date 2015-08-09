package com.storagetest.entity;


import android.content.Context;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.storagetest.App;
import com.storagetest.dao.BabyDao;
import com.storagetest.dao.FeedItemDao;
import com.storagetest.dao.PowderTipDao;

import java.util.List;


/**
 * User
 * Desc:
 * Team: InHand
 * User:Wooxxx
 * Date: 2015-03-19
 * Time: 14:35
 */
public class User extends AVUser {
    public static final String CLASS_NAME = "_USER";

    public static final String NICKNAME_KEY = "nickname"; // 昵称
    public static final String SEX_KEY = "sex"; // 性别
    public static final String CITY_KEY = "city"; // 城市
    public static final String AVATAR_KEY = "avatar"; // 头像
    public static final String EMAIL_KEY = "email"; // 邮箱


    public static int FEMALE = 2; // 女性
    public static int MALE = 1; // 男性
    // 判断用户是否有baby的错误码
    public static final int NO_BABY = 0;
    public static final int HAS_BABY = 1;
    public static final int NETWORK_ERROR = 2;

    /**
     * 获得用户昵称
     *
     * @return 用户昵称
     */
    public String getNickname() {
        return this.getString(NICKNAME_KEY);
    }

    /**
     * 设置用户昵称
     *
     * @param nickname 用户昵称
     */
    public void setNickname(String nickname) {
        this.put(NICKNAME_KEY, nickname);
    }

    /**
     * 获得用户性别
     *
     * @return 用户性别
     */
    public int getSex() {
        return this.getInt(SEX_KEY);
    }

    /**
     * 设置用户性别
     *
     * @param sex 用户性别
     */
    public void setSex(int sex) {
        this.put(SEX_KEY, sex);
    }

    /**
     * 获得用户所在城市
     */
    public String getCity() {
        return this.getString(CITY_KEY);
    }

    /**
     * 设置当前城市
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.put(CITY_KEY, city);
    }

    /**
     * 设置用户头像
     */
    public void setAvatar(AVFile avatar) {
        this.put(AVATAR_KEY, avatar);
    }

    /**
     * 获得用户头像
     *
     * @return
     */
    public AVFile getAvatar() {
        return this.getAVFile(AVATAR_KEY);
    }

    /**
     * 获得用户email
     *
     * @return 用户email
     */
    public String getEmail() {
        return this.getString(EMAIL_KEY);
    }

    /**
     * 设置用户email
     *
     * @param email 用户email
     */
    public void setEmail(String email) {

    }

    /**
     * 异步地取得当前用户的所有宝宝
     *
     * @param findCallback 回调接口
     */
    public void fetchBabies(final FindCallback<Baby> findCallback) {
        BabyDao babyDao = new BabyDao();
        babyDao.findByUserFromCloud(this, findCallback);
    }

    /**
     * 同步地取得当前用户的所有宝宝
     */
    public List<Baby> fetchBabies() {
        BabyDao babyDao = new BabyDao();
        return babyDao.findByUserFromCloud(this);
    }

    /**
     * 查看该用户是否填写了宝宝信息,此方法请用异步过程或新开线程执行
     */
    public int hasBaby(final Context ctx) {
        // 首先判断本地缓存中是否有Baby
        if (App.getCurrentBaby() == null) {
            // 若没有，再判断云端是否有宝宝，若有，选择第一个宝宝将其缓存
            List<Baby> babies = fetchBabies();
            if (babies != null) {
                if (babies.size() == 0)
                    return NO_BABY;
                Baby baby = babies.get(0);
                initBaby(ctx, baby);
                // 初始化宝宝其他信息
                return HAS_BABY;
            } else {
                return NETWORK_ERROR;
            }
        }
        return HAS_BABY;
    }

    /**
     * 初始化宝宝信息
     *
     * @param ctx  上下文环境
     * @param baby 宝宝
     */
    private void initBaby(final Context ctx, Baby baby) {
        // 缓存奶粉信息
        Powder powder = baby.getPowder();
        if (powder != null) {
            PowderTipDao ptd = new PowderTipDao();
            List<PowderTip> tips = ptd.findByPowderFromCloud(powder);
            powder.cacheTips(ctx, tips);
        }

        // 缓存喂养计划信息
        FeedPlan feedPlan = baby.getFeedPlan();
        FeedItemDao fid = new FeedItemDao();
        List<FeedItem> items = fid.findByFeedPlanFromCloud(feedPlan);
        feedPlan.cacheItems(ctx, items);

        baby.saveInCache(ctx);
    }


}
