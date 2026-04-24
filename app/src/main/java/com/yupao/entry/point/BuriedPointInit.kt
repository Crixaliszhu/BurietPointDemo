package com.yupao.entry.point

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.yupao.pointer.entity.PointInitEntity
import com.yupao.pointer.factory.PointerApiFactory
import com.yupao.pointer.factory.PointerApiSetting
import com.yupao.pointer.factory.PointerApiStarter
import com.yupao.pointer.point.impl.IPointerImpl
import com.yupao.pointer.volcengine.autotrack.ManualAutoTrackEntity
import com.yupao.pointer.volcengine.autotrack.VolcanoLifecycleChanger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Locale

/**
 * 埋点初始化入口
 *
 * 封装了火山引擎埋点 SDK 的初始化、登录/登出、公共属性注册、
 * 事件上报等完整功能，与 recruitment_android 主工程保持一致。
 */
object BuriedPointInit {

    private const val TAG = "BuriedPoint"

    /**
     * 火山引擎 appId（测试环境）
     */
    private const val VOLCENGINE_APP_ID = "10000002"

    /**
     * 火山引擎 appName
     */
    private const val VOLCENGINE_APP_NAME = "yupaowang_test"

    /**
     * SDK 是否已初始化
     */
    private var isInitialized = false

    /**
     * 在 Application.onCreate 中调用，完成埋点 SDK 初始化
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun init(application: Application, activity: Activity? = null) {
        if (isInitialized) return
        GlobalScope.launch(Dispatchers.IO) {
            initPointVolcengine(application, activity)
        }
    }

    /**
     * 初始化火山引擎埋点
     */
    private fun initPointVolcengine(
        application: Application,
        activity: Activity? = null,
    ) {
        val entity = PointInitEntity(
            application = application,
            activity = activity,
            appId = VOLCENGINE_APP_ID,
            appName = VOLCENGINE_APP_NAME,
            userId = "",
            uuid = "",
            userName = "",
            channel = "demo",
        )

        // 初始化火山引擎 SDK
        PointerApiStarter.instance.apiFind(volcengineEnable = true).inInit(entity)

        // 注册公共属性
        val properties = JSONObject().apply {
            put("platform_type", "app")
            put("app_name", "鱼泡网测试")
            put("electric_quantity", getBatteryLevel(application))
            put("os_version", Build.VERSION.RELEASE)
            put("device_model", Build.MODEL)
            put("device_brand", Build.BRAND)
            put("app_language", getCurrentAppLanguage(application))
        }
        PointerApiSetting.instance.propertiesFind(volcengineEnable = true)
            .registerSuperProperties(properties)

        // 激活事件（多次调用只生效一次）
        PointerApiStarter.instance.apiFind(volcengineEnable = true).trackAppInstall()

        isInitialized = true
    }

    // ==================== 登录/登出 ====================

    /**
     * 用户登录后调用，关联用户 ID
     */
    fun login(userId: String) {
        PointerApiStarter.instance.apiFindAll().login(userId)
    }

    /**
     * 用户登出后调用
     */
    fun logout() {
        PointerApiStarter.instance.apiFindAll().logout()
    }

    /**
     * 登录状态变化时调用
     */
    fun loginStatusChange(isLogin: Boolean) {
        if (isLogin) {
            // demo 中无真实 userId，传空字符串
            login("")
        } else {
            logout()
        }
    }

    // ==================== 事件上报 ====================

    /**
     * 上报自定义埋点事件
     *
     * 使用示例：
     * ```
     * BuriedPointInit.trackEvent(
     *     IPointerImpl("page_view")
     *         .addStringParam("page_name", "home")
     *         .addIntParam("duration", 3000)
     * )
     * ```
     */
    fun trackEvent(pointer: IPointerImpl) {
        PointerApiFactory.instance.apiFindAll().autoCommit(pointer)
    }

    /**
     * 便捷方法：上报简单事件（仅事件名）
     */
    fun trackEvent(eventName: String) {
        PointerApiFactory.instance.apiFindAll().autoCommit(IPointerImpl(eventName))
    }

    /**
     * 便捷方法：上报带参数的事件
     */
    fun trackEvent(eventName: String, params: Map<String, String>) {
        val pointer = IPointerImpl(eventName)
        params.forEach { (key, value) ->
            pointer.addStringParam(key, value)
        }
        PointerApiFactory.instance.apiFindAll().autoCommit(pointer)
    }

    // ==================== 公共属性 ====================

    /**
     * 注册静态公共属性（所有后续事件都会携带）
     */
    fun registerSuperProperties(properties: JSONObject) {
        PointerApiSetting.instance.propertiesFindAll().registerSuperProperties(properties)
    }

    /**
     * 取消指定的静态公共属性
     */
    fun unregisterSuperProperty(propertyName: String) {
        PointerApiSetting.instance.propertiesFindAll().unregisterSuperProperties(propertyName)
    }

    // ==================== 用户属性 ====================

    /**
     * 设置用户属性
     */
    fun setUserProfile(properties: JSONObject) {
        PointerApiSetting.instance.userApiFindAll().userProfiler(properties)
    }

    /**
     * 设置仅首次生效的用户属性
     */
    fun setUserProfileOnce(properties: JSONObject) {
        PointerApiSetting.instance.userApiFindAll().userProfilerOnce(properties)
    }

    // ==================== 页面追踪 ====================

    /**
     * 手动上报 Fragment 页面浏览事件（火山引擎全埋点补充）
     */
    fun trackFragmentPageView(
        pageKey: String?,
        pagePath: String?,
        pageTitle: String? = null,
        referPageKey: String? = null,
        referPagePath: String? = null,
        referPageTitle: String? = null,
        activity: Activity? = null,
    ) {
        VolcanoLifecycleChanger.fragmentOnResume(
            ManualAutoTrackEntity(
                page_key = pageKey,
                page_path = pagePath,
                page_title = pageTitle,
                refer_page_key = referPageKey,
                referrer_page_path = referPagePath,
                refer_page_title = referPageTitle,
            ),
            activity
        )
    }

    // ==================== 工具方法 ====================

    private fun getBatteryLevel(context: Context): Int {
        return context.applicationContext.registerReceiver(
            null, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )?.getIntExtra("level", 0) ?: 0
    }

    private fun getCurrentAppLanguage(context: Context): String {
        return context.applicationContext.resources.configuration.locale.language
    }
}
