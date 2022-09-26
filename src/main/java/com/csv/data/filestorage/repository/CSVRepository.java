package com.csv.data.filestorage.repository;

import com.csv.data.filestorage.entity.CsvFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CSVRepository extends JpaRepository<CsvFile,String> {

@Query(
        value = "select c.name , c.stock from csv_file c where c.supplier = ?1 and " +
                "c.name =?2 group by c.batch",
        nativeQuery = true
)
public List<String> getProductsBySupplierName(String supplierName,String productName);

public List<CsvFile> findByExpGreaterThan(Date todaysDate);
}
