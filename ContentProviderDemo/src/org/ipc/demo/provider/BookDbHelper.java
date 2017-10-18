
package org.ipc.demo.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "book_provider.db";

    private static final int DB_VERSION = 1;

    public static final String USER_TABLE_NAME = "user";

    public static final String BOOK_TABLE_NAME = "book";

    private static final String CREATE_USER_TABLE = "create table if not exists " + USER_TABLE_NAME
            + " (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT, \"name\"  TEXT,\"sex\"  INTEGER)";

    private static final String CREATE_BOOK_TABLE = "create table if not exists " + BOOK_TABLE_NAME
            + "(\"_id\"  INTEGER PRIMARY KEY AUTOINCREMENT, \"name\"  TEXT)";

    public BookDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
