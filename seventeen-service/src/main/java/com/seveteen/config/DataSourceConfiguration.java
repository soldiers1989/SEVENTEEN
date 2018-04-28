package com.seveteen.config; /**
 * @author DELL
 * @createDate 2016/11/23 15:38
 */

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 类功能描述: TODO
 *
 * @author csk
 * @version 1.0
 * @createDate Nov 19, 2018 11:16:44 AM
 */
@Configuration
public class DataSourceConfiguration {

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	@Value("${spring.datasource.tomcat.max-active}")
	private int maxActive;

	@Value("${spring.datasource.tomcat.max-wait}")
	private int maxWait;

	@Value("${spring.datasource.tomcat.initial-size}")
	private int initialSize;

	// @Value("${mybatis.mapper-locations}")
	// private String mapperLocations;

	@Value("${mybatis.configuration.map-underscore-to-camel-case}")
	private Boolean mapUnderscoreToCamelCase;

	@Value("${mybatis.type-aliases-package}")
	private String typeAliasesPackage;

	/**
	 * 配置druid数据源
	 *
	 * @return
	 * @throws SQLException
	 */
	@Bean(name = "dataSource")
	@Primary
	public DataSource dataSource() throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName(driverClassName);
		dataSource.setFilters("stat");
		// 如果连接超过30分钟未关闭，就会被强行回收，并且日志记录连接申请时的调用堆栈
		dataSource.setRemoveAbandoned(true);
		dataSource.setRemoveAbandonedTimeoutMillis(30 * 60 * 1000);
		dataSource.setLogAbandoned(true);
		Properties properties = new Properties();
		properties.setProperty("druid.stat.mergeSql", "true");
		properties.setProperty("druid.stat.slowSqlMillis", "5000");
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(3);
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxWait(maxWait);
		dataSource.setConnectProperties(properties);
		return dataSource;
	}

	@Bean(name = "transactionManager")
	@Primary
	public DataSourceTransactionManager transactionManager() throws SQLException {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean(name = "sqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		org.apache.ibatis.session.Configuration ibatisConfiguration = new org.apache.ibatis.session.Configuration();
		ibatisConfiguration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
		sessionFactory.setConfiguration(ibatisConfiguration);
		sessionFactory.setVfs(SpringBootVFS.class);
		sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
		// sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
		// .getResources(mapperLocations));
		return sessionFactory.getObject();
	}

	@Bean(name = "sqlSessionTemplate")
	@Primary
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	/**
	 * 配置druid监控servlet
	 *
	 * @return
	 */
	@Bean
	@Primary
	public ServletRegistrationBean servletRegistrationBean() {
		StatViewServlet statViewServlet = new StatViewServlet();
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
		servletRegistrationBean.setServlet(statViewServlet);
		servletRegistrationBean.addUrlMappings("/druid/*");
		return servletRegistrationBean;
	}

	/**
	 * 配置druid监控web
	 *
	 * @return
	 */
	@Bean
	@Primary
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

}
