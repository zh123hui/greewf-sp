package com.gree.mobile.push4wf.axisext;

import org.apache.axis.EngineConfigurationFactory;
import org.apache.axis.configuration.EngineConfigurationFactoryDefault;


public class EngineConfigurationFactoryDefaultGree extends EngineConfigurationFactoryDefault{

    public static EngineConfigurationFactory newFactory(Object param) {
        return EngineConfigurationFactoryDefault.newFactory(param);
    }
}
