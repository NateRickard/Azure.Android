package com.microsoft.azureandroiddatasample.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.TabFragmentPagerAdapter
import com.microsoft.azureandroiddatasample.fragment.DocumentsFragment
import com.microsoft.azureandroiddatasample.fragment.StoredProceduresFragment
import com.microsoft.azureandroiddatasample.fragment.TriggersFragment
import com.microsoft.azureandroiddatasample.fragment.UserDefinedFunctionsFragment

/**
 * Created by Nate Rickard on 11/21/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class CollectionActivity : BaseTabActivity() {

    private lateinit var databaseId: String
    private lateinit var collId: String

    override fun onCreate(savedInstanceState: Bundle?) {

        databaseId = intent.extras.getString("db_id")
        collId = intent.extras.getString("coll_id")

        super.onCreate(savedInstanceState)
    }

    override fun configureToolbar(actionBar: ActionBar) {

        actionBar.title = "Collection: $collId"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun configureViewPager(adapter: TabFragmentPagerAdapter) {

        adapter.addFragment (DocumentsFragment (), getString(R.string.docs).toUpperCase())
        adapter.addFragment (StoredProceduresFragment (), getString(R.string.stored_procs).toUpperCase())
        adapter.addFragment (TriggersFragment (), getString(R.string.triggers).toUpperCase())
        adapter.addFragment (UserDefinedFunctionsFragment (), getString(R.string.udfs).toUpperCase())
    }
}