package com.example.project.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyPlan implements Serializable {

    public String userName;
    public String imgTag;
    public String name;
    public String sd;
    public String ed;
    public String ac;
    public String isDel;
    public String isShare;
    public String isDone;
    public String doneDate;
    public int thumb;
    public List<Comment> comments;
    public String note;
    public int remindingGap;

    public MyPlan(String name, String sd, String ed, String ac, String isDel, String isDone, String isShare, String doneDate,
                  int thumb, String userName, String imgTag, String note, int remindingGap ) {

        this.name = name;
        this.sd = sd;
        this.ed = ed;
        this.ac = ac;
        this.isDel = isDel;
        this.isDone = isDone;
        this.isShare = isShare;
        this.doneDate = doneDate;
        this.thumb = thumb;
        this.userName = userName;
        this.imgTag = imgTag;
        this.note = note;
        this.remindingGap = remindingGap;
    }

    public MyPlan(String name, String sd, String ed, String ac, String isDel, String isDone, String isShare, String doneDate,
                  int thumb, String userName, String note, int remindingGap) {

        this.name = name;
        this.sd = sd;
        this.ed = ed;
        this.ac = ac;
        this.isDel = isDel;
        this.isDone = isDone;
        this.isShare = isShare;
        this.doneDate = doneDate;
        this.thumb = thumb;
        this.userName = userName;
        this.note = note;
        this.remindingGap = remindingGap;
    }

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("name", name);
            jsonObject.putOpt("sd", sd);
            jsonObject.putOpt("ed", ed);
            jsonObject.putOpt("ac", ac);
            jsonObject.putOpt("isDel", isDel);
            jsonObject.putOpt("isShare", isShare);
            jsonObject.putOpt("isDone", isDone);
            jsonObject.putOpt("doneDate", doneDate);
            jsonObject.putOpt("thumb", thumb);
            jsonObject.putOpt("user_name", userName);
            jsonObject.putOpt("img_tag", imgTag);
            jsonObject.putOpt("note", note);
            jsonObject.putOpt("reminding_gap", remindingGap);
            if (comments != null && !comments.isEmpty()) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < comments.size(); i++) {
                    jsonArray.put(i, comments.get(i).toJSON());
                }
                jsonObject.putOpt("comments", jsonArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

    public static String toJSONArrayString(List<MyPlan> myPlans) {
        try {
            if (myPlans != null && !myPlans.isEmpty()) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < myPlans.size(); i++) {
                    jsonArray.put(i, myPlans.get(i).toJSONString());
                }
                return jsonArray.toString();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static MyPlan parseJSON(String jsonStr) {

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            MyPlan myPlan = new MyPlan(jsonObject.optString("name"),jsonObject.optString("sd"),jsonObject.optString("ed"),
                    jsonObject.optString("ac"),jsonObject.optString("isDel"),jsonObject.optString("isDone"),jsonObject.optString("isShare"),
                    jsonObject.optString("doneDate"),jsonObject.optInt("thumb",0), jsonObject.optString("user_name"),
                    jsonObject.optString("img_tag"), jsonObject.optString("note"), jsonObject.optInt("reminding_gap",0));
            JSONArray jsonArray = jsonObject.optJSONArray("comments");
            if (jsonArray != null && jsonArray.length() > 0) {
                myPlan.comments = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    myPlan.comments.add(new Comment(jsonObject1));
                }
            }
            return myPlan;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static List<MyPlan> parseJSONArray(String jsonArrayStr) {

        try {
            JSONArray jsonArray = new JSONArray(jsonArrayStr);
            List<MyPlan> myPlans = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                MyPlan myPlan = parseJSON(jsonArray.optString(i));
                if (myPlan != null) {
                    myPlans.add(myPlan);
                }
            }
            return myPlans;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getEd() {
        return ed;
    }

    public void setEd(String ed) {
        this.ed = ed;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyPlan myPlan = (MyPlan) o;
        return userName.equals(myPlan.userName) &&
                imgTag.equals(myPlan.imgTag) &&
                name.equals(myPlan.name) &&
                sd.equals(myPlan.sd) &&
                ed.equals(myPlan.ed) &&
                ac.equals(myPlan.ac);
    }

    @Override
    public String toString() {
        return "MyPlan{" +
                "userName='" + userName + '\'' +
                ", imgTag='" + imgTag + '\'' +
                ", name='" + name + '\'' +
                ", sd='" + sd + '\'' +
                ", ed='" + ed + '\'' +
                ", ac='" + ac + '\'' +
                ", isDel='" + isDel + '\'' +
                ", isShare='" + isShare + '\'' +
                ", isDone='" + isDone + '\'' +
                ", doneDate='" + doneDate + '\'' +
                ", thumb=" + thumb +
                ", note='" + note + '\'' +
                ", remindingGap=" + remindingGap +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sd, ed, ac);
    }
}
