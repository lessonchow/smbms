package cn.smbms.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String, Date>{

//	private String DatePtr1 = "yyyy-MM-dd";
//	private String DatePtr2 = "yyyy-MM-dd";
//	private String DatePtr3 = "yyyy-MM-dd";
	
	@Override
	public Date convert(String dateStr) {
		System.out.println("StringToDateConverter===========");
		// 将字符串参数转换为指定格式(yyyy-MM-dd)
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat[] sdfArray={simpleDateFormat1,simpleDateFormat2,simpleDateFormat3};
		for(SimpleDateFormat sdf:sdfArray){
			// 成功返回需要的格式
			try {
				return sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
				continue;
			}
		}
		// 如果失败返回null
		return null;
	}
}
