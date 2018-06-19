package com.example.admin.gows;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by admin on 04-12-2017.
 */

public class Cat_add extends Fragment {
    EditText category_name;
    Button category_save;
    String enable = "1";
    SQLiteDatabase db;

//    Spinner category_spinner;
//    private String[] listOfObjects;
//    private TypedArray images;
//    private ImageView itemImage;
//    int image_position,SELECT_IMAGE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_add, container, false);
        db = getActivity().openOrCreateDatabase("Master.db",android.content.Context.MODE_PRIVATE ,null);
        db.execSQL("create table if not exists Category (Category_Code Integer primary key autoincrement ,Name Varchar," +
                "Created_date Date,Created_time Time,Enable int)");
        final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String time =  mdformat.format(calendar.getTime());
        category_name =view.findViewById(R.id.cat_name);
        category_save = view.findViewById(R.id.cat_save);


//        itemImage = view.findViewById(R.id.images);
//
//        listOfObjects = getResources().getStringArray(R.array.object_array);
//
//        images = getResources().obtainTypedArray(R.array.object_image);
//        final Spinner spinner = view.findViewById(R.id.category_spinner);
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, listOfObjects);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(spinnerAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String image_spinner = spinner.getSelectedItem().toString();
//                String g = "Gallery";
//                String gs = "Google Search";
//                if(image_spinner.equals(g)){
//                    Intent intent1 = new Intent();
//                    intent1.setType("image/*");
//                    intent1.setAction(Intent.ACTION_GET_CONTENT);//
//                    startActivityForResult(Intent.createChooser(intent1, "Select Picture"),SELECT_IMAGE);
//                }
//                else if(image_spinner.equals(gs))
//                {
//                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                    String term = category_name.getText().toString();
//                    intent.putExtra(SearchManager.QUERY, term);
//                    startActivity(intent);
//
//                    Intent int1 = new Intent();
//                    int1.setType("image/*");
//                    int1.setAction(int1.ACTION_GET_CONTENT);//
//                    startActivityForResult(int1.createChooser(intent, "Select Picture"),SELECT_IMAGE);
//                    startActivity(int1);
//                }
//                itemImage.setImageResource(images.getResourceId(spinner.getSelectedItemPosition(), -1));
//                // image_position = images.getResourceId(spinner.getSelectedItemPosition(),-1);
//               }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//

        category_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
             //   Toast.makeText(getApplicationContext(),"keyboard: "+imm,Toast.LENGTH_SHORT).show();

//                Opening gallery directly from app
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);//
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
//
//                //   Google searching
//                try {
//                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                    String term = category_name.getText().toString();
//                    intent.putExtra(SearchManager.QUERY, term);
//                  //  intent.setAction(Intent.ACTION_GET_CONTENT);//
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
//                    startActivity(intent);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }


                String na = category_name.getText().toString().trim();
                if( ! na.isEmpty() ) {
                    ContentValues values = new ContentValues();
                    values.put("Name", na);
                    values.put("Created_date", date);
                    values.put("Created_time", time);
                    values.put("Enable", enable);

//                    String imag = itemImage.getResources().toString();
//                    byte[] bytes = imag.getBytes();
//                    values.put("Images",bytes);
//                    values.put("Images",itemImage.getResources().getInteger(image_position));
                    if(rowIdExists( category_name.getText().toString().trim())){
//                        if(!category_name.getText().toString().contains(" "))
//                        {
                        Pattern ps = Pattern.compile("^[a-zA-Z]+$");
                        Matcher ms = ps.matcher(category_name.getText().toString());
                        boolean bs = ms.matches();
                        if (bs == false) {
                            Toast.makeText(getActivity(),"Alphabetical letters are only accepted",Toast.LENGTH_SHORT).show();
                        }  else {
                       //     Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                            db.insert("Category", null, values);
                            final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setMessage("Category" + " " + category_name.getText() + " has been added");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Toast.makeText(getActivity(),"ok",Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            category_name.getText().clear();
                        }
                        //}
//                    else {
//                            Toast.makeText(getContext(),"No Space in between name",Toast.LENGTH_SHORT).show();
//                        }
                    }
                    else {
                        Toast.makeText(getContext(),"Category Name Already Used",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), " Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SELECT_IMAGE)
//        {
//            if (resultCode == Activity.RESULT_OK)
//            {
//                if (data != null)
//                {
//                    try
//                    {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
//                        itemImage.setImageBitmap(bitmap);
//                    } catch (IOException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED)
//            {
//                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    public boolean rowIdExists(String name) {
        String select = "select Name from Category ";
        Cursor cursor = db.rawQuery(select, null);
        List<String> labels = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
                // Toast.makeText(getContext(),""+labels.get(1),Toast.LENGTH_SHORT).show();

            } while (cursor.moveToNext());
        }
      /*  cursor.close();
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).equalsIgnoreCase(category_name.getText().toString())) {
                return false;
            }
            else{
                return true;
            }
        }
        return true;*/
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(name)) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }


}
