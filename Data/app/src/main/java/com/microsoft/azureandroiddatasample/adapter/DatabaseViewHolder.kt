package com.microsoft.azureandroiddatasample.adapter

import android.view.View
import com.microsoft.azureandroid.data.model.Database

import kotlinx.android.synthetic.main.database_view.view.*

/**
 * Created by nater on 10/27/17.
 */

class DatabaseViewHolder(itemView: View) : ViewHolderBase<Database>(itemView) {

    override fun setData(item: Database) {

        itemView.idTextView.text = item.id
        itemView.ridTextView.text = item.resourceId
        itemView.selfTextView.text = item.selfLink
        itemView.eTagTextView.text = item.etag
        itemView.collsTextView.text = item.collectionsLink
        itemView.usersTextView.text = item.usersLink
    }
}