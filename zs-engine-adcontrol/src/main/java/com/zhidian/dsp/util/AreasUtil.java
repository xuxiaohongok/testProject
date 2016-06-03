package com.zhidian.dsp.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Joiner;
import com.zhidian.common.util.CommonLoggerUtil;
 
public class AreasUtil {
	
	private static ConcurrentHashMap<String,String> mapCache = new ConcurrentHashMap<String, String>();
	
	static {
		init();
	}
	
	private static void init(){
		CommonLoggerUtil.addBaseLog("=================区域初始化============");
		try {
			String filePath = AreasUtil.class.getResource("/").getPath() + "areas.xlsx";
			InputStream input = new FileInputStream(filePath);
			Workbook wb  = new XSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);		//获得第一个表单
			int OK = sheet.getLastRowNum();
			String dspArea = null;
			String ipArea = null;
			for (int i=1; i<OK; i++) {
				//获得行数据
				Row row = sheet.getRow(i);
				dspArea = row.getCell(1).getStringCellValue().trim();
				ipArea = row.getCell(2).getStringCellValue();
				ipArea = ipArea != null ? ipArea.trim() : null;
				try {
					mapCache.put(dspArea, ipArea);
				} catch (Exception e) {
					System.out.println( dspArea + "=" + ipArea);
					e.printStackTrace();
				}
			}
			CommonLoggerUtil.addBaseLog("=================区域初始化结束============");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getArea(String dspArea) {
		return mapCache.get(dspArea);
	}
	
	public static String getAllAreas() {
		return Joiner.on(",").skipNulls().join(mapCache.values()).replace("|", ",");
	}
	
	public static void main(String[] args) {
//		System.out.println(getAarea("基隆市"));
		System.out.println(getAllAreas());
 	}
}