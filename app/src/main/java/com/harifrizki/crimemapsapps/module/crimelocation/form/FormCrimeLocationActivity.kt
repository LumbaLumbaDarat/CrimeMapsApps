package com.harifrizki.crimemapsapps.module.crimelocation.form

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.harifrizki.core.component.ParentArea
import com.harifrizki.core.component.activity.BaseActivity
import com.harifrizki.core.component.activity.CropPhotoActivity
import com.harifrizki.crimemapsapps.module.maps.MapsActivity
import com.harifrizki.core.data.remote.response.CrimeLocationResponse
import com.harifrizki.core.model.*
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.ActivityName.*
import com.harifrizki.core.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.core.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.core.utils.CRUD.*
import com.harifrizki.core.utils.ImageState.*
import com.harifrizki.core.utils.ImageType.*
import com.harifrizki.core.utils.MenuAreaType.*
import com.harifrizki.core.utils.MenuSetting.*
import com.harifrizki.core.utils.ParentAreaAction.*
import com.harifrizki.core.utils.ResponseStatus.*
import com.harifrizki.crimemapsapps.BuildConfig
import com.harifrizki.crimemapsapps.adapter.AddImageAdapter
import com.harifrizki.crimemapsapps.databinding.ActivityFormCrimeLocationBinding
import com.harifrizki.crimemapsapps.module.area.list.ListOfAreaActivity
import com.orhanobut.logger.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import com.harifrizki.core.R
import com.harifrizki.core.data.remote.response.MessageResponse

class FormCrimeLocationActivity : BaseActivity() {
    private val binding by lazy {
        ActivityFormCrimeLocationBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<FormCrimeLocationViewModel>()
    private val admin: Admin? by lazy {
        PreferencesManager.getInstance(this)
            .getPreferences(LOGIN_MODEL, Admin::class.java)
    }
    private val maxUploadImage: Int by lazy {
        PreferencesManager
            .getInstance(this)
            .getPreferences(MAX_UPLOAD_IMAGE_CRIME_LOCATION)!!.toInt()
    }

    private var parentAreaProvince: ParentArea? = null
    private var parentAreaCity: ParentArea? = null
    private var parentAreaSubDistrict: ParentArea? = null
    private var parentAreaUrbanVillage: ParentArea? = null
    private var parentAreaLocation: ParentArea? = null

    private var provinceAsParent: Province? = null
    private var cityAsParent: City? = null
    private var subDistrictAsParent: SubDistrict? = null
    private var urbanVillageAsParent: UrbanVillage? = null
    private var address: String? = null
    private var latitude: String? = null
    private var longitude: String? = null

    private var addImageAdapter: AddImageAdapter? = null
    private var imageResources: ArrayList<ImageResource>? = ArrayList()
    private var deleteImageResource: ImageResource? = null
    private var isAfterCRUD: CRUD? = NONE
    private var crud: CRUD? = null
    private var fromActivity: ActivityName? = null
    private var map: HashMap<String, Any>? = null
    private var getImageFrom: MenuSetting? = MenuSetting.MENU_NONE

    private var latestTempUri: Uri? = null
    private var file: File? = null
    private var crimeLocation: CrimeLocation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        map = getMap(intent)
        fromActivity = getEnumActivityName(map!![FROM_ACTIVITY].toString())
        crud = map!![OPERATION_CRUD] as CRUD

        when (crud) {
            CREATE -> {
                appBar(
                    binding.iAppBarFormCrimeLocation,
                    getString(
                        R.string.label_add,
                        getString(R.string.crime_location_menu)
                    ),
                    R.drawable.ic_round_location_on_24,
                    R.color.primary,
                    R.drawable.frame_background_secondary
                )
                initializeForm()
            }
            UPDATE_DATA_NON_IMAGE -> {
                crimeLocation = map!![CRIME_LOCATION_MODEL] as CrimeLocation
                appBar(
                    binding.iAppBarFormCrimeLocation,
                    getString(
                        R.string.label_edit,
                        getString(R.string.crime_location_menu)
                    ),
                    R.drawable.ic_round_location_on_24,
                    R.color.primary,
                    R.drawable.frame_background_secondary
                )
                initializeEditForm(crimeLocation)
            }
            UPDATE_IMAGE -> {
                crimeLocation = map!![CRIME_LOCATION_MODEL] as CrimeLocation
                appBar(
                    binding.iAppBarFormCrimeLocation,
                    getString(R.string.label_edit,
                        getString(R.string.label_plus_two_string,
                            getString(R.string.label_photo),
                            getString(R.string.crime_location_menu))),
                    R.drawable.ic_round_location_on_24,
                    R.color.primary,
                    R.drawable.frame_background_secondary
                )
                initializeEditImage(crimeLocation)
            }
            else -> {}
        }
        binding.apply {
            btnSubmitCrimeLocation.setOnClickListener { submitCrimeLocation() }
            btnBackCrimeLocation.setOnClickListener { onBackPressed() }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!) {

            }
            else {
                val map = getMap(it.data)
                when (getEnumActivityName(map[FROM_ACTIVITY].toString())) {
                    CROP_PHOTO -> {
                        try {
                            val uriAfterCrop = Uri.parse(map[URI_IMAGE].toString())
                            file = File(uriAfterCrop.path!!)
                            when (crud) {
                                CREATE -> {
                                    imageResources?.add(
                                        ImageResource().apply {
                                            imageState = IS_IMAGE
                                            imageFile = file
                                            imageName = file!!.name
                                            imagePath = file!!.path
                                            imageUri = uriAfterCrop
                                        }
                                    )
                                    addImageAdapter?.apply {
                                        setImageResources(imageResources = imageResources)
                                        notifyDataSetChanged()
                                    }
                                }
                                UPDATE_IMAGE -> {
                                    crimeLocationAddImage(
                                        CrimeLocation().apply {
                                            crimeLocationId = crimeLocation?.crimeLocationId
                                            updatedBy = Admin().apply {
                                                adminId = admin?.adminId
                                            }
                                        },
                                        arrayListOf(
                                            file!!
                                        )
                                    )
                                }
                                else -> {}
                            }
                        }
                        catch (e: IOException) {
                            Logger.e(e.message.toString())
                            showError(
                                message = e.message.toString(),
                                onClick = { onBackPressed() })
                        }
                    }
                    LIST_OF_AREA -> {
                        when (map[AREA] as MenuAreaType) {
                            MENU_AREA_PROVINCE_ID -> {
                                provinceAsParent = getModel(map[AREA_MODEL]!!, Province::class.java)
                                resetParam(
                                    MENU_AREA_PROVINCE_ID,
                                    provinceAsParent?.provinceName,
                                    resetProvince = false,
                                    resetCity = true,
                                    resetSubDistrict = true,
                                    resetUrbanVillage = true
                                )
                            }
                            MENU_AREA_CITY_ID -> {
                                cityAsParent = getModel(map[AREA_MODEL], City::class.java)
                                resetParam(
                                    MENU_AREA_CITY_ID,
                                    cityAsParent?.cityName,
                                    resetProvince = false,
                                    resetCity = false,
                                    resetSubDistrict = true,
                                    resetUrbanVillage = true
                                )
                            }
                            MENU_AREA_SUB_DISTRICT_ID -> {
                                subDistrictAsParent = getModel(map[AREA_MODEL], SubDistrict::class.java)
                                resetParam(
                                    MENU_AREA_SUB_DISTRICT_ID,
                                    subDistrictAsParent?.subDistrictName,
                                    resetProvince = false,
                                    resetCity = false,
                                    resetSubDistrict = false,
                                    resetUrbanVillage = true
                                )
                            }
                            MENU_AREA_URBAN_VILLAGE_ID -> {
                                urbanVillageAsParent = getModel(map[AREA_MODEL], UrbanVillage::class.java)
                                resetParam(
                                    MENU_AREA_URBAN_VILLAGE_ID,
                                    subDistrictAsParent?.subDistrictName,
                                    resetProvince = false,
                                    resetCity = false,
                                    resetSubDistrict = false,
                                    resetUrbanVillage = false
                                )
                            }
                            else -> {}
                        }
                    }
                    MAPS -> {
                        val crimeLocation = map[CRIME_LOCATION_MODEL] as CrimeLocation
                        address = crimeLocation.crimeMapsAddress
                        latitude = crimeLocation.crimeMapsLatitude
                        longitude = crimeLocation.crimeMapsLongitude
                        parentAreaLocation?.apply {
                            setContent(address)
                        }
                        binding.apply {
                            tvLatitude.text = makeSpannable(
                                text = getString(
                                    R.string.label_latitude_of,
                                    latitude
                                )
                            )
                            tvLongitude.text = makeSpannable(
                                text = getString(
                                    R.string.label_longitude_of,
                                    longitude
                                )
                            )
                        }
                    }
                    else -> {
                        showMessage(getMap(it.data))
                    }
                }
            }
        }
    }

    private val openCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture())
        {
            if (it) {
                goTo(
                    CropPhotoActivity(),
                    hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(PROFILE),
                        URI_IMAGE to latestTempUri!!
                    )
                )
            }
        }

    private val openGallery =
        registerForActivityResult(ActivityResultContracts.GetContent())
        { uri: Uri? ->
            goTo(
                CropPhotoActivity(),
                hashMapOf(
                    FROM_ACTIVITY to getNameOfActivity(PROFILE),
                    URI_IMAGE to uri!!
                )
            )
        }

    private val resultLauncherPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            var invalidCountPermission = ZERO
            permissions.entries.forEach {
                val isGranted = it.value
                if (!isGranted)
                    invalidCountPermission++
            }

            if (invalidCountPermission >= ONE)
                showWarning(
                    message = getString(R.string.message_error_permission_change_image_profile),
                    onClick = { onBackPressed() })
            else {
                when (getImageFrom) {
                    MENU_CAMERA -> {
                        try {
                            lifecycleScope.launchWhenStarted {
                                getTempFileUri(IMAGE_PROFILE,
                                    BuildConfig.APPLICATION_ID).let { uri ->
                                    latestTempUri = uri
                                    openCamera.launch(uri)
                                }
                            }
                        } catch (e: IOException) {
                            Logger.e(e.message.toString())
                            showError(
                                message = e.message.toString(),
                                onClick = { onBackPressed() })
                        }
                    }
                    MENU_GALLERY -> {
                        openGallery.launch(IMAGE_FORMAT_GALLERY)
                    }
                    else -> {}
                }
            }
        }

    private val resultLauncherLocationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            var invalidCountPermission = ZERO
            permissions.entries.forEach {
                val isGranted = it.value
                if (!isGranted)
                    invalidCountPermission++
            }

            if (invalidCountPermission >= ONE)
                showWarning(
                    message = getString(R.string.message_error_permission_location),
                    onClick = { onBackPressed() })
            else goTo(
                MapsActivity()
            )
        }

    override fun onBackPressed() {
        if (crud != UPDATE_IMAGE ||
            (crud == UPDATE_IMAGE && imageResources?.size!! >= TWO)) {
            onBackPressed(
                getNameOfActivity(FORM_CRIME_LOCATION),
                isAfterCRUD
            )
            super.onBackPressed()
        }
        else {
            if ((crud == UPDATE_IMAGE && imageResources?.size!! < TWO))
                showWarning(
                    message = getString(
                        R.string.label_plus_two_string,
                        getString(
                            R.string.message_error_empty,
                            getString(
                                R.string.label_plus_two_string,
                                getString(R.string.label_photo),
                                getString(R.string.crime_location_menu)
                            )
                        ), getString(
                            R.string.message_minimum_upload_image,
                            ONE.toString(),
                            getString(R.string.label_photo),
                            getString(R.string.crime_location_menu)
                        )
                    )
                )
        }
    }

    private val crimeLocationAdd = Observer<DataResource<CrimeLocationResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_add_append,
                            getString(R.string.crime_location_menu)
                        )
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = CREATE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_add,
                            getString(R.string.crime_location_menu)
                        ),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            binding.apply {
                                tvLatitude.text = makeSpannable(
                                    text = getString(
                                        R.string.label_not_yet, getString(R.string.label_latitude)
                                    )
                                )
                                tvLongitude.text = makeSpannable(
                                    text = getString(
                                        R.string.label_not_yet, getString(R.string.label_longitude)
                                    )
                                )
                                tieName.text?.clear()
                                tieDescription.text?.clear()
                            }
                            initializeUploadImage()
                            resetParam(MenuAreaType.MENU_NONE,
                                EMPTY_STRING,
                                resetProvince = true,
                                resetCity = true,
                                resetSubDistrict = true,
                                resetUrbanVillage = true
                            )
                            parentAreaProvince?.apply {
                                setContent(
                                    getString(
                                        R.string.label_not_yet,
                                        getString(R.string.label_address)
                                    )
                                )
                            }
                            address = null
                            latitude = null
                            longitude = null
                            addImageAdapter?.apply {
                                setImageResources(imageResources = imageResources)
                            }
                        }
                    )
                }
            }
            ERROR -> {
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun crimeLocationAdd(crimeLocation: CrimeLocation?, files: ArrayList<File>?) {
        if (networkConnected()) {
            viewModel.crimeLocationAdd(crimeLocation, files)
                .observe(this, crimeLocationAdd)
        }
    }

    private val crimeLocationUpdate = Observer<DataResource<CrimeLocationResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_edit_append,
                            getString(R.string.crime_location_menu)
                        )
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = UPDATE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_add,
                            getString(R.string.crime_location_menu)
                        ),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            onBackPressed()
                        }
                    )
                }
            }
            ERROR -> {
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun crimeLocationUpdate(crimeLocation: CrimeLocation?) {
        if (networkConnected()) {
            viewModel.crimeLocationUpdate(crimeLocation)
                .observe(this, crimeLocationUpdate)
        }
    }

    private val crimeLocationDeleteImage = Observer<DataResource<MessageResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(R.string.label_delete_append,
                            getString(R.string.label_plus_two_string,
                                getString(R.string.label_photo),
                                getString(R.string.crime_location_menu)))
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = DELETE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_delete,
                            getString(R.string.label_plus_two_string,
                                getString(R.string.label_photo),
                                getString(R.string.crime_location_menu))
                        ),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            imageResources?.remove(deleteImageResource)
                            addImageAdapter?.apply {
                                setImageResources(imageResources = imageResources)
                            }
                            deleteImageResource = null
                        }
                    )
                }
            }
            ERROR -> {
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun crimeLocationDeleteImage(crimeLocation: CrimeLocation?) {
        if (networkConnected()) {
            viewModel.crimeLocationDeleteImage(crimeLocation)
                .observe(this, crimeLocationDeleteImage)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val crimeLocationAddImage = Observer<DataResource<CrimeLocationResponse>> { it ->
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(R.string.label_add_append,
                            getString(R.string.label_plus_two_string,
                                getString(R.string.label_photo),
                                getString(R.string.crime_location_menu)))
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    imageResources?.clear()
                    initializeUploadImage()
                    it.data?.crimeLocation?.imageCrimeLocations?.forEach { image ->
                        imageResources?.add(
                            ImageResource(
                                imageId = image.imageCrimeLocationId,
                                imageState = IS_IMAGE,
                                imagePath = image.imageCrimeLocationName
                            )
                        )
                    }
                    isAfterCRUD = UPDATE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_add,
                            getString(R.string.label_plus_two_string,
                                getString(R.string.label_photo),
                                getString(R.string.crime_location_menu))
                        ),
                        message = it.data?.message?.message,
                        onClick = {
                            addImageAdapter?.apply {
                                setImageResources(imageResources = imageResources)
                                notifyDataSetChanged()
                            }
                            dismissNotification()
                        }
                    )
                }
            }
            ERROR -> {
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun crimeLocationAddImage(crimeLocation: CrimeLocation?, files: ArrayList<File>?) {
        if (networkConnected()) {
            viewModel.crimeLocationAddImage(crimeLocation, files)
                .observe(this, crimeLocationAddImage)
        }
    }

    private fun initializeUploadImage() {
        imageResources?.clear()
        imageResources = arrayListOf(
            ImageResource().apply {
                imageState = ADD_IMAGE
            }
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeForm() {
        initializeUploadImage()
        addImageAdapter = AddImageAdapter(
            this
        ).apply {
            setImageResources(imageResources = imageResources)
            notifyDataSetChanged()
            onClickAdd = {
                if (imageResources?.size!! >= (maxUploadImage + ONE))
                    showInformation(
                        titleNotification = getString(R.string.crime_location_menu),
                        message = getString(
                            R.string.message_overload_upload_image,
                            getString(R.string.label_plus_two_string,
                                getString(R.string.label_photo),
                                getString(R.string.crime_location_menu)),
                            maxUploadImage.toString(),
                            getString(R.string.label_photo)
                        )
                    )
                else showBottomOption(
                    getString(R.string.label_get_image_from),
                    imageMenus(),
                    onClickMenu = {
                        getImageFrom = it.menuSetting
                        resultLauncherPermission.launch(APP_PERMISSION_GET_IMAGE)
                        dismissBottomOption()
                    })
            }
            onClickPreviewImage = {
                showImagePreview(it.imageUri)
            }
            onClickDelete = {
                showOption(
                    titleOption = getString(
                        R.string.message_title_delete,
                        getString(R.string.label_plus_two_string,
                            getString(R.string.label_photo),
                            getString(R.string.crime_location_menu))
                    ),
                    message = getString(
                        R.string.message_delete,
                        getString(R.string.label_photo),
                        getString(R.string.crime_location_menu)
                    ),
                    titlePositive = getString(R.string.yes_delete),
                    titleNegative = getString(R.string.no),
                    colorButtonPositive = R.color.red,
                    onPositive = {
                        this@FormCrimeLocationActivity.imageResources?.remove(it)
                        addImageAdapter.apply {
                            setImageResources(imageResources = imageResources)
                        }
                        dismissOption()
                    },
                )
            }
        }
        binding.apply {
            parentAreaProvince = ParentArea().apply {
                init(this@FormCrimeLocationActivity, iParentAreaProvince)
                title = getString(
                    R.string.choose,
                    getString(R.string.province_menu)
                )
                content = getString(
                    R.string.label_not_yet,
                    getString(R.string.province_menu)
                )
                iconLeftAction = R.drawable.ic_round_dashboard_24
                parentAreaAction = PARENT_AREA_ACTION_LEFT
                onActionLeft = {
                    getParentArea(MENU_AREA_PROVINCE_ID, EMPTY_STRING)
                }
                create()
            }
            parentAreaCity = ParentArea().apply {
                init(this@FormCrimeLocationActivity, iParentAreaCity)
                title = getString(
                    R.string.choose,
                    getString(R.string.city_menu)
                )
                content = getString(
                    R.string.label_not_yet,
                    getString(R.string.city_menu)
                )
                iconRightAction = R.drawable.ic_round_dashboard_24
                parentAreaAction = PARENT_AREA_ACTION_RIGHT
                onActionRight = {
                    if (provinceAsParent != null)
                        getParentArea(MENU_AREA_CITY_ID,
                            provinceAsParent?.provinceId)
                    else showWarning(
                        message = getString(
                            R.string.message_error_empty,
                            getString(R.string.province_menu)
                        )
                    )
                }
                create()
            }
            parentAreaSubDistrict = ParentArea().apply {
                init(this@FormCrimeLocationActivity, iParentAreaSubDistrict)
                title = getString(
                    R.string.choose,
                    getString(R.string.sub_district_menu)
                )
                content = getString(
                    R.string.label_not_yet,
                    getString(R.string.sub_district_menu)
                )
                iconLeftAction = R.drawable.ic_round_dashboard_24
                parentAreaAction = PARENT_AREA_ACTION_LEFT
                onActionLeft = {
                    if (cityAsParent != null)
                        getParentArea(MENU_AREA_SUB_DISTRICT_ID,
                            cityAsParent?.cityId)
                    else showWarning(
                        message = getString(
                            R.string.message_error_empty,
                            getString(R.string.city_menu)
                        )
                    )
                }
                create()
            }
            parentAreaUrbanVillage = ParentArea().apply {
                init(this@FormCrimeLocationActivity, iParentAreaUrbanVillage)
                title = getString(
                    R.string.choose,
                    getString(R.string.urban_village_menu)
                )
                content = getString(
                    R.string.label_not_yet,
                    getString(R.string.urban_village_menu)
                )
                iconRightAction = R.drawable.ic_round_dashboard_24
                parentAreaAction = PARENT_AREA_ACTION_RIGHT
                onActionRight = {
                    if (subDistrictAsParent != null)
                        getParentArea(MENU_AREA_URBAN_VILLAGE_ID,
                            subDistrictAsParent?.subDistrictId)
                    else showWarning(
                        message = getString(
                            R.string.message_error_empty,
                            getString(R.string.sub_district_menu)
                        )
                    )
                }
                create()
            }
            parentAreaLocation = ParentArea().apply {
                init(this@FormCrimeLocationActivity, iParentAreaAddAddress)
                title = getString(
                    R.string.choose,
                    getString(R.string.label_address)
                )
                content = getString(
                    R.string.label_not_yet,
                    getString(R.string.label_address)
                )
                iconRightAction = R.drawable.ic_round_location_on_24
                parentAreaAction = PARENT_AREA_ACTION_RIGHT
                onActionRight = {
                    resultLauncherLocationPermission.launch(APP_PERMISSION_LOCATION)
                }
                create()
            }

            tvLatitude.text = makeSpannable(
                text = getString(
                    R.string.label_not_yet, getString(R.string.label_latitude)
                )
            )
            tvLongitude.text = makeSpannable(
                text = getString(
                    R.string.label_not_yet, getString(R.string.label_longitude)
                )
            )

            tilName.hint = getString(
                R.string.label_enter_of,
                getString(R.string.label_name),
                getString(R.string.crime_location_menu)
            )
            tilDescription.hint = getString(
                R.string.label_enter_of,
                getString(R.string.label_description),
                getString(R.string.crime_location_menu)
            )

            btnSubmitCrimeLocation.text = getString(
                R.string.label_add,
                getString(R.string.crime_location_menu)
            )
            btnBackCrimeLocation.text = getString(R.string.back)

            rvPhoto.apply {
                layoutManager = LinearLayoutManager(
                    this@FormCrimeLocationActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = addImageAdapter
            }
        }
    }

    private fun initializeEditForm(crimeLocation: CrimeLocation?) {
        binding.apply {
            parentAreaProvince = ParentArea().apply {
                provinceAsParent = crimeLocation?.province
                init(this@FormCrimeLocationActivity, iParentAreaProvince)
                title = getString(
                    R.string.message_area_registration,
                    getString(R.string.crime_location_menu),
                    getString(R.string.province_menu)
                )
                content = getContentArea(
                    this@FormCrimeLocationActivity,
                    MENU_AREA_PROVINCE_ID,
                    crimeLocation?.province,
                    getString(R.string.province_menu)
                )
                iconLeftAction = R.drawable.ic_round_edit_24
                parentAreaAction = PARENT_AREA_ACTION_LEFT
                onActionLeft = {
                    getParentArea(MENU_AREA_PROVINCE_ID, EMPTY_STRING)
                }
                create()
            }
            parentAreaCity = ParentArea().apply {
                cityAsParent = crimeLocation?.city
                init(this@FormCrimeLocationActivity, iParentAreaCity)
                title = getString(
                    R.string.message_area_registration,
                    getString(R.string.crime_location_menu),
                    getString(R.string.city_menu)
                )
                content = getContentArea(
                    this@FormCrimeLocationActivity,
                    MENU_AREA_CITY_ID,
                    crimeLocation?.city,
                    getString(R.string.city_menu)
                )
                iconRightAction = R.drawable.ic_round_edit_24
                parentAreaAction = PARENT_AREA_ACTION_RIGHT
                onActionRight = {
                    getParentArea(MENU_AREA_CITY_ID,
                        provinceAsParent?.provinceId)
                }
                create()
            }
            parentAreaSubDistrict = ParentArea().apply {
                subDistrictAsParent = crimeLocation?.subDistrict
                init(this@FormCrimeLocationActivity, iParentAreaSubDistrict)
                title = getString(
                    R.string.message_area_registration,
                    getString(R.string.crime_location_menu),
                    getString(R.string.sub_district_menu)
                )
                content = getContentArea(
                    this@FormCrimeLocationActivity,
                    MENU_AREA_SUB_DISTRICT_ID,
                    crimeLocation?.subDistrict,
                    getString(R.string.sub_district_menu)
                )
                iconLeftAction = R.drawable.ic_round_edit_24
                parentAreaAction = PARENT_AREA_ACTION_LEFT
                onActionLeft = {
                    getParentArea(MENU_AREA_SUB_DISTRICT_ID,
                        cityAsParent?.cityId)
                }
                create()
            }
            parentAreaUrbanVillage = ParentArea().apply {
                urbanVillageAsParent = crimeLocation?.urbanVillage
                init(this@FormCrimeLocationActivity, iParentAreaUrbanVillage)
                title = getString(
                    R.string.message_area_registration,
                    getString(R.string.crime_location_menu),
                    getString(R.string.urban_village_menu)
                )
                content = getContentArea(
                    this@FormCrimeLocationActivity,
                    MENU_AREA_URBAN_VILLAGE_ID,
                    crimeLocation?.urbanVillage,
                    getString(R.string.urban_village_menu)
                )
                iconRightAction = R.drawable.ic_round_edit_24
                parentAreaAction = PARENT_AREA_ACTION_RIGHT
                onActionRight = {
                    getParentArea(MENU_AREA_URBAN_VILLAGE_ID,
                        subDistrictAsParent?.subDistrictId)
                }
                create()
            }
            parentAreaLocation = ParentArea().apply {
                init(this@FormCrimeLocationActivity, iParentAreaAddAddress)
                title = getString(R.string.label_address)
                content = crimeLocation?.crimeMapsAddress
                iconRightAction = R.drawable.ic_round_location_on_24
                parentAreaAction = PARENT_AREA_ACTION_RIGHT
                onActionRight = {

                }
                create()
            }

            tvLatitude.text = makeSpannable(
                text = getString(
                    R.string.label_latitude_of,
                    crimeLocation?.crimeMapsLatitude
                )
            )
            tvLongitude.text = makeSpannable(
                text = getString(
                    R.string.label_longitude_of,
                    crimeLocation?.crimeMapsLongitude
                )
            )

            tilName.hint = getString(
                R.string.label_plus_two_string,
                getString(R.string.label_name),
                getString(R.string.crime_location_menu)
            )
            tieName.setText(crimeLocation?.crimeMapsName)
            tilDescription.hint = getString(
                R.string.label_plus_two_string,
                getString(R.string.label_description),
                getString(R.string.crime_location_menu)
            )
            tieDescription.setText(crimeLocation?.crimeMapsDescription)

            btnSubmitCrimeLocation.text = getString(
                R.string.label_edit,
                getString(R.string.crime_location_menu)
            )
            btnBackCrimeLocation.text = getString(R.string.cancel)
            rvPhoto.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeEditImage(crimeLocation: CrimeLocation?) {
        initializeUploadImage()
        crimeLocation?.imageCrimeLocations?.forEach {
            imageResources?.add(
                ImageResource(
                    imageId = it.imageCrimeLocationId,
                    imageState = IS_IMAGE,
                    imagePath = it.imageCrimeLocationName
                )
            )
        }
        addImageAdapter = AddImageAdapter(
            this
        ).apply {
            customParentPath = PreferencesManager
                .getInstance(this@FormCrimeLocationActivity)
                .getPreferences(URL_CONNECTION_API_IMAGE_CRIME_LOCATION)
            useMaxWidth = true
            setImageResources(imageResources = imageResources)
            notifyDataSetChanged()
            onClickAdd = {
                if (imageResources?.size!! >= (maxUploadImage + ONE))
                    showInformation(
                        titleNotification = getString(R.string.crime_location_menu),
                        message = getString(
                            R.string.message_overload_upload_image,
                            getString(R.string.label_plus_two_string,
                                getString(R.string.label_photo),
                                getString(R.string.crime_location_menu)),
                            maxUploadImage.toString(),
                            getString(R.string.label_photo)
                        )
                    )
                else showBottomOption(
                    getString(R.string.label_get_image_from),
                    imageMenus(),
                    onClickMenu = {
                        getImageFrom = it.menuSetting
                        resultLauncherPermission.launch(APP_PERMISSION_GET_IMAGE)
                        dismissBottomOption()
                    })
            }
            onClickPreviewImage = {
                showImagePreview(it.imagePath,
                    PreferencesManager
                        .getInstance(this@FormCrimeLocationActivity)
                        .getPreferences(URL_CONNECTION_API_IMAGE_CRIME_LOCATION))
            }
            onClickDelete = {
                showOption(
                    titleOption = getString(
                        R.string.message_title_delete,
                        getString(R.string.label_plus_two_string,
                            getString(R.string.label_photo),
                            getString(R.string.crime_location_menu))
                    ),
                    message = getString(
                        R.string.message_delete,
                        getString(R.string.label_photo),
                        getString(R.string.crime_location_menu)
                    ),
                    titlePositive = getString(R.string.yes_delete),
                    titleNegative = getString(R.string.no),
                    colorButtonPositive = R.color.red,
                    onPositive = {
                        deleteImageResource = it
                        crimeLocationDeleteImage(CrimeLocation().apply {
                            crimeLocationId = crimeLocation?.crimeLocationId
                            imageCrimeLocations = arrayListOf(
                                ImageCrimeLocation(
                                    imageCrimeLocationId = it.imageId
                                )
                            )
                            updatedBy = Admin().apply { adminId = admin?.adminId }
                        })
                        dismissOption()
                    },
                )
            }
        }
        binding.apply {
            dismissView(
                arrayOf(
                    iParentAreaProvince.root,
                    iParentAreaCity.root,
                    iParentAreaSubDistrict.root,
                    iParentAreaUrbanVillage.root,
                    iParentAreaAddAddress.root,
                    llLatLang,
                    tilName,
                    tilDescription,
                    btnSubmitCrimeLocation
                )
            )

            rvPhoto.apply {
                layoutManager = GridLayoutManager(
                    this@FormCrimeLocationActivity, ROW_MENU_AREA)
                adapter = addImageAdapter
            }
            btnBackCrimeLocation.text = getString(R.string.back)
        }
    }

    private fun submitCrimeLocation() {
        if (provinceAsParent != null) {
            if (cityAsParent != null) {
                if (subDistrictAsParent != null) {
                    if (urbanVillageAsParent != null) {
                        if (!latitude?.isEmpty()!! && !longitude?.isEmpty()!!) {
                            if (!address?.isEmpty()!!) {
                                if (!textInputEditTextIsEmpty(binding.tieName)) {
                                    if (!textInputEditTextIsEmpty(binding.tieDescription))
                                    {
                                        when (crud) {
                                            CREATE -> {
                                                if (imageResources?.size!! >= TWO) {
                                                    val files: ArrayList<File> = ArrayList()
                                                    for (image in imageResources!!) {
                                                        if (image.imageState == IS_IMAGE)
                                                            image.imageFile?.let { files.add(it) }
                                                    }
                                                    crimeLocationAdd(
                                                        CrimeLocation().apply {
                                                            province = Province().apply {
                                                                provinceId = provinceAsParent?.provinceId
                                                            }
                                                            city = City().apply {
                                                                cityId = cityAsParent?.cityId
                                                            }
                                                            subDistrict = SubDistrict().apply {
                                                                subDistrictId =
                                                                    subDistrictAsParent?.subDistrictId
                                                            }
                                                            urbanVillage = UrbanVillage().apply {
                                                                urbanVillageId =
                                                                    urbanVillageAsParent?.urbanVillageId
                                                            }
                                                            crimeMapsName =
                                                                binding.tieName.text.toString().trim()
                                                            crimeMapsAddress = address
                                                            crimeMapsDescription =
                                                                binding.tieDescription.text.toString()
                                                                    .trim()
                                                            crimeMapsLatitude = latitude
                                                            crimeMapsLongitude = longitude
                                                            createdBy = Admin().apply { adminId = admin?.adminId }
                                                        },
                                                        files
                                                    )
                                                } else showWarning(
                                                    message = getString(
                                                        R.string.label_plus_two_string,
                                                        getString(
                                                            R.string.message_error_empty,
                                                            getString(
                                                                R.string.label_plus_two_string,
                                                                getString(R.string.label_photo),
                                                                getString(R.string.crime_location_menu)
                                                            )
                                                        ), getString(
                                                            R.string.message_minimum_upload_image,
                                                            ONE.toString(),
                                                            getString(R.string.label_photo),
                                                            getString(R.string.crime_location_menu)
                                                        )
                                                    )
                                                )
                                            }
                                            UPDATE -> {
                                                crimeLocationUpdate(
                                                    CrimeLocation().apply {
                                                        crimeLocationId = crimeLocation?.crimeLocationId
                                                        province = Province().apply {
                                                            provinceId = provinceAsParent?.provinceId
                                                        }
                                                        city = City().apply {
                                                            cityId = cityAsParent?.cityId
                                                        }
                                                        subDistrict = SubDistrict().apply {
                                                            subDistrictId =
                                                                subDistrictAsParent?.subDistrictId
                                                        }
                                                        urbanVillage = UrbanVillage().apply {
                                                            urbanVillageId =
                                                                urbanVillageAsParent?.urbanVillageId
                                                        }
                                                        crimeMapsName =
                                                            binding.tieName.text.toString().trim()
                                                        crimeMapsAddress = address
                                                        crimeMapsDescription =
                                                            binding.tieDescription.text.toString()
                                                                .trim()
                                                        crimeMapsLatitude = latitude
                                                        crimeMapsLongitude = longitude
                                                        updatedBy = Admin().apply { adminId = admin?.adminId }
                                                    }
                                                )
                                            }
                                            else -> {}
                                        }
                                    } else showWarning(
                                        message = getString(
                                            R.string.message_error_empty,
                                            getString(
                                                R.string.label_plus_two_string,
                                                getString(R.string.label_description),
                                                getString(R.string.crime_location_menu)
                                            )
                                        )
                                    )
                                } else showWarning(
                                    message = getString(
                                        R.string.message_error_empty,
                                        getString(
                                            R.string.label_plus_two_string,
                                            getString(R.string.label_name),
                                            getString(R.string.crime_location_menu)
                                        )
                                    )
                                )
                            } else showWarning(
                                message = getString(
                                    R.string.message_error_empty,
                                    getString(
                                        R.string.label_plus_two_string,
                                        getString(R.string.label_address),
                                        getString(R.string.crime_location_menu)
                                    )
                                )
                            )
                        } else showWarning(
                            message = getString(
                                R.string.message_error_empty,
                                getString(
                                    R.string.label_plus_two_string,
                                    getString(R.string.label_latitude),
                                    getString(R.string.label_longitude)
                                )
                            )
                        )
                    } else showWarning(
                        message = getString(
                            R.string.message_error_empty, getString(R.string.urban_village_menu)
                        )
                    )
                } else showWarning(
                    message = getString(
                        R.string.message_error_empty, getString(R.string.sub_district_menu)
                    )
                )
            } else showWarning(
                message = getString(
                    R.string.message_error_empty, getString(R.string.city_menu)
                )
            )
        } else showWarning(
            message = getString(
                R.string.message_error_empty, getString(R.string.province_menu)
            )
        )
    }

    private fun getParentArea(menuAreaType: MenuAreaType?, parentId: String?) {
        goTo(
            ListOfAreaActivity(),
            hashMapOf(
                FROM_ACTIVITY to getNameOfActivity(FORM_CRIME_LOCATION),
                AREA to menuAreaType!!,
                PARENT_AREA to parentId!!
            )
        )
    }

    private fun resetParam(
        menuAreaType: MenuAreaType?,
        areaName: String?,
        resetProvince: Boolean?,
        resetCity: Boolean?,
        resetSubDistrict: Boolean?,
        resetUrbanVillage: Boolean?
    ) {
        when (menuAreaType) {
            MENU_AREA_PROVINCE_ID -> {
                parentAreaProvince?.apply {
                    setContent(areaName)
                }
            }
            MENU_AREA_CITY_ID -> {
                parentAreaCity?.apply {
                    setContent(areaName)
                }
            }
            MENU_AREA_SUB_DISTRICT_ID -> {
                parentAreaSubDistrict?.apply {
                    setContent(areaName)
                }
            }
            MENU_AREA_URBAN_VILLAGE_ID -> {
                parentAreaUrbanVillage?.apply {
                    setContent(areaName)
                }
            }
            else -> {}
        }

        if (resetProvince!!) {
            provinceAsParent = null
            parentAreaProvince?.apply {
                setContent(
                    getString(
                        R.string.label_not_yet,
                        getString(R.string.province_menu)
                    )
                )
            }
        }

        if (resetCity!!) {
            cityAsParent = null
            parentAreaCity?.apply {
                setContent(
                    getString(
                        R.string.label_not_yet,
                        getString(R.string.city_menu)
                    )
                )
            }
        }

        if (resetSubDistrict!!) {
            subDistrictAsParent = null
            parentAreaSubDistrict?.apply {
                setContent(
                    getString(
                        R.string.label_not_yet,
                        getString(R.string.sub_district_menu)
                    )
                )
            }
        }

        if (resetUrbanVillage!!) {
            urbanVillageAsParent = null
            parentAreaUrbanVillage?.apply {
                setContent(
                    getString(
                        R.string.label_not_yet,
                        getString(R.string.urban_village_menu)
                    )
                )
            }
        }
    }
}