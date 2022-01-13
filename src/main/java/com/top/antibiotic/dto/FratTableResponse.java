package com.top.antibiotic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FratTableResponse {
    public List<String> antibiotics;
    public List<String> bacterias;
    public List<List<String>> rows;
    public List<String> results;
}
