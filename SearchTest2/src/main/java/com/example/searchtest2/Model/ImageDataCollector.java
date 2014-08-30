package com.example.searchtest2.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by Mustafa on 12/2/13.
 */
public class ImageDataCollector extends DataCollector {

    @Override
    public ArrayList<DataCollector> getData(Context context) {

        ArrayList<DataCollector> list = new ArrayList<DataCollector>();

        ContentResolver cr = context.getContentResolver();
        final String CAMERA_IMAGE_BUCKET_NAME =
                Environment.getExternalStorageDirectory().toString()
                        + "/DCIM/Camera";
        final String CAMERA_IMAGE_BUCKET_ID = String.valueOf(CAMERA_IMAGE_BUCKET_NAME.toLowerCase().hashCode());

        String[] columns = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.SIZE};

        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String[] selectionArgs = { CAMERA_IMAGE_BUCKET_ID };
        /*Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, selection, selectionArgs, null);*/
        Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, null);

        /*if (cur.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                imageDataCollector.setBody(cur.getString(cur.getColumnIndexOrThrow("DATA")).toString());
                imageDataCollector.setName(cur.getString(cur.getColumnIndexOrThrow("TITLE")).toString());
                imageDataCollector.setId(cur.getString(cur.getColumnIndexOrThrow("_ID")).toString());
                list.add(imageDataCollector);

                cur.moveToNext();
            }
        }*/
        if (cur.moveToFirst()) {
            for (int i = 0; i < cur.getCount(); i++) {
                ImageDataCollector imageDataCollector = new ImageDataCollector();
                imageDataCollector.setBody(cur.getString(cur.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)));
                imageDataCollector.setName(cur.getString(cur.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE)));//(MediaStore.Images.ImageColumns.TITLE)));
                imageDataCollector.setId(cur.getString(cur.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)));
                imageDataCollector.setType("image");
                list.add(imageDataCollector);

                cur.moveToNext();
            }
        }
        cur.close();

        return list;
    }

}