package com.microsoft.azureandroiddatasample.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.microsoft.azureandroiddatasample.R

/**
 * Created by nater on 10/27/17.
 */

class DatabaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var idTextView: TextView
    var ridTextView: TextView
    var selfTextView: TextView
    var eTagTextView: TextView
    var collsTextView: TextView
    var usersTextView: TextView
    var tsTextView: TextView

    init {

        idTextView = itemView.findViewById<TextView>(R.id.id)
        ridTextView = itemView.findViewById<TextView>(R.id.rid)
        selfTextView = itemView.findViewById<TextView>(R.id.self)
        eTagTextView = itemView.findViewById<TextView>(R.id.eTag)
        collsTextView = itemView.findViewById<TextView>(R.id.colls)
        usersTextView = itemView.findViewById<TextView>(R.id.users)
        tsTextView = itemView.findViewById<TextView>(R.id.ts)
    }
}