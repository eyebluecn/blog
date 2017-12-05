package cn.zicla.blog.rest.tank;

import cn.zicla.blog.rest.base.BaseEntityForm;
import cn.zicla.blog.rest.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = false)
@Data
public class TankForm extends BaseEntityForm<Tank> {
    public TankForm() {
        super(Tank.class);
    }

    @NotNull
    private String name;
    @NotNull
    private Long size;
    @NotNull
    private String type;
    @NotNull
    private String filter;
    @NotNull
    private Boolean privacy;

    private String remark;


    @Override
    protected void update(Tank tank, User operator) {
        tank.setName(name);

        tank.setSize(size);
        tank.setType(type);
        tank.setFilter(filter);
        tank.setPrivacy(privacy);
        tank.setRemark(remark);

    }

    public Tank create(User operator) {
        Tank tank = super.create(operator);
        tank.setUserUuid(operator.getUuid());
        return tank;
    }
}
