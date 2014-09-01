package com.gree.mobile.sp.sso;

import org.apache.log4j.Logger;

import com.kingdee.bos.Context;
import com.kingdee.bos.UserContextCallback;
import com.kingdee.bos.framework.session.BOSLoginException;
import com.kingdee.eas.base.permission.app.PermissionLoginModule;
import com.kingdee.eas.cp.eip.sso.EasMultiAuthManager;
import com.kingdee.eas.cp.eip.sso.IEasAuthHandler;

/**
 * 使用加密密码登录方式
 * 
 * @version 
 * @since 
 * @date 2013-12-5
 */
public class EncryptPasswordAuthHandler implements IEasAuthHandler {

    private static final Logger logger = Logger.getLogger(EncryptPasswordAuthHandler.class);

    static {
        init();
    }
    
    public static void init(){
        String authPatternName = "EncryptPasswordAuth_greewf";
        try{
            IEasAuthHandler authHandler = EasMultiAuthManager.getAuthHandler(authPatternName);
            if(authHandler instanceof EncryptPasswordAuthHandler){
                return ;
            }
            EasMultiAuthManager.registerAuthHandler(authPatternName, new EncryptPasswordAuthHandler());
            logger.info(String.format("成功注册[%s],认证模式为[%s]", EncryptPasswordAuthHandler.class.getName(), authPatternName));
        }catch (Throwable e) {
            logger.error("注册com.gree.mobile.sp.sso.EncryptPasswordAuthHandler失败", e);
        }
    }
    
    public String getEasUserNumber(Context context, String easUserNumber) throws BOSLoginException {
        return easUserNumber;
    }

    public boolean isVerifyEasUserPwd() {
        return false;
    }

    public boolean authenticate(UserContextCallback userCtxCallback, String easUserNumber, String password)
            throws BOSLoginException {
        if (password == null) {
            return false;
        }
        try {
            PermissionLoginModule permissionLoginModule = new PermissionLoginModule();
            userCtxCallback.setUserName(easUserNumber);
            permissionLoginModule._setUserPK(userCtxCallback, easUserNumber);
            boolean result = new EncryptPasswordVerifyStrategy().authenticate(userCtxCallback, easUserNumber, password);
            return result;
        } catch (Exception e1) {
            logger.error("检验用户名密码失败", e1);
            return false;
        }


    }

}
