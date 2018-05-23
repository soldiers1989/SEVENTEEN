package com.seventeen.config;

import com.alibaba.fastjson.JSON;

import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.core.UniversalEnumConverterFactory;
import com.seventeen.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);


    // 统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                                 Object handler, Exception e) {
                Result<String> result = new Result<String>();
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                String message;
                if (e instanceof NoHandlerFoundException) {
                    result.setResultCode(ResultCode.NOT_FOUND.getCode()).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
                } else if (e instanceof ServletException) {
                    result.setResultCode(ResultCode.FAIL.getCode()).setMessage(e.getMessage());
                } else if (e instanceof ServiceException) {
                    ServiceException serviceException= (ServiceException)e;
                    result.setResultCode(serviceException.getError().getCode()).setMessage(message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s", request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(), handlerMethod.getMethod().getName(),
                            e.getMessage()));
                } else {
                    if (handler instanceof HandlerMethod) {
                        if (e instanceof DuplicateKeyException) {
                            result.setResultCode(ResultCode.FAIL.getCode());
                            message = String.format("记录不可重复，接口 [%s] 出现异常", request.getRequestURI());
                        } else if (e instanceof DataIntegrityViolationException) {
                            result.setResultCode(ResultCode.FAIL.getCode());
                            message = String.format("外键或非空约束，接口 [%s] 出现异常", request.getRequestURI());
                        } else if (e instanceof BadCredentialsException) {
                            result.setResultCode(ResultCode.FAIL.getCode());
                            message = String.format("账号或密码错误");
                        } else if (e instanceof MethodArgumentNotValidException) {
                            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
                            message = "";
                            List<ObjectError> allErrors = methodArgumentNotValidException.getBindingResult().getAllErrors();
                            for (ObjectError allError : allErrors) {
                                message = allError.getDefaultMessage();
                            }
                        } else {
                            message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s", request.getRequestURI(),
                                    handlerMethod.getBean().getClass().getName(), handlerMethod.getMethod().getName(),
                                    e.getMessage());
                        }
                    } else {
                        message = e.getMessage();
                    }
                    if (result.getResultCode() == 200) {
                        result.setResultCode(ResultCode.FAIL.getCode());
                    }
                    result.setMessage(message);
                    logger.error(message, e);
                }
                responseResult(response, result);
                return new ModelAndView();
            }

        });
    }

    // 解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*");
    }

    // 添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }

    //    客户端发送请求是枚举类转换
    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addConverterFactory(new UniversalEnumConverterFactory());
    }

    private void responseResult(HttpServletResponse response, Result<String> result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

}