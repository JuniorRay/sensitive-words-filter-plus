package com.odianyun.util.sensi;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * @description 记录返回结果识别类型
 * @author Junior Ray
 * @date 2020/6/19
 */
public enum RetEnum {
    /**被改变后的文字**/
    REPLACED_WORDS("replacedWords", String.class),
    /**敏感文字集合**/
    SENSITIVE_WORD_SET("sensitiveWordSet", Set.class),
    /**代表有敏感词汇标志位**/
    IS_REPLACED("isReplaced", Boolean.class);


    // 成员变量
    private String key;
    private Class clazz;

    // 构造方法
    RetEnum(String key, Class clazz) {
        this.key = key;
        this.clazz = clazz;
    }

    private static final Map<String, RetEnum> ENUM_MAP = new HashMap<String, RetEnum>();

    static {
        for (RetEnum item : values()) {
            ENUM_MAP.put(item.getKey(), item);
        }
    }

    public String getKey() {
        return  this.key ;
    }
    public Class getClazz() {
        return  this.clazz ;
    }

    public static RetEnum getModelByKey(String key) {
        if (key != null) {
            return ENUM_MAP.get(key);
        }
        return null;
    }
}