package apps.mobile.ostium;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import apps.mobile.ostium.Module.eventGeneric;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//        Main Activity
//        Home Activity
//        Settings Activity
//        Location Activity
//        Map Activity
//        Dev Activity One
//        Dev Activity Two
//        Dev Activity Three


public class MainActivity extends AppCompatActivity {
    private static final String LogTagClass = MainActivity.class.getSimpleName();
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    public ArrayList<eventGeneric> userEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false); // Hide the action bar title
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //LOAD EVENTS FROM SAVED EVENTS




        // region Drawer - onCreate
        dl = findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        dl.addDrawerListener(t);
        t.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
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
                }


        );

        // endregion Drawer

        // region CardRecycler - onCreate
        RecyclerView recCardList = (RecyclerView) findViewById(R.id.cardList);
        recCardList.setHasFixedSize(true);
        LinearLayoutManager llmCard = new LinearLayoutManager(this);
        llmCard.setOrientation(LinearLayoutManager.VERTICAL);
        recCardList.setLayoutManager(llmCard);

        CardAdapter ca = new CardAdapter(createCardList(8));
        recCardList.setAdapter(ca);

        // region ListRecycler - onCreate
//        RecyclerView recTagList = (RecyclerView) recCardList.findViewById(R.id.tagList);
//        recTagList.setHasFixedSize(true);
//        LinearLayoutManager llmTag = new LinearLayoutManager(this);
//        llmTag.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recTagList.setLayoutManager(llmTag);
//
//        TagAdapter ta = new TagAdapter(createTagList(8));
//        recTagList.setAdapter(ta);

        // endregion Recycler

//        //region extra recyclers
//        // region Recycler - onCreate
//        RecyclerView recList1 = (RecyclerView) findViewById(R.id.cardList2);
//        recList1.setHasFixedSize(true);
//        LinearLayoutManager llm1 = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recList1.setLayoutManager(llm1);
//
//        CardAdapter ca1 = new CardAdapter(createCardList(3));
//        recList1.setAdapter(ca1);
//
//        // region Recycler - onCreate
//        RecyclerView recList2 = (RecyclerView) findViewById(R.id.cardList3);
//        recList2.setHasFixedSize(true);
//        LinearLayoutManager llm2 = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recList2.setLayoutManager(llm);
//
//        CardAdapter ca2 = new CardAdapter(createCardList(3));
//        recList2.setAdapter(ca2);
//
//        // region Recycler - onCreate
//        RecyclerView recList3 = (RecyclerView) findViewById(R.id.cardList4);
//        recList3.setHasFixedSize(true);
//        LinearLayoutManager llm3 = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recList3.setLayoutManager(llm3);
//
//        CardAdapter ca3 = new CardAdapter(createCardList(3));
//        recList3.setAdapter(ca3);


        //endregion extra recyclers
    }

    //region Drawer Methods
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
    //endregion Drawer


    private List<CardInfo> createCardList(int size) {

        List<CardInfo> result = new ArrayList<CardInfo>();
        for (eventGeneric item : userEvents) {
            CardInfo ci = new CardInfo();
//            ci.name = (CardInfo.NAME_PREFIX) + " title title title title title title title title title title title title title title title title title title title title title title title title title title title title title title title title " + i;
//            ci.surname = CardInfo.SURNAME_PREFIX + " content content content content content content content content content content content content content content content content content content content content content content content"+ i;
//            ci.email = CardInfo.EMAIL_PREFIX + " other other other other other other other other other other other other other other other other other other other other other other other other other other other other other other other other other other "+i + "@test.com";
            ci.title = item.getTitle();
            ci.details = item.getDescription();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

            ci.date = dateFormat.format(new Date(Long.parseLong(item.getStartTime())));
            result.add(ci);

        }

        return result;
    }


    private List<TagInfo> createTagList(int size) {

        List<TagInfo> result = new ArrayList<TagInfo>();
        for (int i = 1; i <= size; i++) {
            TagInfo ci = new TagInfo();
            ci.locationName = "Shops";
            result.add(ci);
        }
        return result;
    }

}
