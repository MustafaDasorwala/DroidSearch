package com.example.searchtest2.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Mustafa on 12/1/13.
 */
public class ContactDataCollector extends DataCollector {

    @Override
    public ArrayList<DataCollector> getData(Context context) {
        ArrayList<DataCollector> list = new ArrayList<DataCollector>();
        ContentResolver cr = context.getContentResolver();//getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        DataCollector d = new AppDataCollector();
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        d.setName(name);
                        d.setNum(phoneNo);
                        d.setType("contacts");
                        d.setId(id);
                        list.add(d);
                        break;
                        //Toast.makeText(context, "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                    }
                    pCur.close();
                }
            }
        }
        return list;
    }
}
