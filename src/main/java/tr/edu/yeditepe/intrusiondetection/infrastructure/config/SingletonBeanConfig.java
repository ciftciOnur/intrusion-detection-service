package tr.edu.yeditepe.intrusiondetection.infrastructure.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import tr.edu.yeditepe.intrusiondetection.domain.model.LoginInfo;

@Configuration
public class SingletonBeanConfig {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public LoginInfo loginInfo() {
        return new LoginInfo();
    }
}