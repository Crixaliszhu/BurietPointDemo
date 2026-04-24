package com.yupao.data.main.yupao

/**
 * 首页底部 Tab 标识枚举
 * 用于区分不同的首页 Tab 页面/场景
 */
enum class TabId(val label: String) {
    /** 找工作（招聘列表） */
    TAB_RECRUITMENT("找工作"),

    /** 求职顾问 */
    TAB_JOB_HELPER("求职顾问"),

    /** 消息 */
    TAB_MESSAGE("消息"),

    /** 我的（会员中心） */
    TAB_MEMBER_CENTER("我的");
}
