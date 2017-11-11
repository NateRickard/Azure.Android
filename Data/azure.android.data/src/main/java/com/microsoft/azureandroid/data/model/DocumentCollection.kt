package com.microsoft.azureandroid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by nater on 11/11/17.
 */

class DocumentCollection : Resource() {

    @SerializedName(conflictsLinkKey)
    var conflictsLink: String? = null

    @SerializedName(documentsLinkKey)
    var documentsLink: String? = null

//    @SerializedName(indexingPolicyKey)
//    var indexingPolicy: IndexingPolicy? = null

//    @SerializedName(partitionKeyKey)
//    var partitionKey: PartitionKeyDefinition? = null

    @SerializedName(storedProceduresLinkKey)
    var storedProceduresLink: String? = null

    @SerializedName(triggersLinkKey)
    var triggersLink: String? = null

    @SerializedName(userDefinedFunctionsLinkKey)
    var userDefinedFunctionsLink: String? = null

    companion object {

        const val conflictsLinkKey                = "_conflicts"
        const val documentsLinkKey                = "_docs"
        const val indexingPolicyKey               = "indexingPolicy"
        const val partitionKeyKey                 = "partitionKey"
        const val storedProceduresLinkKey         = "_sprocs"
        const val triggersLinkKey                 = "_triggers"
        const val userDefinedFunctionsLinkKey     = "_udfs"
    }
}

//    public override init(_ id: String) { super.init(id) }
//
//    required public init?(fromJson dict: [String:Any]) {
//        super.init(fromJson: dict)
//
//        conflictsLink               = dict[conflictsLinkKey]            as? String
//        documentsLink               = dict[documentsLinkKey]            as? String
//        if let indexingPolicy       = dict[indexingPolicyKey]           as? [String:Any] { self.indexingPolicy = ADIndexingPolicy(fromJson: indexingPolicy) }
//        if let partitionKey         = dict[partitionKeyKey]             as? [String:Any] { self.partitionKey = ADPartitionKeyDefinition(fromJson: partitionKey) }
//        storedProceduresLink        = dict[storedProceduresLinkKey]     as? String
//        triggersLink                = dict[triggersLinkKey]             as? String
//        userDefinedFunctionsLink    = dict[userDefinedFunctionsLinkKey] as? String
//    }
//
//    open override var dictionary: [String : Any] {
//        return super.dictionary.merging([
//        conflictsLinkKey:conflictsLink.valueOrEmpty,
//        documentsLinkKey:documentsLink.valueOrEmpty,
//        indexingPolicyKey:indexingPolicy?.dictionary ?? "",
//        partitionKeyKey:partitionKey?.dictionary     ?? "",
//        storedProceduresLinkKey:storedProceduresLink.valueOrEmpty,
//        triggersLinkKey:triggersLink.valueOrEmpty,
//        userDefinedFunctionsLinkKey:userDefinedFunctionsLink.valueOrEmpty])
//        { (_, new) in new }
//    }