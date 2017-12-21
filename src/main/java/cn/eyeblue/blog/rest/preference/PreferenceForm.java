package cn.eyeblue.blog.rest.preference;

import cn.eyeblue.blog.rest.base.BaseEntityForm;
import cn.eyeblue.blog.rest.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = false)
@Data
public class PreferenceForm extends BaseEntityForm<Preference> {


    //网站名称
    @NotNull
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


    public PreferenceForm() {
        super(Preference.class);
    }

    @Override
    protected void update(Preference preference, User operator) {


        preference.setName(name);
        if (logoUrl != null && !logoUrl.equals("")) {
            preference.setLogoUrl(logoUrl);
        } else {
            preference.setLogoUrl(null);
        }
        preference.setLogoTankUuid(logoTankUuid);
        if (faviconUrl != null && !faviconUrl.equals("")) {
            preference.setFaviconUrl(faviconUrl);
        } else {
            preference.setFaviconUrl(null);
        }
        preference.setFaviconTankUuid(faviconTankUuid);
        preference.setMenuName1(menuName1);
        preference.setMenuUrl1(menuUrl1);
        preference.setMenuName2(menuName2);
        preference.setMenuUrl2(menuUrl2);
        preference.setMenuName3(menuName3);
        preference.setMenuUrl3(menuUrl3);
        preference.setMenuName4(menuName4);
        preference.setMenuUrl4(menuUrl4);
        preference.setMenuName5(menuName5);
        preference.setMenuUrl5(menuUrl5);
        preference.setFooterLine1(footerLine1);
        preference.setFooterLine2(footerLine2);

    }


}

