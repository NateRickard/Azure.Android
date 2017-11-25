package com.microsoft.azureandroiddatasample.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.TabFragmentPagerAdapter
import com.microsoft.azureandroiddatasample.fragment.CollectionsFragment
import com.microsoft.azureandroiddatasample.fragment.UsersFragment

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class DatabaseActivity : BaseTabActivity() {

    private lateinit var databaseId: String

    override fun onCreate(savedInstanceState: Bundle?) {

        databaseId = intent.extras.getString("db_id")

        super.onCreate(savedInstanceState)
    }

    override fun configureToolbar(actionBar: ActionBar) {

        actionBar.title = "Database: $databaseId"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun configureViewPager(adapter: TabFragmentPagerAdapter) {

        adapter.addFragment (CollectionsFragment (), getString(R.string.collections).toUpperCase())
        adapter.addFragment (UsersFragment (), getString(R.string.users).toUpperCase())
    }
}