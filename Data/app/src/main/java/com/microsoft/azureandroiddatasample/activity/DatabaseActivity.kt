package com.microsoft.azureandroiddatasample.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.TabFragmentPagerAdapter
import com.microsoft.azureandroiddatasample.fragment.CollectionsFragment
import com.microsoft.azureandroiddatasample.fragment.UsersFragment

import kotlinx.android.synthetic.main.tab_layout.*

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class DatabaseActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: TabFragmentPagerAdapter
    private lateinit var databaseId: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.tab_layout)

        //Toolbar will now take on default Action Bar characteristics
        setSupportActionBar(toolbar)

        databaseId = intent.extras.getString("db_id")

        supportActionBar?.title = "Database: $databaseId"

        //set up tabs + view pager
        setupViewPager()
    }

    private fun setupViewPager() {

        // create & config our adapter
        pagerAdapter = TabFragmentPagerAdapter (this, supportFragmentManager)

        with(pagerAdapter) {
            addFragment (CollectionsFragment (), getString(R.string.collections).toUpperCase())
            addFragment (UsersFragment (), getString(R.string.users).toUpperCase())
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
    }
}