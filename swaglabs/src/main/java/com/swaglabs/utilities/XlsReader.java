package com.swaglabs.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsReader {
    public String path;
    public FileInputStream fis = null;
    public FileOutputStream fileOut = null;
    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row = null;

    /** The cell. */
    private XSSFCell cell = null;

    /**
     * Method to read a Toast message.
     *
     */
    public XlsReader(String path) {

        this.path = path;
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } catch (Exception e) {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e1) {
                Log.error(e1.getMessage());
                Log.error(e1.toString());
            }
            Log.error(e.getMessage());
            Log.error(e.toString());
        }
    }

    /**
     * Method to get Row count.
     *
     * @param: sheetName
     *             the name of the sheet for which row count is required
     * @return: Integer value of the row count
     */
    public int getRowCount(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            return 0;
        } else {
            sheet = workbook.getSheetAt(index);
            int number = sheet.getLastRowNum() + 1;
            return number;
        }

    }

    /**
     * Method to get Celldata.
     *
     * @param: String
     *             sheetName name of the sheet for which data is to be extracted
     * @param: String
     *             colName column name
     * @param: int
     *             rowNum
     *
     * @return: String cell data for the cell
     */
    @SuppressWarnings("deprecation")
    public String getCellData(String sheetName, String colName, int rowNum) {
        try {
            if (rowNum <= 0) {
                return "";
            }

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1) {
                return "";
            }

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
                    colNum = i;
                }
            }
            if (colNum == -1) {
                return "";
            }

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null) {
                return "";
            }
            cell = row.getCell(colNum);

            if (cell == null) {
                return "";
            }
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

                String cellText = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double dt = cell.getNumericCellValue();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(dt));
                    cellText = String.valueOf(cal.get(Calendar.YEAR)).substring(2);
                    cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
                }

                return cellText;
            } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                return "";
            } else {
                return String.valueOf(cell.getBooleanCellValue());
            }

        } catch (Exception e) {
            Log.error(e.getMessage());
            Log.error(e.toString());
            {
                return "row " + rowNum + " or column " + colName + " does not exist in xls";
            }
        }
    }

    /**
     * Method to get Celldata.
     *
     * @param: String
     *             sheetName name of the sheet for which data is to be extracted
     * @param: String
     *             colName column name
     * @param: int
     *             rowNum
     *
     * @return: String cell data for the cell
     */
    @SuppressWarnings("deprecation")
    public String getCellData(String sheetName, int colNum, int rowNum) {
        try {
            if (rowNum <= 0) {
                return "";
            }

            int index = workbook.getSheetIndex(sheetName);

            if (index == -1) {
                return "";
            }

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null) {
                return "";
            }
            cell = row.getCell(colNum);
            if (cell == null) {
                return "";
            }

            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

                String cellText = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double dt = cell.getNumericCellValue();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(dt));
                    cellText = String.valueOf(cal.get(Calendar.YEAR)).substring(2);
                    cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;
                }

                return cellText;
            } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                return "";
            } else {
                return String.valueOf(cell.getBooleanCellValue());
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
            Log.error(e.toString());
            return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
        }
    }

    /**
     * Method to set Celldata.
     *
     * @param: String
     *             sheetName name of the sheet for which data is to be extracted
     * @param: int
     *             colNum column number
     * @param: int
     *             rowNum
     * @param: String
     *             data data to be updated in the sheet
     * @return: boolean confirmation
     */
    public boolean setCellData(String sheetName, int colNum, int rowNum, String data) {
        colNum = colNum - 1;
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if (rowNum <= 0) {
                return false;
            }

            int index = workbook.getSheetIndex(sheetName);
            if (index == -1) {
                return false;
            }

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null) {
                row = sheet.createRow(rowNum - 1);
            }

            cell = row.getCell(colNum);
            if (cell == null) {
                cell = row.createCell(colNum);
            }

            cell.setCellValue(data);

            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        } catch (Exception e) {
            Log.error(e.getMessage());
            Log.error(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Method to set Celldata.
     *
     * @param: String
     *             sheetName name of the sheet for which data is to be extracted
     * @param: String
     *             colName column name
     * @param: int
     *             rowNum
     * @param: String
     *             data data to be updated in the sheet
     * @return: boolean confirmation
     */
    public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if (rowNum <= 0) {
                return false;
            }

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1) {
                return false;
            }

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName)) {
                    colNum = i;
                }
            }
            if (colNum == -1) {
                return false;
            }

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null) {
                row = sheet.createRow(rowNum - 1);
            }

            cell = row.getCell(colNum);
            if (cell == null) {
                cell = row.createCell(colNum);
            }

            cell.setCellValue(data);

            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        } catch (Exception e) {
            Log.error(e.getMessage());
            Log.error(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Method to set Celldata.
     *
     * @param: String
     *             sheetName name of the sheet for which data is to be extracted
     * @param: String
     *             colName column name
     * @param: int
     *             rowNum
     * @param: String
     *             data data to be updated in the sheet
     * @return: boolean confirmation
     */
    public boolean setCellData(String sheetName, String colName, String rowNameForDataInsertion, String dataToBeUpdated) {
        try {
            int rowNum = getRowNumberByCellValue(sheetName, rowNameForDataInsertion);
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if (rowNum <= 0) {
                return false;
            }

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1) {
                return false;
            }

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName)) {
                    colNum = i;
                }
            }
            if (colNum == -1) {
                return false;
            }

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null) {
                row = sheet.createRow(rowNum - 1);
            }

            cell = row.getCell(colNum);
            if (cell == null) {
                cell = row.createCell(colNum);
            }

            cell.setCellValue(dataToBeUpdated);

            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        } catch (Exception e) {
            Log.error(e.getMessage());
            Log.error(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Method to set Celldata.
     *
     * @param: String
     *             sheetName name of the sheet for which data is to be extracted
     * @param: String
     *             colName column name
     * @param: int
     *             rowNum
     *
     * @param: url
     *             to set a URL in the data
     *
     * @return: String cell data for the cell
     */
    public boolean setCellData(String sheetName, String colName, int rowNum, String data, String url) {
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if (rowNum <= 0) {
                return false;
            }

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1) {
                return false;
            }

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName)) {
                    colNum = i;
                }
            }

            if (colNum == -1) {
                return false;
            }
            sheet.autoSizeColumn(colNum); // ashish
            row = sheet.getRow(rowNum - 1);
            if (row == null) {
                row = sheet.createRow(rowNum - 1);
            }

            cell = row.getCell(colNum);
            if (cell == null) {
                cell = row.createCell(colNum);
            }

            cell.setCellValue(data);

            // cell style for hyperlinks
            // by default hypelrinks are blue and underlined
            CellStyle hlinkstyle = workbook.createCellStyle();
            XSSFFont hlinkfont = workbook.createFont();
            hlinkfont.setUnderline(XSSFFont.U_SINGLE);
            hlinkfont.setColor(IndexedColors.BLUE.getIndex());
            hlinkstyle.setFont(hlinkfont);

            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            @SuppressWarnings("deprecation")
            XSSFHyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
            link.setAddress(url);
            cell.setHyperlink(link);
            cell.setCellStyle(hlinkstyle);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);

            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Method to add Sheet.
     *
     * @param: String
     *             sheetName name of the sheet which is to be added
     *
     */
    public boolean addSheet(String sheetname) {

        FileOutputStream fileOut = null;
        try {
            workbook.createSheet(sheetname);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            Log.error(e.toString());
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e1) {
                    Log.error(e1.toString());
                }
                return false;
            }
        }
        return true;

    }

    /**
     * Method to add Sheet.
     *
     * @param: String
     *             sheetName name of the sheet which is to be added
     *
     * @param: returns
     *             confirmation as boolean
     *
     */
    public boolean removeSheet(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            return false;
        }

        FileOutputStream fileOut;
        fileOut = null;
        try {
            workbook.removeSheetAt(index);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            Log.error(e.toString());
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e1) {
                    Log.error(e1.toString());
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Method to add Column.
     *
     * @param: String
     *             sheetName name of the sheet
     *
     * @param: String
     *             colName name of the column
     * @param: returns
     *             confirmation as boolean
     *
     */
    @SuppressWarnings("deprecation")
    public boolean addColumn(String sheetName, String colName) {
        // System.out.println("**************addColumn*********************");

        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            int index = workbook.getSheetIndex(sheetName);
            if (index == -1) {
                return false;
            }

            XSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);
            if (row == null) {
                row = sheet.createRow(0);
            }

            if (row.getLastCellNum() == -1) {
                cell = row.createCell(0);
            } else {
                cell = row.createCell(row.getLastCellNum());
            }

            cell.setCellValue(colName);
            cell.setCellStyle(style);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            Log.error(e.getMessage());
            Log.error(e.toString());
            return false;
        }

        return true;

    }

    /**
     * Method to remove Column.
     *
     * @param: String
     *             sheetName name of the sheet
     * @param: int
     *             colNum Column number to be deleted
     *
     * @param: returns
     *             confirmation as boolean
     *
     */
    @SuppressWarnings("deprecation")
    public boolean removeColumn(String sheetName, int colNum) {
        try {
            if (!isSheetExist(sheetName)) {
                return false;
            }
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            XSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            style.setFillPattern(HSSFCellStyle.NO_FILL);

            for (int i = 0; i < getRowCount(sheetName); i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    cell = row.getCell(colNum);
                    if (cell != null) {
                        cell.setCellStyle(style);
                        row.removeCell(cell);
                    }
                }
            }
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * Method to check if Sheet present.
     *
     * @param: String
     *             sheetName name of the sheet
     *
     * @param: returns
     *             confirmation as boolean
     *
     */
    public boolean isSheetExist(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            index = workbook.getSheetIndex(sheetName.toUpperCase());
            if (index == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Method to get Column count.
     *
     * @param: String
     *             sheetName name of the sheet
     *
     * @param: returns
     *             number of column in the sheet
     *
     */
    public int getColumnCount(String sheetName) {
        // check if sheet exists
        if (!isSheetExist(sheetName)) {
            return -1;
        }

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(0);

        if (row == null) {
            return -1;
        }

        return row.getLastCellNum();

    }

    /**
     * Method to get Cell Rownumber.
     *
     * @param: String
     *             sheetName name of the sheet
     * @param: String
     *             colName name of the column name
     * @param: String
     *             cellValue name of the cellValue to check
     *
     * @param: returns
     *             confirmation as boolean
     *
     */
    public int getCellRowNum(String sheetName, String colName, String cellValue) {

        for (int i = 2; i <= getRowCount(sheetName); i++) {
            if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
                return i;
            }
        }
        return -1;

    }

    public int getRowNumberByCellValue(String sheetName, String cellValue) {
        int rowNum = 0;
        for (rowNum = 0; rowNum <= getRowCount(sheetName); rowNum++) {
            for (int columnNum = 0; columnNum <= sheet.getRow(0).getLastCellNum(); columnNum++) {
                if (getCellData(sheetName, columnNum, rowNum).equalsIgnoreCase(cellValue)) {
                    return rowNum;
                }
            }
        }
        return rowNum;
    }

}
