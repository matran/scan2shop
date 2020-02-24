package com.alienware.scan2shop.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class FirebaseRegManager {
    private static final String KEY_TOKEN = "token";
    private static final String PREF_NAME = "regmanager";
    private static String TAG = com.alienware.scan2shop.helpers.SessionManager.class.getSimpleName();
    int PRIVATE_MODE = 0;
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    public FirebaseRegManager(Context paramContext)
    {
        this._context = paramContext;
        this.pref = this._context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = this.pref.edit();
    }
    public String getReg() {
        return this.pref.getString(KEY_TOKEN, null);
    }
    public void setReg(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }
    public boolean isSent()
    {
        return this.pref.getBoolean("issent", false);
    }
    public void setSent(boolean paramBoolean)
    {
        editor.putBoolean("issent", paramBoolean);
        editor.commit();
    }
}
