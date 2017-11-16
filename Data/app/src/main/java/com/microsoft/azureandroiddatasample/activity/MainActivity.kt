package com.microsoft.azureandroiddatasample.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.MenuItem
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.TabFragmentPagerAdapter
import com.microsoft.azureandroiddatasample.fragment.DatabasesFragment
import com.microsoft.azureandroiddatasample.fragment.OffersFragment

import kotlinx.android.synthetic.main.tab_layout.*

/**
* Created by Nate Rickard on 11/14/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: TabFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.tab_layout)

        //Toolbar will now take on default Action Bar characteristics
        setSupportActionBar (toolbar)
        supportActionBar?.title = title

        //set up tabs + view pager
        setupViewPager ()
    }

    private fun setupViewPager() {

        // create & config our adapter
        pagerAdapter = TabFragmentPagerAdapter (this, supportFragmentManager)

        with(pagerAdapter) {
            addFragment (DatabasesFragment(), getString(R.string.databases).toUpperCase())
            addFragment (OffersFragment (), getString(R.string.offers).toUpperCase())
        }

        viewPager.adapter = pagerAdapter

        // configure tabLayout & viewPager
        with(tabLayout) {
            tabMode =  TabLayout.MODE_FIXED
            tabGravity = TabLayout.GRAVITY_FILL
            setupWithViewPager (viewPager)
        }

        // finally, glue it all together
        pagerAdapter.fillTabLayout (tabLayout)


//        toolbarTitle.Text = PagerAdapter.GetTabFragment (0).Title;

//        viewPager.PageSelected += (sender, e) =>
//        {
//            ////update the query listener
//            //var fragment = PagerAdapter.GetFragmentAtPosition (e.Position);
//            //queryListener = (SearchView.IOnQueryTextListener)fragment;
//
//            //searchView?.SetOnQueryTextListener (queryListener);
//
//            //swap the title into the app bar title rather than including it in the tab
//            var tabFragment = PagerAdapter.GetTabFragment (e.Position);
//            //SupportActionBar.Title = tabFragment.Title;
//            toolbarTitle.Text = tabFragment.Title;
//        };
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false
}