package com.zous.catmaster.interceptor;

import com.zous.catmaster.annotation.Frequency;
import com.zous.catmaster.bean.FrequencyBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * 对请求进行限制，过滤是否过量请求
 */
public class FrequencyInterceptor implements HandlerInterceptor {
    private static final int MAX_BASE_STATION_SIZE = 100000;
    private static Map<String, FrequencyBean> BASE_STATION = new HashMap<>(MAX_BASE_STATION_SIZE);
    private Object syncRoot = new Object();

    private static final float SCALE = 0.75F;
    private static final int MAX_CLEANUP_COUNT = 3;
    private static final int CLEANUP_INTERVAL = 1000;
    private int cleanupCount = 0;
    private Logger logger = LoggerFactory.getLogger(FrequencyInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Frequency methodAnnotation = ((HandlerMethod)handler).getMethodAnnotation(Frequency.class);
        Frequency classAnnotation =  ((HandlerMethod)handler).getBean().getClass().getAnnotation(Frequency.class);
        boolean going = true;
        if(classAnnotation != null) {
            going = handleFrequency(request, response, classAnnotation);
        }

        if(going && methodAnnotation != null) {
            going = handleFrequency(request, response, methodAnnotation);
        }
        return going;
    }

    private boolean handleFrequency(HttpServletRequest request, HttpServletResponse response, Frequency frequency) {
        boolean going = true;
        if(frequency == null) {
            return going;
        }

        String name = frequency.name();
        int limit = frequency.limit();
        int time = frequency.time();

        if(time == 0 || limit == 0) {
            going = false;
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return going;
        }

        long currentTimeMilles = System.currentTimeMillis() / 1000;

        String ip = getRemoteIp(request);
        String key = ip + "_" + name;
        FrequencyBean frequencyBean = BASE_STATION.get(key);

        if(frequencyBean == null) {

            frequencyBean = new FrequencyBean();
            frequencyBean.setUniqueKey(name);
            frequencyBean.setStart(currentTimeMilles);
            frequencyBean.setEnd(currentTimeMilles);
            frequencyBean.setLimit(limit);
            frequencyBean.setTime(time);
            frequencyBean.getAccessPoints().add(currentTimeMilles);

            synchronized (syncRoot) {
                BASE_STATION.put(key, frequencyBean);
            }
            if(BASE_STATION.size() > MAX_BASE_STATION_SIZE * SCALE) {
                cleanup(currentTimeMilles);
            }
        } else {
            frequencyBean.setEnd(currentTimeMilles);
            frequencyBean.getAccessPoints().add(currentTimeMilles);
        }

        //时间是否有效
        if(frequencyBean.getEnd() - frequencyBean.getStart() >= time) {
            if(logger.isDebugEnabled()) {
                logger.debug("frequency struct be out of date, struct will be reset., struct: {}", frequencyBean.toString());
            }
            frequencyBean.reset(currentTimeMilles);
        } else {
            int count = frequencyBean.getAccessPoints().size();
            if(count > limit) {
                if(logger.isDebugEnabled()) {
                    logger.debug("key: {} too frequency. count: {}, limit: {}.", key, count, limit);
                }
                going = false;
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        return going;
    }

    private void cleanup(long currentTimeMilles) {
        synchronized (syncRoot) {
            Iterator<String> it = BASE_STATION.keySet().iterator();
            while(it.hasNext()) {

                String key = it.next();
                FrequencyBean frequencyBean = BASE_STATION.get(key);
                if((currentTimeMilles - frequencyBean.getEnd()) > frequencyBean.getTime()) {
                    it.remove();
                }
            }

            if((MAX_BASE_STATION_SIZE - BASE_STATION.size()) > CLEANUP_INTERVAL) {
                cleanupCount = 0;
            } else {
                cleanupCount++;
            }

            if(cleanupCount > MAX_CLEANUP_COUNT ) {
                randomCleanup(MAX_CLEANUP_COUNT);
            }
        }
    }

    /**
     * 随机淘汰count个key
     *
     * @param count
     */
    private void randomCleanup(int count) {
        //防止调用错误
        if(BASE_STATION.size() < MAX_BASE_STATION_SIZE * SCALE) {
            return;
        }

        Iterator<String> it = BASE_STATION.keySet().iterator();
        Random random = new Random();
        int tempCount = 0;

        while(it.hasNext()) {
            if(random.nextBoolean()) {
                it.remove();
                tempCount++;
                if(tempCount >= count) {
                    break;
                }
            }
        }
    }

    private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
