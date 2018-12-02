package cn.smbms.service.bill;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import cn.smbms.dao.bill.BillMapper;
import cn.smbms.pojo.Bill;

public class BillServiceImpl implements BillService {
	private BillMapper billMapper;
	
	@Override
	public boolean add(Bill bill) throws Exception {
		boolean flag = false;
		int i = 0; //接受数据库操作的返回值
		try {
			i = billMapper.add(bill);
			if(i > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rollback==================");
//			flag = false;
			throw e; //这里抛了就不会return了
		}
		return flag;
	}

	@Override
	public List<Bill> getBillList(Bill bill) throws Exception {
		List<Bill> billList = null;
		System.out.println("query productName ---- > " + bill.getProductName());
		System.out.println("query providerId ---- > " + bill.getProviderId());
		System.out.println("query isPayment ---- > " + bill.getIsPayment());
		
		try {
			billList = billMapper.getBillList(bill);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return billList;
	}

	@Override
	public boolean deleteBillById(String delId) throws Exception {
		boolean flag = false;
		int i = 0; //接受数据库操作的返回值
		try {
			i = billMapper.deleteBillById(delId);
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
	public Bill getBillById(String id) throws Exception {
		Bill bill = null;
		try {
			bill = billMapper.getBillById(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return bill;
	}

	@Override
	public boolean modify(Bill bill) throws Exception {
		boolean flag = false;
		int i = 0; //接受数据库操作的返回值
		try {
			i = billMapper.modify(bill);
			if(i > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("rollback==================");
			throw e;
		}
		return flag;
	}

	public BillMapper getBillMapper() {
		return billMapper;
	}

	public void setBillMapper(BillMapper billMapper) {
		this.billMapper = billMapper;
	}
}
