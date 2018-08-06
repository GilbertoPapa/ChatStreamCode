package com.gilbertopapa.chatstreamcode.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by GilbertoPapa on 16/07/2018.
 */

public class Preferences {

    private Context context;
    private SharedPreferences preferences;
    private final String NAME_DIRECTORY = "chat.preferences";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String KEY_NAME = "name";
    private final String KEY_PHONE = "phone";
    private final String KEY_TOKEN = "token";
    private final String KEY_IDENTIFY = "identifyUserLogin";


    public Preferences(Context contextParams) {

        context = contextParams;
        preferences = context.getSharedPreferences(NAME_DIRECTORY, MODE);
        editor = preferences.edit();
    }

    public void saveUserPreferencesIdentify(String identifyUserLogin/*,String nameUserLogin*/) {
        editor.putString(KEY_IDENTIFY, identifyUserLogin);
        // editor.putString(KEY_NAME,nameUserLogin);
        editor.commit();
    }

    public void saveUserPreferences(String nameUser, String phoneUser, String tokenUser) {

        editor.putString(KEY_NAME, nameUser);
        editor.putString(KEY_PHONE, phoneUser);
        editor.putString(KEY_TOKEN, tokenUser);
        editor.commit();
    }

    public HashMap<String, String> getDataUser() {
        HashMap<String, String> dataUser = new HashMap<>();
        dataUser.put(KEY_NAME, preferences.getString(KEY_NAME, null));
        dataUser.put(KEY_PHONE, preferences.getString(KEY_PHONE, null));
        dataUser.put(KEY_TOKEN, preferences.getString(KEY_TOKEN, null));

        return dataUser;
    }

    public String getIdentify() {
        return preferences.getString(KEY_IDENTIFY, null);
    }


    public String getUserName() {
        return preferences.getString(KEY_NAME, null);
    }

}
