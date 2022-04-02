package uzg.rt.lmx.log.io;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import uzg.rt.lmx.log.model.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ExcelWriter {

    private final XSSFWorkbook workbook;
    private CellStyle dateTimeStyle;

    public ExcelWriter() {
        this.workbook = new XSSFWorkbook();
        this.dateTimeStyle = this.workbook.createCellStyle();
        var helper  = this.workbook.getCreationHelper();
        dateTimeStyle.setDataFormat(
                helper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss")
        );
    }

    public void writeSheet(@NotNull Log log, @NotNull String sheetName) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        var rownum = 0;
        var colnum = 0;
        var n = log.size();

        final var hdrRow = sheet.createRow(rownum);
        rownum++;
        List<String> hdr = List.of("Timestamp", "Checkout", "Checkin", "Status", "Username", "Server", "License version", "License name", "License type", "Error");
        for (int i = 0; i < hdr.size(); i++) {
            Cell cell = hdrRow.createCell(colnum + i);
            cell.setCellValue(hdr.get(i));
        }

        for (int i = 0; i < n; i++) {
            int coloffset = colnum;
            final var entry = log.get(i);
            var row = sheet.createRow(rownum);
            rownum++;
            var timeStampCell = row.createCell(coloffset);
            timeStampCell.setCellStyle(dateTimeStyle);
            coloffset++;
            timeStampCell.setCellValue(entry.getTimeStamp());

            final var checkoutCell = row.createCell(coloffset);
            coloffset++;
            checkoutCell.setCellValue(entry.isCheckOut());

            final var checkinCell = row.createCell(coloffset);
            coloffset++;
            checkinCell.setCellValue(entry.isCheckIn());

            final var statusCell = row.createCell(coloffset);
            coloffset++;
            statusCell.setCellValue(entry.isStatus());

            final var usernameCell = row.createCell(coloffset);
            coloffset++;
            usernameCell.setCellValue(entry.getUsername());

            final var serverCell = row.createCell(coloffset);
            coloffset++;
            serverCell.setCellValue(entry.getHostname());

            final var licenseVersionCell = row.createCell(coloffset);
            coloffset++;
            final var licenseNameCell = row.createCell(coloffset);
            coloffset++;
            final var licenseTypeCell = row.createCell(coloffset);
            coloffset++;
            if (entry.getLicense().isPresent()) {
                final var license = entry.getLicense().get();
                final var version = license.getVersion();
                licenseVersionCell.setCellType(CellType.STRING);
                licenseVersionCell.setCellValue(String.format("%s.%s.%s", version.getMajor(), version.getMinor(), version.getPatch()));
                licenseNameCell.setCellValue(license.getName());
                licenseTypeCell.setCellValue(license.isClinical());
            } else {
                licenseVersionCell.setCellValue("");
                licenseNameCell.setCellValue("");
                licenseTypeCell.setCellValue("");
            }

            final var errMsgCell = row.createCell(coloffset);
            errMsgCell.setCellValue(entry.getErrorMessage());

        }
    }

    public void save(Path path) throws IOException {
        save(path, true);
    }

    public void save(Path path, boolean close) throws IOException {
        var fos = new FileOutputStream(path.toFile());
        workbook.write(fos);
        if (close) workbook.close();
    }
}
