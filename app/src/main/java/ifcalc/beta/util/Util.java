package ifcalc.beta.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

public class Util {
    private static SharedPreferences preferences;

    public static SharedPreferences getPreferences(Context context) {
        if (preferences == null)
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences;
    }

    public static Typeface getFontTheme(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/pfbeausans.ttf");
    }
}
