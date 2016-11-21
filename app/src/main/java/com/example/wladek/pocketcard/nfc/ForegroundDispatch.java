package com.example.wladek.pocketcard.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;

/**
 * Created by wladek on 11/18/16.
 */
public class  ForegroundDispatch {
    private NfcAdapter nfcAdapter;
    private Context context;
    private Class clazz;
    private Activity activity;

    public ForegroundDispatch(NfcAdapter nfcAdapter , Context context , Class c , Activity activity){
        this.activity = activity;
        this.context = context;
        this.nfcAdapter = nfcAdapter;
        this.clazz = c;
    }

    public void enable() {

        if (nfcAdapter != null) {
            Intent intent = new Intent(context,clazz);
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            IntentFilter[] intentFilter = new IntentFilter[]{};

            nfcAdapter.enableForegroundDispatch(activity, pendingIntent, intentFilter, null);
        }
    }

    public void disable() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(activity);
        }
    }
}
