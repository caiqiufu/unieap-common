package com.unieap.base.inf.element;

public class DeviceInfo {
	public String IMEI;
	public String OSType;
	public String OSVersion;
	public String APKVersion;
	public String networkType;
	public String resolution;
	public String brand;
	public String model;
	public String extParameters;

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public String getOSType() {
		return OSType;
	}

	public void setOSType(String oSType) {
		OSType = oSType;
	}

	public String getOSVersion() {
		return OSVersion;
	}

	public void setOSVersion(String oSVersion) {
		OSVersion = oSVersion;
	}

	public String getAPKVersion() {
		return APKVersion;
	}

	public void setAPKVersion(String aPKVersion) {
		APKVersion = aPKVersion;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getExtParameters() {
		return extParameters;
	}

	public void setExtParameters(String extParameters) {
		this.extParameters = extParameters;
	}

}
