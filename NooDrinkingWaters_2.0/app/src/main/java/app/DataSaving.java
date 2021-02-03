package app;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static app.AppConfig.NEXT_NOTIFICATION_ID;

/**
 * Created by Faisali on 6/2/2016.
 */
public class DataSaving {

    // Shared preferences file name
    private String PREF_NAME = "nwc";
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public boolean setPrefValue(Context context, String key, String value) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getPrefValue(Context context, String key) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String defaultValue = "";
        if (key.equalsIgnoreCase(NEXT_NOTIFICATION_ID)) {
            defaultValue = "1";
        }
        return pref.getString(key, defaultValue);
    }
}
