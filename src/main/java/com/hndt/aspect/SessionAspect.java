package com.hndt.aspect;

import com.hndt.common.Const;
import com.hndt.common.ServerResponse;
import com.hndt.enums.ResultEnum;
import com.hndt.pojo.Manager;
import com.hndt.util.SysContentUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hystar
 * @date 2018/2/8
 */
@Aspect
@Component
public class SessionAspect {

    /**
     * 配置返回类型
     */
    private final static String STRING = "String";
    private final static String MAP = "Map";
    private final static String SERVER_RESPONSE = "ServerResponse";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(public * com.hndt.controller..*.*(..)) && !execution(public * com.hndt.controller.LoginController.*(..))")
    public void doPointcut() {
    }

    @Around("doPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("【AOP】处理http请求，判断用户是否登录");
        Object result = null;

        HttpServletRequest request = SysContentUtil.getRequest();
        HttpServletResponse response = SysContentUtil.getResponse();
        HttpSession session = SysContentUtil.getSession();

        // 获取URI
        String uri = request.getRequestURI().toString();
        logger.info("【AOP】URI：{}", uri);

        // 获取请求方法
        String method = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        logger.info("【AOP】请求方法：{}", method);

        // 获取请求参数的名称
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
        // 获取请求参数的值
        Object[] args = joinPoint.getArgs();
        Map<String, Object> nameAndArgs = new HashMap<>();
        for (int i = 0; i < args.length - 1; i++) {
            nameAndArgs.put(strings[i], args[i]);
            logger.info("【AOP】请求参数：{}", strings[i] + "=" + args[i]);
        }

        Manager manager = (Manager) session.getAttribute(Const.LITERAL_MANAGER);
        String redirectStr = "";
        if (manager == null) {
            redirectStr = "http://uc.hndt.com/login.xhtml";
        } else {
            result = joinPoint.proceed(args);
            return result;
        }

        String returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType().getSimpleName().toString();
        if (returnType.equals(STRING)) {
            return redirectStr;
        } else if (returnType.equals(MAP)) {
            Map<String, Object> resMap = new HashMap<>(16);
            resMap.put("error", ResultEnum.NEED_LOGIN.getCode());
            return resMap;
        } else if (returnType.equals(SERVER_RESPONSE)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getValue());
        } else {
            throw new Exception();
        }
    }

}
