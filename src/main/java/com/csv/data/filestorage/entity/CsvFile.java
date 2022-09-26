package com.csv.data.filestorage.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="csv_file")
public class CsvFile {
    @Id
    @NonNull
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "batch")
    private String batch;
    @Column(name = "stock")
    private int stock;
    @Column(name = "deal")
    private int deal;
    @Column(name = "free")
    private int free;
    @Column(name = "mrp")
    private double mrp;
    @Column(name = "rate")
    private double rate;
    @Column(name = "exp")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date exp;
    @Column(name = "company")
    private String company;
    @Column(name="supplier")
    private String supplier;


}
