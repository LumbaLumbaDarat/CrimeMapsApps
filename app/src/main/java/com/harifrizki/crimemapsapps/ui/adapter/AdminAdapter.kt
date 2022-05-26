package com.harifrizki.crimemapsapps.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.databinding.ItemAdminBinding
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.MAX_ITEM_LIST_SHIMMER
import com.harifrizki.crimemapsapps.utils.doGlide
import com.harifrizki.crimemapsapps.utils.layoutStartDrawableShimmer
import com.harifrizki.crimemapsapps.utils.widgetStartDrawableShimmer

class AdminAdapter(
    var isShimmer: Boolean?
) : RecyclerView.Adapter<AdminAdapter.HolderView>() {
    var admins: ArrayList<Admin>? = ArrayList()

    var onClickResetPassword: ((Admin?) -> Unit)? = null
    var onClickLockAndUnlock: ((Admin?) -> Unit)? = null
    var onClickDelete: ((Admin?) -> Unit)? = null
    var onClickAdmin: ((Admin?) -> Unit)? = null

    @JvmName("initializeAdmins")
    @SuppressLint("NotifyDataSetChanged")
    fun setAdmins(admins: ArrayList<Admin>?) {
        this.admins?.apply {
            clear()
            addAll(admins!!)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAdmins(admins: ArrayList<Admin>?) {
        this.admins?.apply {
            addAll(admins!!)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        return HolderView(
            ItemAdminBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val admin: Admin
        if (!isShimmer!!)
        {
            admin = admins!![position]
            holder.apply {
                binding.apply {
                    ivBtnAdminResetPassword.setOnClickListener{
                        onClickResetPassword?.invoke(admin)
                    }
                    ivBtnAdminLockAndUnlock.setOnClickListener {
                        onClickLockAndUnlock?.invoke(admin)
                    }
                    ivBtnAdminDelete.setOnClickListener {
                        onClickDelete?.invoke(admin)
                    }
                }
                itemView.setOnClickListener {
                    onClickAdmin?.invoke(admin)
                }
            }
        } else admin = Admin()
        holder.bind(admin, isShimmer)
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) MAX_ITEM_LIST_SHIMMER
        else admins!!.size
    }

    class HolderView(val binding: ItemAdminBinding,
                     private val context: Context?) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(admin: Admin?, isShimmer: Boolean?) =
            with(binding) {
                if (!isShimmer!!)
                {
                    doGlide(context,
                        ivAdminPhotoProfile,
                        admin?.adminImage,
                        R.drawable.ic_round_account_box_primary_24)
                    tvAdminName.text = admin?.adminName
                    tvAdminEmail.text = admin?.adminUsername
                    ivAdminState.background =
                        if (admin?.isLogin!!) context?.getDrawable(R.drawable.frame_background_green)
                        else context?.getDrawable(R.drawable.frame_background_red)
                    ivBtnAdminLockAndUnlock.apply {
                        if (admin.isActive!!) {
                            background = context.getDrawable(R.drawable.button_red_ripple_white)
                            setImageResource(R.drawable.ic_round_lock_24)
                        } else {
                            background = context.getDrawable(R.drawable.button_dark_green_ripple_white)
                            setImageResource(R.drawable.ic_round_lock_open_24)
                        }
                    }
                } else {
                    layoutStartDrawableShimmer(arrayOf(
                        llAdminBackground
                    ), context)
                    widgetStartDrawableShimmer(arrayOf(
                        ivAdminPhotoProfile,
                        ivAdminState,
                        ivBtnAdminDelete,
                        ivBtnAdminLockAndUnlock,
                        ivBtnAdminResetPassword
                    ), context)
                    widgetStartDrawableShimmer(arrayOf(
                        tvAdminName, tvAdminEmail
                    ), context)
                }
            }
    }
}