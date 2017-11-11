package com.microsoft.azureandroiddatasample.adapter

import android.view.View
import com.microsoft.azureandroid.data.model.Document
import kotlinx.android.synthetic.main.document_view.view.*

/**
 * Created by nater on 10/27/17.
 */

class DocumentViewHolder(itemView: View) : ViewHolderBase<Document>(itemView) {

    override fun setData(item: Document) {

        itemView.idTextView.text = item.id
        itemView.ridTextView.text = item.resourceId
        itemView.selfTextView.text = item.selfLink
        itemView.eTagTextView.text = item.etag
    }
}