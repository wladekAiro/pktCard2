package com.example.wladek.pocketcard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.wladek.pocketcard.helper.DatabaseHelper;
import com.example.wladek.pocketcard.net.LoginRequest;
import com.example.wladek.pocketcard.pojo.SchoolDetails;
import com.example.wladek.pocketcard.util.ConnectivityReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    // UI references.
    private EditText edUserName;
    private EditText edPassword;

    SweetAlertDialog sweetAlertDialog;

    DatabaseHelper databaseHelper;

    boolean loggedIn;

    private ConnectivityReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(LoginActivity.this);

        receiver = new ConnectivityReceiver(getApplicationContext() , LoginActivity.this);

        if(checkLoggedIn()){
            launchApp();
            finish();
        }

        // Set up the login form.
        edUserName = (EditText) findViewById(R.id.edUserName);
        edPassword = (EditText) findViewById(R.id.edPassword);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(receiver.hasNetworkConnection()){
                    attemptLogin();
                }else{
                    Snackbar.make(view, "Connect to internet and try again.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private boolean checkLoggedIn() {
        SchoolDetails schoolDetails = databaseHelper.getSchoolDetails();

        if (schoolDetails != null){
            return schoolDetails.getLoggedIn();
        }else {
            return false;
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        edUserName.setError(null);
        edPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = edUserName.getText().toString();
        String password = edPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            edPassword.setError("Provide password");
            focusView = edPassword;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            edUserName.setError("Provide username");
            focusView = edUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            logIn(username, password);

            if(loggedIn){
                launchApp();
            }else {
                edUserName.setError("Wrong username or password");
                edPassword.setError("Wrong username or password");
                focusView = edUserName;
                focusView.requestFocus();
            }
        }
    }

    private void logIn(String username, String password) {

        Map<String, String> loginParams = new HashMap<String, String>();

        loginParams.put("userName", username);
        loginParams.put("password", password);

        sweetAlertDialog = new SweetAlertDialog(LoginActivity.this , SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Please wait ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();


        Response.Listener<JSONObject> loginResponseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    loggedIn = response.getBoolean("loggedIn");
                    String serverResp = response.getString("logInResponse");

                    if (loggedIn){

                        String schoolName = response.getString("schoolName");
                        String schoolCode = response.getString("schoolCode");

                        SchoolDetails schoolDetails = new SchoolDetails();
                        schoolDetails.setSchoolName(schoolName);
                        schoolDetails.setSchoolCode(schoolCode);
                        schoolDetails.setLoggedIn(loggedIn);

                        sweetAlertDialog.dismiss();

                        saveSchoolDetails(schoolDetails);

                    }else {
                        Toast.makeText(LoginActivity.this, "Login : " + serverResp, Toast.LENGTH_LONG).show();
                        sweetAlertDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(new JSONObject(loginParams) , loginResponseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }

    private void saveSchoolDetails(SchoolDetails schoolDetails) {

        int loggedIn = 0;

        if (schoolDetails.getLoggedIn()){
            loggedIn = 1;
        }
        databaseHelper.insertSchoolDetails(schoolDetails , loggedIn);
        launchApp();
    }

    public void launchApp(){
        Intent intent = new Intent(LoginActivity.this , HomeActivity.class);
        startActivity(intent);
    }
}

