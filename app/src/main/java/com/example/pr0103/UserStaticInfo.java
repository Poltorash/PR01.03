package com.example.pr0103;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

public class UserStaticInfo {

    public static User ActiveUser;
    /**ключ параметра Position.Latitude */
    public final static String POSITION_LATITUDE = "Latitude";
    /**ключ параметра Position.Longitude */
    public final static String POSITION_LONGITUDE = "Longitude";
    /**ключ параметра login */
    public final static String LOGIN = "login";
    //ключ параметра позиции
    public final static String POSITION = "position";
    //ключ списка пользователей используемых в FireBase
    public final static  String USERS_SIGN_IN_INFO = "UsersSignInInfo";
    //ключ списка профилей пользователей используемых в FireBase
    public final static  String USERS_PROFILE_INFO = "UsersProfileInfo";
    //ключ пароля
    public final static String PASSWORD = "password";
    //ключ id профиля
    public final static String PROFILE_ID = "profileId";
    //ключ имени пользователя
    public final static String NAME = "name";
    //ключ возраста пользователя
    public final static String AGE = "age";
    //ключ статуса пользователя
    public final static String STATE = "state";
    //id пользователя
    public static String profileId;
    //список пользователей
    public static List<User> users = new ArrayList<>();
    public UserStaticInfo(){
        if(users.size()==0)
            AddUsersInList();
    }

    private void AddUsersInList() {
        users.add(new User("Настя", "можно не надо", 18, 0));
        users.add(new User("Настя", "можно не надо", 18, 1));
        users.add(new User("Настя", "можно не надо", 18, 2));
        users.add(new User("Настя", "можно не надо", 18, 0));
    }

    public static List<Bitmap> photos = new ArrayList<>();
    public UserStaticInfo(Context context){
        if(photos.size()==0)
            AddPhotosInList(context);
    }

    private void AddPhotosInList(Context context) {
        photos.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.blum));
        photos.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stela));
        photos.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.muza));
    }
}
