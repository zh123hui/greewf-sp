package com.gree.mobile.sp.sso;

import java.util.Date;
import java.util.GregorianCalendar;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.UserContextCallback;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.eas.base.permission.Administrator;
import com.kingdee.eas.base.permission.IUser;
import com.kingdee.eas.base.permission.SecurityException;
import com.kingdee.eas.base.permission.UserException;
import com.kingdee.eas.base.permission.UserFactory;
import com.kingdee.eas.base.permission.UserInfo;
import com.kingdee.eas.base.permission.app.strategy.PasswordVerifyStrategy;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.util.app.ContextUtil;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.util.DateTimeUtils;
import com.kingdee.util.StringUtils;
/**
 * 
 * 与com.kingdee.eas.base.permission.app.strategy.CommonVerifyStrategy相同
 * 修改成使用加密后密码验证
 * @version 
 * @since 
 * @date 2013-12-5
 */
public class EncryptPasswordVerifyStrategy extends PasswordVerifyStrategy {

    public boolean authenticate(UserContextCallback userCtxCallback, String userName, String loginPassword)
            throws Exception {
        if (loginPassword == null) {
            throw new UserException(UserException.USER_OR_PASSWORD_IS_MOT_MATCH);
        }
        checkPasswordSpelling(loginPassword);
        
        Context ctx = userCtxCallback.getUserContext();
        UserInfo userInfo = ContextUtil.getCurrentUserInfo(ctx);
        
        if(userInfo.getPassword()==null){
            userInfo.setPassword("");
        }
        
        if (Administrator.isSuperCUAdmin(new ObjectUuidPK(userInfo.getId()))) {
            if (!loginPassword.equals(userInfo.getPassword())) {
                throw new UserException(UserException.USER_OR_PASSWORD_IS_MOT_MATCH);
            }
        } else {
            boolean isLogon = false;

            checkUserInfoValidity(userInfo);
            try {
                if (userInfo.isIsLocked()) {
                    int goneMinutes = (int) (System.currentTimeMillis() - userInfo.getLockedTime().getTime()) / 60000;
                    if (goneMinutes - 30 >= 0) {
                        autoUnlock(ctx, userInfo);
                        isLogon = loginPassword.equals(userInfo.getPassword());
                        if (!isLogon) {
                            if (userInfo.getSecurity().isNeedLocked()) {
                                if (userInfo.getSecurity().getLockCount() <= userInfo.getErrCount() + 1) {
                                    throw new UserException(UserException.USER_IS_LOCKED,
                                            new Object[] { new Integer(30) });
                                }

                                throw new UserException(UserException.USER_OR_PASSWORD_IS_MOT_MATCH);
                            }

                            throw new UserException(UserException.USER_OR_PASSWORD_IS_MOT_MATCH);
                        }

                    } else {
                        throw new UserException(UserException.USER_IS_LOCKED,
                                new Object[] { String.valueOf(30 - goneMinutes) });
                    }
                } else {
                    isLogon = loginPassword.equals(userInfo.getPassword());
                    if (!isLogon) {
                        if (userInfo.getSecurity().isNeedLocked()) {
                            if (userInfo.getSecurity().getLockCount() <= userInfo.getErrCount() + 1) {
                                throw new UserException(UserException.USER_IS_LOCKED, new Object[] { new Integer(30) });
                            }

                            throw new UserException(UserException.USER_OR_PASSWORD_IS_MOT_MATCH);
                        }

                        throw new UserException(UserException.USER_OR_PASSWORD_IS_MOT_MATCH);
                    }
                }

            } finally {
                handleAfterLogon(ctx, userInfo, isLogon);
            }
        }
        return true;
    }

    private void checkPasswordSpelling(String userPassword) throws UserException {
        if ((StringUtils.isEmpty(userPassword)) || (userPassword.indexOf("'") == -1))
            return;
        throw new UserException(UserException.PASSWORD_INCLUDE_FORBIDDEN_CHAR);
    }

    private void checkUserInfoValidity(UserInfo userInfo) throws EASBizException {
        if (userInfo.isIsForbidden()) {
            throw new UserException(UserException.USER_BEEN_FORBIDDEN);
        }

        Date currentDate = new Date(System.currentTimeMillis());
        if (DateTimeUtils.dayAfter(userInfo.getEffectiveDate(), currentDate)) {
            throw new UserException(UserException.USER_NOT_INUSE);
        }
        if (DateTimeUtils.dayAfter(currentDate, userInfo.getInvalidationDate())) {
            throw new UserException(UserException.USER_BEEN_INVALID);
        }

        if ((userInfo.getSecurity().isPasswordForever()) || (userInfo.getSecurity().isExpiredPassCanChg())) {
            return;
        }

        Date inuseDate = userInfo.getPWEffectiveDate();
        GregorianCalendar gcInuse = new GregorianCalendar();
        gcInuse.setTime(inuseDate);
        GregorianCalendar gcInvalid = new GregorianCalendar();
        gcInvalid.setTime(inuseDate);
        gcInvalid.add(5, userInfo.getSecurity().getPasswordCycle());
        Date invalidDate = new Date(gcInvalid.getTimeInMillis());
        Date now = new Date();
        if ((!DateTimeUtils.dayAfter(inuseDate, now)) && (!DateTimeUtils.dayAfter(now, invalidDate)))
            return;
        throw new SecurityException(SecurityException.PASSWORD_BEEN_INVALID);
    }

    private void autoUnlock(Context ctx, UserInfo userInfo) throws EASBizException, BOSException {
        IUser iUser = UserFactory.getLocalInstance(ctx);
        iUser.lockUsers(new ObjectUuidPK[] { new ObjectUuidPK(userInfo.getId()) }, false);
    }

    private void handleAfterLogon(Context ctx, UserInfo userInfo, boolean isLogon) throws BOSException {
        if (userInfo.getSecurity().isNeedLocked()) {
            String sql = null;
            if (isLogon) {
                Object[] params = { userInfo.getId().toString() };
                sql = "UPDATE T_PM_User SET FErrCount = 0, FLockedTime = NULL, FIsLocked = 0 WHERE FID = ? ";
                DbUtil.execute(ctx, sql, params);
            } else if (userInfo.getSecurity().getLockCount() <= userInfo.getErrCount() + 1) {
                Object[] params = { userInfo.getId().toString() };
                sql = "UPDATE T_PM_User SET FLockedTime = now(), FIsLocked = 1, FErrCount =  FErrCount + 1 WHERE FID = ? ";
                DbUtil.execute(ctx, sql, params);
            } else {
                Object[] params = { userInfo.getId().toString() };
                sql = "UPDATE T_PM_User SET FErrCount = FErrCount + 1 WHERE FID = ? ";
                DbUtil.execute(ctx, sql, params);
            }
        }

        if ((userInfo.getSecurity() == null) || (userInfo.getSecurity().isRequireChgPW()) || (userInfo.isChangedPW()))
            return;
        Object[] params = { userInfo.getId().toString() };
        String sql = "UPDATE T_PM_User SET FIsChangedPW = 1 WHERE FID = ? ";
        DbUtil.execute(ctx, sql, params);
    }

}
