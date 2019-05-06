package com.unieap.base.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.SYSConfig;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.vo.MessageVO;

@Service
public class LoadSysMessageDataHandler implements ConfigHandler {

	@Override
	public void deal(Map<String, Object> parameters) throws Exception {
		// TODO Auto-generated method stub
		loadMessage();
	}

	@Override
	public boolean reload() {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadMessage() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,name,value,app_name as appName,language,remark FROM unieap.mdm_message ");
		sql.append(" where tenant_id = ? and activate_flag ='Y' and language=?");
		List<?> datas = DBManager.getJT().query(sql.toString(),
				new Object[] { SYSConfig.getConfig().get("tenantId"), SYSConfig.getConfig().get("language") },
				new EntityRowMapper(MessageVO.class));
		if (datas != null && datas.size() > 0) {
			List<MessageVO> volist = (List<MessageVO>) datas;
			Map<String, MessageVO> messageList = new HashMap<String, MessageVO>();
			for (MessageVO vo:volist) {
				messageList.put(vo.getName() + "_" + vo.getLanguage(), vo);
			}
			UnieapCacheMgt.setMessageList(messageList);
		}
	}
}
