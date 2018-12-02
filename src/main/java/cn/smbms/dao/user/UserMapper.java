package cn.smbms.dao.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.User;

public interface UserMapper {

	/**
	 * 
	 * 增加用户信息
	 * @param user
	 * @return 受影响行数
	 */
	public int add(User user) throws Exception;

	/**
	 * 通过userCode获取User
	 * @param userCode
	 * @return User对象
	 */
	public User getLoginUser(@Param("userCode") String userCode) throws Exception;

	/**
	 * 通过条件查询-userList
	 * service层要进行以下计算再入参：currentPageNo = (currentPageNo-1)*pageSize
	 * @param userName
	 * @param userRole
	 * @return User的List集合
	 */
	public List<User> getUserList(Map<String, Object> kvMap) throws Exception;
	
	/**
	 * 通过条件查询-用户表记录数
	 * @param userName
	 * @param userRole
	 * @return 受影响行数
	 */
	public int getUserCount(@Param("userName") String userName, @Param("userRole") int userRole) throws Exception;
	
	/**
	 * 通过userId删除user
	 * @param delId
	 * @return 受影响行数
	 */
	public int deleteUserById(@Param("delId") Integer delId) throws Exception; 
	
	/**
	 * 通过userId获取user
	 * @param id
	 * @return User对象
	 */
	public User getUserById(@Param("id") String id) throws Exception; 
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return 受影响行数
	 */
	public int modify(User user) throws Exception;
	
	/**
	 * 修改当前用户密码
	 * @param id
	 * @param pwd
	 * @return 受影响行数
	 */
	public int updatePwd(@Param("id") int id, @Param("pwd") String pwd) throws Exception;
}
