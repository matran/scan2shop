package com.alienware.scan2shop.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.alienware.scan2shop.data.Goods;
import com.alienware.scan2shop.data.Product;
import com.alienware.scan2shop.data.Receipt;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodsHelper extends SQLiteOpenHelper {
    private static final String TAG = GoodsHelper.class.getSimpleName();

    public GoodsHelper(Context paramContext)
    {
        super(paramContext, "goodshelper", null, 2);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {

        paramSQLiteDatabase.execSQL("CREATE TABLE item(id INTEGER PRIMARY KEY,name TEXT,description TEXT,price INTEGER,image TEXT) ");
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

    public void addProduct(int id,String name, String description, Double price, String image)
    {

        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("id",id);
        localContentValues.put("name", name);
        localContentValues.put("description", description);
        localContentValues.put("price", price);
        localContentValues.put("image", image);
        long l = localSQLiteDatabase.insert("item", null, localContentValues);
        localSQLiteDatabase.close();
        Log.e(TAG, "New item inserted into sqlite: " + l);
    }


    public List<Product> getProductById(String barcode){
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

    public List<Goods> getProductInfo() {
        List<Goods> goodsList= new ArrayList();
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        Cursor cursor = localSQLiteDatabase.rawQuery("SELECT  * FROM item", null);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                String price = cursor.getString(3);
                String image = cursor.getString(4);
                BigDecimal bigDecimal=new BigDecimal(price);
                goodsList.add(new Goods(id, name, description, price, image));
                Log.e(TAG, goodsList.toString());
            }
        }finally {
            cursor.close();
        }

        cursor.close();
        localSQLiteDatabase.close();
        return goodsList;
    }




    public List<Receipt> getReceipt() {
        List<Receipt> localArrayList= new ArrayList();
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        Cursor cursor = localSQLiteDatabase.rawQuery("SELECT  * FROM item", null);
        try {
            while (cursor.moveToNext()) {

                String name = cursor.getString(1);
                int quantity = cursor.getInt(4);
                double total=cursor.getDouble(5);
                BigDecimal bigDecimal=new BigDecimal(total);
                Receipt receipt=new Receipt(name,quantity,bigDecimal);
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


    public void getProduct(String barcode){
        SQLiteDatabase database=getReadableDatabase();

    }
    public int getDbQuantity(){
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT SUM(quantity) FROM item", null);
        localCursor.moveToFirst();
        return localCursor.getInt(0);
    }

    public Double getDbTotal()
    {
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT SUM(total) FROM item", null);
        localCursor.moveToFirst();
        return Double.valueOf(localCursor.getDouble(0));
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
