package com.unieap.base.vo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.unieap.base.UnieapConstants;
import com.unieap.base.utils.JSONUtils;

import net.sf.json.JSONObject;

/**
 * <p>
 * Description: [查询VO基类]
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: 深圳市天源迪科技术股份有限公司
 * </p>
 * 
 * @author <a href="mailto: caiqiufu@sohu.com">蔡秋伏</a>
 * @version $Revision$
 */
public class BaseVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final Log log = LogFactory.getLog(BaseVO.class);
	//深度clone
	public Object clone() {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeObject(this);
			out.close();
			ByteArrayInputStream bin = new ByteArrayInputStream(bout
					.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bin);
			Object ret = in.readObject();
			in.close();
			return ret;
		} catch (Exception e) {
			log.error("clone is exception,error message:"+e.getMessage());
		}
		return null;
	}
	public String toJsonString() throws Exception{
		JSONObject json = new JSONObject();
		try {
			 json = JSONUtils.convertBean2JSON(this);
		} catch (Exception e) {
			throw new Exception("data conver to json error,error:" + e.getMessage(), e);
		}
		return json.toString();
	}
	public Map<String,String> extAttris = new HashMap<String,String>();
	public Map<String, String> getExtAttris() {
		return extAttris;
	}
	public void setExtAttris(Map<String, String> extAttris) {
		this.extAttris = extAttris;
	}
	public String activateFlag;
	public Date createDate;
	public Date modifyDate;
	public String createBy;
	public String modifyBy;
	public String remark;
	public String activateFlagDesc;
	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicName("activateFlag", activateFlag);
		return activateFlagDesc;
	}
	public String getActivateFlag() {
		return activateFlag;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public String getModifyBy() {
		return modifyBy;
	}
	public String getRemark() {
		return remark;
	}
	public void setActivateFlag(String activateFlag) {
		this.activateFlag = activateFlag;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
