package cn.eyeblue.blog.rest.user;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.BaseEntityForm;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.util.ValidationUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = false)
@Data
public class UserForm extends BaseEntityForm<User> {

    @NotNull
    private String username;

    //创建时密码必须。
    private String password;

    @NotNull
    private User.Role role;

    //邮箱一旦创建好，自己便不可再修改，只有管理员可以修改。
    @NotNull
    private String email;

    private String phone;

    @NotNull
    private User.Gender gender;

    private String city;

    private String description;

    private String avatarTankUuid;

    private String avatarUrl;

    public UserForm() {
        super(User.class);
    }


    @Override
    protected void update(User user, User operator) {
        user.setUsername(username);
        user.setAvatarTankUuid(avatarTankUuid);
        user.setAvatarUrl(avatarUrl);
        user.setPhone(phone);
        user.setGender(gender);
        user.setCity(city);
        user.setDescription(description);
        user.setAvatarTankUuid(avatarTankUuid);
        user.setAvatarUrl(avatarUrl);

        //只有管理员可以设置以下条目。
        if (operator.hasPermission(FeatureType.USER_MANAGE)) {
            user.setRole(role);
            user.setEmail(email);
        }
    }

    public User create(User operator) {

        User user = new User();
        this.update(user, operator);

        //创建的时候必须指定密码。
        if (!ValidationUtil.checkParam(password)) {
            throw new UtilException("必须给用户指定密码！");
        }
        if (password.length() < 6) {
            throw new UtilException("密码位数不能低于6位");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = AppContextManager.getBean(BCryptPasswordEncoder.class);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        return user;
    }

    //这里是修改用户。
    @Override
    public User get(User operator) {

        User user = this.check(uuid);

        this.update(user, operator);

        return user;
    }

}

