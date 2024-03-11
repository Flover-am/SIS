package com.seciii.prism063.core.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DateRange{
    LocalDateTime start;
    LocalDateTime end;
}