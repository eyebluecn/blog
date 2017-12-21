package cn.eyeblue.blog.util;



import cn.eyeblue.blog.config.exception.UtilException;

import java.net.URL;

/**
 * 路径通用类
 */
public class PathUtil {

    public final static String APPLICATION_PROPERTIES = "application.properties";


    /**
     * 获取模块app路径：
     * 例如：/D:/Group/Neobay/Jay/app
     * 或者：/data/app/jay/app
     */
    public static String getAppPath() {

        URL url = PathUtil.class.getClassLoader().getResource(APPLICATION_PROPERTIES);

        if (url == null) {
            throw new UtilException("No " + APPLICATION_PROPERTIES + ", please fix it!");
        }

        String inputFilePath = url.getPath();

        //在Linux下 inputFilePath 会以 file:开头。
        inputFilePath = inputFilePath.substring(inputFilePath.indexOf("/"));


        //如果是jar包，那么要求Jar包旁边有个jar直接解压的文件夹。
        int index = inputFilePath.indexOf(".jar!");
        if (index == -1) {
            index = inputFilePath.indexOf("/build");
            if (index == -1) {
                index = inputFilePath.indexOf("/out/production");
                if (index == -1) {
                    index = inputFilePath.indexOf("/out/test");
                    if (index == -1) {
                        index = inputFilePath.indexOf("/target/classes");
                        if (index == -1) {
                            index = inputFilePath.indexOf("/target/test-classes");

                        }
                    }
                }
            }
        }

        return inputFilePath.substring(0, index);

    }


    /**
     * 获取build包名起始路径 如： 例如：/D:/Group/Neobay/Jay/app/build/classes/main
     * 或者：/data/app/jay/app/build/classes/main
     */
    public static String getBuildPackageRootPath() {

        return getAppPath() + "/build/classes/main";
    }

    /**
     * 获取源码包名起始路径
     * 例如：/D:/Group/Neobay/Jay/app/src/main/java
     * 或者：/data/app/jay/app/src/main/java
     */
    public static String getSrcPackageRootPath() {
        return getAppPath() + "/src/main/java";
    }


    /**
     * 获取测试包名起始路径
     * 例如：/D:/Group/Neobay/Jay/app/src/test/java
     * 或者：/data/app/jay/app/src/test/java
     */
    public static String getTestPackageRootPath() {
        return getAppPath() + "/src/test/java";
    }


    /**
     * 获取build包名起始路径 如： 例如：/D:/Group/Neobay/Jay/app/build/resources/main
     * 或者：/data/app/jay/app/build/resources/main
     */
    public static String getBuildResourcesRootPath() {
        return getAppPath() + "/build/resources/main";
    }

    /**
     * 获取源码资源起始路径
     * 例如：/D:/Group/Neobay/Jay/app/src/main/resources
     * 或者：/data/app/jay/app/src/main/resources
     */
    public static String getSrcResourcesRootPath() {


        return getAppPath() + "/src/main/resources";

    }

    /**
     * 获取测试资源起始路径
     * 例如：/D:/Group/Neobay/Jay/app/src/main/resources
     * 或者：/data/app/jay/app/src/main/resources
     */
    public static String getTestResourcesRootPath() {


        return getAppPath() + "/src/test/resources";

    }


}
