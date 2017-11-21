package cn.zicla.blog.rest.core;

import lombok.Getter;

/**
 * 网站的所有功能点
 */
public enum FeatureType {

    /**
     * 公共接口，所有人均可访问。
     */
    PUBLIC("公共接口"),





    /*
     * 其他
     */
    OTHER("其他");

    @Getter
    private String name;

    FeatureType(String name) {
        this.name = name;
    }

}
