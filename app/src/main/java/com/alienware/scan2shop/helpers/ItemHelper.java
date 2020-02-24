package com.alienware.scan2shop.helpers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.alienware.scan2shop.data.Product;
import com.alienware.scan2shop.data.Receipt;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by henry cheruiyot on 2/3/2018.
 */

public class ItemHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "productInfo";
    private static final int DATABASE_VERSION = 3;
    private static final String ITEM_ID = "id";
    private static  final String BARCODE_ID="barcode";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_TOTAL = "total";
    private static final String TABLE_ITEM = "item";
    private static final String TAG = ItemHelper.class.getSimpleName();

    public ItemHelper(Context paramContext)
    {
        super(paramContext, "productInfo", null, 7);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {

        paramSQLiteDatabase.execSQL("CREATE TABLE item(id INTEGER PRIMARY KEY,name TEXT,description TEXT,price INTEGER,quantity INTEGER,total INTEGER,image TEXT) ");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS item");
        onCreate(paramSQLiteDatabase);
    }

    public static byte[] getBitmapAsByteArray(Bitmap paramBitmap)
    {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        paramBitmap.compress(Bitmap.CompressFormat.PNG, 0, localByteArrayOutputStream);
        return localByteArrayOutputStream.toByteArray();
    }

    public void addProduct(int id,String name, String description, int price, int quantity, int total, String image)
    {

        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("id", id);
        localContentValues.put("name", name);
        localContentValues.put("description", description);
        localContentValues.put("price", price);
        localContentValues.put("quantity", quantity);
        localContentValues.put("total", total);
        localContentValues.put("image", image);
        long l = localSQLiteDatabase.insert("item", null, localContentValues);
        localSQLiteDatabase.close();
        Log.e(TAG, "New item inserted into sqlite: " + l);
    }


    public List<Product>getProductById(String barcode){
        ArrayList product=new ArrayList();
        SQLiteDatabase database=getReadableDatabase();
        String selectQuery="SELECT * FROM item WHERE barcode=?";
        Cursor cursor=database.rawQuery(selectQuery,new String[]{barcode});
        cursor.moveToFirst();
        if(cursor.getCount() >0){
            int id = cursor.getInt(0);
            String name= cursor.getString(1);
            String description = cursor.getString(2);
            double price = cursor.getDouble(3);
            int quantity = cursor.getInt(4);

            String image = cursor.getString(6);
           // product.add(new Product(id, name, description, price, quantity, image)) ;
        }

        cursor.close();

        return product;
    }


    public List<Product> getProductInfo() {
        List<Product> localArrayList= new ArrayList();
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        Cursor cursor = localSQLiteDatabase.rawQuery("SELECT  * FROM item", null);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                int price = cursor.getInt(3);
                int quantity = cursor.getInt(4);
                int total=cursor.getInt(5);
                String image = cursor.getString(6);
                localArrayList.add(new Product(id, name, description, price, quantity, image,total));

                Log.e(TAG, image);
            }
        }finally {
            cursor.close();
        }

        cursor.close();
        localSQLiteDatabase.close();
        return localArrayList;
    }




    public List<Receipt> getReceipt() {
        List<Receipt> localArrayList= new ArrayList();
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        Cursor cursor = localSQLiteDatabase.rawQuery("SELECT  * FROM item", null);
        try {
            while (cursor.moveToNext()) {

                String name = cursor.getString(1);
                int quantity = cursor.getInt(4);
                int total=cursor.getInt(5);
                BigDecimal amount=new BigDecimal(total);
                Receipt receipt=new Receipt(name,quantity,amount);
                localArrayList.add(receipt);
                Log.e(TAG, localArrayList.toString());
            }
        }finally {
            cursor.close();
        }

        cursor.close();
        localSQLiteDatabase.close();
        return localArrayList;
    }
    public void deleteAllItems() {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        localSQLiteDatabase.delete("item", null, null);
        localSQLiteDatabase.close();
        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteItem(int paramInt)
    {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        localSQLiteDatabase.delete("item", "id=?", new String[] { String.valueOf(paramInt) });
        localSQLiteDatabase.close();
    }
    public void updateQuantity(int quantity,int total,int position){
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", quantity);
        contentValues.put("total", total);
        String query = "id=" + position;
        localSQLiteDatabase.update("item", contentValues, query, null);
        localSQLiteDatabase.close();
    }

    public void getProduct(String barcode){
        SQLiteDatabase database=getReadableDatabase();

    }
    public int getDbQuantity(){
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT SUM(quantity) FROM item", null);
        localCursor.moveToFirst();
        return localCursor.getInt(0);
    }

    public int getDbTotal()
    {
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT SUM(total) FROM item", null);
        localCursor.moveToFirst();
        return localCursor.getInt(0);
    }


    public int getMaxQuantity()
    {
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT MAX(quantity) FROM item", null);
        localCursor.moveToFirst();
        localCursor.getInt(0);
        return localCursor.getInt(0);
    }

    public int getProductCode()
    {
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT MAX(id) FROM item", null);
        localCursor.moveToFirst();
        return localCursor.getInt(0);
    }




}
