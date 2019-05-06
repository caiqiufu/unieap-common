package com.unieap.base.inf.bizservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.inf.handler.BizServiceBO;

//name="MyWebService1",  // 服务实现类的名称
//serviceName="MyWebServiceService1",  // 默认在发布的服务实现者的名称后面添加Service
//portName="MyWebServicePort1",   // 服务类型的名称: 默认在 发布的服务实现者(MyWebService) 后面添加 port
//targetNamespace="ws.client.test"    // 发布ws服务的命名空间,此空间默认为当前服务包路径的 "倒写"此名称也是 wsimport 命令生成 java类时默认的包路径 -p
//http://127.0.0.1:8800/esb/service/bizService?wsdl


@WebService(targetNamespace = "http://www.unieap.easy.com", name = "bizWS", serviceName = "bizService")
public class BizService {
	@WebResult(name = "responseInfo", targetNamespace = "http://www.unieap.easy.com")
	// 可以指定wsdl中的方法名，参数名和返回值
	@WebMethod(operationName = "queryInfo")
	public String queryInfo(@WebParam(name = "requestInfo") String requestInfo) {
		BizServiceBO bizServiceBO = (BizServiceBO) ApplicationContextProvider.getBean("bizServiceBO");
		return bizServiceBO.process(requestInfo, null);
	}

	@WebResult(name = "responseInfo", targetNamespace = "http://www.unieap.easy.com")
	@WebMethod(operationName = "bizHandle")
	public String bizHandle(@WebParam(name = "requestInfo") String requestInfo) {
		BizServiceBO bizServiceBO = (BizServiceBO) ApplicationContextProvider.getBean("bizServiceBO");
		return bizServiceBO.process(requestInfo, null);
	}
}