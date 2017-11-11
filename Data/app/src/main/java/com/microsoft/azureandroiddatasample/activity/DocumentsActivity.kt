package com.microsoft.azureandroiddatasample.activity

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.microsoft.azureandroid.data.AzureData
import com.microsoft.azureandroid.data.model.Document
import com.microsoft.azureandroiddatasample.App
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.CardAdapter
import com.microsoft.azureandroiddatasample.adapter.DocumentViewHolder

import kotlinx.android.synthetic.main.documents_activity.*

/**
 * Created by nater on 11/10/17.
 */

class DocumentsActivity : Activity() {

    private lateinit var adapter: CardAdapter<Document>

//    private var _rxController: CosmosRxController? = null

    private lateinit var databaseId: String

    private lateinit var collectionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.documents_activity)

//        _rxController = CosmosRxController.getInstance(this)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = CardAdapter(R.layout.document_view, DocumentViewHolder::class.java)

        recycler_view.layoutManager = linearLayoutManager
        recycler_view.adapter = adapter

        val extras = intent.extras

        if (extras != null) {
            databaseId = extras.getString("db_id")
            collectionId = extras.getString("coll_id")

            collectionIdTextView.text = collectionId
        }

        button_clear.setOnClickListener { adapter.clear() }

        button_delete.setOnClickListener {
            val dialog = ProgressDialog.show(this@DocumentsActivity, "", "Deleting. Please wait...", true)

//            _rxController!!.deleteCollection(databaseId, collectionId)
//                    // Run on a background thread
//                    .subscribeOn(Schedulers.io())
//                    // Be notified on the main thread
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ x ->
//                        Log.e(TAG, "_rxController.deleteDatabases(databaseId) - finished.")
//
//                        dialog.cancel()
//
//                        finish()
//                    })

            adapter!!.clear()
        }

        button_fetch.setOnClickListener {
            try {
                val dialog = ProgressDialog.show(this@DocumentsActivity, "", "Loading. Please wait...", true)

                AzureData.instance.getDocumentsAs<Document>(collectionId, databaseId) { response ->

                    print(response.result)

                    if (response.isSuccessful) {

                        val docs = response.resource?.items!!

                        runOnUiThread {
                            adapter.clear()

                            for (doc in docs) {
                                adapter.addData(doc)
                            }

                            adapter.notifyDataSetChanged()
                        }
                    }
                    else {
                        print(response.error)
                    }

                    dialog.cancel()
                }

//                _rxController!!.getDocuments(databaseId, collectionId)
//                        // Run on a background thread
//                        .subscribeOn(Schedulers.io())
//                        // Be notified on the main thread
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe({ x ->
//                            // clear current list
//                            adapter!!.clear()
//
//                            Log.e(TAG, "_rxController.getDocuments(databaseId, collectionId) finished.")
//
//                            //                                for (DocumentCollectionContract contract: x.getDocumentCollections()) {
//                            //                                    String partitionKey = contract.getPartitionKey() == null ? "" : contract.getPartitionKey().getKind();
//                            //
//                            //                                    adapter.addData(new DocumentCollection(contract.getId(), contract.getRid(), contract.getSelf(),
//                            //                                            contract.getEtag(), partitionKey));
//                            //                                }
//
//                            dialog.cancel()
//                        })
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
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