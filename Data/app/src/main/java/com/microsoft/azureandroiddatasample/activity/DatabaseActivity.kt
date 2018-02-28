package com.microsoft.azureandroiddatasample.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.TabFragmentPagerAdapter
import com.microsoft.azureandroiddatasample.fragment.CollectionsFragment
import com.microsoft.azureandroiddatasample.fragment.UsersFragment

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
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