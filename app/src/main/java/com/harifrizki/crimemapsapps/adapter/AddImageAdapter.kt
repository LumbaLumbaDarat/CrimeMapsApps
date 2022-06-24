package com.harifrizki.crimemapsapps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.core.R
import com.harifrizki.core.model.ImageResource
import com.harifrizki.core.utils.ImageState.ADD_IMAGE
import com.harifrizki.core.utils.ImageState.IS_IMAGE
import com.harifrizki.core.utils.doGlide
import com.harifrizki.crimemapsapps.databinding.ItemAddImageBinding

class AddImageAdapter(
    var context: Context?,
): RecyclerView.Adapter<AddImageAdapter.HolderView>() {
    private var imageResources: ArrayList<ImageResource>? = ArrayList()
    var customParentPath: String? = null
    var useMaxWidth: Boolean? = false

    var onClickAdd: (() -> Unit)? = null
    var onClickPreviewImage: ((ImageResource) -> Unit)? = null
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
                    itemView.setOnClickListener {
                        onClickPreviewImage?.invoke(imageResource)
                    }
                    binding.ivDeleteAdmin.setOnClickListener {
                        onClickDelete?.invoke(imageResource)
                    }
                }
                else -> {}
            }
            bind(imageResource, customParentPath)
        }
    }

    override fun getItemCount(): Int {
        return imageResources!!.size
    }

    inner class HolderView(
        val binding: ItemAddImageBinding,
        private val context: Context?):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageResource: ImageResource?,
                 customParentPath: String?) = with(binding)
        {
            if (useMaxWidth!!)
                clBackgroundItemAddImage.updateLayoutParams {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                }
            when (imageResource?.imageState) {
                ADD_IMAGE -> {
                    ltAddImage.apply {
                        visibility = View.VISIBLE
                        if (useMaxWidth!!)
                            updateLayoutParams {
                                width = ViewGroup.LayoutParams.MATCH_PARENT
                                height = resources.getDimensionPixelOffset(
                                    R.dimen.image_width_rectangular_default)
                            }
                    }
                }
                IS_IMAGE -> {
                    if (imageResource.imageUri == null)
                        doGlide(context,
                            ivImage,
                            imageResource.imagePath,
                            url = customParentPath)
                    else doGlide(context, ivImage, imageResource.imageUri)
                    rrvImage.apply {
                        visibility = View.VISIBLE
                        if (useMaxWidth!!)
                            updateLayoutParams {
                                width = ViewGroup.LayoutParams.MATCH_PARENT
                                height = resources.getDimensionPixelOffset(
                                    R.dimen.image_width_rectangular_default)
                            }
                    }
                }
                else -> {}
            }
        }
    }
}