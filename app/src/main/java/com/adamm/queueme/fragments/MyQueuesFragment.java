package com.adamm.queueme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.adamm.queueme.pagers.QueuePagerAdapter;
import com.adamm.queueme.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyQueuesFragment extends Fragment {
    private ViewPager2 mViewPager;

    public static MyQueuesFragment newInstance()
    {
        MyQueuesFragment f = new MyQueuesFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_my_queues, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // if(this instanceof )
        // Set up the ViewPager with the sections adapter.
        mViewPager = view.findViewById(R.id.queueContainer);
        mViewPager.setAdapter(new QueuePagerAdapter(getActivity()));
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, mViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(((QueuePagerAdapter)(mViewPager.getAdapter())).mFragmentNames[position]);
                    }
                }
        ).attach();
    }
}
