package com.microsoft.azureandroiddatasample.extensions

import android.view.Menu

/**
 * Created by Nate Rickard on 11/21/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

fun Menu.updateItemEnabledStatus(menuItemId: Int, isEnabled: Boolean) {

    val menuItem = this.findItem(menuItemId)
    menuItem.isEnabled = isEnabled
    menuItem.icon.alpha = if (isEnabled) 255 else 64
}