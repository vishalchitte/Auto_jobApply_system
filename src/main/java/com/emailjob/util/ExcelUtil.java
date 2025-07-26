package com.emailjob.util;

import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtil {

    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue()); // or .toString()
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return cell.toString().trim(); // fallback
        }
    }
}
