package com.example.wladek.pocketcard;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wladek.pocketcard.helper.DatabaseHelper;
import com.example.wladek.pocketcard.pojo.ShopItem;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    DatabaseHelper myDb;

    Button btnCreateStudent;
    Button btnCancelCreateStudent;
    Button btnSkipLogin;
    Button btnLogIn;

    ProgressBar create_progress;

    LinearLayout homeLayOut;
    LinearLayout logInLayOut;
    LinearLayout createStudentLayOut;

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    NfcAdapter nfcAdapter;

    MaterialDialog.Builder builder;

    MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this);
        setContentView(R.layout.activity_home);
        inserItems();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        checkIfEnabled();

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

        btnCreateStudent = (Button) findViewById(R.id.btnCreateStudent);
        btnCancelCreateStudent = (Button) findViewById(R.id.btnCancelCreateStudent);
        btnSkipLogin = (Button) findViewById(R.id.btnSkipLogin);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);

        create_progress = (ProgressBar) findViewById(R.id.create_progress);

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
                        checkNfc(nfcAdapter, HomeActivity.this);
                        break;
                    case R.id.nav_update:
                        Toast.makeText(getApplicationContext(), " Update ", Toast.LENGTH_SHORT).show();
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
                askForLogIn();
            }
        });

        btnCreateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_progress.setVisibility(View.VISIBLE);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RegisterCardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkIfEnabled() {
        if (nfcAdapter != null) {
            Toast.makeText(this , "Nfc supported" , Toast.LENGTH_LONG).show();
        }else {
            builder = new MaterialDialog.Builder(this);
            builder.title("NFC Not Supported !");
            builder.content("This device does not support NFC. You will not be able to access some features of this app.");
            builder.positiveText("Continue");
            builder.negativeText("Exit App");
            builder.cancelable(false);
            dialog = builder.build();
            dialog.show();

            onNewIntent(getIntent());

            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog dialog, DialogAction which) {
                    dialog.dismiss();
                }
            });

            builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog dialog, DialogAction which) {
                    dialog.dismiss();
                    finish();
                }

            });
        }
    }

    private void askForLogIn() {
//        hideViews();
//        createStudentLayOut.setVisibility(View.VISIBLE);

        builder = new MaterialDialog.Builder(this);
        builder.title("Log in required !");
        builder.content("You must log in as an administrator to register new card.");
        builder.positiveText("Ok");
        builder.negativeText("Cancel");
        builder.cancelable(false);
        dialog = builder.build();
        dialog.show();

        onNewIntent(getIntent());

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                dialog.dismiss();
            }
        });

        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                dialog.dismiss();
                hideViews();
                homeLayOut.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkNfc(NfcAdapter nfcAdapter, final Context context) {
        if (nfcAdapter != null) {
            if (nfcAdapter != null && nfcAdapter.isEnabled()) {

                builder = new MaterialDialog.Builder(context);
                builder.title("Swipe card");
                builder.content("waiting for card");
                builder.negativeText("Cancel");
                builder.progress(true, 0);
                builder.cancelable(false);
                dialog = builder.build();
                dialog.show();

                onNewIntent(getIntent());

                builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Toast.makeText(context, "Card not detected", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        homeLayOut.setVisibility(View.VISIBLE);
                    }
                });

            } else {

                builder = new MaterialDialog.Builder(context);
                builder.content("Please enable the nfc adaptor to proceed");
                builder.positiveText("Ok");
                builder.cancelable(false);

                dialog = builder.build();
                dialog.show();

                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        homeLayOut.setVisibility(View.VISIBLE);
                    }
                });

            }
        } else {
            builder = new MaterialDialog.Builder(this);
            builder.title("NFC Not Supported !");
            builder.content("This device does not support NFC. You cannot access this feature.");
            builder.positiveText("Continue");
            builder.negativeText("Exit App");
            builder.cancelable(false);
            dialog = builder.build();
            dialog.show();

            onNewIntent(getIntent());

            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog dialog, DialogAction which) {
                    dialog.dismiss();
                    homeLayOut.setVisibility(View.VISIBLE);
                }
            });

            builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog dialog, DialogAction which) {
                    dialog.dismiss();
                    finish();
                }

            });
        }
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
            Toast.makeText(this, "Importing ", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void hideViews() {
        homeLayOut.setVisibility(View.INVISIBLE);
        logInLayOut.setVisibility(View.INVISIBLE);
        createStudentLayOut.setVisibility(View.INVISIBLE);
        create_progress.setVisibility(View.INVISIBLE);
    }

    public void inserItems() {

        Log.e("TEST", "TESTING INSERT ITEMS ++++++++++++++++++++++++ ");

        List<ShopItem> shopItems = new ArrayList<ShopItem>();

        ShopItem shopItem = new ShopItem();
        shopItem.setName("Avocado");
        shopItem.setCode("AVO");
        shopItem.setUnitPrice(new Double(10));
        shopItems.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setName("Bread");
        shopItem.setCode("BRD");
        shopItem.setUnitPrice(new Double(50));
        shopItems.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setName("Burn");
        shopItem.setCode("BRN");
        shopItem.setUnitPrice(new Double(10));
        shopItems.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setName("Pencil");
        shopItem.setCode("PNC");
        shopItem.setUnitPrice(new Double(15));
        shopItems.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setName("Mandazi");
        shopItem.setCode("MNDZ");
        shopItem.setUnitPrice(new Double(5));
        shopItems.add(shopItem);

        shopItem = new ShopItem();
        shopItem.setName("Dognut");
        shopItem.setCode("DGNT");
        shopItem.setUnitPrice(new Double(10));
        shopItems.add(shopItem);

        boolean inserted = false;

        /**
         * Check if these items already exist in the db.
         */
        if (getShopItems().size() == 0) {
            if (!shopItems.isEmpty()) {
                for (ShopItem s : shopItems) {
                    inserted = myDb.insertItems(s.getName(), s.getCode(), s.getUnitPrice());
                }
            }

            if (!inserted) {
                Log.e("INSERT ", " ++++++++++ NOT INSERTED : " + inserted);
            } else {
                Log.e("INSERT ", " ++++++++++ INSERTED : " + inserted + " SHOP ITEMS " + shopItems.size());
            }
        }
    }

    public ArrayList<ShopItem> getShopItems() {
        ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();

        Cursor res = myDb.getAllShopItems();

        if (res.getCount() > 0) {
            while (res.moveToNext()) {

                ShopItem shopItem = new ShopItem();
                shopItem.setName(res.getString(1));
                shopItem.setCode(res.getString(2));
                shopItem.setUnitPrice(res.getDouble(3));

                shopItems.add(shopItem);
            }
        }

        return shopItems;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {

            if (dialog != null) {

                Toast.makeText(this, "Card detected", Toast.LENGTH_LONG).show();

                dialog.dismiss();

                intent = new Intent(HomeActivity.this, BuyScreenActivity.class);
                intent.putExtra("item_list", getShopItems());
                startActivity(intent);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }

 /*   @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        ACRA.init(getApplication());
    }*/

    public void enableForegroundDispatchSystem() {

        if(nfcAdapter != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            IntentFilter[] intentFilter = new IntentFilter[]{};

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
        }
    }

    public void disableForegroundDispatchSystem() {
        if(nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }
}
