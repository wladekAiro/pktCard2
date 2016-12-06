package com.example.wladek.pocketcard;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.wladek.pocketcard.nfc.NfcHandler;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BalanceActivity extends AppCompatActivity {

    SweetAlertDialog sweetAlertDialog;
    NfcAdapter nfcAdapter;
    NfcHandler nfcHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        nfcHandler = new NfcHandler(nfcAdapter , BalanceActivity.this ,
                BalanceActivity.class , BalanceActivity.this);

        showSweetAlert();

    }

    public void showSweetAlert(){
        sweetAlertDialog = new SweetAlertDialog(this , SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Please tap your card");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.showCancelButton(true);
        sweetAlertDialog.setCancelText("Cancel");
        sweetAlertDialog.show();

        sweetAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                sweetAlertDialog.dismiss();
                onBackPressed();
            }
        });
    }

    public void showPinDialog(){

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            if (sweetAlertDialog != null) {
                Toast.makeText(this, "Card detected", Toast.LENGTH_LONG).show();
                sweetAlertDialog.dismiss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcHandler.enableForeGroundDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcHandler.disableForeGroundDispatch();
    }
}
