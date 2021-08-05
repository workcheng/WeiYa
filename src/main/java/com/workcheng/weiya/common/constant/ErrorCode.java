package com.workcheng.weiya.common.constant;

/**
 * @author andy
 * @date 2016/12/17
 */
public enum ErrorCode {
    /**
     * code
     */
    SUCCESS(1000, "成功"),
    ERROR(1001, "错误"),
    ERROR_INTERNAL(1002, "内部错误"),
    ERROR_WECHAT(2000, "WECHAT SDK错误"),
    HAS_SIGN(2001, "已签到"),
    NOT_SIGN(2002, "未签到"),
    NOT_SUBSCRIBE(2003, "未关注公众号无法签到"),
    ERROR_OPENID(2004, "错误的id信息"),
    NOT_START(2005, "活动未开始"),
    HAS_VOTE(2006, "已投票"),
    ERROR_VOTE(2007, "投票失败"),
    ERROR_LOTTERY(2008, "人数不足");

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    private int code;
    private String description;

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ZoeErrorCode{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }

    public static void main(String args[]) {
        // 静态方法
        for (ErrorCode s : ErrorCode.values()) {
            System.out.println("code: " + s.getCode() + ", description: " + s.getDescription());
        }
    }
}
