package com.top.antibiotic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FratTableResponse {
    public List<String> antibiotics;
    public List<String> bacterias;
    public List<List<String>> rows;
    public HashMap<String, Double> results;  // this needs to have preserved order before sorting

    public void sortByAntibiotics(List<Double> bacteriaFrequency) {
        Map<Integer, Double> unSortedMap = new HashMap<>();
        int ii = 0;
        for (Double v: results.values()) {
            unSortedMap.put(ii, v);
            ii += 1;
        }

        LinkedHashMap<Integer, Double> reverseSortedMap = new LinkedHashMap<>();
        unSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        HashMap<Integer, Integer> mapOldIndexesToSortedOnes = new HashMap<>();
        mapOldIndexesToSortedOnes.put(0, 0);
        mapOldIndexesToSortedOnes.put(1, 1);
        ii = 2;
        for (Integer val : reverseSortedMap.keySet()) {
            mapOldIndexesToSortedOnes.put(ii, 2+2*val);
            mapOldIndexesToSortedOnes.put(ii+1, 2+2*val + 1);
            ii += 2;
        }

        List<String> sortedAntibio = new ArrayList<>();
        for (int i = 0; i < antibiotics.size(); i++) {
            sortedAntibio.add(antibiotics.get((Integer) reverseSortedMap.keySet().toArray()[i]));
        }
        antibiotics = sortedAntibio;

        ///  sort rows
        List<List<String>> sortedRows = new ArrayList<>();
        for (List<String> row : rows) {
            List<String> newRow = new ArrayList<>();
            for (int i = 0; i < row.size(); i++) {
                newRow.add(row.get(mapOldIndexesToSortedOnes.get(i)));
            }
            sortedRows.add(newRow);
        }
        rows = sortedRows;

        // SORT BY bacteria frequency

        Map<Integer, Double> unSortedMapBacterias = new HashMap<>();
        ii = 0;
        for (Double v: bacteriaFrequency) {
            unSortedMapBacterias.put(ii, v);
            ii += 1;
        }
        LinkedHashMap<Integer, Double> reverseSortedMapBacterias = new LinkedHashMap<>();
        unSortedMapBacterias.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMapBacterias.put(x.getKey(), x.getValue()));

        // SORT table by bacterias - by rows
        List<List<String>> sortedRowsByBacterias = new ArrayList<>();
        for (int i = 0; i < rows.size()-1; i++) {
            sortedRowsByBacterias.add(rows.get((Integer) reverseSortedMapBacterias.keySet().toArray()[i]));
        }
        sortedRowsByBacterias.add(rows.get(rows.size()-1));  // add last footer with results (Razem 100%, ...)
        rows = sortedRowsByBacterias;
    }
}
