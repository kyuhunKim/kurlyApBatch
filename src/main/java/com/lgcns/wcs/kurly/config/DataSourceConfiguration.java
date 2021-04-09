package com.lgcns.wcs.kurly.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.lgcns.wcs.kurly.repository"})
public class DataSourceConfiguration  {
 
    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;
    @Value("${mybatis.config-location}")
	private String configLocation;

//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource)throws Exception{
//            SqlSessionFactoryBean sqlSessionFactoryBean  = new SqlSessionFactoryBean();
//            sqlSessionFactoryBean .setDataSource(dataSource);
//            
//            Resource[] arrResource = new PathMatchingResourcePatternResolver().getResources(mapperLocation);
//            
//            sqlSessionFactoryBean.setMapperLocations(arrResource);
//            sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
//            return sqlSessionFactoryBean.getObject();
//    }

    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    	final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources(mapperLocations));
        sessionFactory.setConfigLocation(resolver.getResource(configLocation));
        SqlSessionFactory sessionFactoryBean = sessionFactory.getObject();
        sessionFactoryBean.openSession(false);
        
        return sessionFactoryBean;
    }
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}