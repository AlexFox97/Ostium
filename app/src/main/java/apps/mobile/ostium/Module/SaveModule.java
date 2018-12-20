package apps.mobile.ostium.Module;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SaveModule
{
    private final String FileName = "Ostium_Data_File.meep";

    private Activity activity;

    public SaveModule(Activity activity)
    {
        this.activity = activity;
    }

    public Boolean SaveString(String Tag, String data)
    {
        FileOutputStream fout;
        File file = new File(activity.getApplicationContext().getFilesDir(), FileName);
        try
        {
            if(!file.exists())
            {
                file.createNewFile();
            }

            fout = activity.getApplicationContext().openFileOutput(FileName, Context.MODE_PRIVATE);
            String save = Tag + "~"+ data + "\n";
            fout.write(save.getBytes());
            fout.close();
            return true;
        }
        catch (Exception e)
        {
            Log.d("Write File Failed", e.getStackTrace().toString());
        }

        return false;
    }

    public String LoadString(String tag)
    {
        try
        {
            FileInputStream fin = activity.getApplicationContext().openFileInput(FileName);
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
            {
                int tagEnd = line.indexOf('~');
                String test = line.substring(0, tagEnd);
                if(test.equals(tag))
                {
                    int endline = line.length();
                    String m =  line.substring(tagEnd+1, endline);
                    return m;
                }
            }
        }
        catch (Exception e)
        {
            Log.d("Read File Failed", e.getStackTrace().toString());
        }

        return "";
    }

}