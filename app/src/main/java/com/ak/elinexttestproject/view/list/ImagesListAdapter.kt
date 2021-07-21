package com.ak.elinexttestproject.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ak.elinexttestproject.R
import com.bumptech.glide.Glide

class ImagesListAdapter : RecyclerView.Adapter<ImagesListAdapter.ImageViewHolder>() {

    private var itemsList = arrayListOf<ImageListItem>()

    fun insertData(imagesList: List<ImageListItem>) {
        val diffCallback = ImagesListDiffUtilCallback(itemsList, imagesList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemsList.clear()
        itemsList.addAll(imagesList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val viewHolderItemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.images_list_item, parent, false)
        return ImageViewHolder(viewHolderItemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(itemsList[position])
    }

    override fun getItemCount() = itemsList.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageItem: ImageView = itemView.findViewById(R.id.iv_item_image)
        private val imageItemLoading: ProgressBar =
            itemView.findViewById(R.id.pb_item_image_loading)

        fun bind(item: ImageListItem) {
            if (item.isLoading) {
                imageItem.isVisible = false
                imageItemLoading.isVisible = true
            } else {
                imageItem.isVisible = true
                imageItemLoading.isVisible = false
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .centerInside()
                    .into(imageItem)
            }
        }
    }

    class ImagesListDiffUtilCallback(
        private val oldList: List<ImageListItem>,
        private val newList: List<ImageListItem>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}