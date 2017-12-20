package com.microsoft.azureandroid.data.model

import java.util.*

/**
 * Created by Nate Rickard on 12/19/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

/**
 * Wrapper class around Date that allows us to use custom serialization
 */
class Timestamp(date: Long = 0) : Date(date)