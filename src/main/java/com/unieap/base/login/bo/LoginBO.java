package com.unieap.base.login.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.unieap.base.SYSConfig;
import com.unieap.base.UnieapConstants;
import com.unieap.base.bo.BaseBO;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.utils.JSONUtils;
import com.unieap.base.vo.ButtonVO;
import com.unieap.base.vo.DicDataVO;
import com.unieap.base.vo.MenuVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Jan 5, 2011
 */
@Service
public class LoginBO extends BaseBO {

	public Map<String, List<MenuVO>> getUserMenu() throws Exception {
		List<MenuVO> menus = getUserMenuById(UnieapConstants.getUser().getId());
		Map<String, List<MenuVO>> attributes = new HashMap<String, List<MenuVO>>();
		attributes.put("menus", menus);
		return attributes;
	}

	/**
	 * @param userId
	 * @return List<MenuVO>
	 * @throws Exception
	 */
	public List<MenuVO> getUserMenuById(Integer userId) throws Exception {
		List<?> menus;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT mm.id as menuId,mm.menu_code as menuCode,mm.menu_name as menuName,parent_id as parentId,");
		sql.append("href,iconcls as iconCls,img_src as imgSrc,qtip,title,leaf_flag as leafFlag,");
		sql.append(" mm.id,mm.menu_name as text ");
		sql.append(" FROM mdm_menu mm ");
		if (StringUtils.equals(UnieapConstants.UNIEAP, UnieapConstants.getUser().getUserCode())) {
			sql.append(" where parent_id = 0 and activate_flag ='Y' order by mm.seq ASC ");
			menus = DBManager.getJT().query(sql.toString(), new EntityRowMapper(MenuVO.class));
		} else {
			String filterIds = getMenuFilterSql(userId);
			if (!StringUtils.isEmpty(filterIds)) {

				sql.append(" where mm.id in (" + filterIds + ") ");
			} else {
				sql.append(" where 1=1 ");
			}
			sql.append(" and parent_id = 0 and activate_flag ='Y' order by mm.seq ASC ");
			menus = DBManager.getJT().query(sql.toString(), new EntityRowMapper(MenuVO.class));
		}
		if (menus != null && menus.size() > 0) {
			for (Object menuvo : menus) {
				MenuVO vo = (MenuVO) menuvo;
				vo.setChildrenContainer(getChildrenList(userId, vo.getId()));
			}
		}
		return (List<MenuVO>) menus;
	}

	private List<MenuVO> getChildrenList(Integer userId, Integer parentId) {
		List<?> menus;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT mm.id as menuId,mm.menu_code as menuCode,mm.menu_name as menuName,parent_id as parentId,");
		sql.append("iconcls as iconCls,img_src as imgSrc,qtip,title,leaf_flag as leafFlag,href ");
		sql.append(" FROM mdm_menu mm ");
		if (StringUtils.equals(UnieapConstants.UNIEAP, UnieapConstants.getUser().getUserCode())) {
			sql.append(" where parent_id = ? ");
			sql.append("  and mm.activate_flag = 'Y'  order by mm.seq ASC ");
			menus = DBManager.getJT().query(sql.toString(), new Object[] { parentId },
					new EntityRowMapper(MenuVO.class));
		} else {
			String filterIds = getMenuFilterSql(userId);
			if (!StringUtils.isEmpty(filterIds)) {

				sql.append(" where mm.id in (" + filterIds + ") ");
			} else {
				sql.append(" where 1=1 ");
			}
			sql.append(" and parent_id = ? and mm.activate_flag = 'Y' order by mm.seq ASC ");
			menus = DBManager.getJT().query(sql.toString(), new Object[] { parentId },
					new EntityRowMapper(MenuVO.class));
		}
		return ((List<MenuVO>) menus);
	}

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<DicDataVO> getUserDicdata(Integer userId) throws Exception {
		List<?> dicdatas;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT dd.dic_code as dicCode,dd.dic_name as dicName,");
		sql.append(" dg.group_code as groupCode,dg.group_name as groupName,");
		sql.append(" dd.activate_flag as activateFlag ,dd.seq as seq ");
		sql.append(" FROM mdm_dic_group dg,mdm_dic_item dd ");
		if (StringUtils.equals(UnieapConstants.UNIEAP, UnieapConstants.getUser().getUserCode())) {
			sql.append("  where dg.id = dd.group_id and dd.activate_flag = 'Y' ");
			sql.append("  order by dg.group_name,dd.seq,dd.dic_name ASC ");
			dicdatas = DBManager.getJT().query(sql.toString(), new EntityRowMapper(DicDataVO.class));
		} else {
			String filterIds = getDicFilterSql(userId);
			sql.append("");
			if (!StringUtils.isEmpty(filterIds)) {

				sql.append(" where dd.id in (" + filterIds + ") ");
			} else {
				sql.append(" where 1=1 ");
			}
			sql.append("  and dg.id = dd.group_id and dd.activate_flag = 'Y' ");
			sql.append("  order by dg.group_name,dd.seq,dd.dic_name ASC ");
			dicdatas = DBManager.getJT().query(sql.toString(), new Object[] { SYSConfig.defaultLanguage },
					new EntityRowMapper(DicDataVO.class));
		}
		return (List<DicDataVO>) dicdatas;
	}

	public List<ButtonVO> getUserButtons(Integer userId) throws Exception {
		List<?> datas;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct dd.id as buttonId,dd.menu_Code as buttonCode,");
		sql.append("dd.menu_name as buttonName,true as abled ");
		sql.append(" FROM mdm_menu dd ");
		if (StringUtils.equals(UnieapConstants.UNIEAP, UnieapConstants.getUser().getUserCode())) {
			sql.append(" where  dd.activate_flag = 'Y' and dd.menu_type ='B' and dd.language =?");
			datas = DBManager.getJT().query(sql.toString(), new Object[] { SYSConfig.defaultLanguage },
					new EntityRowMapper(ButtonVO.class));
		} else {
			String filterIds = getButtonFilterSql(userId);
			sql.append("");
			if (!StringUtils.isEmpty(filterIds)) {

				sql.append(" where dd.id in (" + filterIds + ") ");
			} else {
				sql.append(" where 1=1 ");
			}

			sql.append(" and dd.menu_type ='B' and dd.language =? ");
			datas = DBManager.getJT().query(sql.toString(), new Object[] { SYSConfig.defaultLanguage },
					new EntityRowMapper(ButtonVO.class));
		}
		return (List<ButtonVO>) datas;
	}

	/**
	 * @param userId
	 * @param resType
	 * @return String
	 */
	public String getResFilterSql(Integer userId, String resType) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT mri.id FROM mdm_res_item mri,mdm_user_role mur,mdm_role_res mrr, mdm_role mr ");
		sql.append(" where mri.res_type_id=? and mr.role_type = 'N' and mur.user_id = ? and mur.role_id = mr.id ");
		sql.append(" and mr.activate_flag = ? and mur.role_id = mrr.role_id ");
		sql.append("and mrr.res_id = mri.id and mri.activate_flag =?");
		sql.append(" and mri.id not in( ");
		sql.append(" SELECT mri.id FROM mdm_res_item mri,mdm_user_role mur,mdm_role_res mrr, mdm_role mr");
		sql.append(" where mri.res_type_id=? and mr.role_type = 'O' and mur.user_id = ? ");
		sql.append("and mur.role_id = mr.id and mr.activate_flag = ?");
		sql.append(" and mur.role_id = mrr.role_id and mrr.res_id = mri.id and mri.activate_flag =?)");
		List<Map<String, Object>> datas = DBManager.getJT().queryForList(sql.toString(), new Object[] { resType, userId,
				UnieapConstants.YES, UnieapConstants.YES, resType, userId, UnieapConstants.YES, UnieapConstants.YES });
		StringBuffer ids = new StringBuffer();
		if (datas != null && datas.size() > 0) {
			for (Map<String, Object> data : datas) {
				ids.append(data.get("id")).append(",");
			}
			return ids.substring(0, ids.length() - 1);
		} else {
			return "";
		}
	}

	public String getMenuFilterSql(Integer userId) {
		return getResFilterSql(userId, "2");
	}

	public String getDicFilterSql(Integer userId) {
		return getResFilterSql(userId, "1");
	}

	public String getButtonFilterSql(Integer userId) {
		return getResFilterSql(userId, "2");
	}

	public void initUserButton() throws Exception {
		List<ButtonVO> datas = getUserButtons(UnieapConstants.getUser().getId());
		if (datas != null) {
			JSONObject jsonObj = new JSONObject();
			for (Object obj : datas) {
				ButtonVO value = (ButtonVO) obj;
				JSONObject json = JSONUtils.convertBean2JSON(value);
				jsonObj.put(value.getButtonCode(), json);
			}
			UnieapConstants.getUser().getCacheData().put(UnieapConstants.BUTTON_PERMISSION, jsonObj.toString());
		} else {
			UnieapConstants.getUser().getCacheData().put(UnieapConstants.BUTTON_PERMISSION, "{}");
		}
	}

	public void initUserDicdata() throws Exception {
		List<DicDataVO> datas = getUserDicdata(UnieapConstants.getUser().getId());
		if (datas != null) {
			JSONObject jsonObj = new JSONObject();
			JSONArray jagroup = null;
			JSONArray jaoptgroup = null;
			DicDataVO dicVo;
			for (Object vo : datas) {
				dicVo = (DicDataVO) vo;
				String groupCode =  "_" + dicVo.getGroupCode();
				String groupCodeOptional = "_" + dicVo.getGroupCode() + "Opt";
				if (!jsonObj.containsKey(groupCodeOptional)) {
					jaoptgroup = new JSONArray();
					JSONObject dic = new JSONObject();
					dic.put("dicCode", "");
					dic.put("dicName", "Please select...");
					dic.put("parentCode", "");
					jaoptgroup.add(dic);
				} else {
					jaoptgroup = (JSONArray) jsonObj.get(groupCodeOptional);
				}
				JSONObject dic = new JSONObject();
				dic.put("dicCode", dicVo.getDicCode());
				dic.put("dicName", dicVo.getDicName());
				dic.put("parentCode", dicVo.getGroupCode());
				jaoptgroup.add(dic);
				jsonObj.put(groupCodeOptional, jaoptgroup);
				if (!jsonObj.containsKey(groupCode)) {
					jagroup = new JSONArray();
				} else {
					jagroup = (JSONArray) jsonObj.get(groupCode);
				}
				jagroup.add(dic);
				jsonObj.put(groupCode, jagroup);
			}
			UnieapConstants.getUser().getCacheData().put(UnieapConstants.DIC_PERMISSION, jsonObj.toString());
		} else {
			UnieapConstants.getUser().getCacheData().put(UnieapConstants.DIC_PERMISSION, "{}");
		}
	}
}
