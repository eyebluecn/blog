package cn.zicla.blog.rest.user;

import cn.zicla.blog.rest.base.BaseEntityForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@EqualsAndHashCode(callSuper = false)
@Data
public class UserForm extends BaseEntityForm<User> {

    private String username;

    private String password;

    private String email;

    private String phone;

    private User.Gender gender = User.Gender.UNKNOWN;

    private String city;

    private String description;

    private String avatarTankUuid;

    private String avatarUrl;

    private String lastIp;

    private Date lastTime;


    public UserForm() {
        super(User.class);
    }


    @Override
    protected void update(User user, User operator) {

    }

    @Override
    public User get(User operator) {

        User user = this.check(uuid);

        this.update(user, operator);

        return user;
    }

}

