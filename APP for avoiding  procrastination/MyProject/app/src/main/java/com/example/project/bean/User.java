package com.example.project.bean;

import com.example.project.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {

    public String name;
    public String userName;
    public String pass;
    public String gender;
    public String birthDate;
    public String imgTag;


    public User(String name, String userName, String pass, String gender, String birthDate, String imgTag) {
        this.name = name;
        this.userName = userName;
        this.pass = pass;
        this.gender = gender;
        this.birthDate = birthDate;
        this.imgTag = imgTag;
    }

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("name", name);
            jsonObject.putOpt("user_name", userName);
            jsonObject.putOpt("pass", pass);
            jsonObject.putOpt("gender", gender);
            jsonObject.putOpt("birth_date", birthDate);
            jsonObject.putOpt("birth_date", birthDate);
            jsonObject.putOpt("img_tag", imgTag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

    public static User parseJSON(String jsonStr) {

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            User user = new User(jsonObject.optString("name"),jsonObject.optString("user_name"),jsonObject.optString("pass"),jsonObject.optString("gender"),jsonObject.optString("birth_date"),jsonObject.optString("img_tag"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static int parseImageRes(String imgTag) {
        switch (imgTag) {
            case "1":
                return R.mipmap.ic_user_2;
            case "2":
                return R.mipmap.ic_user_3;
            case "3":
                return R.mipmap.ic_user_4;
            case "4":
                return R.mipmap.ic_user_5;
            default:
                return R.mipmap.ic_user_1;
        }
    }
}
