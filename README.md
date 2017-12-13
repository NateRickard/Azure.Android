# Azure.Android [![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://raw.githubusercontent.com/NateRickard/Azure.Android/master/LICENSE.md) [![Build status](https://build.appcenter.ms/v0.1/apps/8959ae85-7b36-48ac-b333-10dfd76fb36b/branches/master/badge)](https://appcenter.ms)


# About

This library aims to provide an elegant API and great developer experience when working with Azure CosmosDB's NoSQL, document-based implementation (previously called DocumentDB).

This should work with any native Android project (Java or Kotlin), although the code and samples below are written in Kotlin.


# Setup

Before making calls to AzureData, you'll need to initialize it using the `init` method:

```kotlin
fun init(context: Context, name: String, key: String, keyType: TokenType = TokenType.MASTER, verboseLogging: Boolean = false)
```

Call the `AzureData.init` method from your application class or main activity.

Example:

```kotlin
override fun onCreate() {
	super.onCreate()

	AzureData.init(applicationContext, "mobile", "gioHmSqPP7J7FE5XlqRgBjmqykWLbm0KnP2FCAOl7gu17ZWlvMTRxOvsUYWQ3YUN2Yvmd077O0hyFyBOIftjOg==", TokenType.MASTER)//, true)
	// uncomment the bool param above to enable verbose logging

	...
}
```


# Usage

| Resource                                              | Create                                                | List                                                  | Get                                                   | Delete                                                | Replace                                               | Query                                                 | Execute                                               |
| ----------------------------------------------------- | ----------------------------------------------------- | ----------------------------------------------------- | ----------------------------------------------------- | ----------------------------------------------------- | ----------------------------------------------------- | ----------------------------------------------------- | ----------------------------------------------------- |
| **[Databases](#databases)**                           | [Create](#create)                                     | [List](#list)                                         | [Get](#get)                                           | [Delete](#delete)                                     | *                                                     | *                                                     | *                                                     |
| **[Collections](#collections)**                       | [Create](#create-1)                                   | [List](#list-1)                                       | [Get](#get-1)                                         | [Delete](#delete-1)                                   | [Replace](#replace)                                   | *                                                     | *                                                     |
| **[Documents](#documents)**                           | [Create](#create-2)                                   | [List](#list-2)                                       | [Get](#get-2)                                         | [Delete](#delete-2)                                   | [Replace](#replace-1)                                 | [Query](#query)                                       | *                                                     |
| **[Attachments](#attachments)**                       | [Create](#create-3)                                   | [List](#list-3)                                       | *                                                     | [Delete](#delete-3)                                   | [Replace](#replace-2)                                 | *                                                     | *                                                     |
| **[Stored Procedures](#stored-procedures)**           | [Create](#create-4)                                   | [List](#list-4)                                       | *                                                     | [Delete](#delete-4)                                   | [Replace](#replace-3)                                 | *                                                     | [Execute](#execute)                                   |
| **[User Defined Functions](#user-defined-functions)** | [Create](#create-5)                                   | [List](#list-5)                                       | *                                                     | [Delete](#delete-5)                                   | [Replace](#replace-4)                                 | *                                                     | *                                                     |
| **[Triggers](#triggers)**                             | [Create](#create-6)                                   | [List](#list-6)                                       | *                                                     | [Delete](#delete-6)                                   | [Replace](#replace-5)                                 | *                                                     | *                                                     |
| **[Users](#users)**                                   | [Create](#create-7)                                   | [List](#list-7)                                       | [Get](#get-3)                                         | [Delete](#delete-7)                                   | [Replace](#replace-6)                                 | *                                                     | *                                                     |
| **[Permissions](#permissions)**                       | [Create](#create-8)                                   | [List](#list-8)                                       | [Get](#get-4)                                         | [Delete](#delete-8)                                   | [Replace](#replace-7)                                 | *                                                     | *                                                     |
| **[Offers](#offers)**                                 | *                                                     | [List](#list-9)                                       | [Get](#get-5)                                         | *                                                     | [Replace](#replace-8)                                 | [Query](#query-1)                                     | *                                                     |


_* not applicable to resource type_


## Databases

#### Create
```kotlin
AzureData.createDatabase (id) {
    // database = it.resource
}
```

#### List
```kotlin
AzureData.databases {
    // databases = it.resource?.items
}
```

#### Get
```kotlin
AzureData.getDatabase (id) {
    // database = it.resource
}
```

#### Delete
```kotlin
AzureData.deleteDatabase (id) {
    // successfully deleted == it.isSuccessful
}
```
or, if you have a `Database` instance:

```kotlin
AzureData.deleteDatabase (db) {
	// successfully deleted == it.isSuccessful
}
```
or, better yet:

```kotlin
db.delete {
	// successfully deleted == it.isSuccessful
}
```


## Collections

#### Create
```kotlin
AzureData.createCollection (collectionId, databaseId) {
    // collection = it.resource
}
```
or, if you have a `Database` instance:

```kotlin
database.create (collectionId) {
    // collection = it.resource
}
```

#### List
```kotlin
AzureData.getCollections (databaseId) {
    // collections = it.resource?.items
}
```
or, if you have a `Database` instance:

```kotlin
database.getCollections {
    // collections = it.resource?.items
}
```

#### Get
```kotlin
AzureData.getCollection (collectionId, databaseId) {
    // collection = it.resource
}
```
or, if you have a `Database` instance:

```kotlin
database.getCollection (collectionId) {
    // collection = it.resource
}
```

#### Delete
```kotlin
AzureData.delete (collection, from: databaseId) { s in
    // s == successfully deleted
}

database.delete (collection) { s in
    // s == successfully deleted
}
```

#### Delete
```kotlin
AzureData.deleteCollection (id) {
    // successfully deleted == it.isSuccessful
}
```
or, if you have a `DocumentCollection` instance:

```kotlin
AzureData.deleteCollection (coll) {
	// successfully deleted == it.isSuccessful
}
```
or, better yet:

```kotlin
coll.delete {
	// successfully deleted == it.isSuccessful
}
```

```kotlin
db.deleteCollection (coll) {
	// successfully deleted == it.isSuccessful
}
```

#### Replace
```kotlin
// TODO...
```


## Documents

#### Create
```kotlin
val newDocument = Document() //optionally specify an Id here, otherwise it will be generated
            
newDocument["aNumber"] = 86
newDocument["aString"] = "Hello!"

AzureData.createDocument (newDocument, collectionId, databaseId) {
	// document = it.resource
}
```
with a `Collection` instance:

```kotlin
AzureData.createDocument (newDocument, collection) {
    // document = it.resource
}

collection.create (document) { r in
    // document = r.resource
}
```

#### List
```kotlin
AzureData.get (documentsAs: ADDocument.self, inCollection: collectionId, inDatabase: databaseId) { r in
    // documents = r.resource?.items
}

AzureData.get (documentsAs: ADDocument.self, in: collection) { r in
    // documents = r.resource?.items
}

collection.get (documentsAs: ADDocument.self) { r in
    // documents in r.resource?.list
}
```

#### Get
```kotlin
AzureData.get (documentWithId: id, as: ADDocument.self, inCollection: collectionId, inDatabase: databaseId) { r in
    // document = r.resource
}

AzureData.get (documentWithId: id, as: ADDocument.self, in: collection) { r in
    // document = r.resource
}

collection.get (documentWithResourceId: id: as: ADDocument.self) { r in
    // document = r.resource
}
```

#### Delete
```kotlin
AzureData.delete (document, fromCollection: collectionId, inDatabase: databaseId) { r in
    // document = r.resource
}

AzureData.delete (document, from: collection) { r in
    // document = r.resource
}

collection.delete (document) { s in
    // s == successfully deleted
}
```

#### Replace
```kotlin
AzureData.replace (document, inCollection: collectionId, inDatabase: databaseId) { r in
    // document = r.resource
}

AzureData.replace (document, in: collection) { r in
    // document = r.resource
}

collection.replace (document) { r in
    // document = r.resource
}
```

#### Query
```kotlin
let query = ADQuery.select("firstName", "lastName", ...)
                   .from("People")
                   .where("firstName", is: "Colby")
                   .and("lastName", is: "Williams")
                   .and("age", isGreaterThanOrEqualTo: 20)
                   .orderBy("_etag", descending: true)

AzureData.query(documentsIn: collectionId, inDatabase: databaseId, with: query) { r in
    // documents = r.resource?.items
}

AzureData.query(documentsIn: collection, with: query) { r in
    // documents = r.resource?.items
}

collection.query (documentsWith: query) { r in
    // documents in r.resource?.list
}
```