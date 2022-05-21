package com.harifrizki.crimemapsapps.ui.module.admin

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.databinding.ActivityListOfAdminBinding
import com.harifrizki.crimemapsapps.ui.component.BaseActivity
import com.harifrizki.crimemapsapps.ui.module.profile.ProfileActivity
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.crimemapsapps.utils.ActivityName.LIST_OF_ADMIN
import com.harifrizki.crimemapsapps.utils.CRUD.CREATE
import com.harifrizki.crimemapsapps.utils.FROM_ACTIVITY
import com.harifrizki.crimemapsapps.utils.OPERATION_CRUD

class ListOfAdminActivity : BaseActivity() {

    private val binding by lazy {
        ActivityListOfAdminBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)
        appBar(binding.iAppBarListOfAdmin,
            getString(R.string.admin_menu),
            R.drawable.ic_round_account_circle_24,
            R.color.primary,
            R.drawable.frame_background_secondary,
            R.drawable.ic_round_add_24,
            R.color.white,
            R.drawable.button_primary_ripple_white,
            onClick = {
                goTo(
                    ProfileActivity(),
                    hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(LIST_OF_ADMIN),
                        OPERATION_CRUD to CREATE
                    ))
            })
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    { }
}