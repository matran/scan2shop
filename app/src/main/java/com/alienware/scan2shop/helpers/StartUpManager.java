package com.alienware.scan2shop.helpers;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by henry cheruiyot on 2/3/2018.
 */

public class StartUpManager {
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String PREF_NAME = "scan2shopPreference";
    int PRIVATE_MODE = 0;
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    public StartUpManager(Context paramContext){
        this._context = paramContext;
        this.pref = this._context.getSharedPreferences("scan2shopPreference", this.PRIVATE_MODE);
        this.editor = this.pref.edit();
    }

    public boolean isFirstTimeLaunch()
    {
        return this.pref.getBoolean("IsFirstTimeLaunch", true);
    }

    public void setFirstTimeLaunch(boolean paramBoolean)
    {
        this.editor.putBoolean("IsFirstTimeLaunch", paramBoolean);
        this.editor.commit();
    }
}
