package com.unieap.base.inf.element;

public class RequestInfo {
	public RequestHeader requestHeader;
	public RequestBody requestBody;
	public RequestHeader getRequestHeader() {
		if(requestHeader ==null){
			this.requestHeader = new RequestHeader();
		}
		return requestHeader;
	}
	public void setRequestHeader(RequestHeader requestHeader) {
		this.requestHeader = requestHeader;
	}
	public RequestBody getRequestBody() {
		if(requestBody==null){
			this.requestBody = new RequestBody();
		}
		return requestBody;
	}
	public void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
	}
	
	
}
