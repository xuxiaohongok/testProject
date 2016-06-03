package com.zhidian3g.nextad.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
public class ReadExcel001 {
	public static void main(String[] args) {
		init();
		System.out.println(getAarea("基隆市"));
 	}
	
	
	private static Map<String,String> mapCache = new HashMap<String, String>();
	private static void init(){
		try {
			InputStream input = new FileInputStream("D:\\MyEclipseWorkspace\\zs-engine-adcontrol\\src\\main\\resources\\areas.xlsx");	//建立输入流
			Workbook wb  = new XSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);		//获得第一个表单
			int OK = sheet.getLastRowNum();
			String dspArea = null;
			String ipArea = null;
			System.out.println("ok=" + OK);
			for (int i=1; i<OK; i++) {
				Row row = sheet.getRow(i);	//获得行数据
				dspArea = row.getCell(1).getStringCellValue();
				ipArea = row.getCell(2).getStringCellValue();
				try {
					mapCache.put(dspArea, ipArea);
				} catch (Exception e) {
					System.out.println( dspArea + "=" + ipArea);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getAarea(String dspArea) {
		return mapCache.get(dspArea);
	}
}