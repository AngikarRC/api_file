package com.csv.data.filestorage.controller;

import com.csv.data.filestorage.csvhelper.CSVProcessor;
import com.csv.data.filestorage.entity.CsvFile;
import com.csv.data.filestorage.model.ResponseMessage;
import com.csv.data.filestorage.service.CSVService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;

@CrossOrigin("http://localhost:8087")
@RestController
//@RequestMapping("/api/csv")
public class CSVController {

    @Autowired
    CSVService csvService;

    Logger logger = LoggerFactory.getLogger(CSVController.class);

    @PostMapping("/api/csv/upload/")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String msg ="";
        if (CSVProcessor.hasCSVFormat(file)) {
            try{
                csvService.saveCSV(file);

                msg="File is saved in DB :"+file.getOriginalFilename();
                logger.info("After file saved :- "+msg);

                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("api/csv/download")
                        .path(file.getOriginalFilename())
                        .toUriString();

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseMessage(msg,fileDownloadUri));
            }catch (Exception ex){
                msg ="Oops! There was an error saving the file :"+file.getOriginalFilename()
                        +"->"+ex.getMessage();
                logger.warn(msg);

                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).
                        body(new ResponseMessage(msg,""));
            }
        }

        msg= "Please upload a CSV file!";
        logger.warn("If file format exception"+msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage(msg,""));

    }

    @GetMapping("/api/csv/files")
    public ResponseEntity<ResponseMessage> getAllFiles(){
        try{
            List<CsvFile> allFiles =  csvService.getAllFiles();
            logger.info(String.valueOf(allFiles));
            if(allFiles.isEmpty()){
                logger.info("No files present");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseMessage("No files present",""));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success",""));
        }catch (Exception ex){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
        InputStreamResource file = new InputStreamResource(csvService.load());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
    @GetMapping("/api/csv/{sname}/{pname}")
    public ResponseEntity<ResponseMessage> getProductsNameBySup(@PathVariable("sname")String sname,
                                                                @PathVariable("pname")String pname){
        try {
            List<String> products = csvService.getProdNamesBySupplier(sname, pname);

            logger.info(String.valueOf(products));

            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseMessage("No files present",""));
            }
            System.out.println(products);;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success",""));
        }catch(Exception ex){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/csv/exp")
    public List<CsvFile> getProductsWithExp() {
        return csvService.getProductsByExpDate(new Date());
    }


}

