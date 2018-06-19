package com.example.admin.gows;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.gows.facebookSignIn.FacebookHelper;
import com.example.admin.gows.facebookSignIn.FacebookResponse;
import com.example.admin.gows.facebookSignIn.FacebookUser;
import com.example.admin.gows.googleAuthSignin.GoogleAuthResponse;
import com.example.admin.gows.googleAuthSignin.GoogleAuthUser;
import com.example.admin.gows.googleAuthSignin.GoogleSignInHelper;

import java.util.ArrayList;
import java.util.List;

public class SiginActivity extends AppCompatActivity implements FacebookResponse, GoogleAuthResponse {
    private SQLiteDatabase db;
    int st, status = 1;
    String date, time, user, pass,companyCheck;
    private EditText sign_username, sign_password;
    private FacebookHelper mFbHelper;
    private GoogleSignInHelper mGAuthHelper;
    public static final int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // fn_permission();
        checkPermissions();
        db = getApplicationContext().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists Company (Code Integer primary key autoincrement,Company_name varchar,Address text,State varchar," +
                "Postal_code varchar,GST_no varchar,Phone bigint,Mobile_no bigint,Created_date date,Created_time time," +
                "Bill_prefix varchar);");

        db.execSQL("create table if not exists LoginDetails(Username varchar,Password varchar,Login_date date,Login_time time,Status int,Logintype int);");
        String uery = "SELECT Username,Password,Status FROM LoginDetails ";
        Cursor cu = db.rawQuery(uery, null);

        if (cu.moveToFirst()) {
            do {
                user = cu.getString(0);
                pass = cu.getString(1);
                st = cu.getInt(2);
            } while (cu.moveToNext());
            cu.close();
        }
        String Query = "SELECT Company_name FROM Company ";
        Cursor c = db.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                companyCheck = c.getString(0);
            } while (c.moveToNext());
            c.close();
        }

        if (user != null && pass != null) {
            if (st == status) {
                if(companyCheck == null) {
                    Intent in = new Intent(SiginActivity.this, CompanyActivity.class);
                    startActivity(in);
                    finish();
                }
                else{
                    Intent in = new Intent(SiginActivity.this, SecondActivity.class);
                    startActivity(in);
                    finish();
                }

            } else {
                Intent in = new Intent(SiginActivity.this, com.example.admin.gows.MainActivity.class);
                startActivity(in);
                finish();
            }

        } else {
            setContentView(R.layout.activity_sigin);
           // fbConnectHelper = new FbConnectHelper(this, this);
            mFbHelper = new FacebookHelper(this,
                    "id,name,email,gender,birthday,picture,cover",
                    this);

            mGAuthHelper = new GoogleSignInHelper(this, null, this);
            sign_username = findViewById(R.id.sign_username);
            sign_password = findViewById(R.id.sign_password);
            sign_username.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Validation.isEmailAddress(sign_username, true);
                }
            });
            findViewById(R.id.sign_log_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String userna =  sign_username.getText().toString();
                    String passwr= sign_password.getText().toString();
                    if (!userna.isEmpty() && !passwr.isEmpty()) {
                    if (checkValidation()) {
                        int logintype = 1;
                          details(userna,passwr,logintype);
                        Intent in = new Intent(SiginActivity.this, CompanyActivity.class);
                        startActivity(in);
                        finish();
                        }
                    else {
                        Toast.makeText(getApplicationContext(), "Email id is invalid", Toast.LENGTH_SHORT).show();

                    }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"UserName or Password is Empty", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            findViewById(R.id.sign_facebook).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  fbConnectHelper.connect();
                    mFbHelper.performSignIn(SiginActivity.this);
                }
            });
            findViewById(R.id.sign_google).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGAuthHelper.performSignIn(SiginActivity.this);
                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        fbConnectHelper.onActivityResult(requestCode, resultCode, data);
        mFbHelper.onActivityResult(requestCode, resultCode, data);
        mGAuthHelper.onActivityResult(requestCode, resultCode, data);
    }
    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.isEmailAddress(sign_username, true)) ret = false;
        return ret;
    }


    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user) {
        Toast.makeText(this, "Google user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
        String nameg =  user.name;
        String emailg = user.email;
        int logintype = 2;
        details(emailg,nameg,logintype);
        Intent in = new Intent(SiginActivity.this, CompanyActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onGoogleAuthSignInFailed() {
        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleAuthSignOut(boolean isSuccess) {
        Toast.makeText(this, isSuccess ? "Sign out success" : "Sign out failed", Toast.LENGTH_SHORT).show();

    }

//    @Override
//    public void OnFbSuccess(GraphResponse graphResponse) {
//        try {
//            JSONObject jsonObject = graphResponse.getJSONObject();
//            String namef= jsonObject.getString("name");
//            String emailf = jsonObject.getString("email");
//            int logintype = 3;
//            details(emailf,namef,logintype);
//            Intent in = new Intent(SiginActivity.this, CompanyActivity.class);
//            startActivity(in);
//            finish();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void OnFbError(String errorMessage) {
//        Toast.makeText(this,errorMessage.toString(), Toast.LENGTH_SHORT).show();
//
//    }
    private void details(String username, String password, int type ){
        ContentValues values = new ContentValues();
        values.put("Username", username);
        values.put("Password", password);
        values.put("Login_date", date);
        values.put("Login_time", time);
        values.put("Status", status);
        values.put("Logintype",type);
        db.insert("LoginDetails", null, values);
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSIONS) {
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                boolean_permission = true;
//               // mGHelper.onPermissionResult(requestCode, grantResults);
//
//            } else {
//                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();
//
//            }
//        }
//    }

//    private void fn_permission() {
//        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
//                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
//
//            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQUEST_PERMISSIONS);
//
//            }
//
//            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        REQUEST_PERMISSIONS);
//
//            }
//        } else {
//            boolean_permission = true;
//
//
//        }
//    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.

                } else {
                    String permiss = "";
                    for (String per : permissions) {
                        permiss += "\n" + per;
                        Toast.makeText(getApplicationContext(),"Please allow Permissions To Access Download", Toast.LENGTH_SHORT).show();
                    }
                    // permissions list of don't granted permission
                }
                return;
            }
        }
    }

    @Override
    public void onFbSignInFail() {
           }

    @Override
    public void onFbSignInSuccess() {
        Toast.makeText(this, "Facebook sign in success", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {
        Toast.makeText(this, "Facebook user data: name= " + facebookUser.name + " email= " + facebookUser.email, Toast.LENGTH_SHORT).show();
        Log.d("Person name: ", facebookUser.name + "");
        Log.d("Person gender: ", facebookUser.gender + "");
        Log.d("Person email: ", facebookUser.email + "");
        Log.d("Person image: ", facebookUser.facebookID + "");
        int logintype = 3;
        String namef= ""+facebookUser.name;
            String emailf = ""+facebookUser.email;
           details(emailf,namef,logintype);
            Intent in = new Intent(SiginActivity.this, CompanyActivity.class);
            startActivity(in);
            finish();

    }

    @Override
    public void onFBSignOut() {
        Toast.makeText(this, "Facebook sign out success", Toast.LENGTH_SHORT).show();
    }
}


