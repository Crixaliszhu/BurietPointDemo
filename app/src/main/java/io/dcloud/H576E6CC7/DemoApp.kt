package io.dcloud.H576E6CC7

import android.app.Application
import com.yupao.entry.point.BuriedPointInit

/**
 * Demo Application
 *
 * 在 Application 层完成埋点 SDK 初始化，保证全局可用。
 */
class DemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化埋点 SDK
        BuriedPointInit.init(this)
    }
}
