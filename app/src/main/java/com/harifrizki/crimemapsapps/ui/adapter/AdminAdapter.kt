package com.harifrizki.crimemapsapps.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.databinding.ItemAdminBinding
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.*

class AdminAdapter(
    var context: Context?,
    var isShimmer: Boolean?
) : RecyclerView.Adapter<AdminAdapter.HolderView>() {
    val adminLogin by lazy {
        context?.let {
            PreferencesManager.getInstance(it).getPreferences(LOGIN_MODEL, Admin::class.java)
        }
    }
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
        if (!isShimmer!!) {
            admin = admins!![position]
            holder.apply {
                binding.apply {
                    ivBtnAdminResetPassword.setOnClickListener {
                        onClickResetPassword?.invoke(admin)
                    }
                    ivBtnAdminLockAndUnlock.setOnClickListener {
                        onClickLockAndUnlock?.invoke(admin)
                    }
                    ivBtnAdminDelete.setOnClickListener {
                        onClickDelete?.invoke(admin)
                    }
                }
                if (!admin.adminUsername
                        .equals(adminLogin?.adminUsername)
                )
                    itemView.setOnClickListener {
                        onClickAdmin?.invoke(admin)
                    }
            }
        } else admin = Admin()
        holder.bind(adminLogin, admin, isShimmer)
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) MAX_ITEM_LIST_SHIMMER
        else admins!!.size
    }

    class HolderView(
        val binding: ItemAdminBinding,
        private val context: Context?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            adminLogin: Admin?,
            admin: Admin?,
            isShimmer: Boolean?
        ) {
            binding.apply {
                if (!isShimmer!!) {
                    doGlide(
                        context,
                        ivAdminPhotoProfile,
                        admin?.adminImage,
                        R.drawable.ic_round_account_box_primary_24
                    )
                    tvAdminName.text = admin?.adminName
                    tvAdminEmail.text = admin?.adminUsername
                    ivAdminState.background =
                        if (admin?.isLogin!!) context?.getDrawable(R.drawable.frame_background_green)
                        else context?.getDrawable(R.drawable.frame_background_red)
                    setActivate(admin, ivBtnAdminLockAndUnlock)

                    if (admin.adminUsername?.equals(adminLogin?.adminUsername)!! ||
                        admin.adminUsername?.equals(context?.let {
                            PreferencesManager
                                .getInstance(it)
                                .getPreferences(DEFAULT_ADMIN_ROOT_USERNAME)
                        })!! ||
                        admin.adminRole?.equals(context?.let {
                            PreferencesManager
                                .getInstance(it)
                                .getPreferences(ROLE_ROOT)
                        })!!
                    ) {
                        ivBtnAdminResetPassword.visibility = View.GONE
                        ivBtnAdminLockAndUnlock.visibility = View.GONE
                        ivBtnAdminDelete.visibility = View.GONE
                    }

                } else {
                    layoutStartDrawableShimmer(
                        arrayOf(
                            llAdminBackground
                        ), context
                    )
                    widgetStartDrawableShimmer(
                        arrayOf(
                            ivAdminPhotoProfile,
                            ivAdminState,
                            ivBtnAdminDelete,
                            ivBtnAdminLockAndUnlock,
                            ivBtnAdminResetPassword
                        ), context
                    )
                    widgetStartDrawableShimmer(
                        arrayOf(
                            tvAdminName, tvAdminEmail
                        ), context
                    )
                }
            }
        }
    }
}