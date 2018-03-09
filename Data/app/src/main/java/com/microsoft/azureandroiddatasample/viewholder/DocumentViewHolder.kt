package com.microsoft.azureandroiddatasample.viewholder

import android.view.View
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroiddatasample.viewholder.ViewHolderBase
import kotlinx.android.synthetic.main.document_view.view.*

/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

class DocumentViewHolder(itemView: View) : ViewHolderBase<Document>(itemView) {

    override fun setData(item: Document) {

        itemView.idTextView.text = item.id
        itemView.ridTextView.text = item.resourceId
        itemView.selfTextView.text = item.selfLink
        itemView.eTagTextView.text = item.etag
    }
}