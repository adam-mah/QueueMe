package com.adamm.queueme.pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.adamm.queueme.MainActivity;
import com.adamm.queueme.R;
import com.adamm.queueme.pagers.ActiveQueuePager;
import com.adamm.queueme.pagers.MyQueuePager;

public class QueuePagerAdapter extends FragmentStateAdapter {

    private final Fragment[] mFragments = new Fragment[] {//Initialize fragments views
            new ActiveQueuePager(),
            new MyQueuePager(),
    };
    public final String[] mFragmentNames = new String[] {//Tabs names array
            MainActivity.appContext.getString(R.string.active_queues),
            MainActivity.appContext.getString(R.string.all_queues)
    };

    public QueuePagerAdapter(FragmentActivity fa){
        super(fa);
    }

    @Override
    public int getItemCount() {
        return mFragments.length;
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
}
