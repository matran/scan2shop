package com.alienware.scan2shop.helpers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.alienware.scan2shop.data.History;
import com.alienware.scan2shop.data.Receipt;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
public class ReceiptHelper extends SQLiteOpenHelper {
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
    public ReceiptHelper(Context paramContext) {
        super(paramContext, "receipthelper", null, 8);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {

        paramSQLiteDatabase.execSQL("CREATE TABLE receipt(id TEXT,barcode TEXT,date TEXT,name TEXT,quantity INTEGER,total INTEGER) ");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS receipt");
        onCreate(paramSQLiteDatabase);
    }
    public void addReceipt(String id,String barcode,String date,String name, int quantity, Double total) {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("id",id);
        localContentValues.put("barcode",barcode);
        localContentValues.put("date",date);
        localContentValues.put("name", name);
        localContentValues.put("quantity", quantity);
        localContentValues.put("total", total);
        long l = localSQLiteDatabase.insert("receipt", null, localContentValues);
        localSQLiteDatabase.close();
        Log.e(TAG, "New item inserted into sqlite: " + l);
    }
    public Double getTotal(){
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT SUM(total) FROM receipt", null);
        localCursor.moveToFirst();
        return Double.valueOf(localCursor.getDouble(0));
    }

   public int getQuantity(){
       Cursor localCursor = getWritableDatabase().rawQuery("SELECT SUM(quantity) FROM receipt", null);
       localCursor.moveToFirst();
       return localCursor.getInt(0);
   }

   public String getDate(){
       Cursor localCursor = getWritableDatabase().rawQuery("SELECT date FROM receipt", null);
       localCursor.moveToFirst();
       return localCursor.getString(0);

   }
   public String getBarcode(){
       Cursor localCursor = getWritableDatabase().rawQuery("SELECT barcode FROM receipt", null);
       localCursor.moveToFirst();
       return localCursor.getString(0);
   }

    public String getID(){
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT id FROM receipt", null);
        localCursor.moveToFirst();
        return localCursor.getString(0);

    }

    public List<Receipt> getReceipt() {
        List<Receipt> localArrayList= new ArrayList();
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        Cursor cursor = localSQLiteDatabase.rawQuery("SELECT  * FROM receipt", null);
        if(cursor.getCount()>0) {
            try {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(3);
                    int quantity = cursor.getInt(4);
                    double total = cursor.getDouble(5);
                    BigDecimal bigDecimal = new BigDecimal(total);
                    Receipt receipt = new Receipt(name, quantity, bigDecimal);
                    localArrayList.add(receipt);
                    Log.e(TAG, localArrayList.toString());
                }
            } finally {
                cursor.close();
            }

            cursor.close();
            localSQLiteDatabase.close();
            return localArrayList;
        }else {
            cursor.close();
            localSQLiteDatabase.close();
            return null;
        }
    }
    public void deleteAllItems() {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        localSQLiteDatabase.delete("receipt", null, null);
        localSQLiteDatabase.close();
        Log.d(TAG, "Deleted all user info from sqlite");
    }
}
