package cn.smbms.service.user;

import java.util.List;

import cn.smbms.pojo.User;

public interface UserService {
	/**
	 * 增加用户信息
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public boolean add(User user) throws Exception;
	
	/**
	 * 用户登录
	 * @param userCode
	 * @param userPassword
	 * @return
	 * @throws Exception 
	 */
	public User login(String userCode,String userPassword) throws Exception;
	
	/**
	 * 根据条件查询用户列表
	 * @param queryUserName
	 * @param queryUserRole
	 * @return
	 * @throws Exception 
	 */
	public List<User> getUserList(String queryUserName,int queryUserRole,int currentPageNo, int pageSize) throws Exception;
	/**
	 * 根据条件查询用户表记录数
	 * @param queryUserName
	 * @param queryUserRole
	 * @return
	 * @throws Exception 
	 */
	public int getUserCount(String queryUserName,int queryUserRole) throws Exception;
	
	/**
	 * 根据userCode查询出User
	 * @param userCode
	 * @return
	 * @throws Exception 
	 */
	public User selectUserCodeExist(String userCode) throws Exception;
	
	/**
	 * 根据ID删除user
	 * @param delId
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteUserById(Integer delId) throws Exception;
	
	/**
	 * 根据ID查找user
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public User getUserById(String id) throws Exception;
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public boolean modify(User user) throws Exception;
	
	/**
	 * 根据userId修改密码
	 * @param id
	 * @param pwd
	 * @return
	 * @throws Exception 
	 */
	public boolean updatePwd(int id,String pwd) throws Exception;
}
