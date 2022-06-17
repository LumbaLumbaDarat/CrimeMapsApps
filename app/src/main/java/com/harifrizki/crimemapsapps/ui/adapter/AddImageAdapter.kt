package com.harifrizki.crimemapsapps.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.crimemapsapps.databinding.ItemAddImageBinding
import com.harifrizki.crimemapsapps.model.ImageResource
import com.harifrizki.crimemapsapps.utils.ImageState.*
import com.harifrizki.crimemapsapps.utils.doGlide

class AddImageAdapter(
    var context: Context?,
): RecyclerView.Adapter<AddImageAdapter.HolderView>() {
    private var imageResources: ArrayList<ImageResource>? = ArrayList()
    var onClickAdd: (() -> Unit)? = null
    var onClickDelete: ((ImageResource) -> Unit)? = null

    @JvmName("initializeImageResources")
    @SuppressLint("NotifyDataSetChanged")
    fun setImageResources(imageResources: ArrayList<ImageResource>?) {
        this.imageResources?.apply {
            clear()
            addAll(imageResources!!)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addImageResources(imageResources: ArrayList<ImageResource>?) {
        this.imageResources?.apply {
            addAll(imageResources!!)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        return HolderView(
            ItemAddImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        holder.apply {
            val imageResource = imageResources!![position]
            when (imageResource.imageState) {
                ADD_IMAGE -> {
                    binding.ltAddImage.setOnClickListener { onClickAdd?.invoke() }
                }
                IS_IMAGE -> {
                    binding.ivDeleteAdmin.setOnClickListener {
                        onClickDelete?.invoke(imageResource)
                    }
                }
                else -> {}
            }
            bind(imageResource)
        }
    }

    override fun getItemCount(): Int {
        return imageResources!!.size
    }

    inner class HolderView(
        val binding: ItemAddImageBinding,
        private val context: Context?):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageResource: ImageResource?) = with(binding)
        {
            when (imageResource?.imageState) {
                ADD_IMAGE -> {
                    ltAddImage.visibility = View.VISIBLE
                }
                IS_IMAGE -> {
                    doGlide(context, ivImage, imageResource.imageUri)
                    rrvImage.visibility = View.VISIBLE
                }
                else -> {}
            }
        }
    }
}