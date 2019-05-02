package com.info.back.vo;

/**
 * Created by cqry_2016 on 2018/3/26
 * 淘宝购物记录
 */
public class TaobaoOrder {
    /*交易状态*/
    private String tradeStatusName;
    /*商品数量*/
    private String totalQuantity;
    /*店铺名称*/
    private String shopName;
    /*实付金额*/
    private String actual;
    /*商品名称*/
    private String itemTitle;
    /*支付完成时间*/
    private String endTime;
    /*是否虚拟商品*/
    private String virtualSign;

    public String getTradeStatusName() {
        return tradeStatusName;
    }

    public void setTradeStatusName(String tradeStatusName) {
        this.tradeStatusName = tradeStatusName;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getVirtualSign() {
        return virtualSign;
    }

    public void setVirtualSign(String virtualSign) {
        this.virtualSign = virtualSign;
    }
}
