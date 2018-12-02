package cn.smbms.tools;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;

public class StringToBigDecimalConverter implements Converter<String, BigDecimal>{

	@Override
	public BigDecimal convert(String s) {
		System.out.println("StringToBigDecimalConverter===========");
		return new BigDecimal(s).setScale(2,BigDecimal.ROUND_DOWN);
	}
	

}
