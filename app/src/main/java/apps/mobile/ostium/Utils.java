package apps.mobile.ostium;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;

public class Utils
{
    private static int sTheme;

    public final static int OSTIUM = 0;
    public final static int OSTIUM_DARK= 1;
    public final static int OSTIUM_TWO = 2;
    public final static int OSTIUM_DARK_TWO= 3;

    public static final int Theme_Ostium_Deuteranopia=0x7f10016d;
    public static final int Theme_Ostium_Protanopia=0x7f10016e;
    public static final int Theme_Ostium_Tritanopia=0x7f10016f;
    public static final int Theme_Ostium_Dark=0x7f100170;

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
            case OSTIUM:
                activity.setTheme(R.style.Theme_Ostium);
                break;
            case OSTIUM_DARK:
                activity.setTheme(R.style.Theme_Ostium_Dark);
                break;
            case OSTIUM_TWO:
                activity.setTheme(R.style.Theme_Ostium_Two);
                break;
            case OSTIUM_DARK_TWO:
                activity.setTheme(R.style.Theme_Ostium_Dark_Two);
                break;
        }
    }
}