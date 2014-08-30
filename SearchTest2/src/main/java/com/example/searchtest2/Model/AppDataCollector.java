package com.example.searchtest2.Model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class AppDataCollector extends DataCollector {

    @Override
    public ArrayList<DataCollector> getData(Context context) {
        // TODO Auto-generated method stub
        final PackageManager pm = context.getPackageManager();
        ArrayList<DataCollector> list = new ArrayList<DataCollector>();
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);
        for (ApplicationInfo appInfo : packages) {
            // Log.d(TAG, "Installed package :" + appInfo.packageName);
            // Log.d(TAG, "Launch Activity :" +
            // pm.getLaunchIntentForPackage(appInfo.packageName));
            if ((pm.getLaunchIntentForPackage(appInfo.packageName)) != null) {
                DataCollector d = new AppDataCollector();
                //d.setName(appInfo.packageName) ;
                d.setName(pm.getApplicationLabel(appInfo).toString());
                d.setType("apk");
                d.setAppIcon(pm.getApplicationIcon(appInfo));
                d.setIntent(pm.getLaunchIntentForPackage(appInfo.packageName));
                //d.setExecutionLink(pm.getLaunchIntentForPackage(
                //	appInfo.packageName).toString());
                list.add(d);
            }
        }
        return list;
    }
}