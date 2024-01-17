package com.binzaijun.stock.domain;

public class AlarmParamsDTO {


    /**
     * 推送信息小标题
     */
    private String first;

    /**
     * 学生姓名
     */
    private String keyword1;

    /**
     * 申请资助类型
     */
    private String keyword2;

    /**
     * 申请状态
     */
    private String keyword3;

    /**
     * 申请结果
     */
    private String remark;

    /**
     * 用户微信openId，唯一标识
     */
    private String openId;

    public AlarmParamsDTO(String first, String keyword1, String keyword2, String keyword3, String remark) {
        this.first = first;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.remark = remark;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(String keyword1) {
        this.keyword1 = keyword1;
    }

    public String getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(String keyword2) {
        this.keyword2 = keyword2;
    }

    public String getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(String keyword3) {
        this.keyword3 = keyword3;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}

