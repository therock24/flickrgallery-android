package com.mindera.flickergallery.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mindera.flickergallery.databinding.ImageItemBinding
import com.mindera.flickergallery.models.ImageItem

class ImageListAdapter(private val context: Context, private val imageItems: List<ImageItem>):
        RecyclerView.Adapter<ImageListAdapter.CustomViewHolder>(){

     class CustomViewHolder(private val binding: ImageItemBinding):
            RecyclerView.ViewHolder(binding.root) {

         var image = binding.imageItem
         var title = binding.imageTitle

        companion object {
            fun from(parent: ViewGroup): CustomViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ImageItemBinding.inflate(layoutInflater, parent, false)
                return CustomViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val imageItem = imageItems[position]


        holder.image
    }

    override fun getItemCount(): Int = imageItems.size
}

fun ImageView.loadImageFromURL(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(context.applicationContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(this)
    }
}