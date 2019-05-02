package com.info.constant;



public class Constant {

    /**
     * 默认密码
     */
    public static final String DEFAULT_PWD = "DEFAULT_PWD";
    /**
     * 下载模板默认密码
     */
    public static final String EXCEL_PWD = "EXCEL_PWD";
    /**
     * 命名空间编码
     */
    public static final String NAME_SPACE = "nameSpace";
    /*** UTF-8编码 */
    public static final String UTF8 = "UTF-8";
    /**
     * 后台请求路径
     */
    public final static String BACK_URL = "BACK_URL";
    /**
     * 后台session名
     */
    public static final String BACK_USER = "BACK_USER";

    public static final String JCAPTCHA_CODE = "JCAPTCHA_CODE";
    /**
     * 后台超级管理员主键ID，该用户不用进行权限判断
     */
    public static final Integer ADMINISTRATOR_ID = 10000;
    /**
     * 后台催收经理主键ID 该用户不用进行权限判断
     **/
    public static final String ROLE_ID = "10019";
    /**
     * 当前页数
     */
    public final static String CURRENT_PAGE = "pageNum";
    /**
     * 每页显示多少条
     */
    public static final String PAGE_SIZE = "numPerPage";
    /**
     * 系统参数中返回list时使用的key的后缀
     */
    public static final String SYS_CONFIG_LIST = "_LIST";
    /**
     * 转派ids-key后缀
     */
    public static final String ORDER_DISPATCH_IDS = "_ORDER_DISPATCH";
    /**
     * 判断是否是http开头
     */
    public static final String HTTP = "http://";
    /**
     * 判断是否是https开头
     */
    public static final String HTTPS = "https://";

    /**
     * 前台用户缓存
     */
    public static final String FRONT_USER = "FRONT_USER";

    /**
     * 缓存名称
     */
    public static final String CACHE_INDEX_KEY = "ZHONGBAO_INDEX";

    /**
     * 注册的验证码
     */
    public static final String SMS_REGISTER = "SMS_REGISTER";
    /**
     * 解绑银行卡的验证码
     */
    public static final String SMS_DEL_BANK = "SMS_DEL_BANK";
    /**
     * 设置交易密码的验证码
     */
    public static final String SMS_SET_PAY = "SMS_SET_PAY";
    /**
     * 修改手机号的时候原手机号的验证码
     */
    public static final String SMS_SET_PHONE_OLD = "SMS_SET_PHONE_OLD";
    /**
     * 修改手机号的时候新手机号的验证码
     */
    public static final String SMS_SET_PHONE_NEW = "SMS_SET_PHONE_NEW";
    /**
     * 短信类型：通知类类
     */
    public static final String NOTICE = "notice";
    /**
     * 短信类型：营销类
     */
    public static final String ADVERT = "advert";

    /**
     * 逾期
     */
    public static final String TYPE_OVERDUE = "OVERDUE";
    /**
     * 续期
     */
    public static final String TYPE_RENEWAL = "RENEWAL";
    /**
     * 还款
     */
    public static final String TYPE_REPAY = "REPAY";
    /**
     * 代扣 redis-key
     */
    public static final String TYPE_WITHHOLD = "WITHHOLD";
    /**
     * 逾期redis-key
     */
    public static final String TYPE_OVERDUE_ = "OVERDUE_";
    /**
     * 续期 redis-key
     */
    public static final String TYPE_RENEWAL_ = "RENEWAL_";
    /**
     * 还款 redis-key
     */
    public static final String TYPE_REPAY_ = "REPAY_";
    /**
     * 代扣 redis-key
     */
    public static final String TYPE_WITHHOLD_ = "WITHHOLD_";
    /**
     * mm-user-loan（3:续期''4''-逾期,''5''-还款结束）            mm-order: 4:催收成功 5：续期  1 催收中
     */
    public static final String STATUS_OVERDUE_THREE = "3";

    public static final String STATUS_OVERDUE_FOUR = "4";

    public static final String STATUS_OVERDUE_FIVE = "5";

    public static final String STATUS_OVERDUE_ONE = "1";

    public static final String OPERATOR_NAME = "系统";

    public static final String PAY_MENT_SUCCESS = "还款成功催收员:";

    /**
     * 逾期1到10天S1
     */
    public static final int CREDITLOANPAY_OVERDUEA = 3;
    /**
     * 续期（该状态催收员不能操作)
     */
    public static final int CREDITLOANPAY_OVERDUE_UNCOMPLETE = 8;
    /**
     * 已还款
     */
    public static final int CREDITLOANPAY_COMPLETE = 2;

    /**
     * 还款中【续期】（催收遇到此状态说明在极速小鱼儿后台已置为续期）
     */
    public static final String STATUS_HKZ = "21";
    /**
     * 已逾期
     */
    public static final String STATUS_YYQ = "-11";
    /**
     * 已坏账
     */
    public static final String STATUS_YHZ = "-20";

    public static final String S_FLAG = "S1";

    public static final String SMS_SEND_SUCC = "0";

    public static final String SALT = "CS";

    /**
     * 用户备注字典
     */
    public static final String USER_REMARK = "user_remark";
    /**
     * 在线客服添加备注标签
     */
    public static final String USER_REMARK_ONLINE = "user_remark_online";

    /**
     * 小鱼儿
     */
    public static final String JX_NAME = "小鱼儿";


    /**
     * session id 的cookie name
     **/
    public static final String SESSION_ID = "JESSIONID_CS";

    /**
     * session 有效期
     **/
    public static final int SESSION_EXPIRE_SECOND = 3600 * 2;

    /**
     * 逾期tag
     */
    public static final String TAG_OVERDUE = "overdue";
    /**
     * 续期tag
     */

    public static final String TAG_RENEWAL = "renewal";
    /**
     * 还款tag
     */
    public static final String TAG_REPAY = "repay";
}
