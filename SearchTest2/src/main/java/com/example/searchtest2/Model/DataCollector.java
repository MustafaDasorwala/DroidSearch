package com.example.searchtest2.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public abstract class DataCollector {

    String id;
    String name = "";
    String type = "";
    String executionLink = "";
    Drawable appIcon;
    Intent intent;
    String num="";
    String body="";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExecutionLink() {
        return executionLink;
    }

    public void setExecutionLink(String executionLink) {
        this.executionLink = executionLink;
    }

    public Intent getIntent() {
        return intent;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public abstract ArrayList<DataCollector> getData(Context context);
}
