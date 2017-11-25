package com.microsoft.azureandroid.data.model

/**
 * Created by Nate Rickard on 11/24/17.
 * Copyright © 2017 Nate Rickard. All rights reserved.
 */

class Attachment(id: String, contentType: String, url: String) : Resource(id) {

    /**
     * Gets or sets the MIME content type of the attachment in the Azure Cosmos DB service.
     */
    var contentType: String? = contentType

    /**
     * Gets or sets the media link associated with the attachment content in the Azure Cosmos DB service.
     */
    var mediaLink: String? = url
}