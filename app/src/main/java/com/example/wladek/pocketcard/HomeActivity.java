package com.example.wladek.pocketcard;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wladek.pocketcard.helper.DatabaseHelper;


public class HomeActivity extends AppCompatActivity {
    DatabaseHelper myDb;

    Button btnCreateStudent;
    Button btnCancelCreateStudent;
    Button btnSkipLogin;

    ProgressBar create_progress;

    LinearLayout homeLayOut;
    LinearLayout logInLayOut;
    LinearLayout createStudentLayOut;

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this);
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

        btnCreateStudent = (Button)findViewById(R.id.btnCreateStudent);
        btnCancelCreateStudent = (Button)findViewById(R.id.btnCancelCreateStudent);
        btnSkipLogin = (Button)findViewById(R.id.btnSkipLogin);

        create_progress = (ProgressBar)findViewById(R.id.create_progress);

        homeLayOut = (LinearLayout) findViewById(R.id.homeLayout);
        logInLayOut = (LinearLayout) findViewById(R.id.logInLayOut);
        createStudentLayOut = (LinearLayout) findViewById(R.id.createStudentLayOut);


        //Hide layouts
        logInLayOut.setVisibility(View.INVISIBLE);
        createStudentLayOut.setVisibility(View.INVISIBLE);

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

                hideViews();
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.nav_register:
                        logInLayOut.setVisibility(View.VISIBLE);

                        break;
                    case R.id.nav_buy:

                        Intent intent = new Intent(HomeActivity.this , BuyScreenActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_settings:
                        Toast.makeText(getApplicationContext() , "Settings" , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_view_items:
                        Toast.makeText(getApplicationContext() , "View item list " , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_update:
                        Toast.makeText(getApplicationContext() , " Update " , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.demo_student:

                        createStudentLayOut.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });


        btnSkipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideViews();
                createStudentLayOut.setVisibility(View.VISIBLE);
            }
        });

        btnCreateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_progress.setVisibility(View.VISIBLE);
            }
        });
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

    public void hideViews(){
        homeLayOut.setVisibility(View.INVISIBLE);
        logInLayOut.setVisibility(View.INVISIBLE);
        createStudentLayOut.setVisibility(View.INVISIBLE);
        create_progress.setVisibility(View.INVISIBLE);
    }
}
