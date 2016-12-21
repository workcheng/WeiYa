package com.zoe.weiya.comm.constant;

/**
 * Created by andy on 2016/12/17.
 */
public enum ZoeErrorCode {
    SUCCESS(1000, "成功"),
    ERROR(1001,"错误"),
    ERROR_INTERNAL(1002,"内部错误"),
    ERROR_WECHAT(2000,"WECHAT SDK错误"),
    ERROR_HAS_SIGN(2001,"已签到")
    ;

    ZoeErrorCode(int number, String description) {
        this.code = number;
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

    public static void main(String args[]) { // 静态方法
        for (ZoeErrorCode s : ZoeErrorCode.values()) {
            System.out.println("code: " + s.getCode() + ", description: " + s.getDescription());
        }
    }
}
