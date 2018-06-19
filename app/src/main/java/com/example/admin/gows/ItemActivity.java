package com.example.admin.gows;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemActivity extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
   public static TextView total_price,price;
    SQLiteDatabase db;
    public static Float k,k1;
    public static Float tax,total;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_item,container,false);
       // getWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view. findViewById(R.id.container_item);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabs_item);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              //  Toast.makeText(getActivity(), "reselect", Toast.LENGTH_SHORT).show();
                total_price.setText("0.00");
                price.setText("0.00");
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                total_price.setText("0.0");
//                price.setText("0.0");
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                total_price.setText("0.00");
                price.setText("0.00");
            }
        });
        total_price =view.findViewById(R.id.total);
        price = view.findViewById(R.id.price);
     //   price.setText("0.0");
        total_price.setText("0.00");
        price.setText("0.00");
      //  price.setText(String.valueOf(0.0f));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        return view;
    }

//    public void  ttview()
//    {
//       // String modify;
//        total_price.setText("0.0");
//        price.setText(String.valueOf(0.0f));
//        String text = total_price.getText().toString();
//        String text1 = price.getText().toString();
//         k = Float.valueOf(text);
//         k1 =Float.valueOf(text1);
////         modify = total_price.getText().toString();
////         Float mod = Float.valueOf(modify);
////         modify = String.valueOf(Item_add.kk1);
////         total_price.setText(modify);
//      //   total_price.append(String.valueOf(Item_add.kk2));
//        // total_price.setText(R.string.tax_price);
//        if(Item_add.kk4!=null) {
//            String decimal = String.format("%.2f",Item_add.kk4);
//            total_price.setText(decimal);
//            price.setText(String.valueOf(Item_add.tpp2));
//            tax = Item_add.kk4;
//            total = Item_add.tpp2;
//        }
//       else if(Item_add.kk3!=null) {
//            String decimal = String.format("%.2f",Item_add.kk3);
//            total_price.setText(decimal);
//            price.setText(String.valueOf(Item_add.tpp1));
//            tax = Item_add.kk3;
//            total = Item_add.tpp1;
//        }
//      else if(Item_add.kk2!=null){
//            String decimal = String.format("%.2f",Item_add.kk2);
//            total_price.setText(decimal);
//            price.setText(String.valueOf(Item_add.tpp));
//            tax = Item_add.kk2;
//            total = Item_add.tpp;
//        }
//        else {
//            String decimal = String.format("%.2f",Item_add.kk1);
//            total_price.setText(decimal );
//            price.setText(String.valueOf(Item_add.pc));
//            tax = Item_add.kk1;
//            total = Item_add.pc;
//        }
//      //  total_price.setText("0.0");
//    }
//    public void deleteview()
//    {
//       // if(Item_add.pp2==null && Item_add.pp3==null && Item_add.pp4==null)
//     if(Item_add.pp2!=null)  {
//            String del = String.format("%.2f",Item_add.pp2);
//            total_price.setText(del);
//        }
//        else  if(Item_add.pp1!=null)   {
//         String del = String.format("%.2f",Item_add.pp1);
//         total_price.setText(del);
//     }
//     else  if(Item_add.pp3!=null)   {
//         String del = String.format("%.2f",Item_add.pp3);
//         total_price.setText(del);
//     }
//     //else  if(Item_add.pp4!=null)   {
//       else{
//         String del = String.format("%.2f",Item_add.pp4);
//         total_price.setText(del);
//     }
//    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
//                    Item_edit ce = new Item_edit();
//                    return ce;
//                    total_price.setText("0.00");
//                    price.setText("0.0");
                    Item_addition it = new Item_addition();
                    return  it ;


                case 1:
                    Item_editing ce = new Item_editing();
                    return ce;
//                    Item_edit ce = new Item_edit();
//                    return ce;
//                    Item_editing ce = new Item_editing();
//                    return ce;
//                    Item_add it = new Item_add();
//                    return  it ;

            }
            return null;
        }
        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0:
                    return "EDIT";
                case 1:
                    return "ADD";
            }
            return null;
        }
    }
}
