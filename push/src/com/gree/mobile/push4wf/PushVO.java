package com.gree.mobile.push4wf;

import com.kingdee.bos.Context;

public class PushVO {

//	private String toUserId;
	private String message;
	private Context dbContext;
	private String toUserNumber;
	private String fromUserNumber;
	private int failCount=0;

	public PushVO(Context dbContext, String toUserNumber, String message, String fromUserNumber) {
		super();
		this.toUserNumber = toUserNumber;
		this.message = message;
		this.dbContext = dbContext;
		this.fromUserNumber = fromUserNumber;
	}

	public void incrementFailCount(){
		failCount++;
	}
	
	public String getMessage() {
		return message;
	}

	public Context getContext4Db() {
		return dbContext;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

    public String getToUserNumber() {
        return toUserNumber;
    }

    public String getFromUserNumber() {
        return fromUserNumber;
    }

}
