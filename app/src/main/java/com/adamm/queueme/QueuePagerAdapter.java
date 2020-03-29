package com.adamm.queueme;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.adamm.queueme.fragments.ActiveQueuesFragment;
import com.adamm.queueme.fragments.MyQueuesFragment;

public class QueuePagerAdapter extends FragmentStateAdapter {

    private final Fragment[] mFragments = new Fragment[] {//Initialize fragments views
            new ActiveQueuesFragment(),
            new MyQueuesFragment(),
    };
    private final String[] mFragmentNames = new String[] {//Tabs names array
            "Test",
            "test2",
    };

    public QueuePagerAdapter(FragmentActivity fa){
        super(fa);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments[position];
    }


    /*@Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }
    @Override
    public int getCount() {
        return mFragments.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentNames[position];
    }*/
}
