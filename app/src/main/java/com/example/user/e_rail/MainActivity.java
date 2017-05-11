package com.example.user.e_rail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,FragmentManager.OnBackStackChangedListener {

    private FragmentManager fragmentManager;
    private final static String MAIN_FRAGMENT_TAG="main_fragment";
    private final static String LIVE_STATUS_TAG="live_status";
    private final static String SEAT_TAG="seat_fragment";
    private int check=0;
    private int indexUpdate=0;

    private final static String TRAIN_BETWEEN_STATION_TAG="trainbetweenstation_fragment";

    private final static String FARE_TAG="fare_fragment";
    private AlertDialog.Builder alertDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        alertDialog=new AlertDialog.Builder(this);
        fragmentManager=getSupportFragmentManager();
        FragmentMain fragmentMain=new FragmentMain();
        fragmentManager.beginTransaction().add(R.id.content_main,fragmentMain,MAIN_FRAGMENT_TAG).commit();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        fragmentManager.addOnBackStackChangedListener(this);


    }



    @Override
    public void onBackPressed() {
        Log.e("check",String.valueOf(check));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(check==1)
                finish();
            else
            {
                check=0;
                super.onBackPressed();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings is in progress",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.home)
            {
                FragmentMain fragmentMain=new FragmentMain();
                fragmentManager.beginTransaction().replace(R.id.content_main,fragmentMain).addToBackStack(MAIN_FRAGMENT_TAG).commit();
            }
        if (id == R.id.nav_live_status) {
            // Handle the camera action
           LiveStatusFragment1 liveStatusFragment1=new LiveStatusFragment1();

            fragmentManager.beginTransaction().replace(R.id.content_main,liveStatusFragment1).addToBackStack(LIVE_STATUS_TAG).commit();
        } else if (id == R.id.nav_trailn_info) {
            TrainsBetweenStation trainsBetweenStation=new TrainsBetweenStation();

            fragmentManager.beginTransaction().replace(R.id.content_main,trainsBetweenStation).addToBackStack(TRAIN_BETWEEN_STATION_TAG).commit();
        } else if (id == R.id.nav_fare_info) {
           FareInfo fareInfo= new FareInfo();

            fragmentManager.beginTransaction().replace(R.id.content_main,fareInfo).addToBackStack(FARE_TAG).commit();

        } else if (id == R.id.nav_seat_availability) {
           SeatAvailability1 seatAvailability1=new SeatAvailability1();

            fragmentManager.beginTransaction().replace(R.id.content_main,seatAvailability1).addToBackStack(SEAT_TAG).commit();
        } else if (id == R.id.nav_share) {

            Intent share=new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT,"E-rail app");
            share.putExtra(Intent.EXTRA_TEXT,"https://www.dropbox.com/s/4r26i8rsiyvsvf2/app-debug.apk?dl=0");
            startActivity(Intent.createChooser(share,"share via:"));

        } else if (id == R.id.aboutus) {
            Intent intent= new Intent(MainActivity.this,Aboutus.class);
            startActivity(intent);


        }
        else if(id==R.id.pnr_status){
            PnrStatus pnrStatus=new PnrStatus();
            fragmentManager.beginTransaction().replace(R.id.content_main,pnrStatus).addToBackStack("pnr").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackStackChanged() {

        int index = fragmentManager.getBackStackEntryCount() - 1;
        if (index >= 0) {
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            String tag = backEntry.getName();

            Log.e("name of the fragment", tag);
            Log.e("check",String.valueOf(check));
            if (tag.equals(MAIN_FRAGMENT_TAG))
                indexUpdate=index;

            if(indexUpdate<index || index==0)
                check=0;
            else
                check=1;


        }

    }
}
