package com.microsoft.azureandroiddatasample.viewholder

import android.view.View
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroiddatasample.viewholder.ViewHolderBase

import kotlinx.android.synthetic.main.collection_view.view.*

/**
 * Created by nater on 11/10/17.
 */

class CollectionViewHolder(itemView: View) : ViewHolderBase<DocumentCollection>(itemView) {

    override fun setData(item: DocumentCollection) {

        itemView.idTextView.text = item.id
        itemView.ridTextView.text = item.resourceId
        itemView.selfTextView.text = item.selfLink
        itemView.eTagTextView.text = item.etag
    }
}