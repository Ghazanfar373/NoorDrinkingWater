package com.pac.ideatech.noordrinkingwater;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import ss.com.bannerslider.Slider;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1122;
    FrameLayout layout_container;
    private int cartCount = 0;
    private SessionManager manager;
    private int hot_number = 0;
    private TextView ui_hot = null;


    private static final String PHONE_NUMBER="tel:+971559011759";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager=new SessionManager(this);

        //Slider for app




        layout_container = (FrameLayout) findViewById(R.id.frag_container);
       // countTextView = (TextView) findViewById(R.id.counterTextView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(PHONE_NUMBER));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    //return;
                } else
                    startActivity(callIntent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MainActivity.this,EditProfile.class));
            }
        });
        TextView tv_name= (TextView) header.findViewById(R.id.header_title);
        TextView tv_email= (TextView) header.findViewById(R.id.header_email);
        tv_name.setText(manager.getUser_fName());
        tv_email.setText(manager.getUserEmail());
        navigationView.setNavigationItemSelectedListener(this);
        //loading Dashboard
        if (Utils.isConnected(this)) {
            ProductFragment fragment = new ProductFragment();
            android.app.FragmentManager manager = getFragmentManager();
            android.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frag_container, fragment);
            transaction.commit();
        } else {
            Utils.showSettingsAlert("Enable Data Connection!", this);
        }

    }
    int i=0;
    @Override
    public void onBackPressed() {

        i++;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            ProductFragment fragment = new ProductFragment();
            android.app.FragmentManager manager = getFragmentManager();
            android.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frag_container, fragment);
            transaction.commit();
            if(i==2)
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(PHONE_NUMBER));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final View menu_hotlist = menu.findItem(R.id.action_addcart).getActionView();
        ui_hot = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
        menu_hotlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Order.orderList.size()<1){
                    Toast.makeText(MainActivity.this, "Cart is Empty!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, CheckoutActivity.class);
                    startActivityForResult(intent, 2244);
                }
            }
        });
        updateHotCount(hot_number);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

//           MenuItem itemData = menu.findItem(R.id.action_addcart);
//           CartCounterActionView actionView = (CartCounterActionView) itemData.getActionView();
//           actionView.setItemData(menu, itemData);
//         actionView.setCount(cartCount);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, EditProfile.class));

            return true;
        }else if(id==R.id.action_addcart){

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProductFragment fragment = new ProductFragment();
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        if(resultCode==RESULT_OK ) {
            if (requestCode == 2244) {
                Order.num_orders = 0;
                Order.orderList.clear();
                Order.setClearEnabled(false);
                //Toast.makeText(this, "Result back" , Toast.LENGTH_SHORT).show();
                updateHotCount(Order.getNum_orders());

                transaction.replace(R.id.frag_container, fragment);
                transaction.commit();
            }
        }else if(resultCode==RESULT_CANCELED){
            updateHotCount(Order.getNum_orders());
            transaction.replace(R.id.frag_container, fragment);
            transaction.commit();
        }
    }

    public void updateHotCount(final int new_hot_number) {
        hot_number = new_hot_number;
        if (ui_hot == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_hot_number == 0)
                    ui_hot.setVisibility(View.INVISIBLE);
                else {
                    ui_hot.setVisibility(View.VISIBLE);
                    ui_hot.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_products) {
            // Handle the camera action
            ProductFragment fragment=new ProductFragment();
            android.app.FragmentManager manager=getFragmentManager();
            android.app.FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.frag_container,fragment);
            transaction.commit();
        } else if (id == R.id.nav_order) {
       //Home Button


    } else if (id == R.id.nav_profile) {
      startActivity(new Intent(MainActivity.this,EditProfile.class));
     //   Toast.makeText(this, "Please Select Some Product!", Toast.LENGTH_LONG).show();
    }else if (id == R.id.nav_rateus) {

            RateUsFragment fragment=new RateUsFragment();
            android.app.FragmentManager manager=getFragmentManager();
            android.app.FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.frag_container,fragment);
            transaction.commit();
        }
        else if (id == R.id.nav_history) {
//            Intent intent=new Intent(MainActivity.this,CheckoutActivity.class);
//            intent.putExtra("from","history");
//            startActivity(intent);
            HistoryFragment fragment=new HistoryFragment();
            android.app.FragmentManager manager=getFragmentManager();
            android.app.FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.frag_container,fragment);
            transaction.commit();

        } else if (id == R.id.nav_suggestion) {
            SuggestionFragment fragment = new SuggestionFragment();
            android.app.FragmentManager manager = getFragmentManager();
            android.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frag_container, fragment);
            transaction.commit();


        } else if (id == R.id.nav_complaint) {
            BlankFragment fragment = new BlankFragment();
            android.app.FragmentManager manager = getFragmentManager();
            android.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frag_container, fragment);
            transaction.commit();
        } else if (id == R.id.nav_share) {
            try {

                ShareCompat.IntentBuilder.from(MainActivity.this)
                        .setType("text/plain")
                        .setChooserTitle("Chooser title")
                        .setText("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())
                        .startChooser();
            } catch(Exception e) {
                //e.toString();
            }
        } else if (id == R.id.nav_logout) {
            SessionManager manager=new SessionManager(this);
            manager.logout();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
