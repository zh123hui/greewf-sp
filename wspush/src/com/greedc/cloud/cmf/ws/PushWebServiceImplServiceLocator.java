/**
 * PushWebServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.greedc.cloud.cmf.ws;

import com.gree.mobile.push4wf.config.GCConfig;

public class PushWebServiceImplServiceLocator extends org.apache.axis.client.Service implements PushWebServiceImplService {

    public PushWebServiceImplServiceLocator() {
    }


    public PushWebServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PushWebServiceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PushWebServiceImplPort
    private java.lang.String PushWebServiceImplPort_address = GCConfig.get().getWebServiceImplPort_address();

    public java.lang.String getPushWebServiceImplPortAddress() {
        return PushWebServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PushWebServiceImplPortWSDDServiceName = "PushWebServiceImplPort";

    public java.lang.String getPushWebServiceImplPortWSDDServiceName() {
        return PushWebServiceImplPortWSDDServiceName;
    }

    public void setPushWebServiceImplPortWSDDServiceName(java.lang.String name) {
        PushWebServiceImplPortWSDDServiceName = name;
    }

    public IPushWebService getPushWebServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PushWebServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPushWebServiceImplPort(endpoint);
    }

    public IPushWebService getPushWebServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            PushWebServiceImplServiceSoapBindingStub _stub = new PushWebServiceImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getPushWebServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPushWebServiceImplPortEndpointAddress(java.lang.String address) {
        PushWebServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (IPushWebService.class.isAssignableFrom(serviceEndpointInterface)) {
                PushWebServiceImplServiceSoapBindingStub _stub = new PushWebServiceImplServiceSoapBindingStub(new java.net.URL(PushWebServiceImplPort_address), this);
                _stub.setPortName(getPushWebServiceImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("PushWebServiceImplPort".equals(inputPortName)) {
            return getPushWebServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("com.greedc.cloud.cmf.ws", "PushWebServiceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("com.greedc.cloud.cmf.ws", "PushWebServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PushWebServiceImplPort".equals(portName)) {
            setPushWebServiceImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
