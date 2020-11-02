package com.tgapp.mytranscribeglass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class SwipeTabsActivity extends AppCompatActivity {

    TabLayout myTab;
    ViewPager myPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_tabs);
        myTab = (TabLayout) findViewById(R.id.myTab);
        myPage = (ViewPager) findViewById(R.id.myViewPager);

        /* Set page adapter for ViewPager */
        myPage.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        myTab.setupWithViewPager(myPage);

//        myTab.addTab(myTab.newTab().setText("Cuckoo"));
//        myTab.addTab(myTab.newTab().setText("Pumpkin"));

        /* Listener for TabLayout */
        myTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myPage.setCurrentItem(tab.getPosition());
                Toast.makeText(getApplicationContext(), "position clicked is " + tab.getPosition(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    class MyPagerAdapter extends FragmentPagerAdapter {

        //you can set the tab's title this way or use addTab()
        String data[] = {"Cupcakes", "Pumpkin Spice"};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            if(position == 0) { //position relative to TabLayout
                return new CupcakeFragment();
            }
            if(position == 1) {
                return new PumpkinSpiceFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return data[position];
        }
    }
}