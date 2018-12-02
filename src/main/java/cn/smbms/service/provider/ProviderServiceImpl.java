package cn.smbms.service.provider;


import java.util.List;

import org.apache.ibatis.session.SqlSession;

import cn.smbms.dao.bill.BillMapper;
import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.pojo.Provider;

public class ProviderServiceImpl implements ProviderService {
	
	private ProviderMapper providerMapper;
	private BillMapper billMapper;
	
	@Override
	public boolean add(Provider provider) throws Exception {
		boolean flag = false;
		int i = 0; //接受数据库操作的返回值
		try {
			i = providerMapper.add(provider);
			if(i > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rollback==================");
			throw e;
		}
		return flag;
	}

	@Override
	public List<Provider> getProviderList(String proName,String proCode) throws Exception {
		List<Provider> providerList = null;
		System.out.println("query proName ---- > " + proName);
		System.out.println("query proCode ---- > " + proCode);
		try {
			providerList = providerMapper.getProviderList(proName,proCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return providerList;
	}

	/**
	 * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
	 * 若订单表中无该供应商的订单数据，则可以删除
	 * 若有该供应商的订单数据，则不可以删除
	 * 返回值billCount
	 * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
	 * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
	 * 
	 * ---判断
	 * 如果billCount = -1 失败
	 * 若billCount >= 0 成功
	 */
	@Override
	public int deleteProviderById(String delId) throws Exception {
		int billCount = -1;
		try {
			billCount = billMapper.getBillCountByProviderId(delId);
			if(billCount == 0){
				providerMapper.deleteProviderById(delId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			billCount = -1;
			System.out.println("rollback==================");
			throw e;
		}
		return billCount;
	}

	@Override
	public Provider getProviderById(String id) throws Exception {
		Provider provider = null;
		try{
			provider = providerMapper.getProviderById(id);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return provider;
	}

	@Override
	public boolean modify(Provider provider) throws Exception {
		boolean flag = false;
		try {
			if(providerMapper.modify(provider) > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rollback==================");
			throw e;
		}
		return flag;
	}

	public ProviderMapper getProviderMapper() {
		return providerMapper;
	}

	public void setProviderMapper(ProviderMapper providerMapper) {
		this.providerMapper = providerMapper;
	}

	public BillMapper getBillMapper() {
		return billMapper;
	}

	public void setBillMapper(BillMapper billMapper) {
		this.billMapper = billMapper;
	}
}
