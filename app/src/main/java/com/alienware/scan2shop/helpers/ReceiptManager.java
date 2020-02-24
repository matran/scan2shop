package com.alienware.scan2shop.helpers;
import android.content.Context;
import android.content.SharedPreferences;
public class ReceiptManager{
    private static final String KEY_IS_PAID = "isPaid";
    private static final String KEY_RECEIPT_ID="receiptid";
    private static final String PREF_NAME = "receiptmanager";
    private static String TAG = ReceiptManager.class.getSimpleName();
    int PRIVATE_MODE = 0;
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    public ReceiptManager(Context paramContext)
    {
        this._context = paramContext;
        this.pref = this._context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = this.pref.edit();
    }

    public void setReceiptId(String receiptid){
        editor.putString(KEY_RECEIPT_ID, receiptid);
        editor.commit();
    }
    public String getReceiptNo(){

        return pref.getString(KEY_RECEIPT_ID, "hh");
    }
    public void setCurrentReceipt(String receiptid){
        editor.putString("currentreceipt",receiptid);
        editor.commit();
    }
    public  String getCurrentReceipt(){
        return pref.getString("currentreceipt", "");
    }
    public boolean isPaid()
    {
        return this.pref.getBoolean(KEY_IS_PAID, false);
    }
    public void setPaid(boolean paramBoolean)
    {
        editor.putBoolean(KEY_IS_PAID, paramBoolean);
        editor.commit();
    }
}
