package com.example.searchtest2.Model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Mustafa on 12/2/13.
 */
public class SmsDataCollector extends DataCollector {

    @Override
    public ArrayList<DataCollector> getData(Context context) {
        ArrayList<DataCollector> list = new ArrayList<DataCollector>();


        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c= context.getContentResolver().query(uri, null, null ,null,null);
        //context.startManagingCursor(c);

        // Read the sms data and store it in the list
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
                SmsDataCollector smsDataCollector = new SmsDataCollector();
                smsDataCollector.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
                smsDataCollector.setNum(c.getString(c.getColumnIndexOrThrow("address")).toString());
                smsDataCollector.setId(c.getString(c.getColumnIndexOrThrow("_ID")).toString());
                //smsDataCollector.setName(c.getString(c.getColumnIndexOrThrow("DISPLAY_NAME")).toString());
                smsDataCollector.setType("sms");
                list.add(smsDataCollector);

                c.moveToNext();
            }
        }
        c.close();

        return list;
    }
}
