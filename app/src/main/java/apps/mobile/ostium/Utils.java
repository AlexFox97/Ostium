package apps.mobile.ostium;

import android.app.Activity;
import android.content.Intent;

public class Utils
{
    private static int sTheme;

    public final static int Ostium_Dark = 0;
    public final static int Deuteranopia= 1;
    public final static int Protanopia = 2;
    public final static int Tritanopia= 3;

    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case Ostium_Dark:
                activity.setTheme(R.style.Theme_Ostium_Dark);
                break;
            case Deuteranopia:
                activity.setTheme(R.style.Theme_Ostium_Deuteranopia);
                break;
            case Protanopia:
                activity.setTheme(R.style.Theme_Ostium_Protanopia);
                break;
            case Tritanopia:
                activity.setTheme(R.style.Theme_Ostium_Tritanopia);
                break;
        }
    }
}