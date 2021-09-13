package com.example.project.utils;

import android.content.Context;

import com.example.project.utils.Preference;


public class PreferenceHandler {

    private final String LOGIN_NAME = "pref_Login";

    private final String IS_SHOW_DECLARED = "is_show_declared";

    private Preference preference = null;
    private final String VIRTUOUS = "pref_virtuous";

    public PreferenceHandler(Context pContext) {
        preference = new Preference(pContext);
    }

    public String getLoginName() {
       return getString(LOGIN_NAME, "");
    }

    public void setLogin(String name) {
        setString(LOGIN_NAME, name);
    }

    public void removeLogin() {
        removeKeyFromPreference(LOGIN_NAME);
    }

    public boolean isShowDeclared() {
        return getBool(IS_SHOW_DECLARED, false);
    }

    public void setShowDeclared() {
        setBool(IS_SHOW_DECLARED, true);
    }

    public String getVirtuous() {
        return getString(VIRTUOUS, "");
    }

    public void setVirtuous(String token) {
        setString(VIRTUOUS, token);
    }


    public void removeKeyFromPreference(String key){
        preference.remove(key);
    }


    public void setString(String key, String value) {
        preference.saveStringInPrefrence(key, value);
    }

    public String getString(String key, String value) {
        return preference.getStringPrefrence(key, value);
    }

    public String getString(String key) {
        return preference.getStringPrefrence(key);
    }

    public boolean isContainBool(String key){
        return preference.getBooleanFlagPrefrence(key,false);
    }
    public void setBool(String key, boolean value){
        preference.saveBooleanFlagInPreference(key,value);
    }

    public boolean getBool(String key, boolean value){
        return preference.getBooleanFlagPrefrence(key,value);
    }
}
