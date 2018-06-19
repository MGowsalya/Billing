package com.example.admin.gows;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
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

public class SecondActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Bill_adding.TextClicked,FacebookResponse, GoogleAuthResponse {
    FragmentTransaction ft;
    Fragment fragment = null;
    FragmentManager fm;
    Bill frag;
    Button profile;
  public static DrawerLayout drawer;
    SQLiteDatabase db;

    NavigationView navigationView;

    public static BluetoothChatService mChatService = null;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Handler customHandler = new Handler();
    private static final boolean D = true;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final String TAG = "BluetoothChat";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    private FacebookHelper mFbHelper;
    private GoogleSignInHelper mGAuthHelper;

    private String mConnectedDeviceName = "";
    BluetoothDevice device;
    int logintype;
    public static final String DEVICE_NAME = "device_name";
    private Handler mHandlern = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    break;
                case MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;
                case MESSAGE_TOAST:
                    break;
                default:
                    break;
            }
        }
    };
    int status=0;
  //  BillList billlist = new BillList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        db = getApplicationContext().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists Billing(ID integer primary key autoincrement,Bill_no  bigint(11),bdate date,pcode int," +
                "Product Varchar,Rate float,Tax int,Qty int,Amount float,Total float,Created_date Date,Created_time time,Enable int)");

        db.execSQL("create table if not exists LoginDetails(Username varchar,Password varchar,Login_date date,Login_time time,Status int,Logintype int);");
        // By Default app always begins with dashboard page.
//        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//        tx.replace(R.id.content_frame, new Dashboard());
//        tx.commit();

        //  mChatService = new BluetoothChatService(getApplicationContext(), mHandlern);

         String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
         String time =  mdformat.format(calendar.getTime());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


      //  ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle()
         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        showHome();
        View headerView = navigationView . getHeaderView(0);
        TextView company_name = headerView.findViewById(R.id.company_name_header);

        String selection = "select Company_name from Company";
        Cursor curs = db.rawQuery(selection,null);
        if(curs.moveToFirst())
        {
            do{
                String name = curs.getString(0);
                company_name.setText(name);
            }while (curs.moveToNext());
            curs.close();
        }
    profile = headerView.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new Company_edit();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                drawer.closeDrawers();
           }
        });
//        findViewById(R.id.printdemo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                StringBuilder Printxt= new StringBuilder();
//                Printxt.append("--------------------------------------" + "\n");
//                    Printxt.append("         Thank you ! Visit Again    " + "\n");
//                    Printxt.append("**************************************" + "\n" + "\n" + "\n" + "\n");
////                    setupChat();
//                    sendMessage(Printxt.toString());
//            }
//        });
        String uery = "SELECT Logintype FROM LoginDetails ";
        Cursor cu = db.rawQuery(uery, null);

        if (cu.moveToFirst()) {
            do {

                logintype = cu.getInt(0);
            } while (cu.moveToNext());
            cu.close();
        }
        mFbHelper = new FacebookHelper(this,
                "id,name,email,gender,birthday,picture,cover",
                this);
        mGAuthHelper = new GoogleSignInHelper(this, null, this);

        }
      /* public void printed(SQLiteDatabase db1,int check) {
         //  BillList billlist = new BillList();
           StringBuilder Printxt= new StringBuilder();
           String prnName ;
           String prnNos;String prnAmt ;
           String prnQty ;
           String prn ;
                           float tot2=0.00f ;
                String select1 = "SELECT * FROM  Billtype";
                Cursor c = db1.rawQuery(select1, null);
                Printxt.append("--------------------------------------" + "\n");
                Printxt.append("SNo  Product       Rate    Qty   Amount" + "\n");
                Printxt.append("--------------------------------------" + "\n");
                if (c.moveToNext()) {
                    do {
                        prnName = String.format("%-4s", c.getString(0));
                        //+"-"+c.getString(10).toString());
                        String va = c.getString(1);
                       // String[] var1 = va.split("-");
                        prn = String.format("%-14s", va);
                        prnNos = String.format("%5s", c.getString(2));
                       prnQty = String.format("%5s", c.getString(3));
                       prnAmt = String.format("%11s", c.getString(4));
                        float ta = c.getFloat(4);
                        tot2 = tot2 + ta;
                        Printxt.append(prnName).append(prn).append(prnNos).append(prnQty).append(prnAmt).append("\n");
                    } while (c.moveToNext());

                    String total = String.format("%13s", Float.toString(tot2)); //= Float.toString(tot2);
                    Printxt.append("--------------------------------------" + "\n");
                    Printxt.append("                    Total " + total + "\n");
//                    Printxt.append("--------------------------------------" + "\n");
//                    Printxt.append("         Thank you ! Visit Again    " + "\n");
//                    Printxt.append("**************************************" + "\n" + "\n" + "\n" + "\n");

                }

          c.close();

//          Printxt.append("--------------------------------------" + "\n");
//           Printxt = Printxt + "Total            " + String.format("%12s", ) + "\n";
           if ( check!= BluetoothChatService.STATE_CONNECTED) {
               Toast.makeText(getApplicationContext(),"Not Connected",Toast.LENGTH_SHORT).show();
             // billlist.toast();
            return;
        }else {
               String message = Printxt.toString();
               if (message.length() > 0) {
                   // Get the message bytes and tell the BluetoothChatService to write
                   byte[] send = message.getBytes();
                   mChatService.write(send);
               }
          }

       }
//        public void sendMessage(String message) {
//        // Check that we're actually connected before trying anything
//        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
//            Toast.makeText(getApplicationContext(), R.string.not_connected, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        else{
//           // Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
//        }
//        if (message.length() > 0) {
//            // Get the message bytes and tell the BluetoothChatService to write
//            byte[] send = message.getBytes();
//            mChatService.write(send);
//        }
//    }
*/
    @Override
    public void onBackPressed() {
       //  drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if(fragment instanceof Dashboard)
            {
                new AlertDialog.Builder(this)
                        .setMessage("Do You Want to Exist")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(),"app closed",Toast.LENGTH_SHORT).show();
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
               // super.onBackPressed();
            }
            else {
                showHome();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_connect:
                //mBtp.showDeviceList(this);
          // //      important 281 to 295
//                if (mBluetoothAdapter.isEnabled()) {
//                    if (mChatService.getState() == BluetoothChatService.STATE_LISTEN) {
//                        Intent serverIntent = new Intent(SecondActivity.this, DeviceListActivity.class);
//                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
//                        return true;
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Already Connected", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else{
//                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//                    if (mChatService == null)
//                        setupChat();
//                }

          //  case R.id.action_dunpair:
//                try {
//                    Method m = device.getClass()
//                            .getMethod("removeBond", (Class[]) null);
//                    m.invoke(device, (Object[]) null);
//                    Toast.makeText(getApplicationContext(),"Disconnected",Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage());
//                }
            //    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showHome()
    { fragment = new Dashboard();
        if (fragment != null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.commit();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
//
//                return true;
//            }
//        });

//        navigationView.setItemBackgroundResource(R.drawable.testing);
//        Toast.makeText(getApplicationContext(), "clicked"+item, Toast.LENGTH_SHORT).show();
      //  item.setIcon(R.drawable.navigation_style);

        if(id == R.id.nav_dashboard)
        {
//            Intent intent = new Intent(SecondActivity.this,SecondActivity.class);
//            startActivity(intent);

            //findViewById(R.id.company_name_header).setBackgroundResource(R.color.menu_color);
            fragment = new Charts();
          //  item.setIcon(R.drawable.navigation_style);
         }
        else if(id == R.id.nav_category){
            fragment = new CategoryActivity();
        }
        else if(id == R.id.nav_tax){
              fragment = new TaxesActivity();
        }
        else if(id == R.id.nav_item){
             fragment = new ItemActivity();
        }
        else if(id == R.id.nav_bill){
            fragment = new Bill();
        }

        else if(id == R.id.nav_report){
                fragment = new Reports();
        }
        else if(id==R.id.nav_Devices)
        {
            fragment = new Bluetooth();
        }
        else if(id == R.id.nav_Logout){
            new AlertDialog.Builder(this)
                    .setMessage("Do You Want to Exist")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

//                            switch (logintype){
//                                case 1:
//
//                                    ContentValues cv = new ContentValues();
//                                    cv.put("Status",status);
//                                    db.update("LoginDetails",cv,null,null);
//                                    finish();
//                                case 2:
//                                    mGAuthHelper.performSignOut();
//
//                                case 3:
//                                    mFbHelper.performSignOut();
//
//
//                            }
                          if(logintype == 1){
                              ContentValues cv = new ContentValues();
                                    cv.put("Status",status);
                                    db.update("LoginDetails",cv,null,null);
                                    finish();
                          }
                            else if (logintype == 2){
                              mGAuthHelper.performSignOut();
                          }
                          else if (logintype == 3){
                              mFbHelper.performSignOut();
                          }
                          Intent intent = new Intent(SecondActivity.this,SiginActivity.class);
                          startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }
        if (fragment != null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void inserttextview() {
        fm = getSupportFragmentManager();
        frag = (Bill) fm.findFragmentById(R.id.content_frame);
        frag.getcode();
    }

    @Override
    public void updatetextview(String bill) {
        fm = getSupportFragmentManager();
        frag = (Bill) fm.findFragmentById(R.id.content_frame);
        frag.getUpdatecode(bill);
    }
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(D) Log.d(TAG, "onActivityResult " + resultCode);
//        switch (requestCode) {
//            case REQUEST_CONNECT_DEVICE:
//                // When DeviceListActivity returns with a device to connect
//                if (resultCode == Activity.RESULT_OK) {
//                    // Get the device MAC address
//                    String address = data.getExtras()
//                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                    // Get the BLuetoothDevice object
//                    device = mBluetoothAdapter.getRemoteDevice(address);
//                    //  mTitle.setText(address.toString());
//                    // Attempt to connect to the device
//                    mChatService.connect(device);
//
//
//                }
//                break;
//            case REQUEST_ENABLE_BT:
//                // When the request to enable Bluetooth returns
//                if (resultCode == Activity.RESULT_OK) {
//                    // Bluetooth is now enabled, so set up a chat session
//                    setupChat();
//
//                } else {
//                    // User did not enable Bluetooth or an error occured
//                    Log.d(TAG, "BT not enabled");
//                    Toast.makeText(getApplicationContext(), "Turn on Your Bluetooth to Connect With Printer", Toast.LENGTH_SHORT).show();
//
//                }
//        }
//    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        mChatService = new BluetoothChatService(getApplicationContext(), mHandlern);
        if(D) Log.e(TAG, "- bluetoooooth -");
    }

    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mChatService == null)
            setupChat();
        }

        //  TextView txtbt = (TextView) findViewById(R.id.TXTBTSTATUS);
        if(mBluetoothAdapter.isEnabled()) {
            //text.setText("Status: Enabled");
            //   txtbt.setText("BT:Enabled");
        }
    }
    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }
//    @Override
//    public synchronized void onPause() {
//        super.onPause();
//        if(D) Log.e(TAG, "- ON PAUSE -");
//    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");

    }

    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user) {

    }

    @Override
    public void onGoogleAuthSignInFailed() {

    }

    @Override
    public void onGoogleAuthSignOut(boolean isSuccess) {
        Toast.makeText(this, isSuccess ? "Sign out success" : "Sign out failed", Toast.LENGTH_SHORT).show();
        ContentValues cv1 = new ContentValues();
        cv1.put("Status",status);
        db.update("LoginDetails",cv1,null,null);
        finish();
    }

    @Override
    public void onFbSignInFail() {
    }

    @Override
    public void onFbSignInSuccess() {

    }

    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {

    }

    @Override
    public void onFBSignOut() {
        Toast.makeText(this, "Facebook sign out success", Toast.LENGTH_SHORT).show();
        ContentValues cv2 = new ContentValues();
        cv2.put("Status",status);
        db.update("LoginDetails",cv2,null,null);
        finish();
    }
}
