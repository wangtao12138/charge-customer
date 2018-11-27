package cn.com.cdboost.charge.customer.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DruidConfig {

    // 配置连接池
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Bean
    public DataSource druid(){
       return new DruidDataSource();
    }
 
    //配置Druid的监控
    //1、配置一个管理后台的Servlet
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet());
        bean.addUrlMappings("/druid/*");
        bean.addInitParameter("loginUsername","admin");
        bean.addInitParameter("loginPassword","123456");
        // IP白名单 ,默认就是允许所有访问
        bean.addInitParameter("allow","");
        // IP黑名单 (存在共同时，deny优先于allow)
        bean.addInitParameter("deny","192.168.15.21");
        // 禁用HTML页面上的“Reset All”功能
        bean.addInitParameter("resetEnable","false");

//        Map<String,String> initParams = new HashMap<>();
//        initParams.put("loginUsername","admin");
//        initParams.put("loginPassword","123456");
//        // IP白名单 ,默认就是允许所有访问
//        initParams.put("allow","");
//        // IP黑名单 (存在共同时，deny优先于allow)
//        initParams.put("deny","192.168.15.21");
//
//        bean.setInitParameters(initParams);
        return bean;
    }
 
 
    //2、配置一个web监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        bean.addUrlPatterns("/*");
        // 忽略资源
        bean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

//        Map<String,String> initParams = new HashMap<>();
//        initParams.put("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//        bean.setInitParameters(initParams);
//        bean.setUrlPatterns(Arrays.asList("/*"));

        return  bean;
    }
}
