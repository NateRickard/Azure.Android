package com.microsoft.azureandroiddatasample.adapter

import android.view.View
import com.microsoft.azureandroid.data.model.DocumentCollection

import kotlinx.android.synthetic.main.collection_view.view.*

/**
 * Created by nater on 11/10/17.
 */

class DocumentCollectionViewHolder(itemView: View) : ViewHolderBase<DocumentCollection>(itemView) {

    override fun setData(item: DocumentCollection) {

        itemView.idTextView.text = item.id
        itemView.ridTextView.text = item.rid
        itemView.selfTextView.text = item.self
        itemView.eTagTextView.text = item.etag
    }
}