package com.gree.mobile.push4wf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.kingdee.bos.util.backport.concurrent.atomic.AtomicBoolean;

/**
 * 
 * 推送管理类
 * @version 
 * @since 
 * @date 2013-12-5
 */
public class PushManager {
	
	private static final Logger logger = Logger.getLogger(PushManager.class);
	private static final PushManager instance = new PushManager();
	/**
	 * 第一次推送队列
	 */
	private BlockingQueue<PushVO> pushQueue;
	/**
	 * 失败队列
	 */
	private BlockingQueue<PushVO> failQueue;
	private List<DestroyThread> threads=new ArrayList<DestroyThread>();
	/**
	 * 最大尝试次数
	 */
	private static final int MAX_FAIL_COUNT = 3;
	private static final PushVO STOP_PUSHVO = new PushVO(null, "", "", "");
	private PushService pushService;
	private PushManager() {
	    pushService = new PushService();
		pushQueue = new LinkedBlockingQueue<PushVO>(2000);
		failQueue = new LinkedBlockingQueue<PushVO>(2000);
		startTask(new FirstPushTask());
		startTask(new FailPushTask());
		bindShutdownHook();
	}
	private void bindShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				logger.info("开始执行线程钩子");
				for(DestroyThread t : PushManager.this.threads){
					IShutdownableRunnable target = t.getTarget();
					if(!target.shutdown()){
						try {
							t.interrupt();
						} catch (Exception e) {
						}
					};
				}
				threads.clear();
				logger.info("结束执行线程钩子");
			}
		});
	}
	public static final PushManager getManager(){
		return instance;
	}
	
	private void startTask(IShutdownableRunnable target){
		DestroyThread thread = new DestroyThread(target);
		thread.setDaemon(true);
		thread.start();
		logger.info(String.format("启动推送线程--class : ", target.getClass().getName()));
		threads.add(thread);
	}
	private static class DestroyThread extends Thread {
		private IShutdownableRunnable target;
		public DestroyThread(IShutdownableRunnable target) {
			super(target);
			this.target=target;
		}
		public IShutdownableRunnable getTarget() {
			return target;
		}
		
	}
	private static interface IShutdownableRunnable extends Runnable{
		public boolean shutdown();
	}
	
	private class FirstPushTask implements IShutdownableRunnable{

		private AtomicBoolean stop = new AtomicBoolean(false);
		public void run() {
			while (true) {
				if(stop.get()){
					return ;
				}
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					logger.error("push wf error", e);
				}
				if(stop.get()){
					return ;
				}
				PushVO vo = null;
				try {
					vo = PushManager.this.pushQueue.take();
					if(vo==STOP_PUSHVO || stop.get()){
						return ;
					}
					pushService.doPush(vo);
					logger.info(String.format("成功推送消息  %s 到用户: %s", vo.getMessage(), vo.getToUserNumber()));
					vo=null;
				} catch (Exception e) {
					vo.incrementFailCount();
					if(vo.getFailCount()<=MAX_FAIL_COUNT){
					    failQueue.offer(vo);
	                    logger.error("push wf error, and try again.", e);
                    }else{
                        logger.error("push wf error, not try again. finish", e);
                    }
					
				}catch (Throwable e) {
					logger.warn("push wf warn, and down", e);
					break;
				}
				if(stop.get()){
					return ;
				}
			}			
		}

		public boolean shutdown() {
			stop.set(true);
			BlockingQueue<PushVO> q = PushManager.this.pushQueue;
			if(q.size()==0){
				q.add(STOP_PUSHVO);
				return true;
			}
			return false;
		}
	}
	
	private class FailPushTask implements IShutdownableRunnable{
		private AtomicBoolean stop = new AtomicBoolean(false);
		public void run() {
			while (true) {
				if(stop.get()){
					return ;
				}
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					logger.error("push wf error", e);
				}
				if(stop.get()){
					return ;
				}
				PushVO vo = null;
				try {
					vo = PushManager.this.failQueue.take();
					if(vo==STOP_PUSHVO || stop.get()){
						return ;
					}
					pushService.doPush(vo);
					logger.info(String.format("成功推送消息  %s 到用户: %s , FailCount=%s", vo.getMessage(), vo.getToUserNumber(), vo.getFailCount()));
					vo=null;
				} catch (Exception e) {
					if(vo.getFailCount()==MAX_FAIL_COUNT){
						logger.error("push wf error, and finish.", e);
						continue;
					}
					vo.incrementFailCount();
					failQueue.offer(vo);
					logger.error("push wf error, and try again.", e);
				}catch (Throwable e) {
					logger.warn("push wf warn, and down", e);
					break;
				}
				if(stop.get()){
					return ;
				}
			}			
		}

		public boolean shutdown() {
			stop.set(true);
			BlockingQueue<PushVO> q = PushManager.this.failQueue;
			if(q.size()==0){
				q.add(STOP_PUSHVO);
				return true;
			}
			return false;
		}
	}
	
	public boolean add(PushVO bean){
		return pushQueue.offer(bean);
	}
}
