package com.damoyeo.healthyLife;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.commons.dbutils.QueryLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class HealthyLifeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthyLifeApplication.class, args);
	}
	@Bean
	public Map<String, String> queryMap() {
		String QUERY_PATH_MADANG_0 = "/com/damoyeo/healthyLife/member_sql.properties";
		String QUERY_PATH_MADANG_1 = "/com/damoyeo/healthyLife/wiseSaying_sql.properties";
		String QUERY_PATH_MADANG_2 = "/com/damoyeo/healthyLife/sport_sql.properties";
		String QUERY_PATH_MADANG_3 = "/com/damoyeo/healthyLife/menu_sql.properties";
		String QUERY_PATH_MADANG_4 = "/com/damoyeo/healthyLife/community_sql.properties";
		String QUERY_PATH_MADANG_5 = "/com/damoyeo/healthyLife/trainer_sql.properties";
		String QUERY_PATH_MADANG_6= "/com/damoyeo/healthyLife/comment_sql.properties";
		String QUERY_PATH_MADANG_7= "/com/damoyeo/healthyLife/scheduler_sql.properties";
		String QUERY_PATH_MADANG_8= "/com/damoyeo/healthyLife/goal_sql.properties";
		String QUERY_PATH_MADANG_9= "/com/damoyeo/healthyLife/calorie_sql.properties";
		String QUERY_PATH_MADANG_10= "/com/damoyeo/healthyLife/product_sql.properties";
		String QUERY_PATH_MADANG_11= "/com/damoyeo/healthyLife/cart_sql.properties";
		String QUERY_PATH_MADANG_12= "/com/damoyeo/healthyLife/ordering_sql.properties";
		String QUERY_PATH_MADANG_13= "/com/damoyeo/healthyLife/postMessage_sql.properties";
		
		Map<String, String> queryMap = new HashMap<>();

		try {
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_0));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_1));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_2));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_3));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_4));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_5));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_6));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_7));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_8));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_9));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_10));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_11));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_12));
			queryMap.putAll(QueryLoader.instance().load(QUERY_PATH_MADANG_13));
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new ExceptionInInitializerError(ioe);
		}
		return queryMap;
	}
	
	//passwordEncoder
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
	


}
