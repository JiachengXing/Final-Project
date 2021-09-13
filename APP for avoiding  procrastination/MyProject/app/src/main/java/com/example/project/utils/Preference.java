package com.example.project.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Preference {

    private static final String TAG = Preference.class.getCanonicalName();
    private String PrefName = "Test";

    private Context context = null;
    private SharedPreferences pref = null;
    private SharedPreferences previousPref = null;

    public Preference(final Context pContext) {
        this.context = pContext;
        previousPref = PreferenceManager.getDefaultSharedPreferences(pContext);
        pref = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
    }

    public void saveStringInPrefrence(final String pRef, final String value) {

        try {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(pRef, value);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void saveStringSetInPrefrence(final String pRef, HashSet<String> value) {

        try {
            SharedPreferences.Editor editor = pref.edit();
            editor.putStringSet(pRef, value);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void saveBooleanFlagInPreference(final String key, final boolean value) {

        try {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public boolean getBooleanFlagPrefrence(final String key, final boolean defaultVal) {
        return pref.getBoolean(key, defaultVal);
    }

    public String getStringPrefrence(final String pRef,
                                     final String defaultValue) {
        return pref.getString(pRef, defaultValue);
    }

    public String getStringPrefrence(final String pRef) {
        return previousPref.getString(pRef, null);
    }

    public void saveIntegerInPrefrence(final String pRef, final int value) {

        try {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(pRef, value);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public int getIntegerPrefrence(final String pRef, final int defaultValue) {
        return pref.getInt(pRef, defaultValue);
    }

    public void saveIntegerLongPrefrence(final String pRef, final long value) {

        try {
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong(pRef, value);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public long getLongPrefrence(final String pRef, final long defaultValue) {
        return pref.getLong(pRef, defaultValue);
    }


    public HashSet<String> getStringHashSet(String pRef, HashSet<String> hashSet) {
        return (HashSet<String>) pref.getStringSet(pRef, hashSet);
    }

    public void setKeySet(final String pRef, final List<String> symptomsList) {
        try {
            Set<String> set = new HashSet<>();
            set.addAll(symptomsList);
            SharedPreferences.Editor editor = pref.edit();
            editor.putStringSet(pRef, set);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public Set<String> getKeySet(final String pRef) {
        return pref.getStringSet(pRef, null);
    }

    public void remove(String key) {
        try {
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(key);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
        try {
            SharedPreferences.Editor editorPrevious = previousPref.edit();
            editorPrevious.remove(key);
            editorPrevious.commit();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }
}