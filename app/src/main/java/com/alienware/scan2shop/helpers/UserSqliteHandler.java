package com.alienware.scan2shop.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.HashMap;
/**
 * Created by henry cheruiyot on 2/3/2018.
 */
public class UserSqliteHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "android_api";
    private static final int DATABASE_VERSION = 15;
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_STORE = "Store";
    private static final String KEY_UID = "uid";
    private static final String TABLE_USER = "user";
    private static final String TAG = "userdb";
    public UserSqliteHandler(Context paramContext)
    {
        super(paramContext, "android_api", null, 18);
    }
    public void addUser(String id,String apikey, String firstname, String secondname, String phone, String email)
    {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("id", id);
        localContentValues.put("apikey",apikey);
        localContentValues.put("firstname", firstname);
        localContentValues.put("secondname",secondname);
        localContentValues.put("phone", phone);
        localContentValues.put("email", email);
       localSQLiteDatabase.insert("user", null, localContentValues);
        localSQLiteDatabase.close();
    }


    public void deleteUsers() {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        localSQLiteDatabase.delete("user", null, null);
        localSQLiteDatabase.close();
    }

    public String getKeyName() {
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("SELECT name FROM user", null);
        localCursor.moveToFirst();
        String str = localCursor.getString(0);
        localCursor.close();
        localSQLiteDatabase.close();
        return str;
    }

    public HashMap<String, String> getUserDetails() {
        HashMap localHashMap = new HashMap();
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("SELECT  * FROM user", null);
        localCursor.moveToFirst();
        if (localCursor.getCount() > 0)
        {
            localHashMap.put("firstname", localCursor.getString(2));
            localHashMap.put("secondname", localCursor.getString(3));
            localHashMap.put("phone", localCursor.getString(4));
            localHashMap.put("email", localCursor.getString(5));
        }
        localCursor.close();
        localSQLiteDatabase.close();
        return localHashMap;
    }

    public String getUserid(){
        try {
            SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
            Cursor localCursor = localSQLiteDatabase.rawQuery("SELECT id FROM user", null);
            localCursor.moveToFirst();
            String str = localCursor.getString(0);
            Log.d("userdb", str);
            localCursor.close();
            localSQLiteDatabase.close();
            return str;
        }catch (Exception e){
            return null;
        }
    }
    public String getuid() {

        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("SELECT apikey FROM user", null);
        localCursor.moveToFirst();
        String str = localCursor.getString(0);
        localCursor.close();
        localSQLiteDatabase.close();
        return str;
    }

    public String getPhoneNo(){

        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("SELECT phone FROM user", null);
        localCursor.moveToFirst();
        String str = localCursor.getString(0);
        Log.d("userdb", str);
        localCursor.close();
        localSQLiteDatabase.close();
        return str;
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
        paramSQLiteDatabase.execSQL("CREATE TABLE user(id TEXT PRIMARY KEY,apikey TEXT,firstname TEXT,secondname TEXT,phone TEXT UNIQUE,email TEXT UNIQUE)");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
        onCreate(paramSQLiteDatabase);
    }

    public void updateUser(String firstname, String secondname, String phone, String email) {
        String str = getUserid();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstname", firstname);
        contentValues.put("secondname",secondname);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
         String query = "id=" + str;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update("user", contentValues, query, null);
        sqLiteDatabase.close();
    }
}
