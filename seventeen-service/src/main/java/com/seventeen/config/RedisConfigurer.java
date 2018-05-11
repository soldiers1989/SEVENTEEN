//package com.seveteen.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//
//import java.lang.reflect.Method;
//
//@Configuration
//@EnableCaching
//public class RedisConfigurer extends CachingConfigurerSupport {
//
//	@Bean
//	@Override
//	public KeyGenerator keyGenerator() {
//		return new KeyGenerator() {
//			@Override
//			public Object generate(Object target, Method method, Object... params) {
//				StringBuilder sb = new StringBuilder();
//				sb.append(target.getClass().getName());
//				sb.append("_");
//				sb.append(method.getName());
//				sb.append("_");
//				for (Object obj : params) {
//					if (obj != null) {
//						sb.append(obj.toString());
//					}
//				}
//				return sb.toString();
//			}
//		};
//	}
//
//	@Bean
//	public CacheManager cacheManager(RedisTemplate<String, String> redisTemplate) {
//		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
//		// 设置缓存过期时间。单位秒
//		rcm.setDefaultExpiration(600);
//		return rcm;
//	}
//
//	/**
//	 * 设置redis序列化的格式
//	 *
//	 * @param factory
//	 * @return
//	 */
//	@Bean
//	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
//		StringRedisTemplate template = new StringRedisTemplate(factory);
//		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
//				Object.class);
//		ObjectMapper om = new ObjectMapper();
//		om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
//		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		jackson2JsonRedisSerializer.setObjectMapper(om);
//		template.setValueSerializer(jackson2JsonRedisSerializer);
//		template.afterPropertiesSet();
//		return template;
//	}
//}
