package cn.smbms.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import cn.smbms.dao.user.UserMapper;
import cn.smbms.pojo.User;

/**
 * service层捕获异常，进行事务处理
 * 事务处理：调用不同dao的多个方法，必须使用同一个connection（connection作为参数传递）
 * 事务完成之后，需要在service层进行connection的关闭，在dao层关闭（PreparedStatement和ResultSet对象）
 * @author Administrator
 */
public class UserServiceImpl implements UserService{
	private UserMapper userMapper; //声明UserMapper接口引用
	
	@Override
	public boolean add(User user) throws Exception {
		boolean flag = false;
		try {
			int updateRows = userMapper.add(user);
			if(updateRows > 0){
				flag = true;
				System.out.println("add success!");
			}else{
				System.out.println("add failed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rollback==================");
			throw e; 
		}
		return flag;
	}
	@Override
	public User login(String userCode, String userPassword) throws Exception {
		User user = null;
		try {
			user = userMapper.getLoginUser(userCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw e; 
		}
		
		//匹配密码
		if(null != user){
			if(!user.getUserPassword().equals(userPassword))
				user = null;
		}
		return user;
	}
	
	@Override
	public List<User> getUserList(String queryUserName,int queryUserRole,int currentPageNo, int pageSize) throws Exception {
		Map<String, Object> kvMap = new HashMap<String, Object>();
		List<User> userList = null;
		kvMap.put("userName", queryUserName);
		kvMap.put("userRole", queryUserRole);
		kvMap.put("currentPageNo", (currentPageNo-1)*pageSize);
		kvMap.put("pageSize", pageSize);
		System.out.println("queryUserName ---- > " + queryUserName);
		System.out.println("queryUserRole ---- > " + queryUserRole);
		System.out.println("currentPageNo ---- > " + currentPageNo);
		System.out.println("pageSize ---- > " + pageSize);
		try {
			userList = userMapper.getUserList(kvMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userList;
	}
	
	@Override
	public User selectUserCodeExist(String userCode) throws Exception {
		User user = null;
		try {
			user = userMapper.getLoginUser(userCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return user;
	}
	@Override
	public boolean deleteUserById(Integer delId) throws Exception {
		boolean flag = false;
		try {
			if(userMapper.deleteUserById(delId) > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rollback==================");
			throw e;
		}
		return flag;
	}
	@Override
	public User getUserById(String id) throws Exception {
		User user = null;
		try{
			user = userMapper.getUserById(id);
//			int a = 9/0;
		}catch (Exception e) {
			e.printStackTrace();
			user = null;
			throw e;
		}
		return user;
	}
	@Override
	public boolean modify(User user) throws Exception {
		boolean flag = false;
		try {
			int ok = userMapper.modify(user);
			if(ok > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rollback==================");
			throw e;
		}
		return flag;
	}
	
	@Override
	public boolean updatePwd(int id, String pwd) throws Exception {
		boolean flag = false;
		try{
			if(userMapper.updatePwd(id,pwd) > 0)
				flag = true;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("rollback==================");
			throw e;
		}
		return flag;
	}
	@Override
	public int getUserCount(String queryUserName, int queryUserRole) throws Exception {
		int count = 0;
		System.out.println("queryUserName ---- > " + queryUserName);
		System.out.println("queryUserRole ---- > " + queryUserRole);
		try {
			count = userMapper.getUserCount(queryUserName,queryUserRole);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return count;
	}
	
	public UserMapper getUserMapper() {
		return userMapper;
	}
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
}
