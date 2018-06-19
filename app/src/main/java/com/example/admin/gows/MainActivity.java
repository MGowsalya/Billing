package com.example.admin.gows;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements FacebookResponse, GoogleAuthResponse {
    private SQLiteDatabase db;
    String companyCheck;
   // private static final String GOOGLE_CLIENT_ID = "944623175858-2fg6pa97a6np2qrt1tr9t2m0kbljfmv7.apps.googleusercontent.com";
   // private FbConnectHelper fbConnectHelper;
   private FacebookHelper mFbHelper;
    private GoogleSignInHelper mGAuthHelper;
    String TAG = "Check";
    private EditText e_username,e_password;
    int st, status = 1;
    String user,pass;
    String date;
  //  String date,time,user,pass;
    public static final int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = getApplicationContext().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE ,null);
        db.execSQL("create table if not exists Company (Code Integer primary key autoincrement,Company_name varchar,Address text,State varchar," +
                "Postal_code varchar,GST_no varchar,Phone bigint,Mobile_no bigint,Created_date date,Created_time time," +
                "Bill_prefix varchar);");

        db.execSQL("create table if not exists LoginDetails(Username varchar,Password varchar,Login_date date,Login_time time,Status int);");
        //fn_permission();
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

         if(companyCheck==null  ){
             Intent in = new Intent(MainActivity.this, CompanyActivity.class);
             startActivity(in);
             finish();
         }
         else {
             if (st == status) {
                 Intent in = new Intent(MainActivity.this, SecondActivity.class);
                 startActivity(in);
                 finish();
             } else {
                 setContentView(R.layout.activity_main);
                 e_username = findViewById(R.id.username);
                 e_password = findViewById(R.id.password);
                 e_username.addTextChangedListener(new TextWatcher() {
                     @Override
                     public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                     }

                     @Override
                     public void onTextChanged(CharSequence s, int start, int before, int count) {
                     }

                     @Override
                     public void afterTextChanged(Editable s) {
                         Validation.isEmailAddress(e_username, true);
                     }
                 });


                // fbConnectHelper = new FbConnectHelper(this, this);
                 mFbHelper = new FacebookHelper(this,
                         "id,name,email,gender,birthday,picture,cover",
                         this);

                 mGAuthHelper = new GoogleSignInHelper(this, null, this);
                 findViewById(R.id.log_button).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         if (checkValidation()) {
                                 if (user.equalsIgnoreCase(e_username.getText().toString()) && pass.equalsIgnoreCase(e_password.getText().toString())) {
                                     details();
                                     Intent in = new Intent(MainActivity.this, SecondActivity.class);
                                     startActivity(in);
                                     finish();
                                 } else
                                     {
                                     Toast.makeText(getApplicationContext(), "invalid username or Password", Toast.LENGTH_SHORT).show();
                                     }
                             }
                     }
                 });

                 findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         mFbHelper.performSignIn(MainActivity.this);
                     }
                 });
                 findViewById(R.id.google).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         mGAuthHelper.performSignIn(MainActivity.this);
                     }
                 });
             }
         }
          }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // fbConnectHelper.onActivityResult(requestCode, resultCode, data);
        mFbHelper.onActivityResult(requestCode, resultCode, data);
        mGAuthHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void details(){
         date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String time =  mdformat.format(calendar.getTime());
            ContentValues values = new ContentValues();
            values.put("Login_date", date);
            values.put("Login_time", time);
            values.put("Status", status);
            db.update("LoginDetails",values,"Username = '"+user+"'",null);
            Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.isEmailAddress(e_username, true))
        {
            ret = false;
        }
        return ret;
    }

    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user1) {
    //    Toast.makeText(this, "Google user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
        String nameg =  user1.name;
        String emailg = user1.email;
        if (user.equalsIgnoreCase(emailg) && pass.equalsIgnoreCase(nameg)) {
            details();
            Intent in = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(in);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "invalid Entry", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGoogleAuthSignInFailed() {
        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleAuthSignOut(boolean isSuccess) {
        Toast.makeText(this, isSuccess ? "Sign out success" : "Sign out failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFbSignInFail() {
        Toast.makeText(this, "Facebook sign in failed.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFbSignInSuccess() {
     //   Toast.makeText(this, "Facebook sign in success", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {
        Toast.makeText(this, "Facebook user data: name= " + facebookUser.name + " email= " + facebookUser.email, Toast.LENGTH_SHORT).show();
        Log.d("Person name: ", facebookUser.name + "");
        Log.d("Person gender: ", facebookUser.gender + "");
        Log.d("Person email: ", facebookUser.email + "");
        Log.d("Person image: ", facebookUser.facebookID + "");
        int logintype = 3;

        String name= ""+facebookUser.name;
        String email = ""+facebookUser.email;

            if (user.equalsIgnoreCase(email) && pass.equalsIgnoreCase(name)) {
                details();
                Intent in = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(in);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "invalid Entry ", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onFBSignOut() {
        Toast.makeText(this, "Facebook sign out success", Toast.LENGTH_SHORT).show();
    }
}
//    @Override
//    public void OnFbSuccess(GraphResponse graphResponse) {
//        try {
//        JSONObject jsonObject = graphResponse.getJSONObject();
//        String name= jsonObject.getString("name");
//        String email = jsonObject.getString("email");
//        String id = jsonObject.getString("id");
//        String profileImg = "http://graph.facebook.com/"+ id+ "/picture?type=large";
//
//            if (user.equalsIgnoreCase(email) && pass.equalsIgnoreCase(name)) {
//                details();
//                Intent in = new Intent(MainActivity.this, SecondActivity.class);
//                startActivity(in);
//                finish();
////                                     //details();
////                                     if (companyCheck == null) {
////                                         Intent in = new Intent(MainActivity.this, CompanyActivity.class);
////                                         startActivity(in);
////                                         finish();
////                                     } else {
////                                         Intent in = new Intent(MainActivity.this, SecondActivity.class);
////                                         startActivity(in);
////                                         finish();
////                                     }
//            } else {
//                Toast.makeText(getApplicationContext(), "invalid ", Toast.LENGTH_SHORT).show();
//            }
//
//            // openHomeActivity(name,email,id,profileImg);
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
//    }
//
//    @Override
//    public void OnFbError(String errorMessage) {
//        Toast.makeText(this,errorMessage.toString(), Toast.LENGTH_SHORT).show();
//
//    }