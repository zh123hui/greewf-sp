package com.gree.mobile.push4wf;

import java.util.List;
import java.util.Locale;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.gree.mobile.push4wf.config.GCConfig;
import com.kingdee.bos.Context;
import com.kingdee.bos.workflow.AssignmentInfo;
import com.kingdee.bos.workflow.define.ActivityDef;
import com.kingdee.bos.workflow.define.ActivityEvent;
import com.kingdee.bos.workflow.define.extended.ApproveActivityDef;
import com.kingdee.bos.workflow.define.extended.ManualDecisionActivityDef;
import com.kingdee.bos.workflow.enactment.IWfActivity;
import com.kingdee.bos.workflow.enactment.IWfAssignment;
import com.kingdee.bos.workflow.enactment.handler.AbstractActivityEventHandler;
import com.kingdee.bos.workflow.enactment.handler.IActivityEventHandler;
import com.kingdee.eas.util.app.DbUtil;

/**
 * 工作流流程监听处理器
 * 
 * @version 
 * @since 
 * @date 2013-12-5
 */
public class PushActivityEventHandler extends AbstractActivityEventHandler implements
		IActivityEventHandler {

	private static final long serialVersionUID = -6226559803422674294L;
	private static final Logger logger = Logger.getLogger(PushActivityEventHandler.class.getName());

	public void execute(ActivityEvent event, IWfActivity activity) throws Exception {
		if (!ActivityEvent.OnActivitiyInit.equals(event)) {
			return;
		}
		
		ActivityDef actDef = activity.getDefinition();
		if (!(actDef instanceof ApproveActivityDef || actDef instanceof ManualDecisionActivityDef)) {
			return;
		}
		try {
			List list = activity.getAssignments();
			if (list == null || list.size() == 0) {
				return;
			}
			Context context = activity.getContext();
			Context dbContext = buildDbContext(context);
			String formUserNumber = context.getUserName();
			String type = GCConfig.get().getString("push.message.sender.type");
            if("initiator".equalsIgnoreCase(type)){
			    String initiatorId = activity.getProcess().getInstanceInfo().getInitiatorId();
			    formUserNumber = getUserNumber(dbContext, initiatorId);
			}
			Locale locale = context.getLocale();
			for (Object o : list) {
				IWfAssignment assign = (IWfAssignment) o;
				AssignmentInfo info = assign.getAssignmentInfo();
				String userId = info.getUserId();
				String subject = info.getSubject(locale);
				String name = info.getUserName(locale);
				String performerUserName = info.getPerformerUserName(locale);
				String userNumber = getUserNumber(dbContext, userId);
				logger.info(String.format("添加一条推送任务userNumber=%s, name=%s, performerUserName=%s, userId=%s, subject=%s", userNumber, name, performerUserName, userId, subject));
                PushVO vo = new PushVO(dbContext, userNumber, subject, formUserNumber);
                boolean add = PushManager.getManager().add(vo);
				if(!add){
					logger.error("[push-工作流移动推送]失败: 等待推送的消息过多,丢失此次推送");
				}
			}
		} catch (Throwable e) {
			logger.error("[push-工作流移动推送]异常: ", e);
		}
	}
	
	private String getUserNumber(Context dbContext, String userId){
	    String sql = " SELECT U.FNumber FROM T_PM_User U WHERE U.FID =  ? AND U.FIsDelete = 0  ";
        try{
            RowSet rowSet = DbUtil.executeQuery(dbContext, sql, new Object[]{userId});
            if (rowSet.next()){
                return rowSet.getString("FNumber");
            }
        }catch (Exception e) {
            logger.error(String.format("获取ID=[%s]的用户number失败", userId), e);
        }
        return null;
	}
	private Context buildDbContext(Context userContext){
		return new Context(userContext.getCaller(), userContext.getSolution(), userContext.getAIS(), userContext.getLocale());
	}

}
