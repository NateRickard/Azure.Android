_This SDK was originally created as part of **[Azure.Mobile](https://aka.ms/mobile)** — a framework for rapidly creating iOS and android apps with modern, highly-scalable backends on Azure. Azure.Mobile has two simple objectives:_

1. _Enable developers to create, configure, deploy all necessary backend services fast — ideally under 10 minutes with only a few clicks_
2. _Provide native iOS and android SDKs with delightful APIs to interact with the services_


# Azure.Android [![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://raw.githubusercontent.com/NateRickard/Azure.Android/master/LICENSE.md) [![Build status](https://build.appcenter.ms/v0.1/apps/8959ae85-7b36-48ac-b333-10dfd76fb36b/branches/master/badge)](https://appcenter.ms)



# Configure

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

AzureData.deleteDatabase (database) {
    // successfully deleted == it.isSuccessful
}

database.delete {
    // successfully deleted == it.isSuccessful
}
```



## Collections

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
AzureData.deleteCollection (collection, from: databaseId) { s in
    // s == successfully deleted
}

database.deleteCollection (collection) { s in
    // s == successfully deleted
}

collection.delete {
    // successfully deleted == it.isSuccessful
}
```

#### Replace
```kotlin
// TODO...
```


## Documents

There are two different classes you can use to interact with documents:

### Document

The `Document` type is intended to be inherited by your custom model types. ~~Subclasses must conform to the `Codable` protocal and require minimal boilerplate code for successful serialization/deserialization.~~

Here is an example of a class `CustomDocument` that inherits from `Document`:

```swift
class CustomDocument: Document {
    
    var testDate:   Date?
    var testNumber: Double?
    
    public override init () { super.init() }
    public override init (_ id: String) { super.init(id) }
    
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
        
        let container = try decoder.container(keyedBy: CodingKeys.self)
        
        testDate    = try container.decode(Date.self,   forKey: .testDate)
        testNumber  = try container.decode(Double.self, forKey: .testNumber)
    }
    
    public override func encode(to encoder: Encoder) throws {
        try super.encode(to: encoder)

        var container = encoder.container(keyedBy: CodingKeys.self)
        
        try container.encode(testDate,      forKey: .testDate)
        try container.encode(testNumber,    forKey: .testNumber)
    }

    private enum CodingKeys: String, CodingKey {
        case testDate
        case testNumber
    }
}
```

### DictionaryDocument

The `DictionaryDocument` type behaves very much like a `[String:Any]` dictionary while handling all properties required by the database.  This allows you to interact with the document directly using subscript syntax.  `DictionaryDocument` cannot be subclassed.

Here is an example of using `DictionaryDocument` to create a document with the same properties as the `CustomDocument` above:

```swift
let document = DictionaryDocument()

document["testDate"]   = Date(timeIntervalSince1970: 1510865595)         
document["testNumber"] = 1_000_000
```


#### Create
```kotlin
val document = CustomDocument()

document.testDate   = Date(timeIntervalSince1970: 1510865595)
document.testNumber = 1_000_000

// or

val document = DictionaryDocument() //optionally specify an Id here, otherwise it will be generated
            
document["testDate"]   = Date(timeIntervalSince1970: 1510865595)         
document["testNumber"] = 1_000_000

AzureData.createDocument (document, collectionId, databaseId) {
    // document = it.resource
}

AzureData.createDocument (document, collection) {
    // document = it.resource
}

collection.createDocument (document) { r in
    // document = r.resource
}
```

#### List
```kotlin
AzureData.get (documentsAs: CustomDocument.self, inCollection: collectionId, inDatabase: databaseId) { r in
    // documents = r.resource?.items
}

AzureData.get (documentsAs: CustomDocument.self, in: collection) { r in
    // documents = r.resource?.items
}

collection.get (documentsAs: CustomDocument.self) { r in
    // documents in r.resource?.list
}
```

#### Get
```kotlin
AzureData.get (documentWithId: id, as: CustomDocument.self, inCollection: collectionId, inDatabase: databaseId) { r in
    // document = r.resource
}

AzureData.get (documentWithId: id, as: CustomDocument.self, in: collection) { r in
    // document = r.resource
}

collection.get (documentWithResourceId: id: as: CustomDocument.self) { r in
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