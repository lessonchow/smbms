package cn.smbms.dao.bill;

import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.Bill;

public interface BillMapper {
	/**
	 * 增加订单
	 * @param bill
	 */
	public int add(Bill bill) throws Exception;
	
	/**
	 * 通过查询条件获取供应商列表-模糊查询-getBillList
	 * @param bill
	 */
	public List<Bill> getBillList(Bill bill) throws Exception;
	
	/**
	 * 通过delId删除Bill
	 * @param delId
	 */
	public int deleteBillById(@Param("delId")String delId) throws Exception;
	
	/**
	 * 通过billId获取Bill
	 * @param id
	 */
	public Bill getBillById(@Param("id")String id) throws Exception;
	
	/**
	 * 修改订单信息
	 * @param bill
	 */
	public int modify(Bill bill) throws Exception;
	
	/**
	 * 根据供应商ID查询订单数量
	 * @param providerId
	 */
	public int getBillCountByProviderId(@Param("providerId")String providerId) throws Exception;
}
