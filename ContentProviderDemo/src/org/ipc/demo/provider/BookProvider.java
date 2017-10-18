
package org.ipc.demo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class BookProvider extends ContentProvider {

    private static final String TAG = "BookProvider";

    // BookProvider的标识为 org.ipc.demo.BookProvider
    public static final String AUTHORITY = "org.ipc.demo.BookProvider";

    private static final String SCHEME = "content://";

    // user表的uri content://org.ipc.demo.BookProvider/user
    public static final Uri USER_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/user");

    // user表的uri content://org.ipc.demo.BookProvider/user
    public static final Uri BOOK_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/book");

    public static final int BOOK_URI_CODE = 0;

    public static final int USER_URI_CODE = 1;

    private static final UriMatcher SUR_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private BookDbHelper mDbHelper;

    private SQLiteDatabase mDb;

    static {
        SUR_MATCHER.addURI(AUTHORITY, "user", USER_URI_CODE);
        SUR_MATCHER.addURI(AUTHORITY, "book", BOOK_URI_CODE);
    }

    public BookProvider() {

    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate,current Thread = " + Thread.currentThread().getName());
        mDbHelper = new BookDbHelper(getContext());
        mDb = mDbHelper.getWritableDatabase();
        return false;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (SUR_MATCHER.match(uri)) {
            case BOOK_URI_CODE:
                tableName = BookDbHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = BookDbHelper.USER_TABLE_NAME;
                break;
        }
        return tableName;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalStateException("unknow uri:" + uri);
        }
        int delete = mDb.delete(tableName, selection, selectionArgs);
        if (delete > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delete;
    }

    @Override
    public String getType(Uri uri) {
        return "";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalStateException("unknow uri:" + uri);
        }
        long insert = mDb.insert(tableName, null, values);
        if (insert != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalStateException("unknow uri:" + uri);
        }
        return mDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalStateException("unknow uri:" + uri);
        }
        int update = mDb.update(tableName, values, selection, selectionArgs);
        if (update > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return update;
    }
}
