package com.microsoft.azureandroiddatasample.activity

import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.TabFragmentPagerAdapter
import com.microsoft.azureandroiddatasample.fragment.DatabasesFragment
import com.microsoft.azureandroiddatasample.fragment.OffersFragment

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

class MainActivity : BaseTabActivity() {

    override fun configureViewPager(adapter: TabFragmentPagerAdapter) {

        adapter.addFragment (DatabasesFragment(), getString(R.string.databases).toUpperCase())
        adapter.addFragment (OffersFragment(), getString(R.string.offers).toUpperCase())
    }
}