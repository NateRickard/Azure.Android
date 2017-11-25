package com.microsoft.azureandroiddatasample.activity

import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.TabFragmentPagerAdapter
import com.microsoft.azureandroiddatasample.fragment.DatabasesFragment
import com.microsoft.azureandroiddatasample.fragment.OffersFragment

/**
* Created by Nate Rickard on 11/14/17.
* Copyright © 2017 Nate Rickard. All rights reserved.
*/

class MainActivity : BaseTabActivity() {

    override fun configureViewPager(adapter: TabFragmentPagerAdapter) {

        adapter.addFragment (DatabasesFragment(), getString(R.string.databases).toUpperCase())
        adapter.addFragment (OffersFragment(), getString(R.string.offers).toUpperCase())
    }
}