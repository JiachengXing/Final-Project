package com.example.project.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Comment implements Serializable {

    public String name;
    public String imgTag;
    public String time;
    public String content;

    public Comment(String name, String imgTag, String time, String content) {
        this.name = name;
        this.imgTag = imgTag;
        this.time = time;
        this.content = content;
    }

    public Comment(JSONObject jsonObject) {
        if (jsonObject != null) {
            this.name = jsonObject.optString("name");
            this.imgTag = jsonObject.optString("imgTag");
            this.time = jsonObject.optString("time");
            this.content = jsonObject.optString("content");

        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("name", name);
            jsonObject.putOpt("imgTag", imgTag);
            jsonObject.putOpt("time", time);
            jsonObject.putOpt("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
