package cn.eyeblue.blog.rest.preference;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.Config;
import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.rest.tank.Tank;
import cn.eyeblue.blog.util.NetworkUtil;
import cn.eyeblue.blog.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Preference extends BaseEntity {

    //网站名称
    private String name;

    //logo
    private String logoUrl;
    private String logoTankUuid;
    private String faviconUrl;
    private String faviconTankUuid;

    //5个菜单
    private String menuName1;
    private String menuUrl1;
    private String menuName2;
    private String menuUrl2;
    private String menuName3;
    private String menuUrl3;
    private String menuName4;
    private String menuUrl4;
    private String menuName5;
    private String menuUrl5;

    //底部第一行文字。
    private String footerLine1;
    private String footerLine2;

    //版本号
    private String version;

    @Transient
    private Tank logoTank;
    @Transient
    private Tank faviconTank;


    //默认的一个appPreference.
    public static Preference create() {
        Preference preference = new Preference();

        preference.setName("蓝眼博客");
        preference.setLogoUrl(null);
        preference.setLogoTankUuid(null);
        preference.setLogoTankUuid(null);
        preference.setFaviconUrl(null);
        preference.setFaviconTankUuid(null);

        preference.setMenuName1("首页");
        preference.setMenuUrl1("/");

        preference.setFooterLine1("CopyRight 2017&copy;蓝眼博客 版权所有");
        preference.setFooterLine2("<a href=\"http://www.miitbeian.gov.cn\" target=\"_blank\">沪ICP备14038360号-2</a>");

        Config config = AppContextManager.getBean(Config.class);
        preference.setVersion(config.getBlogVersion());

        return preference;
    }


    //是否为blank类型
    public List<Link> ftlLinks() {

        ArrayList<Link> links = new ArrayList<>();


        if (StringUtil.isNotBlank(menuName1) && StringUtil.isNotBlank(menuUrl1)) {
            links.add(new Link(menuName1, menuUrl1));
        }

        if (StringUtil.isNotBlank(menuName2) && StringUtil.isNotBlank(menuUrl2)) {
            links.add(new Link(menuName2, menuUrl2));
        }


        if (StringUtil.isNotBlank(menuName3) && StringUtil.isNotBlank(menuUrl3)) {
            links.add(new Link(menuName3, menuUrl3));
        }


        if (StringUtil.isNotBlank(menuName4) && StringUtil.isNotBlank(menuUrl4)) {
            links.add(new Link(menuName4, menuUrl4));
        }


        if (StringUtil.isNotBlank(menuName5) && StringUtil.isNotBlank(menuUrl5)) {
            links.add(new Link(menuName5, menuUrl5));
        }


        return links;

    }

    ///////////////以下方法提供给ftl使用/////////////////////////
    @Data
    public static class Link {
        private String name;
        private String url;
        private String target;

        public Link(@NonNull String name, @NonNull String url) {
            this.name = name;
            this.url = url;

            if (this.url.startsWith(NetworkUtil.getHost())) {
                this.target = "_self";
            } else {
                this.target = "_blank";
            }

        }

    }


}


