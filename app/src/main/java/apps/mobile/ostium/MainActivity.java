package apps.mobile.ostium;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

//        Main Activity
//        Home Activity
//        Settings Activity
//        Location Activity
//        Map Activity
//        Dev Activity One
//        Dev Activity Two
//        Dev Activity Three

public class MainActivity extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private static final String LogTagClass = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        gotoHome(nv);
                        break;
                    case R.id.nav_location:
                        gotoLocation(nv);
                        break;
                    case R.id.nav_map:
                        gotoMap(nv);
                        break;
                    case R.id.nav_settings:
                        gotoSettings(nv);
                        break;
                    case R.id.nav_dev_one:
                        gotoDevOne(nv);
                        break;
                    case R.id.nav_dev_two:
                        gotoDevTwo(nv);
                        break;
                    case R.id.nav_dev_three:
                        gotoDevThree(nv);
                        break;
                    default:
                        return true;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void gotoHome(View view) {
        Log.d(LogTagClass, "Button Home clicked!");
        startActivity(new Intent(this, MainActivity.class));
    }

    public void gotoSettings(View view) {
        Log.d(LogTagClass, "Button Settings clicked!");
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void gotoLocation(View view) {
        Log.d(LogTagClass, "Button Location clicked!");
        startActivity(new Intent(this, LocationActivity.class));
    }

    public void gotoMap(View view) {
        Log.d(LogTagClass, "Button Map clicked!");
        startActivity(new Intent(this, MapActivity.class));
    }

    public void gotoDevOne(View view) {
        Log.d(LogTagClass, "Button Dev One clicked!");
        startActivity(new Intent(this, DevActivityOne.class));
    }

    public void gotoDevTwo(View view) {
        Log.d(LogTagClass, "Button Dev Two clicked!");
        startActivity(new Intent(this, DevActivityTwo.class));
    }

    public void gotoDevThree(View view) {
        Log.d(LogTagClass, "Button Dev Three clicked!");
        startActivity(new Intent(this, DevActivityThree.class));
    }


}