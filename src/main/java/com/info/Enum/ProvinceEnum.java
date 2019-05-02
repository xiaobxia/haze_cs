package com.info.Enum;

/**
 * 省份
 *
 * @author cqry_2016
 * @create 2018-08-25 10:37
 **/

@SuppressWarnings("unchecked")
public enum ProvinceEnum {
    TIAN_JING("12", "天津市"), BEI_JIN("11", "北京市"), HE_BEI("13", "河北省"), SHAN_XI("14", "山西省"), NEI_MENG_GU("15", "内蒙古自治区"),
    LIAO_NING("21", "辽宁省"), JI_NING("22", "吉林省"), HE_LOGN_JIANG("23", "黑龙江省"), SHAN_HAI("31", "上海市"), JIANG_SHU("32", "江苏省"),
    ZHE_JIANG("33", "浙江省"), AN_HUI("34", "安徽省"), HU_JIAN("35", "福建省"), JIANG_XI("36", "江西省"), SHAN_DONG("37", "山东省"),
    HE_NAN("41", "河南省"), HU_BEI("42", "湖北省"), HU_NAN("43", "湖南省"), GUANG_DONG("44", "广东省"), GUANG_XI("46", "广西壮族自治区"),
    HAI_NAN("46", "海南省"), CHONG_QING("50", "重庆市"), SI_CHUANG("51", "四川省"), GUI_ZHOU("52", "贵州省"), YUN_NAN("53", "云南省"),
    XI_ZHANG("54", "西藏自治区"), SHANG_XI("61", "陕西省"), GAN_SHU("62", "甘肃省"), QING_HAI("63", "青海省"), NING_XIA("64", "宁夏回族自治区"),
    XIN_JIANG("65", "新疆维吾尔自治区"), TAI_WANG("71", "台湾省"), XIANG_GANG("81", "香港特别行政区"), AO_MENG("91", "澳门特别行政区");

    private String code;
    private String name;

    private ProvinceEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getProvince(String paramCode) {
        for (ProvinceEnum c : ProvinceEnum.values()) {
            if (c.code.equals(paramCode)) {
                return c.name;
            }
        }
        return "未知";
    }

}