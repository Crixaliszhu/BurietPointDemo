package com.yupao.entry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yupao.entry.point.BuriedPointInit
import com.yupao.feature.main.yupao.tab.fragment.MainTabContainerFragment
import com.yupao.feature.main.yupao.vp.fragment.MainViewPagerFragment
import com.yupao.pointer.factory.PointerApiFactory
import com.yupao.pointer.point.impl.IPointerImpl
import io.dcloud.H576E6CC7.R

/**
 * 首页 Activity
 *
 * 布局层级：
 *   MainActivity
 *     ├── MainViewPagerFragment（内容区域，承载各 Tab 对应的 Fragment）
 *     └── MainTabContainerFragment（底部 Tab 栏）
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fcvContent, MainViewPagerFragment(), MainViewPagerFragment.TAG)
                .replace(R.id.bottomContainer, MainTabContainerFragment(), MainTabContainerFragment.TAG)
                .commit()
        }

        // 埋点：首页曝光
        BuriedPointInit.trackEvent(
            IPointerImpl("main_page_exposure")
                .addStringParam("page_name", "main")
                .addStringParam("source", "cold_boot")
        )
    }

    override fun onResume() {
        super.onResume()
        // 埋点：页面浏览
        BuriedPointInit.trackFragmentPageView(
            pageKey = "MainActivity",
            pagePath = "com.yupao.entry.MainActivity",
            pageTitle = "首页",
            activity = this
        )
    }
}
