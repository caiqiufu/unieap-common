package com.unieap.base.vo;

/**
 * @author caibo
 *
 */
public class ResourceVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String resCode;
	private String resName;
	private Integer resTypeId;
	private String resTypeCode;
	private String resTypeName;
	
	public ResourceVO(){
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getResTypeId() {
		return resTypeId;
	}

	public void setResTypeId(Integer resTypeId) {
		this.resTypeId = resTypeId;
	}

	public String getResTypeCode() {
		return resTypeCode;
	}

	public void setResTypeCode(String resTypeCode) {
		this.resTypeCode = resTypeCode;
	}

	public String getResTypeName() {
		return resTypeName;
	}

	public void setResTypeName(String resTypeName) {
		this.resTypeName = resTypeName;
	}

	public String getResCode() {
		return resCode;
	}
	public String getResName() {
		return resName;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	
}
