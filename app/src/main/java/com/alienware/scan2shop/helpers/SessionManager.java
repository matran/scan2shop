package com.alienware.scan2shop.helpers;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by henry cheruiyot on 2/3/2018.
 */

public class SessionManager{

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String PREF_NAME = "shopperLogin";
    private static String TAG = SessionManager.class.getSimpleName();
    int PRIVATE_MODE = 0;
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public SessionManager(Context paramContext)
    {
        this._context = paramContext;
        this.pref = this._context.getSharedPreferences("shopperLogin", PRIVATE_MODE);
        this.editor = this.pref.edit();
    }

    public boolean isLoggedIn()
    {
        return this.pref.getBoolean("isLoggedIn", false);
    }

    public void setLogin(boolean paramBoolean)
    {
        this.editor.putBoolean("isLoggedIn", paramBoolean);
        this.editor.commit();
        Log.d(TAG, "User login session modified!");
    }
    public void setToken(String token){
       this.editor.putString("token",token);
       this.editor.commit();
    }
    public String getToken(){
        return this.pref.getString("token","");
    }

}
