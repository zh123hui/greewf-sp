1.推送配置
在${EAS_Home}/server/properties下找wf_events.xml文件
  1)如果没有找到，将压缩包的wf_events.xml拷贝过去
  2)如果已经找到，编辑wf_events.xml文件，在<events>根节点下增加下面这一行并保存

。
    <event name='OnActivitiyInit' 

handler='com.gree.mobile.push4wf.PushActivityEventHandler'/> 

2.将greemobile文件夹拷贝到${EAS_Home}/server/properties下.
用文本编辑器打开greemobile/greedc_cloud.properties.修改对应的选择项

wspush.soap.address表示pushservice.wsdl配置文件中wsdl:service/wsdl:port/soap:address/location的属性值.
push.message.plugId表示调用推送接口IPushWebService.pushEasMessage()中的plugId参数.
push.message.title表示调用推送接口IPushWebService.pushEasMessage()中的title参数.