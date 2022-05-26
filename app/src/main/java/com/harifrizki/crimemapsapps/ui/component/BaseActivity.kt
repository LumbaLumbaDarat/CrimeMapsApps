package com.harifrizki.crimemapsapps.ui.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText
import com.harifrizki.crimemapsapps.BuildConfig
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.ErrorResponse
import com.harifrizki.crimemapsapps.databinding.AppBarBinding
import com.harifrizki.crimemapsapps.databinding.EmptyBinding
import com.harifrizki.crimemapsapps.databinding.NotificationAndOptionMessageBinding
import com.harifrizki.crimemapsapps.databinding.SearchBinding
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.model.Menu
import com.harifrizki.crimemapsapps.model.Message
import com.harifrizki.crimemapsapps.ui.module.ConnectionErrorActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.crimemapsapps.utils.ActivityName.PASSWORD
import com.harifrizki.crimemapsapps.utils.ActivityName.PROFILE
import com.harifrizki.crimemapsapps.utils.CRUD.*
import com.harifrizki.crimemapsapps.utils.CRUD.Companion.getEnumCRUD
import com.harifrizki.crimemapsapps.utils.Error.IS_API_RESPONSE
import com.harifrizki.crimemapsapps.utils.Error.IS_NO_NETWORK
import com.harifrizki.crimemapsapps.utils.MenuSetting.*
import com.harifrizki.crimemapsapps.utils.NotificationType.*
import com.lumbalumbadrt.colortoast.ColorToast
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import java.io.File

open class BaseActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val loading by lazy {
        Loading()
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

    private var context: Context? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    private var ties: Array<TextInputEditText>? = null
    private var passwordTies: Array<TextInputEditText>? = null

    private var rootView: View? = null
    private var sfl: ShimmerFrameLayout? = null

    @Override
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
        createNotification()
        createOption()
        createOptionList()
    }

    @Override
    override fun onRefresh() {
    }

    @Override
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

    @Override
    fun onBackPressed(fromActivity: String?, operationCRUD: String?) {
        onBackPressed(
            hashMapOf(
                FROM_ACTIVITY to fromActivity!!,
                OPERATION_CRUD to operationCRUD!!
            )
        )
    }

    private fun createLoading() {
        loading.apply {
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

    @Override
    fun createEmpty(binding: EmptyBinding?) {
        empty.create(binding)
    }

    @Override
    fun createNotificationAndOptionMessage(binding: NotificationAndOptionMessageBinding?) {
        notificationAndOptionMessage.create(context, binding)
    }

    @Override
    fun createSearch(binding: SearchBinding?) {
        search.create(binding)
    }

    @Override
    fun createRootView(view: View?) {
        this.rootView = view
    }

    @Override
    fun createRootView(view: View?, sfl: ShimmerFrameLayout?) {
        this.rootView = view
        this.sfl = sfl
    }

    @Override
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

    @Override
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

    @Override
    fun scrollToUp(nestedScrollView: NestedScrollView?) {
        nestedScrollView?.fullScroll(View.FOCUS_UP)
    }

    @Override
    fun showRootView() {
        rootView?.visibility = View.VISIBLE
    }

    @Override
    fun showView(views: Array<View>?) {
        views?.forEach {
            it.visibility = View.VISIBLE
        }
    }

    @Override
    fun dismissRootView() {
        rootView?.visibility = View.GONE
    }

    @Override
    fun dismissView(views: Array<View>?) {
        views?.forEach {
            it.visibility = View.GONE
        }
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    fun prepareResetTextInputEditText(ties: Array<TextInputEditText>?) {
        this.ties = ties
    }

    @Override
    fun resetTextInputEditText() {
        ties?.forEach {
            it.text?.clear()
        }
    }

    @Override
    fun resetTextInputEditText(ties: Array<TextInputEditText>?) {
        ties?.forEach {
            it.text?.clear()
        }
    }

    @Override
    fun prepareShowAndHidePassword(passwordTies: Array<TextInputEditText>?) {
        this.passwordTies = passwordTies
    }

    @Override
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

    @Override
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

    @Override
    fun showLoading() {
        loading.show()
    }

    @Override
    fun dismissLoading() {
        loading.dismiss()
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    fun dismissNotification() {
        notification.dismiss()
    }

    @Override
    fun showOption(
        titleOption: String?,
        message: String?,
        optionAnimation: String? = LOTTIE_QUESTION_JSON,
        titlePositive: String? = context?.getString(R.string.ok),
        titleNegative: String? = context?.getString(R.string.cancel),
        onPositive: (() -> Unit)?,
        onNegative: (() -> Unit)? = { dismissOption() }
    ) {
        option.apply {
            titleOption(titleOption)
            message?.let { option(it) }
            optionAnimation(optionAnimation)
            buttonPositive(titlePositive)
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

    @Override
    fun dismissOption() {
        option.dismiss()
    }

    @Override
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

    @Override
    fun dismissBottomInput() {
        bottomInput.dismiss()
    }

    @Override
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

    @Override
    fun dismissOptionList() {
        optionList.dismiss()
    }

    @Override
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

    @Override
    fun dismissBottomOption() {
        bottomOption.dismiss()
    }

    @Override
    fun showMessage(map: HashMap<String, Any>?): Boolean {
        val fromActivity = getEnumActivityName(map!![FROM_ACTIVITY].toString())
        return when (getEnumCRUD(map[OPERATION_CRUD] as String)) {
            CREATE -> {
                true
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
                    else -> {
                        false
                    }
                }
            }
            DELETE -> {
                true
            }
            else -> {
                false
            }
        }
    }

    @Override
    fun showEmpty(
        title: String?,
        message: String?,
        animation: String? = LOTTIE_QUESTION_JSON,
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

    @Override
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

    @Override
    fun showEmpty() {
        empty.show()
    }

    @Override
    fun dismissEmpty() {
        empty.dismiss()
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    fun showNotificationAndOptionMessage() {
        notificationAndOptionMessage.show()
    }

    @Override
    fun dismissNotificationAndOptionMessage() {
        notificationAndOptionMessage.dismiss()
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

    @Override
    fun isResponseSuccess(message: Message?): Boolean {
        return if (message?.success == true) true
        else {
            showError(message = message?.message)
            false
        }
    }

    @Override
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

    @Override
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

    fun titleOverloadData(title: String?): String? {
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

    @Override
    fun getCreated(admin: Admin?): String {
        return if (admin?.createdBy == null)
            getString(R.string.label_null_created_and_updated)
        else getString(
            R.string.label_create_by_and_created_date,
            admin.createdBy?.adminName, admin.createdDate
        )
    }

    @Override
    fun getUpdated(admin: Admin?): String {
        return if (admin?.updatedBy == null)
            getString(R.string.label_not_yet_updated)
        else getString(
            R.string.label_updated_by_and_updated_date,
            admin.updatedBy?.adminName,
            admin.updatedDate
        )
    }

    @Override
    fun getMap(intent: Intent?): HashMap<String, Any> {
        return intent?.getSerializableExtra(INTENT_DATA) as HashMap<String, Any>
    }

    @Override
    fun getTempFileUri(imageType: ImageType?): Uri {
        val tempFile = File.createTempFile(
            getNameForImageTemp(context, imageType), IMAGE_FORMAT_PNG, cacheDir
        ).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            applicationContext,
            BuildConfig.APPLICATION_ID.plus(context?.getString(R.string.file_provider_name)),
            tempFile
        )
    }

    @Override
    fun getTempFile(imageType: ImageType?): File {
        return File.createTempFile(
            getNameForImageTemp(context, imageType), IMAGE_FORMAT_PNG, cacheDir
        ).apply {
            createNewFile()
            deleteOnExit()
        }
    }

    @Override
    fun goTo(errorResponse: ErrorResponse?) {
        goTo(
            ConnectionErrorActivity(),
            hashMapOf(
                ERROR_STATE to IS_API_RESPONSE,
                ERROR_RESPONSE to errorResponse!!
            )
        )
    }

    @Override
    fun goTo(appCompatActivity: AppCompatActivity?, map: HashMap<String, Any>?) {
        Intent(
            this,
            appCompatActivity!!::class.java
        ).apply {
            putExtra(INTENT_DATA, map)
            resultLauncher?.launch(this)
        }
    }

    @Override
    fun goTo(appCompatActivity: AppCompatActivity?) {
        Intent(
            this,
            appCompatActivity!!::class.java
        ).apply {
            resultLauncher?.launch(this)
        }
    }
}