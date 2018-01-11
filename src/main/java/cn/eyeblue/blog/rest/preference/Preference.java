package cn.eyeblue.blog.rest.preference;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.Config;
import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.rest.tank.Tank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Transient;

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

}


