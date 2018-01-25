_This SDK was originally created as part of **[Azure.Mobile](https://aka.ms/mobile)** — a framework for rapidly creating iOS and android apps with modern, highly-scalable backends on Azure. Azure.Mobile has two simple objectives:_

1. _Enable developers to create, configure, deploy all necessary backend services fast — ideally under 10 minutes with only a few clicks_
2. _Provide native iOS and android SDKs with delightful APIs to interact with the services_

---

# Azure.Android [![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://raw.githubusercontent.com/NateRickard/Azure.Android/master/LICENSE) [![Release](https://jitpack.io/v/NateRickard/Azure.Android.svg)](https://jitpack.io/#NateRickard/Azure.Android) [![Build status](https://build.appcenter.ms/v0.1/apps/8959ae85-7b36-48ac-b333-10dfd76fb36b/branches/master/badge)](https://appcenter.ms)

# Configure

AzureData is built via [JitPack](https://jitpack.io/#NateRickard/Azure.Android) and the latest version(s), and instructions to add them to your Android project, can be found [there](https://jitpack.io/#NateRickard/Azure.Android).

For example, to add **v0.4.1** to an Android app, you would add the JitPack repository to the root `build.gradle` file:

```javascript
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Then, in the app project you want to use AzureData in, add the AzureData dependency:

```javascript
dependencies {
	compile 'com.github.NateRickard:Azure.Android:v0.4.1'
}
```

To grab the latest from GitHub:

```javascript
dependencies {
	compile 'com.github.NateRickard:Azure.Android:-SNAPSHOT'
	
	// you can also specify branch, i.e. master-SNAPSHOT
}
```

...or a [specific commit](https://github.com/NateRickard/Azure.Android/commit/594b795e10cda101b9b2572ed7a22f8a315cae30):

```javascript
dependencies {
	compile 'com.github.NateRickard:Azure.Android:594b795'
	
	// 594b795 == short commit hash from GitHub
}
```

## App Configuration

Before making calls to AzureData, you'll need to call `AzureData.configure` from your application class or main activity.

```kotlin
override fun onCreate() {
    super.onCreate()

    AzureData.configure(applicationContext, "cosmosDb name", "read-write key", TokenType.MASTER)

    // uncomment to enable verbose logging
    // AzureData.verboseLogging = true

    // ...
}
```


# Usage

## General Information

Still using Java?  [See the below note](#using-from-java) about some needed syntactical differences.

### Responses

All operations defined below will return a response that has the following properties:

| Property      |                    Value                 |
| ------------- | -----------------------------------------|
| `isSuccessful` | Returns `true` if the result is a success, `false` otherwise. |
| `isErrored` | Returns `true` if the result is an error, `false` otherwise. |
| `error` | Returns the associated error value if the result if it is a failure, null otherwise. |
| `jsonData` | The json data returned by the server (if applicable) |
| `request` | The (OkHttp) request object sent to the server. (If available) |
| `response` | The (OkHttp) response object returned from the server. (If available) |
| `resource` | For operations that return a resource or list of resources, this will contain that (typed) result. |

## Operations

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


### Databases

#### Create

```kotlin
AzureData.createDatabase (id) {
    // database = it.resource
}
```

#### List

```kotlin
AzureData.getDatabases {
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

AzureData.deleteDatabase (database) {
    // successfully deleted == it.isSuccessful
}

database.delete {
    // successfully deleted == it.isSuccessful
}
```



### Collections

#### Create

```kotlin
AzureData.createCollection (collectionId, databaseId) {
    // collection = it.resource
}

database.create (collectionId) {
    // collection = it.resource
}
```

#### List

```kotlin
AzureData.getCollections (databaseId) {
    // collections = it.resource?.items
}

database.getCollections {
    // collections = it.resource?.items
}
```

#### Get

```kotlin
AzureData.getCollection (collectionId, databaseId) {
    // collection = it.resource
}

database.getCollection (collectionId) {
    // collection = it.resource
}
```

#### Delete

```kotlin
AzureData.deleteCollection (collection, databaseId) {
    // successfully deleted == it.isSuccessful
}

database.deleteCollection (collection) {
    // successfully deleted == it.isSuccessful
}

database?.deleteCollection(collectionId) {
    // successfully deleted == it.isSuccessful
}

collection.delete {
    // successfully deleted == it.isSuccessful
}
```

#### Replace

```kotlin
// TODO...
```


### Documents

There are two different classes you can use to interact with documents:

#### Document

The `Document` type is intended to be inherited by your custom document model types.

Here is an example of a class `CustomDocument` that inherits from `Document`:

```kotlin
class CustomDocument(id: String? = null) : Document(id) {

    var customString = "My Custom String"
    var customNumber = 123000
    var customDate: Date = Date()
    var customBool = true
    var customArray = arrayOf(1, 2, 3)
    var customObject: User? = User()
}
```

#### DictionaryDocument

The `DictionaryDocument` type behaves very much like a `<String, Any?>` Map while handling all properties required by the database.  This allows you to interact with the document directly using subscript/indexing syntax.  `DictionaryDocument` cannot be subclassed.

Here is an example of using `DictionaryDocument` to create a document with the same properties as the `CustomDocument` above:

```kotlin
val document = DictionaryDocument()

document["customString"] = "My Custom String"
document["customNumber"] = 123000
document["customDate"] = Date()
document["customBool"] = true
document["customArray"] = arrayOf(1, 2, 3)
document["customObject"] = User()
```

##### ** Limitations **

When using `DictionaryDocument`, the data is subject to the limitations of json's lack of typing.  This means that when the above `DictionaryDocument` is deserialized, the deserializer won't know the specific types for your data.  In practice, this means the following types of data may appear differently once they've been "round tripped":

| Data Type  |   Roundtrip Data Type       | Sample Conversion |
| ---------- | --------------------------- | ----------------- |
| Number types (Int, Long, etc.) | `Number` | `(document["customNumber"] as Number).toInt()` |
| Array/List types | `ArrayList<*>`<br/>`ArrayList<Any?>` | `document["customArray"] as ArrayList<*>` |
| Object types | `Map<*,*>`<br/>`Map<String, Any?>` | `document["customObject"] as Map<*,*>` |

Due to these limitations, we recommend only using `DictionaryDocument` for simple data types and/or rapid prototyping.  Subclassing `Document`, as shown above, will yield much better results with proper typing based on the structure of your document class.

#### Create

```kotlin
// ridiculous code to get a Date using Calendar API
val cal = Calendar.getInstance()
cal.set(Calendar.YEAR, 1988)
cal.set(Calendar.MONTH, Calendar.JANUARY)
cal.set(Calendar.DAY_OF_MONTH, 1)
val customDateValue = cal.time

// Create Document

val document = CustomDocument() //optionally specify an Id here, otherwise it will be generated

document.customDate = customDateValue
document.customNumber = 1_000_000

// or

val document = DictionaryDocument() //optionally specify an Id here, otherwise it will be generated
            
document["customDate"] = customDateValue
document["customNumber"] = 1_000_000

// Document creation in CosmosDB

AzureData.createDocument (document, collectionId, databaseId) {
    // created document = it.resource
}

AzureData.createDocument (document, collection) {
    // created document = it.resource
}

collection.createDocument (document) {
    // created document = it.resource
}
```

#### List

```kotlin
AzureData.getDocuments (collectionId, databaseId, CustomDocument::class.java) {
    // documents = it.resource?.items
}

AzureData.getDocuments (collection, CustomDocument::class.java) {
    // documents = it.resource?.items
}

collection.getDocuments (CustomDocument::class.java) {
    // documents = it.resource?.items
}
```

#### Get

```kotlin
AzureData.getDocument (documentId, collectionId, databaseId, CustomDocument::class.java) {
    // document = it.resource
}

AzureData.getDocument (documentResourceId, collection, CustomDocument::class.java) {
    // document = it.resource
}

collection.getDocument (documentResourceId, CustomDocument::class.java) {
    // document = it.resource
}
```

#### Delete

```kotlin
AzureData.deleteDocument (document, collectionId, databaseId) {
    // successfully deleted == it.isSuccessful
}

AzureData.deleteDocument (document, collection) {
    // successfully deleted == it.isSuccessful
}

AzureData.deleteDocument (documentId, collectionId, databaseId) {
    // successfully deleted == it.isSuccessful
}

collection.deleteDocument (document) {
    // successfully deleted == it.isSuccessful
}

collection.deleteDocument (documentResourceId) {
    // successfully deleted == it.isSuccessful
}

document.delete {
    // successfully deleted == it.isSuccessful
}
```

#### Replace

```kotlin
AzureData.replaceDocument (document, inCollection: collectionId, inDatabase: databaseId) {
    // updated document = it.resource
}

AzureData.replaceDocument (document, collection) {
    // updated document = it.resource
}

collection.replaceDocument (document) {
    // updated document = it.resource
}
```

#### Query

```kotlin
val query = Query.select()
                .from(collectionId)
                .where("stringProperty", "stringValue")
                .andWhere("numberProperty", 12)
                .orderBy("_etag", true) // descending = true/false

AzureData.queryDocuments (collectionId, databaseId, query, CustomDocument::class.java) {
    // matching documents = it.resource?.items
}

AzureData.queryDocuments (collection, query, CustomDocument::class.java) {
    // matching documents = it.resource?.items
}

collection.queryDocuments (query, CustomDocument::class.java) {
    // matching documents = it.resource?.items
}
```

### Attachments

#### Create

Link to existing external media asset:

```kotlin
AzureData.createAttachment (attachmentId, "image/jpeg", mediaUrl, documentId, collectionId, databaseId) {
    // attachment = it.resource
}

document.createAttachment (attachmentId, "image/jpeg", mediaUrl) {
    // attachment = it.resource
}
```

`mediaUrl` can be of type `HttpUrl` (from OkHttp), `URL`, or a string url.

...or upload the media directly:

```kotlin
AzureData.createAttachment (attachmentId, "image/jpeg", data, documentId, collectionId, databaseId) {
    // attachment = it.resource
}

document.createAttachment (attachmentId, "image/jpeg", data) {
    // attachment = it.resource
}

```

`data` here is a `ByteArray` containing the bytes for the media/blob, and "image/jpeg" is the content type of the blob.

#### List

```kotlin
AzureData.getAttachments (documentId, collectionId, databaseId) {
    // attachments = it.resource?.items
}

document.getAttachments {
    // attachments = it.resource?.items
}
```

#### Delete

```kotlin
AzureData.deleteAttachment (attachmentId, documentId, collectionId, databaseId) {
    // successfully deleted == it.isSuccessful
}

AzureData.deleteAttachment (attachment, documentId, collectionId, databaseId) {
    // successfully deleted == it.isSuccessful
}

document.deleteAttachment (attachment) {
    // successfully deleted == it.isSuccessful
}

document.deleteAttachment (attachmentResourceId) {
    // successfully deleted == it.isSuccessful
}
```

#### Replace

Link to existing external media asset:

```kotlin
AzureData.replaceAttachment (attachmentId, "image/jpeg", mediaUrl, documentId, collectionId, databaseId) {
    // replaced attachment = it.resource
}

document.replaceAttachment (attachmentId, attachmentResourceId, "image/jpeg", url) {
    // replaced attachment = it.resource
}

```

`mediaUrl` can be of type `HttpUrl` (from OkHttp), `URL`, or a string url.

...or upload the media directly:

```kotlin
AzureData.replaceAttachment (attachmentId, "image/jpeg", data, documentId, collectionId, databaseId) {
    // replaced attachment = it.resource
}

document.replaceAttachment (attachmentId, "image/jpeg", data) {
    // replaced attachment = it.resource
}

```

`data` here is a `ByteArray` containing the bytes for the media/blob, and "image/jpeg" is the content type of the blob.

### Stored Procedures

#### Create

Given a stored procedure body:

```kotlin
val storedProcedureBody = """
        function () {
            var context = getContext();
            var r = context.getResponse();

            r.setBody('Hello World!');
        }
        """
```

A Stored Procedure can be created like so:

```kotlin
AzureData.createStoredProcedure (storedProcedureId, storedProcedureBody, collectionId, databaseId) {
    // storedProcedure = it.resource
}

AzureData.createStoredProcedure (storedProcedureId, storedProcedureBody, collection) {
    // storedProcedure = it.resource
}

collection.createStoredProcedure (storedProcedureId, storedProcedureBody) {
    // storedProcedure = it.resource
}
```

#### List

```kotlin
AzureData.getStoredProcedures (collectionId, databaseId) {
    // storedProcedures = it.resource?.items
}

AzureData.getStoredProcedures (collection) {
    // storedProcedures = it.resource?.items
}

collection.getStoredProcedures () {
    // storedProcedures = it.resource?.items
}
```

#### Delete

```kotlin
AzureData.deleteStoredProcedure (storedProcedureId, collectionId, databaseId) {
    // successfully deleted == it.isSuccessful
}

AzureData.deleteStoredProcedure (storedProcedure, collection) {
    // successfully deleted == it.isSuccessful
}

AzureData.deleteStoredProcedure (storedProcedureResourceId, collection) {
    // successfully deleted == it.isSuccessful
}

AzureData.deleteStoredProcedure (storedProcedure, collectionId, databaseId) {
    // successfully deleted == it.isSuccessful
}

collection.deleteStoredProcedure (storedProcedure) {
    // successfully deleted == it.isSuccessful
}

collection.deleteStoredProcedure (storedProcedureResourceId) {
    // successfully deleted == it.isSuccessful
}
```

#### Replace

```kotlin
AzureData.replaceStoredProcedure (storedProcedureId, storedProcedureBody, collectionId, databaseId) {
    // storedProcedure = it.resource
}

AzureData.replaceStoredProcedure (storedProcedureId, storedProcedureResourceId, storedProcedureBody, collection) {
    // storedProcedure = it.resource
}

collection.replaceStoredProcedure(storedProcedureId, storedProcedureResourceId, storedProcedureBody) {
    // storedProcedure = it.resource
}

collection.replaceStoredProcedure(storedProcedure) {
    // storedProcedure = it.resource
}
```

#### Execute

```kotlin
AzureData.executeStoredProcedure (storedProcedureId, parameters, collectionId, databaseId) {
    // raw response data = it.resource
}

AzureData.executeStoredProcedure (storedProcedureResourceId, parameters, collection) {
    // raw response data = it.resource
}

collection.executeStoredProcedure (storedProcedureResourceId, parameters) {
    // raw response data = it.resource
}
```

## Using from Java

As noted, this library is written in and optimized for Kotlin.  If your app is written in Java, it's still possible to use this library (assuming [your app targets JDK 1.8](https://developer.android.com/studio/write/java8-support.html)), with a few syntactical differences to the sample code found above:

* Callbacks in Java will be in lambda form and passed as an argument to the method.
* Due to [some compiler intricacies](https://stackoverflow.com/questions/37828790/why-do-i-have-to-return-unit-instance-when-implementing-in-java-a-kotlin-functio) with the way lambdas returning `Unit` (void in Java) are interpreted in Java, the callbacks from the [operations](#operations) either need to return `Unit.INSTANCE` or be wrapped in something that handles that for you.

Example: To get the [list of databases](#list), the call would look like:

```java
AzureData.getDatabases(response -> {
	if (response.isSuccessful()) {
		Database[] dbs = response.getResource().getItems();
	}
	...
	return Unit.INSTANCE;
});
```

For an improved development experience, a functional wrapper has been added to make this a bit cleaner:

```java
AzureData.getDatabases(onCallback(response -> {
	if (response.isSuccessful()) {
		Database[] dbs = response.getResource().getItems();
	}
	...
}));
```

`onCallback()` is found in the `com.microsoft.azureandroid.data.util` package, and will in essence 'inject' the return statement for you and remove the need to end your callback with a returned `Unit.INSTANCE`.

[See here](https://github.com/NateRickard/cosmos_db_example) for a complete example using this library from Java.