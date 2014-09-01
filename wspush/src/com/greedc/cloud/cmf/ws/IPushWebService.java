/**
 * IPushWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.greedc.cloud.cmf.ws;

public interface IPushWebService extends java.rmi.Remote {
    public java.lang.String sayYeahHeyJude(java.lang.String arg0) throws java.rmi.RemoteException;
    /**
     * 
     * @param senderId
     * @param receiverIds
     * @param expire
     * @param title
     * @param summary
     * @param plugId
     * @return
     * @throws java.rmi.RemoteException
     */
    public java.lang.String pushEasMessage(java.lang.String senderId, java.lang.String[] receiverIds, java.util.Calendar expire,
            java.lang.String title, java.lang.String summary, java.lang.String plugId)
            throws java.rmi.RemoteException;
}
