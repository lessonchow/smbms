package cn.smbms.service.role;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Role;

public class RoleServiceImpl implements RoleService{
	
	private RoleMapper roleMapper;
	
	@Override
	public List<Role> getRoleList() throws Exception {
		List<Role> roleList = null;
		try {
			roleList = roleMapper.getRoleList();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return roleList;
	}

	public RoleMapper getRoleMapper() {
		return roleMapper;
	}

	public void setRoleMapper(RoleMapper roleMapper) {
		this.roleMapper = roleMapper;
	}
	
}
