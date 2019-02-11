import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class export {
	int[][] dpop;
	String[][] export;
	String[] volNames;
	String fileName;
	String[] startEndTimes;

	public export(int[][] schedule, String[] names, String file, String[] startEnd) {
		dpop = schedule;
		export = new String[dpop.length + 1][dpop[0].length];
		volNames = names;
		fileName = file;
		startEndTimes = startEnd;
	}

	public void process() {
		// Add verticle lines for repeated activities
		for (int i = 0; i < dpop.length; i++) {
			for (int a = 0; a < dpop[0].length; a++) {
				if (i > 1 && i % 2 == 1 && dpop[i - 1][a] == dpop[i][a] && dpop[i][a] != 0) {
					export[i][a] = "|";
				} else {
					export[i][a] = getName(dpop[i][a]);
				}
			}
		}

		// Add xxxx to bottom of dpop
		for (int i = 0; i < export[0].length; i++) {
			export[export.length - 1][i] = "xxxx";
		}

		// Add times to when people stop
		for (int i = 0; i < export[0].length; i++) {
			for (int a = 0; a < export.length - 1; a++) {
				if (!export[a][i].equals("xxxx") && export[a + 1][i].equals("xxxx")) {
					export[a + 1][i] = clockTime(a + 1);
					break;
				}
			}
		}

		// Remove the "xxxx"
		for (int i = 0; i < export[0].length; i++) {
			for (int a = 0; a < export.length; a++) {
				if (export[a][i].equals("xxxx")) {
					export[a][i] = " ";
				}
			}
		}

	}

	public void toExcel() {
		Workbook wb;
		wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("DPOP");
		sheet.setHorizontallyCenter(true);
		sheet.setPrintGridlines(true);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		CellStyle atrium = wb.createCellStyle();
		atrium.setAlignment(HorizontalAlignment.CENTER);
		atrium.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
		atrium.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle planet = wb.createCellStyle();
		planet.setAlignment(HorizontalAlignment.CENTER);
		planet.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		planet.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle movie = wb.createCellStyle();
		movie.setAlignment(HorizontalAlignment.CENTER);
		movie.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		movie.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle dinos = wb.createCellStyle();
		dinos.setAlignment(HorizontalAlignment.CENTER);
		dinos.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
		dinos.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle lunch = wb.createCellStyle();
		lunch.setAlignment(HorizontalAlignment.CENTER);
		lunch.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		lunch.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle greet = wb.createCellStyle();
		greet.setAlignment(HorizontalAlignment.CENTER);
		greet.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
		greet.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle amnh = wb.createCellStyle();
		amnh.setAlignment(HorizontalAlignment.CENTER);
		amnh.setFillForegroundColor(IndexedColors.PLUM.getIndex());
		amnh.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle bottom = wb.createCellStyle();
		bottom.setBorderBottom(BorderStyle.THICK);

		CellStyle right = wb.createCellStyle();
		right.setAlignment(HorizontalAlignment.CENTER);
		right.setBorderRight(BorderStyle.THICK);

		CellStyle left = wb.createCellStyle();
		left.setAlignment(HorizontalAlignment.CENTER);
		left.setBorderLeft(BorderStyle.THICK);

		Row row;
		Cell cell;

		row = sheet.createRow(0);
		cell = row.createCell(1);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Guest Services Volunteers");

		// sheet.addMergedRegion(new CellRangeAddress(0,0,1,3));

		cell = row.createCell(4);
		cell.setCellStyle(cellStyle);
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("EEEEEEEEEE, MMMMMMMMMMMMMM dd, yyyy");
		cell.setCellValue(formatter.format(now));

		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Name");
		for (int i = 0; i < volNames.length; i++) {
			cell = row.createCell(i + 1);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(volNames[i]);
		}

		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Shift");
		for (int i = 0; i < startEndTimes.length; i++) {
			cell = row.createCell(i + 1);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(startEndTimes[i]);
		}

		row = sheet.createRow(3);
		cell = row.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Notes");
		for (int i = 0; i < startEndTimes.length; i++) {
			cell = row.createCell(i + 1);
			cell.setCellStyle(bottom);
		}

		String activityName = "";
		int rownum = 4;
		for (int i = 0; i < export.length; i++, rownum++) {
			row = sheet.createRow(rownum);
			cell = row.createCell(0);
			cell.setCellStyle(right);
			cell.setCellValue(clockTime(i));
			for (int j = 0; j < export[i].length; j++) {

				cell = row.createCell(j + 1);
				cell.setCellValue(export[i][j]);

				if (export[i][j].equals("|"))
					activityName = export[i - 1][j];
				else
					activityName = export[i][j];

				cell.setCellStyle(cellStyle);
				if (activityName.equals("Planet"))
					cell.setCellStyle(planet);
				if (activityName.equals("Movie"))
					cell.setCellStyle(movie);
				if (activityName.equals("Atrium"))
					cell.setCellStyle(atrium);
				if (activityName.equals("Dinos"))
					cell.setCellStyle(dinos);
				if (activityName.equals("Greet"))
					cell.setCellStyle(greet);
				if (activityName.equals("Lunch"))
					cell.setCellStyle(lunch);
				if (activityName.equals("AMNH Doors"))
					cell.setCellStyle(amnh);
			}
			cell = row.createCell(export[i].length + 1);
			cell.setCellStyle(left);
			cell.setCellValue(clockTime(i));
		}
		row = sheet.createRow(rownum);
		cell = row.createCell(0);
		cell.setCellValue("Generated by DPOP Crafter Extreme ©2019");
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("ERROR trying to create file");
			}
		}
		try (FileOutputStream out = new FileOutputStream(file)) {
			wb.write(out);
		} catch (IOException e) {
			System.out.println("ERROR Trying to write");
		}
		try {
			wb.close();
		} catch (IOException e) {
			System.out.println("ERROR TRING TO CLOSE");
		}
	}

	public String getName(int volunteerAssignment) {
		String result = "";
		String[] roveOptions = { " Progress", " Gadgets", " Life", " Dinos", " Energy" };
		if (volunteerAssignment == 0) {
			result += "xxxx";
		}
		if (volunteerAssignment == 1) {
			result += "------";
		}
		if (volunteerAssignment == 2) {
			result += "Lunch";
		}
		if (volunteerAssignment == 3) {
			result += "Movie";
		}
		if (volunteerAssignment == 4) {
			result += "Planet";
		}
		if (volunteerAssignment == 5) {
			result += "Dinos";
		}
		if (volunteerAssignment == 6) {
			result += "Atrium";
		}
		if (volunteerAssignment == 7) {
			result += "STARS";
		}
		if (volunteerAssignment == 8) {
			result += "Meeting";
		}
		if (volunteerAssignment == 9) {
			result += "Greet";
		}
		if (volunteerAssignment == 10) {
			result += "STARS";
		}
		if (volunteerAssignment == 11) {
			result += "Rove" + roveOptions[(int) (Math.random() * roveOptions.length)];
		}
		if (volunteerAssignment == 12) {
			result += "Load Movie";
		}
		if (volunteerAssignment == 13) {
			result += "Load Planet";
		}
		if (volunteerAssignment == 14) {
			result += "AMNH Doors";
		}
		return result;
	}

	public String clockTime(int i) {
		String time = "";
		double k;
		if (i * .5 + 9 > 12.5) {
			k = (i * .5 + 9) - 12;
		} else {
			k = (i * .5 + 9);
		}
		if (k % 1 != 0) {
			time = Math.round(Math.floor(k)) + ":30";
		} else {
			time = Math.round(Math.floor(k)) + ":00";
		}
		return time;

	}
}
