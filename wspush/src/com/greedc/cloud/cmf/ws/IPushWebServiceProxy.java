package com.greedc.cloud.cmf.ws;

public class IPushWebServiceProxy implements IPushWebService {
  private String _endpoint = null;
  private IPushWebService iPushWebService = null;
  
  public IPushWebServiceProxy() {
    _initIPushWebServiceProxy();
  }
  
  public IPushWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initIPushWebServiceProxy();
  }
  
  private void _initIPushWebServiceProxy() {
    try {
      iPushWebService = (new PushWebServiceImplServiceLocator()).getPushWebServiceImplPort();
      if (iPushWebService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iPushWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iPushWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iPushWebService != null)
      ((javax.xml.rpc.Stub)iPushWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public IPushWebService getIPushWebService() {
    if (iPushWebService == null)
      _initIPushWebServiceProxy();
    return iPushWebService;
  }
  
  public java.lang.String sayYeahHeyJude(java.lang.String arg0) throws java.rmi.RemoteException{
    if (iPushWebService == null)
      _initIPushWebServiceProxy();
    return iPushWebService.sayYeahHeyJude(arg0);
  }
  
  public java.lang.String pushEasMessage(java.lang.String arg0, java.lang.String[] arg1, java.util.Calendar arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5) throws java.rmi.RemoteException{
    if (iPushWebService == null)
      _initIPushWebServiceProxy();
    return iPushWebService.pushEasMessage(arg0, arg1, arg2, arg3, arg4, arg5);
  }
  
  
}