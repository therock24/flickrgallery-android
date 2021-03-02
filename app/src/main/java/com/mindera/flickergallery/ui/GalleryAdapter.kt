package com.mindera.flickergallery.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mindera.flickergallery.R
import com.mindera.flickergallery.models.ImageItem
import com.mindera.flickergallery.utils.URLUtils
import kotlinx.android.synthetic.main.image_item.view.*
import java.util.*

/**
 * Adapter that manages Image items of the RecyclerView in [GalleryAdapter].
 */
class GalleryAdapter: RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

        // callback for checking unique items (to avoid duplicates)
        private val differCallback = object : DiffUtil.ItemCallback<ImageItem>() {

                override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                        return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                        return oldItem == newItem
                }
        }

        // differ for adding only new unique items to the list
        private val differ = AsyncListDiffer(this,differCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_item,parent,false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val image = differ.currentList[position]
                holder.itemView.apply {

                        // set text under image
                        cvImage.tvName.text = image.id

                        // load image to image view
                        loadImageToImageView(image,this)


                        // set click listener for the image item
                        setOnClickListener {
                                onItemClickListener?.let { it(image) }
                        }
                }
        }

        /**
         * Loads the image thumbnail to its view
         */
        private fun loadImageToImageView(imageItem: ImageItem, view: View) {
                val imageUrl = URLUtils.getFlickrImageLink(imageItem.id,
                        imageItem.secret,
                        imageItem.server,
                        imageItem.farm,
                        URLUtils.LARGE_SQUARE_SIZE)

                Glide.with(view).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.DATA).into(view.cvImage.ivImage)
        }

        override fun getItemCount(): Int = differ.currentList.size

        /**
         * Submit new items for the images list.
         */
        fun submitItems(responseItems: MutableList<ImageItem>) {
                differ.submitList(responseItems.toList());
        }

        private var onItemClickListener: ((ImageItem) -> Unit)? = null

        fun setOnItemClickListener(listener: (ImageItem) -> Unit) {
                onItemClickListener = listener
        }

}
