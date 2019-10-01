package com.innovapptive.appium.utils;

import java.lang.reflect.*;
import java.util.Hashtable;

import org.testng.annotations.DataProvider;

import com.innovapptive.appium.base.BasePage;

/**
 * @author Ranjith Sanda
 *
 */
public class CommonDataProvider extends BasePage {

	//@DataProvider(name = "testData")
	public static Object[][] getData(Method m) {

		String sheetName = m.getName();

		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);

		Object[][] data = new Object[rows - 1][cols];

		for (int rowNum = 1; rowNum < rows; rowNum++) {

			try {
				for (int colNum = 0; colNum < cols; colNum++) {

					data[rowNum - 1][colNum] = excel.getCellData(sheetName, rowNum, colNum);

				}
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}

		}

		return data;

	}
	
	
	@DataProvider(name="commonTestData")
	  public static Object[][] getData(String sheetName){
		  
		  int rows = excel.getRowCount(sheetName);
			int cols = excel.getColumnCount(sheetName);

			Object[][] data = new Object[rows-1][1];

			Hashtable<String, String> table = null;
			
			for (int rowNum = 1; rowNum < rows; rowNum++) {
				
				table = new Hashtable<String, String>();

					for (int colNum = 0; colNum < cols; colNum++) {

						table.put(excel.getCellData(sheetName, 0, colNum),excel.getCellData(sheetName,rowNum, colNum ));
						
					}  data[rowNum-1][0]=table;
				} 
			return data;
	  }
	  

}

