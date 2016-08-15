package com.example.wladek.pocketcard;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterCardActivity extends AppCompatActivity {

    @InjectView(R.id.inputFirstName)
    EditText inputFirstName;
    @InjectView(R.id.inputSecondName)
    EditText inputSecondName;
    @InjectView(R.id.inputSirName)
    EditText inputSirName;
    @InjectView(R.id.inputStudentNumber)
    EditText inputStudentNumber;
    @InjectView(R.id.btnSubmit)
    Button btnSubmit;

    ActionBar actionBar;

    String firstName;
    String secondName;
    String sirName;
    String studentNumber;

    NfcAdapter nfcAdapter;

    MaterialDialog.Builder builder;

    MaterialDialog dialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ButterKnife.inject(this);
        toolbar.setTitle("Card Management");
        setSupportActionBar(toolbar);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    Toast.makeText(RegisterCardActivity.this, "Form error", Toast.LENGTH_LONG).show();
                } else {
                    scanCard();
                }
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void scanCard() {
        builder = new MaterialDialog.Builder(RegisterCardActivity.this);
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
                Toast.makeText(RegisterCardActivity.this, "Card not detected", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        firstName = inputFirstName.getText().toString();
        secondName = inputSecondName.getText().toString();
        sirName = inputSirName.getText().toString();
        studentNumber = inputStudentNumber.getText().toString();

        if (firstName.isEmpty()) {
            inputFirstName.setError("first name must be provide");
            valid = false;
        } else {
            inputFirstName.setError(null);
        }

        if (secondName.isEmpty()) {
            inputSecondName.setError("last name must be provided");
            valid = false;
        } else {
            inputSecondName.setError(null);
        }

        if (sirName.isEmpty()) {
            inputSirName.setError("sir name must be provided");
            valid = false;
        } else {
            inputSirName.setError(null);
        }

        if (studentNumber.isEmpty()) {
            inputStudentNumber.setError("student number must be provided");
            valid = false;
        } else {
            inputStudentNumber.setError(null);
        }

        return valid;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {

            if (dialog != null) {

                Toast.makeText(this, "Card detected", Toast.LENGTH_LONG).show();

                dialog.dismiss();
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

    public void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, RegisterCardActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilter = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
    }

    public void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RegisterCard Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.wladek.pocketcard/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RegisterCard Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.wladek.pocketcard/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
