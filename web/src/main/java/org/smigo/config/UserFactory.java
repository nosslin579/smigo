package org.smigo.config;

import org.smigo.user.UserBean;
import org.springframework.beans.factory.FactoryBean;

//@Component
public class UserFactory implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        return new UserBean();
    }

    @Override
    public Class<?> getObjectType() {
        return UserBean.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
