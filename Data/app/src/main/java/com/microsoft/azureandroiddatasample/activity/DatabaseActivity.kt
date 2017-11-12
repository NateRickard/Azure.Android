package com.microsoft.azureandroiddatasample.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.microsoft.azureandroid.data.AzureData

import com.microsoft.azureandroiddatasample.App
import com.microsoft.azureandroiddatasample.R
import com.microsoft.azureandroiddatasample.adapter.*
import com.microsoft.azureandroid.data.model.Database
import com.microsoft.azureandroiddatasample.framework.RecyclerItemClickListener

import kotlinx.android.synthetic.main.databases_activity.*


class DatabaseActivity : AppCompatActivity() {

    private lateinit var adapter: CardAdapter<Database>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.databases_activity)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = CardAdapter(R.layout.database_view, DatabaseViewHolder::class.java)

        recycler_view.layoutManager = linearLayoutManager
        recycler_view.adapter = adapter

        recycler_view.addOnItemTouchListener(
                RecyclerItemClickListener(this, recycler_view, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {

                        val db = adapter.getData(position)

                        val intent = Intent(baseContext, CollectionsActivity::class.java)
                        intent.putExtra("db_id", db.id)
                        startActivity(intent)
                    }

                    override fun onLongItemClick(view: View, position: Int) {
                        // do whatever
                    }
                })
        )


        button_clear.setOnClickListener { adapter!!.clear() }

        button_create.setOnClickListener {

            val editTextView = layoutInflater.inflate(R.layout.edit_text, null)
            val editText = editTextView.findViewById<EditText>(R.id.editText)
            val messageTextView = editTextView.findViewById<TextView>(R.id.messageText)
            messageTextView.setText(R.string.database_dialog)

            AlertDialog.Builder(this@DatabaseActivity)
                    .setView(editTextView)
                    .setPositiveButton("Create", { dialog, whichButton ->

                        val databaseId = editText.text.toString()
                        val progressDialog = ProgressDialog.show(this@DatabaseActivity, "", "Creating. Please wait...", true)

                        try {
                            AzureData.instance.createDatabase(databaseId) { response ->

                                print(response.result)

                                if (response.isSuccessful) {

                                    val db = response.resource

                                    runOnUiThread {
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

//                        _rxController!!.createDatabase(databaseId)
//                                // Run on a background thread
//                                .subscribeOn(Schedulers.io())
//                                // Be notified on the main thread
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe({ x ->
//                                    _adapter!!.clear()
//
//                                    Log.e(TAG, "_rxController.createDatabase(databaseId) - finished.")
//
//                                    dialog.cancel()
//                                    progressDialog.cancel()
//                                })
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, whichButton -> }).show()
        }

        button_fetch.setOnClickListener {
            fetchDbs()
        }
    }

    private fun fetchDbs() {

        try {
            val dialog = ProgressDialog.show(this@DatabaseActivity, "", "Loading. Please wait...", true)

            AzureData.instance.databases { response ->

                print(response.result)

                if (response.isSuccessful) {

                    val dbs = response.resource?.items!!

                    runOnUiThread {
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

    override fun onResume() {
        super.onResume()

        adapter.clear()

        App.activityResumed()
    }

    override fun onPause() {
        super.onPause()
        App.activityPaused()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    internal fun onRunSchedulerExampleButtonClicked() {
        /*disposables.add(sampleObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }

                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "onError()", e);
                    }

                    @Override public void onNext(String string) {
                        Log.d(TAG, "onNext(" + string + ")");
                    }
                }));*/
    }

    /*static Observable<String> sampleObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override public ObservableSource<? extends String> call() throws Exception {
                // Do some long running operation
                SystemClock.sleep(5000);
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }*/

    fun didError() {

    }

    companion object {
        private val TAG = "DatabaseActivity"
    }
}