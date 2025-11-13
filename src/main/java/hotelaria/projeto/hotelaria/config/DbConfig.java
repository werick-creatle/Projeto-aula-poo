package hotelaria.projeto.hotelaria.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DbProperties.class)
public class DbConfig {
    private final DbProperties PROPS;

    public DbConfig(DbProperties props){
        this.PROPS = props;
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create().driverClassName(PROPS.getDriverClassName()).url(PROPS.getUrl())
                .password(PROPS.getPassword()).username(PROPS.getUsername()).build();
    }
}
