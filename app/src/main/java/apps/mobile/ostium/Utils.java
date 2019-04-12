package apps.mobile.ostium;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

        switch (sTheme)
        {
            case OSTIUM:
                activity.setTheme(Theme_Ostium_Dark);
                sharedPref.edit().putInt("currentTheme",0).apply();
               //setTheme(activity, 0);
                break;
            case OSTIUM_DARK:
                activity.setTheme(Theme_Ostium_Deuteranopia);
                sharedPref.edit().putInt("currentTheme",1).apply();
               // setTheme(activity, 1);
                break;
            case OSTIUM_TWO:
                activity.setTheme(Theme_Ostium_Protanopia);
                sharedPref.edit().putInt("currentTheme",2 ).apply();
                //setTheme(activity, 2);
                break;
            case OSTIUM_DARK_TWO:
                activity.setTheme(Theme_Ostium_Tritanopia);
                sharedPref.edit().putInt("currentTheme",3 ).apply();
                //setTheme(activity, 3);
                break;
        }
        //activity.recreate();
    }

    public static void setTheme(Activity a, int theme)
    {
      if(a == null)
      {
          return;
      }
      else if(a.getParent() == null)
      {
          switch (theme ) {
              case 0:
                  a.setTheme(Theme_Ostium_Dark);
                  break;
              case 1:
                  a.setTheme(Theme_Ostium_Deuteranopia);
                  break;
              case 2:
                  a.setTheme(Theme_Ostium_Protanopia);
                  break;
              case 3:
                  a.setTheme(Theme_Ostium_Tritanopia);
                  break;
              default:
                  a.setTheme(R.style.Theme_Ostium);
          }

         // a.recreate();
      }
      else
      {
          setTheme(a.getParent(), theme);
          switch (theme ) {
              case 0:
                  a.setTheme(Theme_Ostium_Dark);
                  break;
              case 1:
                  a.setTheme(Theme_Ostium_Deuteranopia);
                  break;
              case 2:
                  a.setTheme(Theme_Ostium_Protanopia);
                  break;
              case 3:
                  a.setTheme(Theme_Ostium_Tritanopia);
                  break;
              default:
                  a.setTheme(R.style.Theme_Ostium);
          }
        //  a.recreate();
      }
    }
}