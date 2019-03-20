package com.hndt.common;

/**
 * 通用常量
 *
 * @author Hystar
 * @date 2018/1/9
 */
public class Const {

    public static final String ENCODING_UTF8 = "utf-8";
    public static final String LITERAL_MANAGER = "manager";

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    /**
     * 用户中心分配的登录信息
     */
    public static final String APP_ID = "9807e616e99d5dc3839ad580bcb785af";
    public static final String APP_SECRET = "f361c3a329df175c63c29b62e161ece089214e38";

    /**
     * 对接用户中心域名
     */
    public static final String DO_MAIN_USER_CENTER = "http://uc.hndt.com";
    /**
     * 在用户中心获取用户详细信息，需要传入user_id参数
     */
    public static final String USER_INFO_URI = "/api/user/info";
    /**
     * 在用户中心获取所有B类用户信息，如果没有参数，则获取所有B类用户，如果加上参数name，则通过姓名查询用户信息
     */
    public static final String USER_LIST_B_URI = "/api/user/list_b";
    /**
     * 过期时间：5分钟
     */
    public static final int EXPIRATION_TIME = 5 * 60 * 1000;
    /**
     * 项目名
     */
    public static final String PROJECT_NAME = "/live_interaction";

    /**
     * 获取频率下的节目单信息，需要传入频率id
     */
    public static final String PROGRAM_INFO_URL = "http://program.hndt.com/get/vod/";

    /**
     * 对接宋鎏鑫的域名
     *
     * 测试域名：http://songliuxin.55555.io
     */
    public static final String D0_MAIN_SLX = "http://talk.hndt.com";
    /**
     * 操作消息，其中包括删除一条或多条消息（参数ids，逗号分隔）、发布、推荐，与 D0_MAIN_SLX 域名配合使用
     */
    public static final String OPERATION_MESSAGE = "/live/api/comOperation.do";

    /**
     * 生成的文件类型：临时文件、正式文件
     */
    public static final int FILE_TYPE_TEMP = 0;
    public static final int FILE_TYPE_FORMAL = 1;

    // 频率名称
    /**
     * 新闻
     */
    public static final String XIN_WEN = "xinwen";
    /**
     * 星河音悦台
     */
    public static final String XING_HE = "1061";
    /**
     * 交通
     */
    public static final String JIAO_TONG = "jiaotong";
    /**
     * 教育
     */
    public static final String JIAO_YU = "jiaoyu";
    /**
     * 经济
     */
    public static final String JING_JI = "jingji";
    /**
     * 乐龄
     */
    public static final String LE_LING = "leling";
    /**
     * 国乐
     */
    public static final String MIN_YUE = "minyue";
    /**
     * 农村
     */
    public static final String NONG_CUN = "nongcun";
    /**
     * 私家车
     */
    public static final String SI_JIA_CHE = "sijiache";
    /**
     * 网络戏曲
     */
    public static final String WANG_LUO_XI_QU = "wangluoxiqu";
    /**
     * MyRadio
     */
    public static final String MY_RADIO = "yingshi";
    /**
     * 881
     */
    public static final String MEI_LI_881 = "yinyue";
    /**
     * 有声文摘
     */
    public static final String YOU_SHENG_WEN_ZHAI = "yswz";
    /**
     * 娱乐976
     */
    public static final String YU_LE_976 = "yule";
    /**
     * 易象书场
     */
    public static final String YI_XIANG_SHU_CHANG = "yxsc";

}
