package com.hndt.enums;

import lombok.Getter;

/**
 * 各频率名称枚举类
 *
 * @author Hystar
 * @date 2018/2/11
 */
@Getter
public enum ChannelNameEnum {

    XIN_WEN("新闻", "xinwen"),
    // 对应的topic：24
    XING_HE_YIN_YUE_TAI("星河音乐台", "1061"),
    JIAO_TONG("交通", "jiaotong"),
    // 对应的topic：9
    JIAO_YU("教育广播·UPRADIO", "jiaoyu"),
    JING_JI("经济", "jingji"),
    LE_LING("乐龄", "leling"),
    GUO_YUE("国乐", "minyue"),
    NONG_CUN("农村", "nongcun"),
    SI_JIA_CHE("私家车", "sijiache"),
    // 对应的topic：4
    WANG_LUO_XI_QU("戏曲广播·娱乐976", "wangluoxiqu"),
    MY_RADIO("MyRadio", "yingshi"),
    MEI_LI_881("881", "yinyue"),
    YOU_SHENG_WEN_ZAI("有声文摘", "yswz"),
    YU_LE_976("娱乐976", "yule"),
    YI_XIANG_SHU_CHANG("易象书场", "yxsc"),
    ;

    private String key;
    private String value;

    ChannelNameEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static ChannelNameEnum keyOf(String key) {
        for (ChannelNameEnum channelNameEnum : values()) {
            if (channelNameEnum.getKey().equals(key)) {
                return channelNameEnum;
            }
        }

        return null;
    }

}
