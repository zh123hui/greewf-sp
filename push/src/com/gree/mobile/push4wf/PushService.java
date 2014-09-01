package com.gree.mobile.push4wf;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.axis.AxisProperties;
import org.apache.axis.EngineConfigurationFactory;
import org.apache.axis.components.net.DefaultSocketFactory;
import org.apache.axis.components.net.SocketFactory;
import org.apache.axis.components.net.SocketFactoryFactory;
import org.apache.axis.configuration.EngineConfigurationFactoryDefault;
import org.apache.axis.configuration.EngineConfigurationFactoryServlet;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.discovery.tools.ClassUtils;
import org.apache.log4j.Logger;

import com.gree.mobile.push4wf.axisext.DefaultSocketFactoryExt;
import com.gree.mobile.push4wf.axisext.EngineConfigurationFactoryDefaultGree;
import com.gree.mobile.push4wf.config.GCConfig;
import com.greedc.cloud.cmf.ws.IPushWebService;
import com.greedc.cloud.cmf.ws.IPushWebServiceProxy;

public class PushService {

    private IPushWebService service;
    private static final Logger logger = Logger.getLogger(PushService.class);

    public PushService() {
        doTest();
        init();
        try {
            service = (new IPushWebServiceProxy()).getIPushWebService();
            logger.info("第一次成功实例化IPushWebService");
        } catch (Throwable e) {
            logger.error("第一次实例化IPushWebService失败,准备尝试第二次", e);
            try {
                AxisProperties.setClassDefaults(EngineConfigurationFactory.class, new String[] {
                        "org.apache.axis.configuration.EngineConfigurationFactoryServlet",
                        "org.apache.axis.configuration.EngineConfigurationFactoryDefault",
                        "com.gree.mobile.push4wf.axisext.EngineConfigurationFactoryDefaultGree", });
                service = (new IPushWebServiceProxy()).getIPushWebService();
                logger.info("第二次成功实例化IPushWebService");
            } catch (Throwable e2) {
                logger.error("第二次实例化IPushWebService失败,无法使用推送服务", e2);
            }
        }
        
    }

    private void init() {
        try {
            PushService.class.getClassLoader().loadClass(org.apache.axis.attachments.AttachmentsImpl.class.getName());
            PushService.class.getClassLoader().loadClass(org.apache.axis.attachments.Attachments.class.getName());
            PushService.class.getClassLoader().loadClass(org.apache.axis.components.net.DefaultSocketFactory.class.getName());
            PushService.class.getClassLoader().loadClass(org.apache.axis.components.net.SocketFactory.class.getName());
            setStaticField(AxisProperties.class, "loaders", ClassLoaders.getAppLoaders(PushService.class, null, true));
        } catch (Throwable e1) {
            logger.error("加载类org.apache.axis.*失败", e1);
        }
        
    }

    
    private static void doTest() {
        logger.info("查找jar包");
        try {
            Enumeration<URL> resources = org.apache.axis.client.Service.class.getClassLoader().getResources(
                    "simplelog.properties");
            if (resources.hasMoreElements()) {
                logger.info("找到org.apache.axis.client.Service类jar包");
            } else {
                logger.info("没有找到org.apache.axis.client.Service类jar包");
            }
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (url == null) {
                    continue;
                }
                logger.info("找到org.apache.axis.client.Service类jar包路径: " + url.getPath());
            }
        } catch (Throwable e) {
            logger.error("查找路径时出错啦!", e);
        }
        
        logger.info("findPublicStaticMethod测试");
        try {
            Method method = findPublicStaticMethod(org.apache.axis.configuration.EngineConfigurationFactoryDefault.class,
                    EngineConfigurationFactory.class, "newFactory", new Class[] { Object.class });
            if(method == null){
                logger.info("没找到EngineConfigurationFactoryDefault.newFactory");
            }else{
                logger.info("到EngineConfigurationFactoryDefault.newFactory");
            }
            method = findPublicStaticMethod(EngineConfigurationFactoryDefaultGree.class,
                    EngineConfigurationFactory.class, "newFactory", new Class[] { Object.class });
            if(method == null){
                logger.info("没找到EngineConfigurationFactoryDefaultGree.newFactory");
            }else{
                logger.info("找到EngineConfigurationFactoryDefaultGree.newFactory");
            }
            method = findPublicStaticMethod(org.apache.axis.configuration.EngineConfigurationFactoryServlet.class,
                    EngineConfigurationFactory.class, "newFactory", new Class[] { Object.class });
            if(method == null){
                logger.info("没找到EngineConfigurationFactoryServlet.newFactory");
            }else{
                logger.info("找到EngineConfigurationFactoryServlet.newFactory");
            }
            
        } catch (Throwable e) {
            logger.error("findPublicStaticMethod出错啦!", e);
        }
        logger.info("ClassUtils.findPublicStaticMethod测试");
        try {
            Method method = ClassUtils.findPublicStaticMethod(org.apache.axis.configuration.EngineConfigurationFactoryDefault.class,
                    EngineConfigurationFactory.class, "newFactory", new Class[] { Object.class });
            if(method == null){
                logger.info("没找到EngineConfigurationFactoryDefault.newFactory");
            }else{
                logger.info("找到EngineConfigurationFactoryDefault.newFactory");
            }
            method = ClassUtils.findPublicStaticMethod(EngineConfigurationFactoryDefaultGree.class,
                    EngineConfigurationFactory.class, "newFactory", new Class[] { Object.class });
            if(method == null){
                logger.info("没找到EngineConfigurationFactoryDefaultGree.newFactory");
            }else{
                logger.info("找到EngineConfigurationFactoryDefaultGree.newFactory");
            }
            method = ClassUtils.findPublicStaticMethod(org.apache.axis.configuration.EngineConfigurationFactoryServlet.class,
                    EngineConfigurationFactory.class, "newFactory", new Class[] { Object.class });
            if(method == null){
                logger.info("没找到EngineConfigurationFactoryServlet.newFactory");
            }else{
                logger.info("找到EngineConfigurationFactoryServlet.newFactory");
            }
            
        } catch (Throwable e) {
            logger.error("findPublicStaticMethod出错啦!", e);
        }
        
        logger.info("可用性测试");
        try {
            EngineConfigurationFactoryDefaultGree.newFactory(null);
            logger.info("EngineConfigurationFactoryDefaultGree.newFactory可用");
        } catch (Throwable e) {
            logger.error("EngineConfigurationFactoryDefaultGree.newFactory不可用", e);
        }
        try {
            EngineConfigurationFactoryDefault.newFactory(null);
            logger.info("EngineConfigurationFactoryDefault.newFactory可用");
        } catch (Throwable e) {
            logger.error("EngineConfigurationFactoryDefault.newFactory不可用", e);
        }
        try {
            EngineConfigurationFactoryServlet.newFactory(null);
            logger.info("EngineConfigurationFactoryServlet.newFactory可用");
        } catch (Throwable e) {
            logger.error("EngineConfigurationFactoryServlet.newFactory不可用", e);
        }
    }

    public static Method findPublicStaticMethod(Class clazz, Class returnType, String methodName, Class[] paramTypes) {
        boolean problem = false;
        Method method = null;
        // verify '<methodName>(<paramTypes>)' is directly in class.
        try {
            method = clazz.getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            problem = true;
            logger.info("Class " + clazz.getName() + ": missing method '" + methodName + "(...)", e);
        }

        // verify 'public static <returnType>'
        if (!problem
                && !(Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers()) && method.getReturnType() == returnType)) {
            if (logger.isDebugEnabled()) {
                if (!Modifier.isPublic(method.getModifiers())) {
                    logger.info(methodName + "() is not public");
                }
                if (!Modifier.isStatic(method.getModifiers())) {
                    logger.info(methodName + "() is not static");
                }
                if (method.getReturnType() != returnType) {
                    logger.info("Method returns: " + method.getReturnType().getName() + "@@"
                            + method.getReturnType().getClassLoader());
                    logger.info("Should return:  " + returnType.getName() + "@@" + returnType.getClassLoader());
                }
            }
            problem = true;
            method = null;
        }

        return method;
    }

    /**
     * 推送消息
     * @param vo
     * @throws Exception
     */
    public void doPush(PushVO vo) throws Exception {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try{
            Thread.currentThread().setContextClassLoader(PushService.class.getClassLoader());
            org.apache.axis.utils.ClassUtils.setClassLoader(org.apache.axis.components.net.DefaultSocketFactory.class.getName(), PushService.class.getClassLoader());
            org.apache.axis.utils.ClassUtils.setClassLoader(org.apache.axis.components.net.SocketFactory.class.getName(), PushService.class.getClassLoader());
            
            setSocketFactoryImpl();
            try{
                _doPush(vo);
            }catch (Throwable e) {
                logger.error("执行doPush失败 one, 准备第二次 ", e);
                Hashtable options = new Hashtable();
                options.put(DefaultSocketFactory.CONNECT_TIMEOUT,Integer.toString(600000));
                
                SocketFactory factory = SocketFactoryFactory.getFactory("http", options);
                if(factory==null){
                    Hashtable factories = getStaticField(SocketFactoryFactory.class, "factories", Hashtable.class);
                    if(factories!=null){
                        factories.put("http", new DefaultSocketFactoryExt(options));
                    }
                    logger.info("重新设置SocketFactoryFactory.factories");
                }
                _doPush(vo);
                logger.info("执行doPush成功 two");
            }
        }finally{
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
        
    }

    private void setSocketFactoryImpl() {
        try {
            Hashtable options = new Hashtable();
            options.put(DefaultSocketFactory.CONNECT_TIMEOUT,Integer.toString(600000));
            Class classes[] = new Class[] { Hashtable.class };
            Object objects[] = new Object[] { options };
            Object newInstance = AxisProperties.newInstance(org.apache.axis.components.net.SocketFactory.class, classes, objects);
            if(newInstance!=null){
                logger.info("SocketFactory.class实现类为"+newInstance.getClass().getName());
            }
            AxisProperties.setProperty("axis.socketFactory", DefaultSocketFactoryExt.class.getName());
            AxisProperties.setClassDefault(org.apache.axis.components.net.SocketFactory.class, DefaultSocketFactoryExt.class.getName());
            logger.info("成功设置SocketFactory.class");
            newInstance = AxisProperties.newInstance(org.apache.axis.components.net.SocketFactory.class, classes, objects);
            if(newInstance!=null){
                logger.info("SocketFactory.class实现类为"+newInstance.getClass().getName());
            }
        } catch (Throwable e1) {
            logger.error("设置SocketFactory.class失败", e1);
        }
    }


    private void _doPush(PushVO vo) throws RemoteException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        GCConfig gcConfig = GCConfig.get();
        String title = gcConfig.getPushMessageTitle();
        String plugId = gcConfig.getPushPlugId();
        service.pushEasMessage(vo.getFromUserNumber(), new String[] { vo.getToUserNumber() }, cal, title, vo.getMessage(),
                plugId);
    }

    private void setStaticField(Class clazz, String fieldName, Object value){
        try{
            Field declaredField = clazz.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            Object obj = new Object();
            declaredField.set(obj, value);
            logger.info("成功设置"+clazz.getName()+"."+fieldName);
        }catch (Exception e) {
            logger.error("设置"+clazz.getName()+"."+fieldName+"失败", e);
        }
    }
    private <T> T getStaticField(Class clazz, String fieldName, Class<T> fieldClazz){
        try{
            Field declaredField = clazz.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            Object obj = new Object();
            T t = (T)declaredField.get(obj);
            logger.info("成功获取"+clazz.getName()+"."+fieldName);
            return t;
        }catch (Exception e) {
            logger.error("获取"+clazz.getName()+"."+fieldName+"失败", e);
        }
        return null;
    }

}
