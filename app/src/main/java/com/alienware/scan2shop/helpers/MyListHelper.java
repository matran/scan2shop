package com.alienware.scan2shop.helpers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alienware.scan2shop.data.MyListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry cheruiyot on 2/3/2018.
 */

public class MyListHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myList";
    private static final int DATABASE_VERSION = 6;
    private static final String ITEM_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_TOTAL = "total";
    private static final String TABLE_ITEM = "item";
    private static final String TAG = MyListHelper.class.getSimpleName();

    public MyListHelper(Context paramContext)
    {
        super(paramContext, "myList", null, 7);
    }

    public void addMyList(int paramInt1, String paramString, String paramDouble1, String paramInt2, String paramDouble2)
    {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("id", Integer.valueOf(paramInt1));
        localContentValues.put("name", paramString);
        localContentValues.put("price", paramDouble1);
        localContentValues.put("quantity", paramInt2);
        localContentValues.put("total", paramDouble2);
        localSQLiteDatabase.insert("item", null, localContentValues);
        localSQLiteDatabase.close();
    }

    public void deleteMyListItem(int paramInt) {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        localSQLiteDatabase.delete("item", "id=?", new String[] { String.valueOf(paramInt) });
        localSQLiteDatabase.close();
    }

    public void deleteMylistAllItems()
    {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        localSQLiteDatabase.delete("item", null, null);
        localSQLiteDatabase.close();

    }

    public int getMyListCode()
    {
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT MAX(id) FROM item", null);
        localCursor.moveToFirst();
        return localCursor.getInt(0);
    }

    public Double getMyListTotal()
    {
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT SUM(total) FROM item", null);
        localCursor.moveToFirst();
        return Double.valueOf(localCursor.getDouble(0));
    }

    public int getMylistMaxQuantity()
    {
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT MAX(quantity) FROM item", null);
        localCursor.moveToFirst();
        localCursor.getInt(0);
        return localCursor.getInt(0);
    }

    public List<MyListData> getMylistProductInfo()
    {
        List<MyListData> localArrayList= new ArrayList();
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("SELECT  * FROM item", null);

        while (localCursor.moveToNext())
        {
           int id=localCursor.getInt(0);
            String name=localCursor.getString(1);
            String price= localCursor.getString(2);
            String quantity=localCursor.getString(3);
            String total=localCursor.getString(4);
            Log.e("mykkks",price);
            Log.e("hhdh",quantity);
            if( !price.isEmpty() && !quantity.isEmpty()){
                localArrayList.add(new MyListData(id,name,"ksh." +price,"(" +quantity+")",total));

            }else {
                localArrayList.add(new MyListData(id, name, price, quantity,total));
            }
        }
        localCursor.close();
        localSQLiteDatabase.close();
        return localArrayList;
    }

    public int getMylistQuantity()
    {
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT SUM(quantity) FROM item", null);
        localCursor.moveToFirst();
        return localCursor.getInt(0);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
        paramSQLiteDatabase.execSQL("CREATE TABLE item(id INTEGER PRIMARY KEY,name TEXT,price TEXT ,quantity TEXT,total INTEGER) ");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS item");
        onCreate(paramSQLiteDatabase);
    }
}
