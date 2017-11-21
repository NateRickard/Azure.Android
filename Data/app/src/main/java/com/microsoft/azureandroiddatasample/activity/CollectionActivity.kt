package com.microsoft.azureandroiddatasample.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.TabFragmentPagerAdapter
import com.microsoft.azureandroiddatasample.fragment.DocumentsFragment
import kotlinx.android.synthetic.main.tab_layout.*

/**
 * Created by Nate Rickard on 11/21/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class CollectionActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: TabFragmentPagerAdapter
    private lateinit var databaseId: String
    private lateinit var collId: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.tab_layout)

        //Toolbar will now take on default Action Bar characteristics
        setSupportActionBar(toolbar)

        //set up tabs + view pager
        setupViewPager ()

        databaseId = intent.extras.getString("db_id")
        collId = intent.extras.getString("coll_id")

        supportActionBar?.title = "Collection: $collId"
    }

    private fun setupViewPager() {

        // create & config our adapter
        pagerAdapter = TabFragmentPagerAdapter (this, supportFragmentManager)

        with(pagerAdapter) {
            addFragment (DocumentsFragment (), getString(R.string.documents).toUpperCase())
        }

        viewPager.adapter = pagerAdapter

        // configure tabLayout & viewPager
        with(tabLayout) {
            tabMode = android.support.design.widget.TabLayout.MODE_FIXED
            tabGravity = android.support.design.widget.TabLayout.GRAVITY_FILL
            setupWithViewPager (viewPager)
        }

        // finally, glue it all together
        pagerAdapter.fillTabLayout (tabLayout)
    }
}