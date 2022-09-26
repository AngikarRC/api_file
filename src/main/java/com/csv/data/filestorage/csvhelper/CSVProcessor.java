package com.csv.data.filestorage.csvhelper;

import com.csv.data.filestorage.entity.CsvFile;
import org.apache.commons.csv.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CSVProcessor {

    public static String type="text/csv";
    static String[] HEADERs = { "code", "name", "batch", "stock","deal","free",
            "mrp","rate","exp","company" };

    public static boolean hasCSVFormat(MultipartFile file){
        if(!type.equals(file.getContentType())||
                file.getContentType().equals("application/vnd.ms-excel")) {
            return true;
        }
        return false;
    }

    public static List<CsvFile> readCSVfile(InputStream is){
        try (
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
                ) {

        List<CsvFile> files = new ArrayList<>();

        Iterable<CSVRecord> csvRecords = csvParser.getRecords();

        for (CSVRecord csvRecord : csvRecords) {
            CsvFile csvFile = new CsvFile(
                    csvRecord.get("code"),
                    csvRecord.get("name"),
                    csvRecord.get("batch"),
                    Integer.parseInt(csvRecord.get("stock")),
                    Integer.parseInt(csvRecord.get("deal")),
                    Integer.parseInt(csvRecord.get("free")),
                    Double.parseDouble(csvRecord.get("mrp")),
                    Double.parseDouble(csvRecord.get("rate")),
                    new Date(csvRecord.get("exp")),
                    csvRecord.get("company"),
                    csvRecord.get("supplier")
            );

            files.add(csvFile);
        }

        return files;
    } catch (IOException e) {
        throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
    }

    public static ByteArrayInputStream WriteToCSV(List<CsvFile> csvList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);
             ) {
            for (CsvFile csvFiles : csvList) {
                List<String> data = Arrays.asList(
                        csvFiles.getCode(),
                        csvFiles.getName(),
                        csvFiles.getBatch(),
                        String.valueOf(csvFiles.getStock()),
                        String.valueOf(csvFiles.getDeal()),
                        String.valueOf(csvFiles.getFree()),
                        String.valueOf(csvFiles.getMrp()),
                        String.valueOf(csvFiles.getRate()),
                        String.valueOf(csvFiles.getExp()),
                        csvFiles.getCompany(),
                        csvFiles.getSupplier()
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to import data to CSV file: " + e.getMessage());
        }
    }


}
