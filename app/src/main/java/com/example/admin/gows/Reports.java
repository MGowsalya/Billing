package com.example.admin.gows;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Reports extends android.support.v4.app.Fragment {
    Button from, to,ok,download_button;
    LinearLayout list_header;
    public static final int REQUEST_PERMISSIONS = 1;
    String gk,g;
    RecyclerView recycler;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerLayoutManager;
    ArrayList<ListProvider> arrayList = new ArrayList<ListProvider>();
    List<String> s_no = new ArrayList<String>() ;
    List<String> pro_duct = new ArrayList<String>() ;
    List<Float> ra_te = new ArrayList<Float>() ;
    List<String> q_ty= new ArrayList<String>() ;
    List<Float> am_t= new ArrayList<Float>() ;
    SQLiteDatabase db;
    int curs_count;
    String standard_date="";
    File file;
    boolean boolean_permission;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
    String dirpath;
//    PDFView pdfView;
//    Integer pageNumber = 0;
//    String pdfFileName;

    private static final String TAG = com.example.admin.gows.MainActivity.class.getSimpleName();
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};


    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.alert_calendar, container, false);
        db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);

        db.execSQL("create table if not exists Billing(ID bigint(20),Bill_no  bigint(11),bdate date,pcode int," +
                "Product Varchar,Rate float,Tax int,Qty int,Amt float,Total float,Created_date Date,Created_time time,Enable int)");

       // db.execSQL("create table if not exists Billtype (Bill_code integer primary key autoincrement,Product varchar,Rate float," +
         //       "Qty int,Amount float,Created_date date,Created_time time)");
        from = v.findViewById(R.id.from);
        to = v.findViewById(R.id.to);
        ok = v.findViewById(R.id.ok);
        list_header = v.findViewById(R.id.list_header);
       // list_header.setVisibility(View.INVISIBLE);


        //  pdfView= v.findViewById(R.id.pdfView);
        download_button = v.findViewById(R.id.download_button);
        download_button.setVisibility(View.INVISIBLE);
        recycler = v.findViewById(R.id.recycle2);
        recyclerLayoutManager= new LinearLayoutManager(getContext());
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setHasFixedSize(true);
        recycler.setHasFixedSize(true);
     //   Toast.makeText(getContext(),"cursor count:"+curs_count,Toast.LENGTH_SHORT).show();
        from.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showFromDatePicker();
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToDatePicker();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                arrayList.clear();

                g = from.getText().toString();
                gk = to.getText().toString();
                getDet();

               // Toast.makeText(getContext(),"cursor count:"+curs_count, Toast.LENGTH_SHORT).show();

//                CreatePdf();
//                pdfFileName = file.getAbsolutePath();
//
//                pdfView.fromFile(file)
//                        .enableSwipe(true)
//                        .swipeHorizontal(false)
//                        .enableDoubletap(true)
//                        .enableAnnotationRendering(true)
//                        .scrollHandle(new DefaultScrollHandle(getContext()))
//                        .load();

                //  Toast.makeText(getContext(),"count :"+curs_count,Toast.LENGTH_SHORT).show();
                if (curs_count != 0) {
                 //   list_header.setVisibility(View.VISIBLE);
                    download_button.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No Entries Found ", Toast.LENGTH_SHORT).show();
                 //   list_header.setVisibility(View.INVISIBLE);
                    download_button.setVisibility(View.INVISIBLE);
                }

                int count = 0;

                for (String SNO : s_no) {
                    ListProvider listProvider = new ListProvider(SNO, pro_duct.get(count), ra_te.get(count), q_ty.get(count), am_t.get(count));
                    arrayList.add(listProvider);

                    count++;
                }
                recyclerAdapter = new RecyclerAdapter1(arrayList);
                recycler.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }
        });


        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePdf();
                Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_SHORT).show();
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getActivity());

                //Create the intent that’ll fire when the user taps the notification//

                Intent intent = new Intent(Intent.ACTION_VIEW);
                //Uri uri = Uri.fromFile(file);
                Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setContentTitle("Download Completed");
                mBuilder.setContentText("Demo");

                NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(001, mBuilder.build());

//                File outputFile = new File(Environment.getExternalStoragePublicDirectory
//                        (Environment.DIRECTORY_DOWNLOADS), "DemoPDF.pdf");
//                Uri uri1 = Uri.fromFile(outputFile);
//
//                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
//
//                whatsappIntent.setType("text/pdf");
//                whatsappIntent.setPackage("com.whatsapp");
//                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "text value: "+uri1);
//                try {
//                    getActivity().startActivity(whatsappIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    //  ToastHelper.MakeShortText("Whatsapp have not been installed.");
//                    Toast.makeText(getContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        return v;
    }

    void getDet() {
        s_no.clear();
        pro_duct.clear();
        ra_te.clear();
        q_ty.clear();
        am_t.clear();

        String select = "Select ID,Product,Rate,Qty,Amount from Billing where Created_date  BETWEEN '" + g + "' AND '" + gk + "' ORDER BY ID ASC";
    //    String select = "Select ID,Product,Rate,Qty,Amount from Billing where Created_date ='"+g+"'";
        Cursor c = db.rawQuery(select, null);
        curs_count = c.getCount();
        String code, prod,  qty;
        Float ra,amt;
        if (c.moveToFirst()) {
            do {
                code = c.getString(0);
                prod = c.getString(1);
                ra = c.getFloat(2);
                qty = c.getString(3);
                amt = c.getFloat(4);
                // String[] var = prod.split("-");
                // String valu = var[1];
                s_no.add(code);
                pro_duct.add(prod);
                ra_te.add(ra);
                q_ty.add(qty);
                am_t.add(amt);
            } while (c.moveToNext());
            c.close();}}
    private void showFromDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }
    private void showToDatePicker() {
        ToDatePicker date = new ToDatePicker();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.set(todate);
        date.show(getFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth)
        {
            int m = monthOfYear+1;
            if(dayOfMonth<10)
            {
                standard_date= standard_date+(String.valueOf("0"+dayOfMonth) + "-");
            }
            else
            {
                standard_date= standard_date+(String.valueOf(dayOfMonth) + "-");
            }
            if(m<10)
            {
                standard_date= standard_date+(String.valueOf("0"+m) + "-");
            }
            else
            {
                standard_date= standard_date+(String.valueOf(m) + "-");
            }

            standard_date= standard_date+ String.valueOf(year);

            from.setText(standard_date);
            standard_date="";
        }
    };
    DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int m = monthOfYear+1;
            if(dayOfMonth<10)
            {
                standard_date= standard_date+(String.valueOf("0"+dayOfMonth) + "-");
            }
            else
            {
                standard_date= standard_date+(String.valueOf(dayOfMonth) + "-");
            }
            if(m<10)
            {
                standard_date= standard_date+(String.valueOf("0"+m) + "-");
            }
            else
            {
                standard_date= standard_date+(String.valueOf(m) + "-");
            }
            standard_date= standard_date+ String.valueOf(year);
            to.setText(standard_date);
            standard_date="";
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Report");
    }


    void CreatePdf(){
        try {
            dirpath = android.os.Environment.getExternalStorageDirectory().toString();
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            file = new File(dirpath+"/DemoPDF"+randomUUIDString+".pdf");
//           if(file.exists()){
//               increase++;
//               file = new File(dirpath + "/DemoPDF" +increase+".pdf");
//           }
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            createTable(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addContent(Document document) throws DocumentException {

        Chapter catPart = new Chapter(1);
        Paragraph subPara = new Paragraph("Report", subFont);
        Section subCatPart = catPart.addSection(subPara);


        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 2);
        subCatPart.add(paragraph);

        createTable(document);
        document.add(catPart);

    }
    private void createTable(Document document)
            throws DocumentException {
        Paragraph paragraph = new Paragraph("Report");
        paragraph.setAlignment(Element.ALIGN_CENTER);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        document.add(paragraph);
        float[] columnWidths = {0.5f, 2.0f, 0.8f, 0.8f,1.3f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(80f);
        //insert column headings

        insertCell(table, "S.No", Element.ALIGN_LEFT, 1, catFont);
        insertCell(table, "Product", Element.ALIGN_LEFT, 1, catFont);
        insertCell(table, "Rate", Element.ALIGN_CENTER, 1, catFont);
        insertCell(table, "Quantity", Element.ALIGN_CENTER, 1, catFont);
        insertCell(table, "Amount", Element.ALIGN_RIGHT, 1, catFont);
        table.setHeaderRows(1);

//        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        String total = null;
        float to = 0.00f;
        int count = 0;
        for (String SNO : s_no) {
            insertCell(table, ""+SNO, Element.ALIGN_LEFT, 1, normal);
            insertCell(table, ""+ pro_duct.get(count), Element.ALIGN_LEFT, 1, normal);
            insertCell(table, ""+ra_te.get(count), Element.ALIGN_CENTER, 1, normal);
            insertCell(table, ""+q_ty.get(count), Element.ALIGN_CENTER, 1, normal);
            insertCell(table, ""+am_t.get(count), Element.ALIGN_RIGHT, 1, normal);
            to = to + am_t.get(count);
            // Toast.makeText(getActivity(),""+total,Toast.LENGTH_SHORT).show();
            count++;
        }
        total = Float.toString(to);
        insertCell(table, "Total", Element.ALIGN_RIGHT, 4, normal);
        insertCell(table,""+total,Element.ALIGN_RIGHT,1,normal);
        insertCell(table,"",Element.ALIGN_LEFT,1,normal);
        insertCell(table,"",Element.ALIGN_LEFT,1,normal);

        try {
            insertCell(table, "Total", Element.ALIGN_RIGHT, 1, catFont);
            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri uri = Uri.fromFile(new File(dirpath,  "NewPDF.pdf"));
//        intent.setDataAndType(uri, "application/pdf");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(intent);
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }



}






















