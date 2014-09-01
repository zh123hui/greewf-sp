package com.gree.mobile.push4wf.config;

import org.apache.log4j.Logger;


public class GCConfig extends PropertiesConfigBean {
    private static final Logger logger = Logger.getLogger(GCConfig.class);
    
	private static final GCConfig instance = new GCConfig();

	public static GCConfig get(){
		return instance;
	}
    private GCConfig() {
		super(System.getProperty("eas.properties.dir")+"/greemobile/greewf_to_greecloud.properties");
	}

    public String getWebServiceImplPort_address() {
        String url = getString("wspush.soap.address", true);
        logger.info(String.format("读取wspush.soap.address值为: %s", url));
        return url;
    }
    public String getPushPlugId() {
    	return getString("push.message.plugId", true);
    }
    public String getPushMessageTitle() {
    	return getString("push.message.title", "EAS工作流");
    }
    private String getString(String key, String defaultValue){
    	return getConfiguration().getString(key, defaultValue);
    }
}
