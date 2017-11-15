package com.microsoft.azureandroiddatasample.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.activity.CollectionsActivity
import com.microsoft.azureandroiddatasample.adapter.CardAdapter
import com.microsoft.azureandroiddatasample.viewholder.DatabaseViewHolder
import com.microsoft.azureandroiddatasample.framework.RecyclerItemClickListener

import kotlinx.android.synthetic.main.databases_fragment.*

/**
 * Created by Nate Rickard on 11/14/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class DatabaseFragment : Fragment() {

    private lateinit var adapter: CardAdapter<Database>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate (R.layout.databases_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        adapter = CardAdapter(R.layout.database_view, DatabaseViewHolder::class.java)

        recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.adapter = adapter

        recycler_view.addOnItemTouchListener(
                RecyclerItemClickListener(activity, recycler_view, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {

                        val db = adapter.getData(position)

                        val intent = Intent(activity.baseContext, CollectionsActivity::class.java)
                        intent.putExtra("db_id", db.id)
                        startActivity(intent)
                    }

                    override fun onLongItemClick(view: View, position: Int) {
                        // do whatever
                    }
                })
        )

        button_clear.setOnClickListener { adapter.clear() }

        button_create.setOnClickListener {

            val editTextView = layoutInflater.inflate(R.layout.edit_text, null)
            val editText = editTextView.findViewById<EditText>(R.id.editText)
            val messageTextView = editTextView.findViewById<TextView>(R.id.messageText)
            messageTextView.setText(R.string.database_dialog)

            AlertDialog.Builder(activity)
                    .setView(editTextView)
                    .setPositiveButton("Create", { dialog, whichButton ->

                        val databaseId = editText.text.toString()
                        val progressDialog = ProgressDialog.show(activity, "", "Creating. Please wait...", true)

                        try {
                            AzureData.instance.createDatabase(databaseId) { response ->

                                print(response.result)

                                if (response.isSuccessful) {

                                    val db = response.resource

                                    activity.runOnUiThread {
                                        fetchDbs()
                                    }
                                } else {
                                    print(response.error)
                                }
                            }
                        }
                        catch (ex: Exception) {
                            ex.printStackTrace()
                        }

                        progressDialog.cancel()
                    })
                    .setNegativeButton("Cancel", { dialog, whichButton -> }).show()
        }

        button_fetch.setOnClickListener {
            fetchDbs()
        }
    }

    private fun fetchDbs() {

        try {
            val dialog = ProgressDialog.show(activity, "", "Loading. Please wait...", true)

            AzureData.instance.databases { response ->

                print(response.result)

                if (response.isSuccessful) {

                    val dbs = response.resource?.items!!

                    activity.runOnUiThread {
                        adapter.clear()

                        for (db in dbs) {
                            adapter.addData(db)
                        }

                        adapter.notifyDataSetChanged()
                    }
                }
                else {
                    print(response.error)
                }

                dialog.cancel()
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}