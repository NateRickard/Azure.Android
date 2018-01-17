package com.microsoft.azureandroid.data.integration

import android.support.test.runner.AndroidJUnit4
import com.microsoft.azureandroid.data.model.DictionaryDocument
import org.junit.runner.RunWith

/**
 * Created by Nate Rickard on 12/12/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class DocumentTests : DocumentTest<DictionaryDocument>(DictionaryDocument::class.java)