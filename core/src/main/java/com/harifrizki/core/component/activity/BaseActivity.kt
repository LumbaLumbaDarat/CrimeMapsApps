package com.harifrizki.core.component.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText
import com.harifrizki.core.R
import com.harifrizki.core.component.*
import com.harifrizki.core.component.imageslider.ImageSlider
import com.harifrizki.core.data.remote.ERROR_CODE_200_SUCCESS
import com.harifrizki.core.data.remote.ERROR_CODE_404_PAGE_NOT_FOUND
import com.harifrizki.core.data.remote.response.ErrorResponse
import com.harifrizki.core.databinding.*
import com.harifrizki.core.model.Admin
import com.harifrizki.core.model.ImageCrimeLocation
import com.harifrizki.core.model.Menu
import com.harifrizki.core.model.Message
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.ActivityName.*
import com.harifrizki.core.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.core.utils.CRUD.*
import com.harifrizki.core.utils.Error.IS_API_RESPONSE
import com.harifrizki.core.utils.Error.IS_NO_NETWORK
import com.harifrizki.core.utils.MenuAreaType.*
import com.harifrizki.core.utils.MenuSetting.*
import com.harifrizki.core.utils.NotificationType.*
import com.lumbalumbadrt.colortoast.ColorToast
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import java.io.File

open class BaseActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val loading by lazy {
        Loading()
    }
    private val imagePreview by lazy {
        ImagePreview()
    }
    private val notification by lazy {
        Notification()
    }
    private val option by lazy {
        Option()
    }
    private val optionList by lazy {
        OptionList()
    }
    private val bottomInput by lazy {
        BottomInput()
    }
    private val bottomOption by lazy {
        BottomOption()
    }
    private val search by lazy {
        Search()
    }
    private val empty by lazy {
        Empty()
    }
    private val notificationAndOptionMessage by lazy {
        NotificationAndOptionMessage()
    }
    private val imageSlider: ImageSlider by lazy {
        ImageSlider()
    }

    private var context: Context? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    private var ties: Array<TextInputEditText>? = null
    private var passwordTies: Array<TextInputEditText>? = null

    private var rootView: View? = null
    private var sfl: ShimmerFrameLayout? = null

    fun create(
        buildContext: Context,
        activityResultLauncher: ActivityResultLauncher<Intent>
    ) {
        this.apply {
            context = buildContext
            resultLauncher = activityResultLauncher
        }

        Logger.addLogAdapter(AndroidLogAdapter())

        createLoading()
        createImagePreview()
        createNotification()
        createOption()
        createOptionList()
    }

    override fun onRefresh() {
    }

    fun onBackPressed(
        map: HashMap<String, Any>?,
        directOnBackPressed: Boolean? = false
    ) {
        Intent().apply {
            putExtra(INTENT_DATA, map)
            setResult(RESULT_OK, this)
        }
        if (directOnBackPressed!!) onBackPressed()
    }

    fun onBackPressed(fromActivity: String?, crud: CRUD?) {
        onBackPressed(
            hashMapOf(
                FROM_ACTIVITY to fromActivity!!,
                OPERATION_CRUD to crud!!
            )
        )
    }

    fun onBackPressed(fromActivity: String?, crud: CRUD?, menuAreaType: MenuAreaType?) {
        onBackPressed(
            hashMapOf(
                FROM_ACTIVITY to fromActivity!!,
                OPERATION_CRUD to crud!!,
                AREA to menuAreaType!!
            )
        )
    }

    private fun createLoading() {
        loading.apply {
            create(context)
        }
    }

    private fun createImagePreview() {
        imagePreview.apply {
            create(context)
        }
    }

    private fun createNotification() {
        notification.apply {
            create(context)
        }
    }

    private fun createOption() {
        option.apply {
            create(context)
        }
    }

    private fun createOptionList() {
        optionList.apply {
            create(context)
        }
    }

    fun createEmpty(binding: EmptyBinding?) {
        empty.create(binding)
    }

    fun createNotificationAndOptionMessage(binding: NotificationAndOptionMessageBinding?) {
        notificationAndOptionMessage.create(context, binding)
    }

    fun createImageSlider(binding: ImageSliderBinding?) {
        imageSlider.create(binding)
    }

    fun createSearch(binding: SearchBinding?) {
        search.create(binding)
    }

    fun createRootView(view: View?) {
        this.rootView = view
    }

    fun createRootView(view: View?, sfl: ShimmerFrameLayout?) {
        this.rootView = view
        this.sfl = sfl
    }

    fun appBar(
        binding: AppBarBinding?,
        title: String?,
        iconBar: Int?,
        iconBarColor: Int?,
        iconBarBackground: Int?
    ) {
        binding?.apply {
            ivIconAppBar.apply {
                iconBar?.let { setImageResource(it) }
                iconBarColor?.let {
                    ContextCompat.getColor(
                        context!!,
                        it
                    )
                }?.let {
                    setColorFilter(
                        it, android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                }
                iconBarBackground?.let { setBackgroundResource(it) }
            }
            tvTitleAppBar.text = title
            ivBtnRightAppBar.visibility = View.INVISIBLE
        }
    }

    fun appBar(
        binding: AppBarBinding?,
        title: String?,
        iconBar: Int?,
        iconBarColor: Int?,
        iconBarBackground: Int?,
        iconBarRight: Int?,
        iconBarRightColor: Int?,
        iconBarRightBackground: Int?,
        onClick: (() -> Unit)?
    ) {
        binding?.apply {
            ivIconAppBar.apply {
                iconBar?.let { setImageResource(it) }
                iconBarColor?.let {
                    ContextCompat.getColor(
                        context!!,
                        it
                    )
                }?.let {
                    setColorFilter(
                        it, android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                }
                iconBarBackground?.let { setBackgroundResource(it) }
            }
            tvTitleAppBar.text = title
            ivBtnRightAppBar.apply {
                visibility = View.VISIBLE
                iconBarRight?.let { setImageResource(it) }
                iconBarRightColor?.let {
                    ContextCompat.getColor(
                        context!!,
                        it
                    )
                }?.let {
                    setColorFilter(
                        it, android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                }
                iconBarRightBackground?.let { setBackgroundResource(it) }
                setOnClickListener {
                    onClick?.invoke()
                }
            }
        }
    }

    fun scrollToUp(nestedScrollView: NestedScrollView?) {
        nestedScrollView?.fullScroll(View.FOCUS_UP)
    }

    fun useScrollForRefresh(use: Boolean?) {

    }

    fun showRootView() {
        rootView?.visibility = View.VISIBLE
    }

    fun showView(views: Array<View>?) {
        views?.forEach {
            it.visibility = View.VISIBLE
        }
    }

    fun dismissRootView() {
        rootView?.visibility = View.GONE
    }

    fun dismissView(views: Array<View>?) {
        views?.forEach {
            it.visibility = View.GONE
        }
    }

    fun networkConnected(isGoToErrorActivity: Boolean = true): Boolean {
        return if (context?.let { isNetworkConnected(it) } == true)
            true
        else {
            if (isGoToErrorActivity) {
                goTo(
                    ConnectionErrorActivity(),
                    hashMapOf(
                        ERROR_STATE to IS_NO_NETWORK
                    )
                )
            }
            false
        }
    }

    fun settingMenus(): ArrayList<Menu> {
        val menus: ArrayList<Menu> = ArrayList()
        menus.add(
            Menu(
                MENU_SETTING_DEBUG_MODE,
                context?.getString(R.string.setting_debug_mode_menu),
                ZERO,
                LOTTIE_DEBUG_MODE_JSON,
                context?.resources?.getBoolean(R.bool.app_debug_mode),
                ZERO,
                R.color.white,
                R.drawable.button_primary_ripple_white
            )
        )
        menus.add(
            Menu(
                MENU_SETTING_PROFILE,
                context?.getString(R.string.setting_profile_menu),
                R.drawable.ic_round_account_circle_24,
                EMPTY_STRING,
                true,
                R.color.white,
                R.color.white,
                R.drawable.button_primary_ripple_white
            )
        )
        menus.add(
            Menu(
                MENU_SETTING_EXIT,
                context?.getString(R.string.setting_exit_apps_menu),
                R.drawable.ic_round_exit_to_app_24,
                EMPTY_STRING,
                true,
                R.color.white,
                R.color.white,
                R.drawable.button_primary_ripple_white
            )
        )
        return menus
    }

    fun imageMenus(): ArrayList<Menu> {
        val menus: ArrayList<Menu> = ArrayList()
        menus.add(
            Menu(
                MENU_CAMERA,
                context?.getString(R.string.setting_camera_menu),
                R.drawable.ic_round_photo_camera_24,
                EMPTY_STRING,
                true,
                R.color.primary,
                R.color.primary,
                R.drawable.button_transparent_ripple_primary
            )
        )
        menus.add(
            Menu(
                MENU_GALLERY,
                context?.getString(R.string.setting_gallery_menu),
                R.drawable.ic_round_folder_24,
                EMPTY_STRING,
                true,
                R.color.primary,
                R.color.primary,
                R.drawable.button_transparent_ripple_primary
            )
        )
        return menus
    }

    fun prepareResetTextInputEditText(ties: Array<TextInputEditText>?) {
        this.ties = ties
    }

    fun resetTextInputEditText() {
        ties?.forEach {
            it.text?.clear()
        }
    }

    fun resetTextInputEditText(ties: Array<TextInputEditText>?) {
        ties?.forEach {
            it.text?.clear()
        }
    }

    fun prepareShowAndHidePassword(passwordTies: Array<TextInputEditText>?) {
        this.passwordTies = passwordTies
    }

    fun showAndHide(
        imageView: ImageView?,
        isHide: Boolean?,
        tvMessageShowHide: TextView? = null
    ): Boolean {
        return if (!isHide!!) {
            imageView?.setImageResource(R.drawable.ic_round_visibility_24)
            passwordTies?.forEach {
                it.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            tvMessageShowHide?.text = getString(R.string.label_show_password)
            true
        } else {
            imageView?.setImageResource(R.drawable.ic_round_visibility_off_24)
            passwordTies?.forEach {
                it.transformationMethod = null
            }
            tvMessageShowHide?.text = getString(R.string.label_hide_password)
            false
        }
    }

    fun showAndHide(
        imageButton: ImageButton?,
        isHide: Boolean?,
        tvMessageShowHide: TextView? = null
    ): Boolean {
        return if (!isHide!!) {
            imageButton?.setImageResource(R.drawable.ic_round_visibility_24)
            passwordTies?.forEach {
                it.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            tvMessageShowHide?.text = getString(R.string.label_show_password)
            true
        } else {
            imageButton?.setImageResource(R.drawable.ic_round_visibility_off_24)
            passwordTies?.forEach {
                it.transformationMethod = null
            }
            tvMessageShowHide?.text = getString(R.string.label_hide_password)
            false
        }
    }

    fun showLoading(message: String? = getString(R.string.message_loading_splash)) {
        loading.setMessage(true, message, Color.BLACK)
        loading.show()
    }

    fun dismissLoading() {
        loading.dismiss()
    }

    fun showImagePreview(imagePath: String?, url: String?) {
        imagePreview.apply {
            setImageToPreview(context, imagePath, url)
            show()
        }
    }

    fun showImagePreview(imageUri: Uri?) {
        imagePreview.apply {
            setImageToPreview(context, imageUri)
            show()
        }
    }

    fun dismissImagePreview() {
        imagePreview.dismiss()
    }

    fun showError(
        titleNotification: String? = context?.getString(R.string.notification_error_title),
        message: String?,
        buttonTitle: String? = context?.getString(R.string.ok),
        onClick: (() -> Unit)? = { dismissNotification() }
    ) {
        notification.apply {
            message?.let { notification(it) }
            titleNotification(titleNotification)
            buttonTitle(buttonTitle)
            notificationType(NOTIFICATION_ERROR)
            onClickButton = { onClick?.invoke() }
            show()
        }
    }

    fun showWarning(
        titleNotification: String? = context?.getString(R.string.notification_warning_title),
        message: String?,
        buttonTitle: String? = context?.getString(R.string.ok),
        onClick: (() -> Unit)? = { dismissNotification() }
    ) {
        notification.apply {
            message?.let { notification(it) }
            titleNotification(titleNotification)
            buttonTitle(buttonTitle)
            notificationType(NOTIFICATION_WARNING)
            onClickButton = { onClick?.invoke() }
            show()
        }
    }

    fun showInformation(
        titleNotification: String? = context?.getString(R.string.notification_information_title),
        message: String?,
        buttonTitle: String? = context?.getString(R.string.ok),
        onClick: (() -> Unit)? = { dismissNotification() }
    ) {
        notification.apply {
            message?.let { notification(it) }
            titleNotification(titleNotification)
            buttonTitle(buttonTitle)
            notificationType(NOTIFICATION_INFORMATION)
            onClickButton = { onClick?.invoke() }
            show()
        }
    }

    fun showSuccess(
        titleNotification: String? = context?.getString(R.string.notification_success_title),
        message: String?,
        buttonTitle: String? = context?.getString(R.string.ok),
        onClick: (() -> Unit)? = { dismissNotification() }
    ) {
        notification.apply {
            message?.let { notification(it) }
            titleNotification(titleNotification)
            buttonTitle(buttonTitle)
            notificationType(NOTIFICATION_SUCCESS)
            onClickButton = { onClick?.invoke() }
            show()
        }
    }

    fun dismissNotification() {
        notification.dismiss()
    }

    fun showOption(
        titleOption: String?,
        message: String?,
        optionAnimation: String? = LOTTIE_QUESTION_JSON,
        titlePositive: String? = context?.getString(R.string.ok),
        titleNegative: String? = context?.getString(R.string.cancel),
        colorButtonPositive: Int? = R.color.primary,
        onPositive: (() -> Unit)?,
        onNegative: (() -> Unit)? = { dismissOption() }
    ) {
        option.apply {
            titleOption(titleOption)
            message?.let { option(it) }
            optionAnimation(optionAnimation)
            buttonPositive(titlePositive)
            buttonPositive(colorButtonPositive)
            buttonNegative(titleNegative)
            onClickPositive = {
                onPositive?.invoke()
            }
            onClickNegative = {
                onNegative?.invoke()
            }
            show()
        }
    }

    fun dismissOption() {
        option.dismiss()
    }

    fun showBottomInput(
        activity: AppCompatActivity?,
        hint: String?,
        input: String?,
        titlePositive: String?,
        titleNegative: String?,
        onPositive: ((String?) -> Unit)?,
        onNegative: (() -> Unit?) = { dismissBottomInput() }
    ) {
        bottomInput.apply {
            this.activity = activity
            this.hint = hint
            this.input = input
            buttonPositive = titlePositive
            buttonNegative = titleNegative
            onClickPositive = {
                onPositive?.invoke(it)
            }
            onClickNegative = {
                onNegative.invoke()
            }
            show(supportFragmentManager, null)
        }
    }

    fun dismissBottomInput() {
        bottomInput.dismiss()
    }

    fun showOptionList(
        view: View?,
        menus: ArrayList<Menu>?,
        background: Int?,
        xPosition: Int?,
        yPosition: Int?,
        onClickMenu: ((Menu) -> Unit)?
    ) {
        optionList.apply {
            setBackground(background?.let { context?.getDrawable(it) })
            setMenus(menus)
            show(view, xPosition, yPosition)
            onClick = {
                it?.let { menu -> onClickMenu?.invoke(menu) }
            }
        }
    }

    fun dismissOptionList() {
        optionList.dismiss()
    }

    fun showBottomOption(
        title: String?,
        menus: ArrayList<Menu>?,
        onClickMenu: ((Menu) -> Unit)?
    ) {
        bottomOption.apply {
            this.title = title
            options = menus
            onClick = {
                it?.let { menu -> onClickMenu?.invoke(menu) }
            }
            show(supportFragmentManager, null)
        }
    }

    fun dismissBottomOption() {
        bottomOption.dismiss()
    }

    fun showMessage(map: HashMap<String, Any>?): Boolean {
        val fromActivity = getEnumActivityName(map!![FROM_ACTIVITY].toString())
        return when (map[OPERATION_CRUD] as CRUD) {
            CREATE -> {
                when (fromActivity) {
                    PROFILE -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_add,
                                getString(R.string.admin_menu)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    LIST_OF_ADMIN -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_add,
                                getString(R.string.admin_menu)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    LIST_OF_AREA -> {
                        when (map[AREA] as MenuAreaType) {
                            MENU_AREA_PROVINCE_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_add,
                                        getString(R.string.province_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_CITY_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_add,
                                        getString(R.string.city_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_SUB_DISTRICT_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_add,
                                        getString(R.string.sub_district_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_URBAN_VILLAGE_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_add,
                                        getString(R.string.urban_village_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    }
                    FORM_AREA -> {
                        when (map[AREA] as MenuAreaType) {
                            MENU_AREA_PROVINCE_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_add,
                                        getString(R.string.province_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_CITY_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_add,
                                        getString(R.string.city_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_SUB_DISTRICT_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_add,
                                        getString(R.string.sub_district_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_URBAN_VILLAGE_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_add,
                                        getString(R.string.urban_village_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    }
                    LIST_OF_CRIME_LOCATION,
                    FORM_CRIME_LOCATION -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_add,
                                getString(R.string.crime_location_menu)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            READ -> {
                true
            }
            UPDATE -> {
                when (fromActivity) {
                    PROFILE -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_update,
                                getString(R.string.setting_profile_menu)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    PASSWORD -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_update,
                                getString(R.string.label_password)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    FORM_AREA -> {
                        when (map[AREA] as MenuAreaType) {
                            MENU_AREA_PROVINCE_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_update,
                                        getString(R.string.province_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_CITY_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_update,
                                        getString(R.string.city_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_SUB_DISTRICT_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_update,
                                        getString(R.string.sub_district_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_URBAN_VILLAGE_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_update,
                                        getString(R.string.urban_village_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    }
                    DETAIL_CRIME_LOCATION -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_update,
                                getString(R.string.crime_location_menu)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    FORM_CRIME_LOCATION -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_update,
                                getString(R.string.crime_location_menu)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            DELETE -> {
                when (fromActivity) {
                    PROFILE,
                    LIST_OF_ADMIN -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_delete,
                                getString(R.string.admin_menu)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    LIST_OF_AREA -> {
                        when (map[AREA] as MenuAreaType) {
                            MENU_AREA_PROVINCE_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_delete,
                                        getString(R.string.province_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_CITY_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_delete,
                                        getString(R.string.city_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_SUB_DISTRICT_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_delete,
                                        getString(R.string.sub_district_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            MENU_AREA_URBAN_VILLAGE_ID -> {
                                ColorToast.roundLineSuccess(
                                    this,
                                    getString(R.string.app_name),
                                    getString(
                                        R.string.message_success_delete,
                                        getString(R.string.urban_village_menu)
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    }
                    LIST_OF_CRIME_LOCATION -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_delete,
                                getString(R.string.crime_location_menu)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    DETAIL_CRIME_LOCATION -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_delete,
                                getString(R.string.crime_location_menu)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    FORM_CRIME_LOCATION -> {
                        ColorToast.roundLineSuccess(
                            this,
                            getString(R.string.app_name),
                            getString(
                                R.string.message_success_delete,
                                getString(R.string.crime_location_menu)
                            ),
                            Toast.LENGTH_LONG
                        )
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            else -> {
                false
            }
        }
    }

    fun showEmpty(
        title: String?,
        message: String?,
        animation: String? = LOTTIE_SEARCH_NOT_FOUND_JSON,
        useAnimation: Boolean? = true,
        directShow: Boolean? = false
    ) {
        empty.apply {
            title(title)
            message(message)
            animation(animation, useAnimation)
            if (directShow!!) show()
        }
    }

    fun showEmptyError(
        title: String?,
        message: String?,
        animation: String? = LOTTIE_ERROR_JSON,
        useAnimation: Boolean? = true,
        directShow: Boolean? = false
    ) {
        empty.apply {
            title(
                if (context?.resources?.getBoolean(R.bool.app_debug_mode)!!) title
                else getString(R.string.message_error_title_general)
            )
            message(
                if (context?.resources?.getBoolean(R.bool.app_debug_mode)!!) message
                else getString(R.string.message_error_general)
            )
            animation(animation, useAnimation)
            if (directShow!!) show()
        }
    }

    fun showEmpty() {
        empty.show()
    }

    fun dismissEmpty() {
        empty.dismiss()
    }

    fun showNotificationAndOptionMessage(
        title: String?,
        message: String?,
        animation: String? = LOTTIE_QUESTION_JSON,
        useAnimation: Boolean? = true,
        background: Int?,
        useOption: Boolean? = false,
        buttonNegative: String? = context?.getString(R.string.cancel),
        buttonNegativeColor: Int? = null,
        buttonPositive: String? = context?.getString(R.string.ok),
        buttonPositiveColor: Int? = null,
        onNegative: (() -> Unit)? = { dismissNotificationAndOptionMessage() },
        onPositive: (() -> Unit)? = { },
        directShow: Boolean? = false
    ) {
        notificationAndOptionMessage.apply {
            title(title)
            message(message)
            animation(animation, useAnimation)
            background(context, background)
            useButton(useOption)
            buttonNegative(buttonNegative)
            if (buttonNegativeColor != null) buttonNegative(buttonNegativeColor)
            buttonPositive(buttonPositive)
            if (buttonPositiveColor != null) buttonPositive(buttonPositiveColor)
            onClickNegative = { onNegative?.invoke() }
            onClickPositive = { onPositive?.invoke() }
            if (directShow!!) show()
        }
    }

    fun showNotificationAndOptionMessageInformation(
        title: String? = context?.getString(R.string.notification_information_title),
        message: String?,
        animation: String? = LOTTIE_INFORMATION_JSON,
        useAnimation: Boolean? = true,
        useOption: Boolean? = false,
        buttonNegative: String? = context?.getString(R.string.cancel),
        buttonPositive: String? = context?.getString(R.string.ok),
        onNegative: (() -> Unit)? = { dismissNotificationAndOptionMessage() },
        onPositive: (() -> Unit)? = { },
        directShow: Boolean? = false
    ) {
        notificationAndOptionMessage.apply {
            title(title)
            message(message)
            animation(animation, useAnimation)
            background(context, R.drawable.frame_background_secondary)
            useButton(useOption)
            buttonNegative(buttonNegative)
            buttonPositive(buttonPositive)
            onClickNegative = { onNegative?.invoke() }
            onClickPositive = { onPositive?.invoke() }
            if (directShow!!) show()
        }
    }

    fun showNotificationAndOptionMessageError(
        title: String? = context?.getString(R.string.notification_error_title),
        message: String?,
        animation: String? = LOTTIE_ERROR_JSON,
        useAnimation: Boolean? = true,
        useOption: Boolean? = false,
        buttonNegative: String? = context?.getString(R.string.cancel),
        buttonPositive: String? = context?.getString(R.string.ok),
        onNegative: (() -> Unit)? = { dismissNotificationAndOptionMessage() },
        onPositive: (() -> Unit)? = { },
        directShow: Boolean? = false
    ) {
        notificationAndOptionMessage.apply {
            title(title)
            message(message)
            animation(animation, useAnimation)
            background(context, R.drawable.frame_background_light_red)
            useButton(useOption)
            buttonNegative(buttonNegative)
            buttonNegative(R.color.red)
            buttonPositive(buttonPositive)
            buttonPositive(R.color.red)
            onClickNegative = { onNegative?.invoke() }
            onClickPositive = { onPositive?.invoke() }
            if (directShow!!) show()
        }
    }

    fun showNotificationAndOptionMessageSuccess(
        title: String? = context?.getString(R.string.notification_success_title),
        message: String?,
        animation: String? = LOTTIE_SUCCESS_JSON,
        useAnimation: Boolean? = true,
        useOption: Boolean? = false,
        buttonNegative: String? = context?.getString(R.string.cancel),
        buttonPositive: String? = context?.getString(R.string.ok),
        onNegative: (() -> Unit)? = { dismissNotificationAndOptionMessage() },
        onPositive: (() -> Unit)? = { },
        directShow: Boolean? = false
    ) {
        notificationAndOptionMessage.apply {
            title(title)
            message(message)
            animation(animation, useAnimation)
            background(context, R.drawable.frame_background_light_green)
            useButton(useOption)
            buttonNegative(buttonNegative)
            buttonNegative(R.color.dark_green)
            buttonPositive(buttonPositive)
            buttonPositive(R.color.dark_green)
            onClickNegative = { onNegative?.invoke() }
            onClickPositive = { onPositive?.invoke() }
            if (directShow!!) show()
        }
    }

    fun showNotificationAndOptionMessage() {
        notificationAndOptionMessage.show()
    }

    fun dismissNotificationAndOptionMessage() {
        notificationAndOptionMessage.dismiss()
    }

    fun setImageSlider(imageCrimeLocations: ArrayList<ImageCrimeLocation>?,
                       isCanEdit: Boolean?,
                       onClickPreview: ((ImageCrimeLocation) -> Unit)? = { },
                       onClickEdit: ((ImageCrimeLocation) -> Unit)? = { }) {
        imageSlider.apply {
            this.isCanEdit = isCanEdit
            this.setImageSlider(
                context as FragmentActivity,
                imageCrimeLocations)
            this.onClickPreview = { onClickPreview?.invoke(it) }
            this.onClickEdit = { onClickEdit?.invoke(it) }
        }
    }

    fun search(
        hint: String?,
        onSearch: ((String?) -> Unit)?
    ) {
        search.apply {
            hint(hint)
            onClickSearch = {
                onSearch?.invoke(it)
            }
        }
    }

    fun loadingList(
        isOn: Boolean?,
        isGetData: Boolean? = false,
        isOverloadData: Boolean? = false
    ) {
        if (isOn!!) {
            dismissNotificationAndOptionMessage()
            dismissRootView()
            dismissEmpty()
            shimmerOn(sfl, true)
        } else {
            shimmerOn(sfl, false)
            if (isGetData!!) {
                if (isOverloadData!!) {
                    showNotificationAndOptionMessage()
                    dismissRootView()
                } else {
                    dismissNotificationAndOptionMessage()
                    showRootView()
                }
                dismissEmpty()
            } else {
                dismissRootView()
                showEmpty()
            }
        }
    }

    fun isResponseSuccess(message: Message?): Boolean {
        return if (message?.success == true) true
        else {
            showError(message = message?.message)
            false
        }
    }

    fun setThemeForSwipeRefreshLayoutLoadingAnimation(
        context: Context?,
        swipeRefreshLayout: SwipeRefreshLayout?
    ) {
        swipeRefreshLayout?.apply {
            context?.resources?.getColor(R.color.white, null)?.let {
                setColorSchemeColors(
                    it
                )
            }
            context?.resources?.getColor(R.color.primary, null)?.let {
                setProgressBackgroundColorSchemeColor(
                    it
                )
            }
        }
    }

    fun shimmerOn(
        shimmerFrameLayout: ShimmerFrameLayout?,
        isOn: Boolean?
    ) {
        if (isOn!!) {
            shimmerFrameLayout?.startShimmer()
            shimmerFrameLayout?.visibility = View.VISIBLE
        } else {
            shimmerFrameLayout?.stopShimmer()
            shimmerFrameLayout?.visibility = View.GONE
        }
    }

    fun titleOverloadData(title: String?): String {
        return getString(R.string.message_title_overload_data, title)
    }

    fun messageOverloadData(
        totalOverloadData: Int?,
        menuOverloadData: String?,
        searchBy: String?
    ): String {
        return getString(
            R.string.label_plus_two_string_enter,
            getString(
                R.string.message_overload_data,
                totalOverloadData.toString(),
                menuOverloadData,
                menuOverloadData,
                getString(
                    R.string.label_plus_two_string,
                    searchBy,
                    menuOverloadData
                )
            ), getString(
                R.string.message_question_overload_data,
                menuOverloadData
            )
        )
    }

    fun messageOverloadData(
        totalOverloadData: Int?,
        menuOverloadData: String?,
        searchBy: String?,
        startBy: String?
    ): String {
        return getString(
            R.string.label_plus_two_string_enter, getString(
                R.string.label_plus_two_string, getString(
                    R.string.message_overload_data,
                    totalOverloadData.toString(),
                    menuOverloadData,
                    menuOverloadData,
                    getString(
                        R.string.label_plus_two_string,
                        searchBy,
                        menuOverloadData
                    )
                ), getString(
                    R.string.message_overload_data_area,
                    menuOverloadData,
                    startBy
                )
            ), getString(
                R.string.message_question_overload_data,
                menuOverloadData
            )
        )
    }

    fun enableAccess(buttons: Array<Button>?) {
        buttons!!.forEach {
            it.isEnabled = true
        }
    }

    fun disableAccess(buttons: Array<Button>?) {
        buttons!!.forEach {
            it.isEnabled = false
        }
    }

    fun enableAccess(imageView: Array<ImageView>?) {
        imageView!!.forEach {
            it.isEnabled = true
        }
    }

    fun disableAccess(imageView: Array<ImageView>?) {
        imageView!!.forEach {
            it.isEnabled = false
        }
    }

    fun parentAreaVisibility(parentAreas: Array<ParentAreaBinding>?, visibility: Int?) {
        parentAreas?.forEach {
            it.root.visibility = visibility!!
        }
    }

    fun buttonVisibility(buttons: Array<Button>?, visibility: Int?) {
        buttons?.forEach {
            it.visibility = visibility!!
        }
    }

    fun imageViewVisibility(imageViews: Array<ImageView>?, visibility: Int?) {
        imageViews?.forEach {
            it.visibility = visibility!!
        }
    }

    fun getCreated(admin: Admin?): String {
        return if (admin?.createdBy == null)
            getString(R.string.label_null_created_and_updated)
        else getString(
            R.string.label_create_by_and_created_date,
            admin.createdBy?.adminName, admin.createdDate
        )
    }

    fun getCreated(admin: Admin?, createdDate: String?): String {
        return if (admin?.adminId == null)
            getString(R.string.label_null_created_and_updated)
        else getString(
            R.string.label_create_by_and_created_date,
            admin.adminName, createdDate
        )
    }

    fun getUpdated(admin: Admin?): String {
        return if (admin?.updatedBy == null)
            getString(R.string.label_not_yet_updated)
        else getString(
            R.string.label_updated_by_and_updated_date,
            admin.updatedBy?.adminName,
            admin.updatedDate
        )
    }

    fun getUpdated(admin: Admin?, updatedDate: String?): String {
        return if (admin?.adminId == null)
            getString(R.string.label_not_yet_updated)
        else getString(
            R.string.label_updated_by_and_updated_date,
            admin.adminName,
            updatedDate
        )
    }

    fun getLabelActivate(
        admin: Admin?,
        isUseMessageAppend: Boolean? = false,
        isReverse: Boolean? = false
    ): String {
        return if (!isReverse!!) {
            if (!isUseMessageAppend!!) {
                if (admin?.isActive!!) getString(R.string.label_non_active)
                else getString(R.string.label_active)
            } else {
                if (admin?.isActive!!) getString(R.string.label_non_active_append)
                else getString(R.string.label_active_append)
            }
        } else {
            if (!isUseMessageAppend!!) {
                if (admin?.isActive!!) getString(R.string.label_active)
                else getString(R.string.label_non_active)
            } else {
                if (admin?.isActive!!) getString(R.string.label_active_append)
                else getString(R.string.label_non_active_append)
            }
        }
    }

    fun getColorActivate(admin: Admin?): Int {
        return if (admin?.isActive!!) R.color.red
        else R.color.dark_green
    }

    fun getMap(intent: Intent?): HashMap<String, Any> {
        return intent?.getSerializableExtra(INTENT_DATA) as HashMap<String, Any>
    }

    fun getTempFileUri(imageType: ImageType?, applicationId: String?): Uri {
        val tempFile = File.createTempFile(
            getNameForImageTemp(context, imageType), IMAGE_FORMAT_PNG, cacheDir
        ).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            applicationContext, applicationId.plus(
                context?.getString(R.string.file_provider_name)
            ), tempFile
        )
    }

    fun getTempFile(imageType: ImageType?): File {
        return File.createTempFile(
            getNameForImageTemp(context, imageType), IMAGE_FORMAT_PNG, cacheDir
        ).apply {
            createNewFile()
            deleteOnExit()
        }
    }

    fun goTo(errorResponse: ErrorResponse?) {
        goTo(
            ConnectionErrorActivity(),
            hashMapOf(
                ERROR_STATE to IS_API_RESPONSE,
                ERROR_RESPONSE to errorResponse!!
            ),
            errorCode = errorResponse.errorCode
        )
    }

    fun goTo(
        appCompatActivity: AppCompatActivity?,
        map: HashMap<String, Any>?,
        errorCode: String? = ERROR_CODE_200_SUCCESS
    ) {
        Intent(
            this,
            appCompatActivity!!::class.java
        ).apply {
            putExtra(INTENT_DATA, map)
            if (errorCode.equals(ERROR_CODE_404_PAGE_NOT_FOUND))
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            resultLauncher?.launch(this)
        }
    }

    fun goTo(appCompatActivity: AppCompatActivity?) {
        Intent(
            this,
            appCompatActivity!!::class.java
        ).apply {
            resultLauncher?.launch(this)
        }
    }
}