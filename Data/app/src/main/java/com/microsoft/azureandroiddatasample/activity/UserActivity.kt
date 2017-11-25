package com.microsoft.azureandroiddatasample.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.TabFragmentPagerAdapter
import com.microsoft.azureandroiddatasample.fragment.CollectionsFragment
import com.microsoft.azureandroiddatasample.fragment.PermissionsFragment
import com.microsoft.azureandroiddatasample.fragment.UsersFragment
import kotlinx.android.synthetic.main.tab_layout.*

/**
 * Created by Nate Rickard on 11/16/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class UserActivity : BaseTabActivity() {

    private lateinit var databaseId: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {

        databaseId = intent.extras.getString("db_id")
        userId = intent.extras.getString("user_id")

        super.onCreate(savedInstanceState)
    }

    override fun configureToolbar(actionBar: ActionBar) {

        actionBar.title = "User: $userId"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun configureViewPager(adapter: TabFragmentPagerAdapter) {

        adapter.addFragment (PermissionsFragment (), getString(R.string.permissions).toUpperCase())
    }
}