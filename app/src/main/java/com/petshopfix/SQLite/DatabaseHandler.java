package com.petshopfix.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "User";
    private static final String TABLE_User = "users";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_User + "("
                + "id" + " INTEGER PRIMARY KEY,"
                + "NIP" + " TEXT,"
                + "nama_pegawai" + " TEXT,"
                + "jabatan" + " TEXT,"
                + "status" + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    //on Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_User);
        onCreate(db);
    }

    public void addUser(UserDefaults user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NIP", user.getNIP());
        values.put("nama_pegawai", user.getNama_pegawai());
        values.put("jabatan", user.getJabatan());
        values.put("status", user.getStatus());

        db.insert(TABLE_User, null, values);
        db.close();
    }

    public UserDefaults getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_User, new String[] { "id", "NIP", "nama_pegawai", "jabatan", "status" },
                "id" + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        UserDefaults user = new UserDefaults(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4));

        return user;
    }

    public int updateUser(UserDefaults user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NIP", user.getNIP());
        values.put("nama_pegawai", user.getNama_pegawai());
        values.put("jabatan", user.getJabatan());
        values.put("status", user.getStatus());

        // updating row
        return db.update(TABLE_User, values, "id" + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    public void deleteUser(UserDefaults user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_User, "id" + " = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
    }

    public boolean isEmpty()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_User, null);

        if (mCursor.moveToFirst())
        {
            db.close();
            return false;
        }
        db.close();
        return true;
    }
}
