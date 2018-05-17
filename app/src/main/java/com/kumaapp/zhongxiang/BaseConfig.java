package com.kumaapp.zhongxiang;

/**
 * 该项目配置信息//
 */
public class BaseConfig {

    /**
     * 是否打印信息开关
     */
    public static final boolean DEBUG = true;
    /**
     * 后台服务接口根路径
     */
    //public static final String SYS_ROOT = "http://app.qwicar.com/";
    public static final String SYS_ROOT = "http://223.98.184.56:8008/yscx_xjc/";

    /**
     * 图片压缩的最大宽度
     */
    public static final int IMAGE_WIDTH = 640;
    public static final int IMAGE_HEIGHT = 3000;
    /**
     * 图片压缩的失真率
     */
    public static final int IMAGE_QUALITY = 100;
    /**
     * 微信appid
     */
    public static final String APPID_WEIXIN = "wx5bff865f7d0ef0ad";
    /**
     * 银联支付环境--"00"生产环境,"01"测试环境
     */
    public static final String UNIONPAY_TESTMODE = "00";
    /**
     * 数据库版本号
     */
    public static final int DATA_BASE_VERSION = 1;
/*
证书指纹:众享APP
         MD5: CE:FC:42:29:1F:A9:4E:63:3A:21:39:54:09:8D:4F:8C
         SHA1: B6:DC:D4:07:18:BB:67:F3:C3:3F:56:C4:9A:2B:C0:D4:77:9A:63:85
         SHA256: 97:A4:F7:5C:BC:DE:E0:F3:32:3A:E4:D9:06:E0:90:57:63:CC:D5:DA:F8:39:1C:DF:03:47:56:A4:B3:7C:4D:AB
         签名算法名称: SHA256withRSA
         版本: 3

 */
}
