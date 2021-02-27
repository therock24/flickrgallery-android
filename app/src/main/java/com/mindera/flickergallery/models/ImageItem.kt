package com.mindera.flickergallery.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class ImageItem(
    @field:PrimaryKey
    var id: String,
    var owner: String,
    var secret: String,
    var server: String,
    var farm: Int,
    var title: String?,
    var ispublic: Short,
    var isfriend: Short,
    var isfamily: Short,
    var url_q: String
)