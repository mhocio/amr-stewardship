package com.top.antibiotic.servcice;

import com.top.antibiotic.data.BacteriaAntibiotic;
import com.top.antibiotic.data.BacteriaSeveralAntibiotics;
import com.top.antibiotic.data.SuspectibleResistant;
import com.top.antibiotic.dto.FratRequest;
import com.top.antibiotic.dto.FratTableResponse;
import com.top.antibiotic.dto.SeveralFratTablesResponse;
import com.top.antibiotic.entities.Antibiogram;
import com.top.antibiotic.entities.Examination;
import com.top.antibiotic.entities.Material;
import com.top.antibiotic.entities.Ward;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class FratTableService {

    private final AntibiogramRepository antibiogramRepository;
    private final WardRepository wardRepository;
    private final PatientRepository patientRepository;
    private final MaterialRepository materialRepository;
    private final BacteriaRepository bacteriaRepository;
    private final AntibioticRepository antibioticRepository;

    private final ExaminationRepository examinationRepository;

    public SeveralFratTablesResponse getTable(FratRequest fratRequest) {

        // find Ward and Material from the request
        Ward ward = wardRepository.findByName(fratRequest.getWard())
                .orElseThrow(() -> new AntibioticsException("no ward found with name" +
                        fratRequest.getWard()));
        Material material = materialRepository.findByName(fratRequest.getMaterial())
                .orElseThrow(() -> new AntibioticsException("no ward found with name: " +
                        fratRequest.getMaterial()));

        // find Examinations - also by date if they were correct
        List<Examination> examinations;
        if (fratRequest.getStartDate() != null &&
            fratRequest.getEndDate() != null) {
            Date startDate = fratRequest.getStartDate();
            Date endDate = fratRequest.getEndDate();

            examinations = examinationRepository.findByWardAndMaterialAndOrderDateBetween(
                    ward, material, startDate, endDate
            );
        } else {
            examinations = examinationRepository.findByWardAndMaterial(
                    ward, material);
        }

        HashMap<String, Long> antibioticsOccurrence = new HashMap<String, Long>();
        HashMap<String, Long> bacteriaOccurrence = new HashMap<String, Long>();
        // map bacteria->antibiotic to its susceptibility
        HashMap<BacteriaAntibiotic, SuspectibleResistant> sucpectabilities =
                new HashMap<BacteriaAntibiotic, SuspectibleResistant>();
        int noOfExaminations = examinations.size();

        // collect data from each examination
        for (Examination exam: examinations) {
            //boolean added = false;
            HashMap<String, Boolean> addedBacteria = new HashMap<String, Boolean>();
            for (Antibiogram biogram: exam.getAntibiograms()) {

                String antibioName = biogram.getAntibiotic().getName();
                String bacteriaName = biogram.getBacteria().getName();

                // create antibiotic if not existing
                if (!antibioticsOccurrence.containsKey(antibioName)) {
                    antibioticsOccurrence.put(antibioName, 0L);
                }
                // create bacteria if not existing
                if (!bacteriaOccurrence.containsKey(bacteriaName)) {
                    bacteriaOccurrence.put(bacteriaName, 0L);
                }

                // for each examination we must count only a single bacteria occurrence
                if (!addedBacteria.containsKey(bacteriaName)) {
                    bacteriaOccurrence.put(bacteriaName,
                            bacteriaOccurrence.get(bacteriaName) + 1);
                    // set that we have already noticed this bacteria in this examination
                    addedBacteria.put(bacteriaName, true);
                }

                Long s = 0L;
                Long r = 0L;
                if (biogram.getSusceptibility().startsWith("S")) {
                    s = 1L;
                } else {
                    r = 1L;
                }

                // update or create our resulting Map of susceptibilities
                BacteriaAntibiotic key = new BacteriaAntibiotic(bacteriaName, antibioName);
                if (sucpectabilities.containsKey(key)) {
                    Long newS = sucpectabilities.get(key).getSuspectible() + s;
                    Long newR = sucpectabilities.get(key).getResistant() + r;
                    sucpectabilities.put(key, new SuspectibleResistant(newS, newR));
                } else {
                    sucpectabilities.put(key, new SuspectibleResistant(0L+s ,0L+r));
                }
            }
        }

        // original single antibiotic FRAT table
        FratTableResponse res = new FratTableResponse();
        // several antibiotics FRAT table
        FratTableResponse severalAntibioticsFRAT = new FratTableResponse();

        // list all preset antibiotics
        List<String> antibiotics = new ArrayList<String>();
        antibioticsOccurrence.forEach((antibiotic, numberOfOccurrence) -> {
            antibiotics.add(antibiotic);
        });
        res.setAntibiotics(antibiotics);

        int MAX_SEVERAL_ANTI = 3;
        int MIN_SEVERAL_ANTI = 2;

        // split antibiotics into tuples, triples, ...
        List<HashSet<String>> multipleAntibioticsList = new ArrayList<>();
        for (int i = MIN_SEVERAL_ANTI; i <= MAX_SEVERAL_ANTI; i++) {
            Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(
                    antibiotics.size(), i);
            while (iterator.hasNext()) {
                final int[] combination = iterator.next();
                HashSet<String> severalAntibiotics = new HashSet<>();
                for (int iter : combination) {
                    severalAntibiotics.add(antibiotics.get(iter));
                }
                multipleAntibioticsList.add(severalAntibiotics);
            }
        }
        List<String> severalAntibioticsList = new ArrayList<>();
        for (HashSet<String> e : multipleAntibioticsList) {
            severalAntibioticsList.add(e.toString());
        }
        severalAntibioticsFRAT.setAntibiotics(severalAntibioticsList);

        HashMap<HashSet<String>, Double> resultSum2 = new HashMap<>();
        for (HashSet<String> antibio: multipleAntibioticsList) {
            resultSum2.put(antibio, 0.0);
        }

        // calculate how many bacterias were present overall
        List<String> bacterias = new ArrayList<String>();
        List<Double> bacteriaFrequency = new ArrayList<Double>();  // in percent
        AtomicReference<Integer> sumOfOccurrence = new AtomicReference<>(0);
        bacteriaOccurrence.forEach((bacteria, numberOfOccurrence) -> {
            sumOfOccurrence.set((int) (sumOfOccurrence.get() + numberOfOccurrence));
        });
        // calculate relative occurrence of each bacteria
        bacteriaOccurrence.forEach((bacteria, numberOfOccurrence) -> {
            bacterias.add(bacteria);
            bacteriaFrequency.add((double) 100 * numberOfOccurrence / sumOfOccurrence.get());
        });
        res.setBacterias(bacterias);
        severalAntibioticsFRAT.setBacterias(bacterias);

        // last row in rows response of severalAntibioticsFRAT
        List<List<String>> rows = new ArrayList<>();
        HashMap<String, Double> resultSum = new HashMap<String, Double>();
        for (String antibio: antibiotics) {
            resultSum.put(antibio, 0.0);
        }

        // setup data for several_antibiotics->bacteria
        HashMap<BacteriaSeveralAntibiotics, SuspectibleResistant>
                multipleAntibioticsSuspectabilities = new HashMap<>();
        for (Examination exam : examinations) {
            HashSet<String> setBacterias = new HashSet<>();
            for (Antibiogram biogram : exam.getAntibiograms()) {
                setBacterias.add(biogram.getBacteria().getName());
            }
            List<String> tmpBacterias = setBacterias.stream().toList();

            List<HashSet<String>> multipleAntiInExamination = new ArrayList<>();
            for (int i = MIN_SEVERAL_ANTI; i <= MAX_SEVERAL_ANTI; i++) {
                Iterator<int[]> tmpIterator = CombinatoricsUtils.combinationsIterator(
                        antibiotics.size(), i);
                while (tmpIterator.hasNext()) {
                    HashSet<String> severalAntibiotics = new HashSet<>();
                    for (int iter : tmpIterator.next()) {
                        severalAntibiotics.add(antibiotics.get(iter));
                    }
                    // creating every pair from given antibiotics
                    multipleAntiInExamination.add(severalAntibiotics);
                }
            }

            HashMap<BacteriaSeveralAntibiotics, SuspectibleResistant>
                    currentExaminationResults = new HashMap<>();
            for (String b: tmpBacterias) {
                for (HashSet<String> antis : multipleAntiInExamination) {
                    currentExaminationResults.put(
                            new BacteriaSeveralAntibiotics(b, antis), new SuspectibleResistant(0L, 1L)
                    );
                }
            }

            // actually calculating susceptibilities
            for (Antibiogram biogram : exam.getAntibiograms()) {
                String antibioName = biogram.getAntibiotic().getName();
                String bacteriaName = biogram.getBacteria().getName();
                for (Map.Entry<BacteriaSeveralAntibiotics, SuspectibleResistant> mapElem : currentExaminationResults.entrySet()) {
                    BacteriaSeveralAntibiotics key = (BacteriaSeveralAntibiotics) mapElem.getKey();
                    if (key.getAntibiotics().contains(antibioName) && key.getBacteria().equals(bacteriaName)
                        && biogram.getSusceptibility().startsWith("S")) {
                        currentExaminationResults.put(key, new SuspectibleResistant(1L, 0L));
                    }
                }
            }

            for (Map.Entry<BacteriaSeveralAntibiotics, SuspectibleResistant> mapElem : currentExaminationResults.entrySet()) {
                BacteriaSeveralAntibiotics key = (BacteriaSeveralAntibiotics) mapElem.getKey();
                SuspectibleResistant val = (SuspectibleResistant) mapElem.getValue();
                if (!multipleAntibioticsSuspectabilities.containsKey(key)) {
                    multipleAntibioticsSuspectabilities.put(key, val);
                } else {
                    Long newS = multipleAntibioticsSuspectabilities.get(key).getSuspectible() + val.getSuspectible();
                    Long newR = multipleAntibioticsSuspectabilities.get(key).getResistant() + val.getResistant();
                    multipleAntibioticsSuspectabilities.put(key, new SuspectibleResistant(newS, newR));
                }
            }
        }

        List<List<String>> rows2 = new ArrayList<>();
        // create rows - main result info
        for (int i = 0; i < bacterias.size(); i++) {
            String bacteria = bacterias.get(i);
            List<String> row = new ArrayList<String>();
            List<String> row2 = new ArrayList<String>();

            row.add(bacteria);
            row.add(String.format("%.2f", bacteriaFrequency.get(i)));
            row2.add(bacteria);
            row2.add(String.format("%.2f", bacteriaFrequency.get(i)));

            // calculate F and S for each antibiotic vs specific bacteria
            // bacteria is in this row for every antibiotic
            for (String antibio: antibiotics) {
                String valS = "-";
                String valF = "-";

                BacteriaAntibiotic key = new BacteriaAntibiotic(bacteria, antibio);
                if (sucpectabilities.containsKey(key)) {
                    Long s = sucpectabilities.get(key).getSuspectible();
                    Long r = sucpectabilities.get(key).getResistant();

                    Double percent = Double.valueOf((100.0 * s / (r * 1.0 + s * 1.0)));
                    Double F = percent * bacteriaFrequency.get(i) / 100;
                    // update the last row of final summed F results
                    resultSum.put(antibio, resultSum.get(antibio) + F);

                    valS = (String.format("%.2f", percent));
                    valF = (String.format("%.2f", F));
                }
                // fill row data for this antibiotic
                row.add(valS);
                row.add(valF);
            }
            // calculate F and S for several antibiotics combination
            for (HashSet<String> severalAntibiotics : multipleAntibioticsList) {
                String valS = "-";
                String valF = "-";

                BacteriaSeveralAntibiotics key = new BacteriaSeveralAntibiotics(bacteria, severalAntibiotics);
                if (multipleAntibioticsSuspectabilities.containsKey(key)) {
                    Long s = multipleAntibioticsSuspectabilities.get(key).getSuspectible();
                    Long r = multipleAntibioticsSuspectabilities.get(key).getResistant();

                    Double percent = Double.valueOf((100.0 * s / (r * 1.0 + s * 1.0)));
                    Double F = percent * bacteriaFrequency.get(i) / 100;
                    // update the last row of final summed F results
                    resultSum2.put(severalAntibiotics, resultSum2.get(severalAntibiotics) + F);

                    valS = (String.format("%.2f", percent));
                    valF = (String.format("%.2f", F));
                }

                // fill row data for this antibiotic
                row2.add(valS);
                row2.add(valF);
            }

            rows.add(row);
            rows2.add(row2);
        }
        List<String> toAdd2 = new ArrayList<>();
        toAdd2.add("Razem:");
        toAdd2.add("100.00");
        for (int i = 0; i < resultSum2.size(); i++) {
            toAdd2.add("");
            toAdd2.add(String.format("%.2f", resultSum2.get(multipleAntibioticsList.get(i))));
        }
        rows2.add(toAdd2);
        severalAntibioticsFRAT.setRows(rows2);

        ///////////////////SORTING/////////////////////////////
        // TODO: sorting
        Map<Integer, Double> unSortedMap = new HashMap<>();
        int ii = 0;
        for (Double v: resultSum.values()) {
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
        res.setAntibiotics(sortedAntibio);

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

        // TODO: sort by bacterias
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
        log.info(reverseSortedMapBacterias.toString());

        // SORT table by bacterias - by rows
        List<List<String>> sortedRowsByBacterias = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            sortedRowsByBacterias.add(rows.get((Integer) reverseSortedMapBacterias.keySet().toArray()[i]));
        }
        rows = sortedRowsByBacterias;

        // NEXT
        List<String> toAdd = new ArrayList<>();
        toAdd.add("Razem:");
        toAdd.add("100.00");
        for (int i = 0; i < resultSum.size(); i++) {
            toAdd.add("");
            toAdd.add(String.format("%.2f", resultSum.get(sortedAntibio.get(i))));
        }
        rows.add(toAdd);

        res.setRows(rows);
        //return severalAntibioticsFRAT;
        return new SeveralFratTablesResponse(res, severalAntibioticsFRAT);
    }
}
