package com.example.wladek.pocketcard.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Created by wladek on 8/28/16.
 */
public class TagHandler {

    private String TAG = TagHandler.class.getName();


    public TagHandler(){

    }


    public String readTag(Intent intent) {

        String cardNumber = null;

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {

            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (parcelables != null && parcelables.length > 0) {

                cardNumber = readTextFromMessage((NdefMessage) parcelables[0]);

            } else {
                return cardNumber;
            }

        }

        return cardNumber;

    }



    private String readTextFromMessage(NdefMessage ndefMessage) {

        String tagContent = null;

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            tagContent = getTextFromNdefRecord(ndefRecord);

        } else {

            return null;

        }

        return tagContent;

    }


    /**
     * @param ndefRecord
     * @return string of ndef record payload
     * <p>
     * To Do : Convert byte[] to string.
     */
    private String getTextFromNdefRecord(NdefRecord ndefRecord) {

        String cardContent = null;

        byte[] contents = ndefRecord.getPayload();

        try {
            cardContent = new String(contents, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e(TAG, " RECORD ++++ " + cardContent.substring(3));

        cardContent = cardContent.substring(3);

        return cardContent;

    }

    public void wrightTag(Intent intent , String cardNumber){
        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            NdefMessage ndefMessage = createNdefMessage(cardNumber);

            writeNdefMessage(tag, ndefMessage);
        }
    }

    private NdefMessage createNdefMessage(String content){

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ ndefRecord});

        return ndefMessage;
    }


    private NdefRecord createTextRecord(String content){

        try {

            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;

            final ByteArrayOutputStream payLoad = new ByteArrayOutputStream(1 + languageSize + textLength);

            payLoad.write((byte)(languageSize & 0x1F));
            payLoad.write(language , 0 , languageSize);
            payLoad.write(text , 0 , textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN , NdefRecord.RTD_TEXT , new byte[0] , payLoad.toByteArray());

        }catch (UnsupportedEncodingException e){
            Log.e(TAG , e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage){
        try {

            if(tag == null){
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if(ndef == null){
                //Format tag with the ndef format and writes the message
                formatTag(tag, ndefMessage);
            }else {
                ndef.connect();

                if (!ndef.isWritable()){
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
            }

        }catch (Exception e){
            Log.e(TAG , e.getMessage());
            e.printStackTrace();
        }
    }


    private void formatTag(Tag tag , NdefMessage ndefMessage){

        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null){
                Log.e("formatTag", " Tag in not NDF formatable ");
                return;
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

        }catch (Exception e){
            Log.e(TAG , e.getMessage());
            e.printStackTrace();
        }
    }
}
