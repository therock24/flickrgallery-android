package com.mindera.flickergallery.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "images")
data class ImageItem(
    @field:PrimaryKey
    val id: String,
    val farm: Int,
    val isfamily: Int,
    val isfriend: Int,
    val ispublic: Int,
    val owner: String,
    val secret: String,
    val server: String,
    val title: String,
    val url_q: String,
    val width_q: Int,
    val height_q: Int,
) : Serializable