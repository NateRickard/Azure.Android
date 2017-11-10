package com.microsoft.azureandroid.data.services

import com.microsoft.azureandroid.data.model.contract.CollectionsContract
import com.microsoft.azureandroid.data.model.contract.DatabaseContract
import com.microsoft.azureandroid.data.model.contract.DatabasesContract
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by nater on 10/31/17.
 */

interface CosmosRxSevice {

    interface CosmosRxService {
        @GET("/dbs")
        fun getDatabases(@Header("x-ms-date") date: String, @Header("x-ms-version") version: String,
                         @Header("authorization") authString: String): Observable<DatabasesContract>

        @GET("/dbs/{dbid}")
        fun getDatabaseById(@Path("dbid") databaseId: String, @Header("x-ms-date") date: String,
                            @Header("x-ms-version") version: String, @Header("authorization") authString: String): Observable<DatabaseContract>

        @POST("/dbs")
        fun createDatabase(@Body body: HashMap<String, Any>, @Header("x-ms-date") date: String,
                           @Header("x-ms-version") version: String, @Header("authorization") authString: String): Observable<Any>

        @DELETE("/dbs/{dbid}")
        fun deleteDatabase(@Path("dbid") databaseId: String, @Header("x-ms-date") date: String,
                           @Header("x-ms-version") version: String, @Header("authorization") authString: String): Observable<Any>

        @GET("/dbs/{dbid}/colls")
        fun getCollections(@Path("dbid") databaseId: String, @Header("x-ms-date") date: String,
                           @Header("x-ms-version") version: String, @Header("authorization") authString: String): Observable<CollectionsContract>

        @GET("/dbs/{dbid}/colls/{collid}")
        fun getCollectionById(@Path("dbid") databaseId: String, @Path("collid") collectionId: String,
                              @Header("x-ms-date") date: String, @Header("x-ms-version") version: String,
                              @Header("authorization") authString: String): Observable<Any>

        @POST("/dbs/{dbid}/colls")
        fun createCollection(@Path("dbid") databaseId: String, @Body body: HashMap<String, Any>,
                             @Header("x-ms-date") date: String, @Header("x-ms-version") version: String,
                             @Header("authorization") authString: String): Observable<Any>

        @DELETE("/dbs/{dbid}/colls/{collid}")
        fun deleteCollection(@Path("dbid") databaseId: String, @Path("collid") collectionId: String,
                             @Header("x-ms-date") date: String, @Header("x-ms-version") version: String,
                             @Header("authorization") authString: String): Observable<Any>

        @GET("/dbs/{dbid}/colls/{collid}/docs")
        fun getDocuments(@Path("dbid") databaseId: String, @Path("collid") collectionId: String,
                         @Header("x-ms-date") date: String, @Header("x-ms-version") version: String,
                         @Header("authorization") authString: String): Observable<Any>

        @GET("/dbs/{dbid}/colls/{collid}/docs/{docid}")
        fun getDocumentById(@Path("dbid") databaseId: String, @Path("collid") collectionId: String,
                            @Path("docid") documentId: String, @Header("x-ms-date") date: String,
                            @Header("x-ms-version") version: String, @Header("authorization") authString: String): Observable<Any>

        @POST("/dbs/{dbid}/colls/{collid}/docs")
        fun createDocument(@Path("dbid") databaseId: String, @Path("collid") collectionId: String,
                           @Body body: HashMap<String, Any>, @Header("x-ms-date") date: String,
                           @Header("x-ms-version") version: String, @Header("authorization") authString: String): Observable<Any>

        @GET("/dbs/{dbid}/colls/{collid}/docs/{docid}/attachments")
        fun getAttachments(@Path("dbid") databaseId: String, @Path("collid") collectionId: String,
                           @Path("docid") documentId: String, @Header("x-ms-date") date: String,
                           @Header("x-ms-version") version: String, @Header("authorization") authString: String): Observable<Any>

        @GET("/dbs/{dbid}/colls/{collid}/docs/{docid}/attachments/{attachid}")
        fun getAttachmentById(@Path("dbid") databaseId: String, @Path("collid") collectionId: String,
                              @Path("docid") documentId: String, @Path("attachid") attachmentId: String,
                              @Header("x-ms-date") date: String, @Header("x-ms-version") version: String,
                              @Header("authorization") authString: String): Observable<Any>

        @POST("/dbs/{dbid}/colls/{collid}/docs/{docid}/attachments")
        fun createAttachment(@Path("dbid") databaseId: String, @Path("collid") collectionId: String,
                             @Path("docid") documentId: String, @Body body: HashMap<String, Any>,
                             @Header("x-ms-date") date: String, @Header("x-ms-version") version: String,
                             @Header("authorization") authString: String): Observable<Any>

        @POST("/dbs/{dbid}/colls/{collid}/docs")
        fun executeQuery(@Path("dbid") databaseId: String, @Path("collid") collectionId: String,
                         @Body body: HashMap<String, Any>, @Header("x-ms-date") date: String,
                         @Header("x-ms-version") version: String, @Header("authorization") authString: String,
                         @Header("x-ms-documentdb-isquery") isQuery: String, @Header("Content-Type") contentType: String): Observable<Any>
    }

}