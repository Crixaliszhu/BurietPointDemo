package com.yupao.feature.main.yupao.tab.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.yupao.data.main.yupao.TabId
import com.yupao.entry.point.BuriedPointInit
import com.yupao.feature.main.yupao.vp.fragment.MainViewPagerFragment
import com.yupao.pointer.factory.PointerApiFactory
import com.yupao.pointer.point.impl.IPointerImpl
import io.dcloud.H576E6CC7.R
import io.dcloud.H576E6CC7.databinding.FragmentMainTabContainerBinding

/**
 * 首页底部 Tab 栏 Fragment
 *
 * 包含 4 个 Tab：找工作、求职顾问、消息、我的。
 * 点击 Tab 时通知 [MainViewPagerFragment] 切换对应内容，并上报埋点。
 */
class MainTabContainerFragment : Fragment() {

    companion object {
        const val TAG = "MainTabContainerFragment"
    }

    private var _binding: FragmentMainTabContainerBinding? = null
    private val binding get() = _binding!!

    private var currentTabId: TabId = TabId.TAB_RECRUITMENT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainTabContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabClicks()
        updateTabUI(TabId.TAB_RECRUITMENT)
    }

    private fun setupTabClicks() {
        binding.tabFindWork.setOnClickListener { onTabClicked(TabId.TAB_RECRUITMENT) }
        binding.tabJobHelper.setOnClickListener { onTabClicked(TabId.TAB_JOB_HELPER) }
        binding.tabMessage.setOnClickListener { onTabClicked(TabId.TAB_MESSAGE) }
        binding.tabMine.setOnClickListener { onTabClicked(TabId.TAB_MEMBER_CENTER) }
    }

    private fun onTabClicked(tabId: TabId) {
        if (currentTabId == tabId) return

        // 埋点：底部 Tab 点击
        PointerApiFactory.instance.apiFindAll().autoCommit(
            IPointerImpl("main_bottom_tab_click")
                .addStringParam("tab_id", tabId.name)
                .addStringParam("tab_name", tabId.label)
                .addStringParam("from_tab_id", currentTabId.name)
                .addStringParam("from_tab_name", currentTabId.label)
        )

        currentTabId = tabId
        updateTabUI(tabId)

        // 通知 MainViewPagerFragment 切换内容
        val vpFragment = requireActivity().supportFragmentManager
            .findFragmentByTag(MainViewPagerFragment.TAG) as? MainViewPagerFragment
        vpFragment?.showTab(tabId)
    }

    private fun updateTabUI(selectedTab: TabId) {
        val tabs = listOf(
            Triple(binding.ivTabFindWork, binding.tvTabFindWork, TabId.TAB_RECRUITMENT),
            Triple(binding.ivTabJobHelper, binding.tvTabJobHelper, TabId.TAB_JOB_HELPER),
            Triple(binding.ivTabMessage, binding.tvTabMessage, TabId.TAB_MESSAGE),
            Triple(binding.ivTabMine, binding.tvTabMine, TabId.TAB_MEMBER_CENTER),
        )

        val selectedColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        val normalColor = ContextCompat.getColor(requireContext(), R.color.black65)

        for ((icon, text, tabId) in tabs) {
            val isSelected = tabId == selectedTab
            text.setTextColor(if (isSelected) selectedColor else normalColor)
            icon.isSelected = isSelected
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
