package com.example.wladek.pocketcard;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity {

    Button btnReg;
    Button btnBuy;
    Button btnRegBack;
    Button btnBuyBack;
    LinearLayout buyLayOut;
    LinearLayout regLayOut;
    LinearLayout homeLayOut;

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    String[] shopItems = {"Bread", "Cake", "Mandazi", "Doughnut", "Book", "Pencil"};

    ListView lstShopItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        /**
         * Logic buttons switch
         */

        btnReg = (Button) findViewById(R.id.btnReg);
        btnBuy = (Button) findViewById(R.id.btnBuy);
        btnRegBack = (Button) findViewById(R.id.btnRegBack);
        btnBuyBack = (Button) findViewById(R.id.btnBuyBack);

        buyLayOut = (LinearLayout) findViewById(R.id.buyLayOut);
        regLayOut = (LinearLayout) findViewById(R.id.regLayout);
        homeLayOut = (LinearLayout) findViewById(R.id.homeLayout);

        lstShopItems = (ListView) findViewById(R.id.lstShopItems);

        //Hide both layouts
        buyLayOut.setVisibility(View.INVISIBLE);
        regLayOut.setVisibility(View.INVISIBLE);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.nav_sync:
                        Toast.makeText(getApplicationContext(), "Syncronise", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_settings:
                        Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        homeLayOut.setVisibility(View.INVISIBLE);
                        break;

                    case R.id.nav_import:
                        Toast.makeText(getApplicationContext(), "Import", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_items:
                        Toast.makeText(getApplicationContext(), "Items", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_share:
                        Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_view:
                        Toast.makeText(getApplicationContext(), "View", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, " Register", Toast.LENGTH_LONG).show();
                homeLayOut.setVisibility(View.INVISIBLE);
                regLayOut.setVisibility(View.VISIBLE);
            }
        });


        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, " Buy ", Toast.LENGTH_LONG).show();
                homeLayOut.setVisibility(View.INVISIBLE);
                buyLayOut.setVisibility(View.VISIBLE);
            }
        });

        btnRegBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regLayOut.setVisibility(View.INVISIBLE);
                homeLayOut.setVisibility(View.VISIBLE);
            }
        });

        btnBuyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyLayOut.setVisibility(View.INVISIBLE);
                homeLayOut.setVisibility(View.VISIBLE);
            }
        });

        /**
         * Populate my list view
         */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_row, R.id.textListItem, shopItems);
        lstShopItems.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
            Toast.makeText(this, "Importing ", Toast.LENGTH_LONG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        Toast.makeText(this , "Importing ", Toast.LENGTH_LONG);
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_import) {
//
//            Toast.makeText(this , "Importing ", Toast.LENGTH_LONG);
//
//        } else  if (id == R.id.nav_items) {
//
//            Toast.makeText(HomeActivity.this , "Navigation ", Toast.LENGTH_LONG);
//
//        } else if (id == R.id.nav_settings) {
//
//            Toast.makeText(this , "Settings", Toast.LENGTH_LONG);
//
//        } else if (id == R.id.nav_share) {
//
//            Toast.makeText(this , "Share", Toast.LENGTH_LONG);
//
//        } else if (id == R.id.nav_sync) {
//
//            Toast.makeText(this , "Syncronising", Toast.LENGTH_LONG);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
