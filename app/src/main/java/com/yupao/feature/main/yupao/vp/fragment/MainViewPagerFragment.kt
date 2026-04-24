package com.yupao.feature.main.yupao.vp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yupao.data.main.yupao.TabId
import com.yupao.entry.point.BuriedPointInit
import com.yupao.pointer.point.impl.IPointerImpl
import com.yupao.worknew.findworker.findworker_list.FindWorkerListFragment
import io.dcloud.H576E6CC7.R

/**
 * 首页 ViewPager 容器 Fragment
 *
 * 负责根据底部 Tab 切换展示不同的子 Fragment。
 * 默认展示 [FindWorkerListFragment]（找工作页面）。
 */
class MainViewPagerFragment : Fragment() {

    companion object {
        const val TAG = "MainViewPagerFragment"
    }

    private var currentTabId: TabId = TabId.TAB_RECRUITMENT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            showTab(TabId.TAB_RECRUITMENT)
        }
    }

    /**
     * 切换展示的 Tab 对应 Fragment
     * @param tabId 页面场景标识
     */
    fun showTab(tabId: TabId) {
        currentTabId = tabId
        val fragment: Fragment = when (tabId) {
            TabId.TAB_RECRUITMENT -> FindWorkerListFragment()
            else -> PlaceholderFragment.newInstance(tabId.label)
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.vpContent, fragment)
            .commit()

        // 埋点：Tab 切换
        BuriedPointInit.trackEvent(
            IPointerImpl("main_tab_switch")
                .addStringParam("tab_id", tabId.name)
                .addStringParam("tab_name", tabId.label)
        )
    }

    override fun onResume() {
        super.onResume()
        // 埋点：页面浏览
        BuriedPointInit.trackFragmentPageView(
            pageKey = "MainViewPagerFragment",
            pagePath = "com.yupao.feature.main.yupao.vp.fragment.MainViewPagerFragment",
            pageTitle = "首页ViewPager",
            activity = activity
        )
    }
}
