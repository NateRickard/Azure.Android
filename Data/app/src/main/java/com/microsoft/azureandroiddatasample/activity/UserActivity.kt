package com.microsoft.azureandroiddatasample.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.TabFragmentPagerAdapter
import com.microsoft.azureandroiddatasample.fragment.PermissionsFragment
import kotlinx.android.synthetic.main.tab_layout.*

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class UserActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: TabFragmentPagerAdapter
    private lateinit var databaseId: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.tab_layout)

        //Toolbar will now take on default Action Bar characteristics
        setSupportActionBar(toolbar)

        //set up tabs + view pager
        setupViewPager ()

        databaseId = intent.extras.getString("db_id")
        userId = intent.extras.getString("user_id")

        supportActionBar?.title = "User: $userId"
    }

    private fun setupViewPager() {

        // create & config our adapter
        pagerAdapter = TabFragmentPagerAdapter (this, supportFragmentManager)

        with(pagerAdapter) {
            addFragment (PermissionsFragment (), getString(R.string.permissions).toUpperCase())
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