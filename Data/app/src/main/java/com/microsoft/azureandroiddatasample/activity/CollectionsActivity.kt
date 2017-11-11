package com.microsoft.azureandroiddatasample.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.microsoft.azureandroid.data.model.DocumentCollection
import com.microsoft.azureandroiddatasample.App
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.CardAdapter
import com.microsoft.azureandroiddatasample.adapter.DocumentCollectionViewHolder
import com.microsoft.azureandroiddatasample.framework.RecyclerItemClickListener

import kotlinx.android.synthetic.main.collections_activity.*

/**
 * Created by nater on 11/10/17.
 */

class CollectionsActivity : Activity() {

    private lateinit var adapter: CardAdapter<DocumentCollection>

    private var dbId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collections_activity)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = CardAdapter(R.layout.collection_view, DocumentCollectionViewHolder::class.java)

        recycler_view.layoutManager = linearLayoutManager
        recycler_view.adapter = adapter

        recycler_view.addOnItemTouchListener(
                RecyclerItemClickListener(this, recycler_view, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {

                        val coll = adapter.getData(position)

                        val intent = Intent(baseContext, DocumentsActivity::class.java)
                        intent.putExtra("db_id", dbId)
                        intent.putExtra("coll_id", coll.id)
                        startActivity(intent)
                    }

                    override fun onLongItemClick(view: View, position: Int) {
                        // do whatever
                    }
                })
        )

        val extras = intent.extras

        if (extras != null) {
            dbId = extras.getString("db_id")
            databaseId.text = dbId
        }

        button_clear.setOnClickListener { adapter.clear() }

        button_delete.setOnClickListener {
            val dialog = ProgressDialog.show(this@CollectionsActivity, "", "Deleting. Please wait...", true)

//            _rxController!!.deleteDatabase(_databaseId)
//                    // Run on a background thread
//                    .subscribeOn(Schedulers.io())
//                    // Be notified on the main thread
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ x ->
//                        Log.e(TAG, "_rxController.deleteDatabase(_databaseId) - finished.")
//
//                        dialog.cancel()
//
//                        // revert back to database page
//                        finish()
//                    })
//
//            _adapter!!.clear()
        }

        button_fetch.setOnClickListener {
            try {
                val dialog = ProgressDialog.show(this@CollectionsActivity, "", "Loading. Please wait...", true)

//                AzureData.instance.databases { response ->
//
//                    print(response.result)
//
//                    if (response.isSuccessful) {
//
//                        val dbs = response.resource?.items!!
//
//                        runOnUiThread {
//                            adapter.clear()
//
//                            for (db in dbs) {
//                                adapter.addData(db)
//                            }
//
//                            adapter.notifyDataSetChanged()
//                        }
//                    }
//                    else {
//                        print(response.error)
//                    }
//
//                    dialog.cancel()
//                }


//                _rxController!!.getCollections(_databaseId)
//                        // Run on a background thread
//                        .subscribeOn(Schedulers.io())
//                        // Be notified on the main thread
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe({ x ->
//                            // clear current list
//                            _adapter!!.clear()
//
//                            Log.e(TAG, "_rxController.getCollections(_databaseId) - finished.")
//
//                            for (contract in x.getDocumentCollections()) {
//                                val partitionKey = if (contract.getPartitionKey() == null) "" else contract.getPartitionKey().getKind()
//
//                                _adapter!!.addData(DocumentCollection(contract.getId(), contract.getRid(), contract.getSelf(),
//                                        contract.getEtag(), partitionKey))
//                            }
//
//                            dialog.cancel()
//                        })
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        button_create.setOnClickListener {
            val databaseId: String

            val editTextView = layoutInflater.inflate(R.layout.edit_text, null)
            val editText = editTextView.findViewById<EditText>(R.id.editText)
            val messageTextView = editTextView.findViewById<TextView>(R.id.messageText)
            messageTextView.setText(R.string.document_collection_dialog)

            AlertDialog.Builder(this@CollectionsActivity)
                    .setView(editTextView)
                    .setPositiveButton("Create", DialogInterface.OnClickListener { dialog, whichButton ->
                        val collectionId = editText.text.toString()
                        val progressDialog = ProgressDialog.show(this@CollectionsActivity, "", "Creating. Please wait...", true)

//                        _rxController!!.createCollection(_databaseId, collectionId)
//                                // Run on a background thread
//                                .subscribeOn(Schedulers.io())
//                                // Be notified on the main thread
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe({ x ->
//                                    _adapter!!.clear()
//
//                                    Log.e(TAG, "_rxController.createCollection(_databaseId, collectionId) - finished.")
//
//                                    dialog.cancel()
//                                    progressDialog.cancel()
//                                })
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, whichButton -> }).show()
        }
    }

    override fun onResume() {
        super.onResume()

        adapter.clear()

        App.activityResumed()
    }

    override fun onPause() {
        super.onPause()
        App.activityPaused()
    }

    fun didError() {

    }

    companion object {
        private val TAG = "CollectionsActivity"
    }
}