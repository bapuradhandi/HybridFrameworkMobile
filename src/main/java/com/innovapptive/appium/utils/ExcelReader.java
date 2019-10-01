package com.innovapptive.appium.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.NotFoundException;

import com.innovapptive.appium.base.BasePage;

/**
 * This Excel Reader class has generic methods to perform various functions on
 * Test data sheet and returns the required test data
 * 
 * @author Ranjith Sanda
 *
 */
public class ExcelReader extends BasePage {
	private String workingdir = System.getProperty("user.dir");
	public String path = workingdir + "/src/test/resources/testdata/TestData.xlsx";
	private Workbook workbook = null;
	private Sheet sheet = null;

	public ExcelReader() {

		workbook = getWorkBook(path);
		sheet = workbook.getSheetAt(0);
	}

	/**
	 * This method returns workbook object of excel file
	 * 
	 * @param path is location of file in file System
	 * @return object of Type XSSFWorkbook
	 */
	public Workbook getWorkBook(String path) {
		Workbook workbook = null;
		File file = new File(path);
		if (file.exists()) {
			try {
				FileInputStream input = new FileInputStream(path);
				String extension = FilenameUtils.getExtension(path);
				workbook = extension.equalsIgnoreCase("xls") ? new HSSFWorkbook(input) : new XSSFWorkbook(input);
				input.close();
			} catch (IOException e) {
				throw new RuntimeException(String.format("Problem While opening the file(%s)", path), e);
			}
		} else {
			throw new NotFoundException(String.format("File(%s) is not exist..!!", path));
		}

		return workbook;
	}

	/**
	 * Depending upon sheet name it returns the sheet object
	 * 
	 * @param sheetName is name of sheet
	 * @return if sheet exist returns HSSF/XSSFSheet object else returns null.
	 */
	public Sheet getSheet(String sheetName) {
		if (workbook.getSheetName(workbook.getSheetIndex(sheet)).equals(sheetName)) {
			return sheet;
		}
		if (sheetName == null || sheetName.isEmpty()) {

		} else {
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1) {
				throw new NotFoundException(
						String.format("Sheet(%s) is not found in Excel Workbook(%s)", sheetName, path));
			} else {
				sheet = workbook.getSheetAt(index);
			}
		}
		return sheet;
	}

	/**
	 * Depending upon index it returns the sheet object
	 * 
	 * @param index - is index of sheet
	 * @return if sheet exist returns HSSF/XSSFSheet object else returns null.
	 */
	public Sheet getSheetAt(int index) {
		if (index < 0) {
			throw new NotFoundException(String.format("Sheet is not found @ index = %s", index));
		} else {
			sheet = workbook.getSheetAt(index);
		}
		return sheet;
	}

	/**
	 * Method returns cell contents as string
	 * 
	 * @param sheetName    name of the sheet
	 * @param columnNumber
	 * @param rowNumber
	 * @return returns cell contents as string
	 */
	public String getCellData(String sheetName, int rowNumber, int columnNumber) {
		String celldata = "";
		if (columnNumber >= 0 || rowNumber >= 0) {
			try {
				sheet = getSheet(sheetName);
				Row row = sheet.getRow(rowNumber);
				Cell cell = row.getCell(columnNumber);
				celldata = getCellContentAsString(cell);
			} catch (NullPointerException e) {
				log.error("Geting NullPointerException while reading cell => Sheet_Name=" + sheetName + " column="
						+ columnNumber + "  rownumber=" + rowNumber);
			} catch (Exception ex) {
				log.error("Geting exception while reading cell => Sheet_Name=" + sheetName + " column=" + columnNumber
						+ "  rownumber=" + rowNumber, ex);
			}

		} else {
			throw new RuntimeException("Invalid index..!! rowIndex= " + rowNumber + "  columnIndex=" + columnNumber);
		}
		return celldata;
	}

	/**
	 * Method returns cell contents as string
	 * 
	 * @param sheetName
	 * @param columnName
	 * @param rowNumber
	 * @return returns cell contents as string
	 */
	public String getCellData(String sheetName, int rowNumber, String columnName) {
		String celldata = "";
		sheet = getSheet(sheetName);
		int columnNumber = getColumnNumber(0, columnName);
		if (columnNumber >= 0 || rowNumber >= 0) {
			try {
				Row row = sheet.getRow(rowNumber);
				Cell cell = row.getCell(columnNumber);
				celldata = getCellContentAsString(cell);
			} catch (NullPointerException e) {
				log.error("Geting NullPointerException while reading cell => Sheet_Name=" + sheetName + " column="
						+ columnNumber + "  rownumber=" + rowNumber);

			} catch (Exception ex) {
				log.error("Geting exception while reading cell => Sheet_Name=" + sheetName + " column=" + columnNumber
						+ "  rownumber=" + rowNumber, ex);
			}
		} else {
			throw new RuntimeException("Invalid index..!! rowIndex= " + rowNumber + "  columnIndex=" + columnNumber);
		}
		return celldata;
	}

	/**
	 * Method returns the total number of column exist in sheet. This function
	 * consider the first row of the sheet as the column row
	 * 
	 * @param sheetName is the name of the sheet
	 * @return returns the column count
	 */
	public int getColumnCount(String sheetName) {
		sheet = getSheet(sheetName);
		Row row = sheet.getRow(0);
		if (row == null)
			return -1;
		return row.getLastCellNum();
	}

	/**
	 * Method returns the RowCount
	 * 
	 * @param sheetName
	 * @return
	 */
	public int getRowCount(String sheetName) {
		sheet = getSheet(sheetName);
		int number = sheet.getLastRowNum();
		return number + 1;
	}

	/**
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellContentAsString(Cell cell) throws Exception {
		String celldata = "";
		switch (cell.getCellType()) {

		case Cell.CELL_TYPE_BLANK:
			celldata = "";
			break;

		case Cell.CELL_TYPE_STRING:
			celldata = cell.getStringCellValue();
			break;

		case Cell.CELL_TYPE_NUMERIC:
			DataFormatter df = new DataFormatter();
			celldata = df.formatCellValue(cell);
			break;

		case Cell.CELL_TYPE_FORMULA:
			celldata = String.valueOf(cell.getNumericCellValue());
			break;

		case Cell.CELL_TYPE_BOOLEAN:
			celldata = String.valueOf(cell.getBooleanCellValue());
			break;

		default:
			celldata = cell.getStringCellValue();
			break;
		}
		return celldata;
	}

	/**
	 * 
	 * @param rowNum
	 * @param columnName
	 * @return
	 */
	public int getColumnNumber(int rowNum, String columnName) {
		int colNum = -1;
		if (rowNum >= 0 && (!columnName.isEmpty())) {
			Row row = sheet.getRow(rowNum);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = null;
				try {
					cell = row.getCell(i);
				} catch (NullPointerException e) {
					log.error(String.format("Cell number %s is not defined @rowNum = %s", i, rowNum));
				}

				if (cell != null && cell.getStringCellValue().trim().equalsIgnoreCase(columnName)) {
					colNum = i;
					break;
				}
			}
		}
		if (colNum == -1) {
			log.debug("Enable to find column " + columnName + " at row number " + rowNum);
		}
		return colNum;
	}

}