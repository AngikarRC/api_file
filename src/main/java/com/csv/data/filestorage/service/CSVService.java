package com.csv.data.filestorage.service;

import com.csv.data.filestorage.csvhelper.CSVProcessor;
import com.csv.data.filestorage.entity.CsvFile;
import com.csv.data.filestorage.repository.CSVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class CSVService {

    @Autowired
    CSVRepository csvRepository;

    public void saveCSV(MultipartFile file){

        try {
            List<CsvFile> savedFiles = CSVProcessor.readCSVfile(file.getInputStream());
            csvRepository.saveAll(savedFiles);
        }catch(IOException io){
            throw new RuntimeException("Failed to save CSV files data:-"+io.getMessage());
        }
    }

    public ByteArrayInputStream load() {
        List<CsvFile> loadedFiles = csvRepository.findAll();

        ByteArrayInputStream in = CSVProcessor.WriteToCSV(loadedFiles);
        return in;
    }

    public List<CsvFile> getAllFiles() {
        return csvRepository.findAll();
    }

    public List<String> getProdNamesBySupplier(String sname,String pname){
        List<String> products = csvRepository.getProductsBySupplierName(sname,pname);
        return products;
    }

    public List<CsvFile> getProductsByExpDate(Date todaysDate){
        todaysDate = new Date();
        return csvRepository.findByExpGreaterThan(todaysDate);
    }


}
