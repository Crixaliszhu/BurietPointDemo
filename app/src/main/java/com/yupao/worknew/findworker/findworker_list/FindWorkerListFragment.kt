package com.yupao.worknew.findworker.findworker_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yupao.entry.point.BuriedPointInit
import com.yupao.pointer.factory.PointerApiFactory
import com.yupao.pointer.master.config.IPointPageConfig
import com.yupao.pointer.point.impl.IPointerImpl
import io.dcloud.H576E6CC7.R
import io.dcloud.H576E6CC7.databinding.FragmentFindWorkerListBinding

/**
 * 找工作大列表 Fragment（C 端 / 工人端首页）
 *
 * 实现 [IPointPageConfig] 接口，为火山引擎全埋点提供页面标识。
 *
 * 布局从上到下：
 *   1. 顶部搜索栏（搜索框 + 搜索按钮，可点击）
 *   2. 中间内容区域（Fragment 展示区，暂不加载数据）
 *   3. 底部 Tab 由 [com.yupao.feature.main.yupao.tab.fragment.MainTabContainerFragment] 管理
 */
class FindWorkerListFragment : Fragment(), IPointPageConfig {

    companion object {
        /**
         * CMS 对应页面码
         */
        const val PAGE_CODE = "recruit_list"
    }

    private var _binding: FragmentFindWorkerListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindWorkerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchBar()

        // 埋点：找工作页面曝光
        PointerApiFactory.instance.apiFindAll().autoCommit(
            IPointerImpl("find_worker_list_exposure")
                .addStringParam("page_code", PAGE_CODE)
        )
    }

    /**
     * 初始化搜索栏点击事件
     * 搜索框和搜索文案均可点击，暂不实现具体跳转逻辑
     */
    private fun setupSearchBar() {
        binding.llSearch.setOnClickListener {
            // 埋点：点击搜索框
            PointerApiFactory.instance.apiFindAll().autoCommit(
                IPointerImpl("search_bar_click")
                    .addStringParam("click_type", "search_box")
                    .addStringParam("page_code", PAGE_CODE)
            )
        }
        binding.tvSearch.setOnClickListener {
            // 埋点：点击搜索文案
            PointerApiFactory.instance.apiFindAll().autoCommit(
                IPointerImpl("search_bar_click")
                    .addStringParam("click_type", "search_text")
                    .addStringParam("page_code", PAGE_CODE)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // 埋点：页面浏览（手动全埋点补充）
        BuriedPointInit.trackFragmentPageView(
            pageKey = "FindWorkerListFragment",
            pagePath = PAGE_CODE,
            pageTitle = "找工作列表",
            activity = activity
        )
    }

    // ========== IPointPageConfig 实现 ==========

    override fun getPageCode(): String {
        return PAGE_CODE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
