package com.seciii.prism063.core.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DateRange{
    LocalDate start;
    LocalDate end;
}