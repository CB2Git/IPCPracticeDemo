
package org.ipc.demo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Uri userUri = Uri.parse("content://org.ipc.demo.BookProvider/user");

    private Uri bookUri = Uri.parse("content://org.ipc.demo.BookProvider/book");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickAddUser(View v) {
        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", "Person");
        values.put("sex", 1);
        resolver.insert(userUri, values);

    }

    public void onClickAddBook(View v) {
        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", "这时一本书");
        resolver.insert(bookUri, values);
    }

    public void onClickDelUser(View v) {
        // TODO ignore
    }

    public void onClickDelBook(View v) {
        // TODO ignore
    }

    public void onClickQueryAll(View v) {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(userUri, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int sex = cursor.getInt(cursor.getColumnIndex("sex"));
            Log.i(TAG, "name = " + name + ",sex = " + sex);
        }
        cursor.close();

        Log.i(TAG, "*************************************************");

        cursor = resolver.query(bookUri, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Log.i(TAG, "name = " + name);
        }
    }
}
