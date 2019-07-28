package orbital.respberry.context;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveState {
    static final String PREF_USER_ID = "0";
    static final String PREF_USER_NAME = "username";

    static SharedPreferences getSaveState(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName, int userId)
    {
        SharedPreferences.Editor editor = getSaveState(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putInt(PREF_USER_ID, userId);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSaveState(ctx).getString(PREF_USER_NAME, "");
    }
    public static int getUserId(Context ctx){
        return getSaveState(ctx).getInt(PREF_USER_ID,0);
    }

    public static void clearData(Context ctx){
        SharedPreferences.Editor editor = getSaveState(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
