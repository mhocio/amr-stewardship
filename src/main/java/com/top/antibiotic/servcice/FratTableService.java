package com.top.antibiotic.servcice;

import com.top.antibiotic.data.BacteriaAntibiotic;
import com.top.antibiotic.data.SuspectibleResistant;
import com.top.antibiotic.dto.FratRequest;
import com.top.antibiotic.dto.FratTableResponse;
import com.top.antibiotic.entities.Antibiogram;
import com.top.antibiotic.entities.Examination;
import com.top.antibiotic.entities.Material;
import com.top.antibiotic.entities.Ward;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    public FratTableResponse getTable(FratRequest fratRequest) {

        Ward ward = wardRepository.findByName(fratRequest.getWard())
                .orElseThrow(() -> new AntibioticsException("no ward found with name" +
                        fratRequest.getWard()));
        Material material = materialRepository.findByName(fratRequest.getMaterial())
                .orElseThrow(() -> new AntibioticsException("no ward found with name: " +
                        fratRequest.getMaterial()));

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
        HashMap<BacteriaAntibiotic, SuspectibleResistant> sucpectabilities =
                new HashMap<BacteriaAntibiotic, SuspectibleResistant>();
        int noOfExaminations = examinations.size();

        for (Examination exam: examinations) {
            //boolean added = false;
            HashMap<String, Boolean> addedBacteria = new HashMap<String, Boolean>();
            for (Antibiogram biogram: exam.getAntibiograms()) {

                String antibioName = biogram.getAntibiotic().getName();
                String bacteriaName = biogram.getBacteria().getName();

                if (!antibioticsOccurrence.containsKey(antibioName)) {
                    antibioticsOccurrence.put(antibioName, 0L);
                }

                if (!bacteriaOccurrence.containsKey(bacteriaName)) {
                    bacteriaOccurrence.put(bacteriaName, 0L);
                }

                if (!addedBacteria.containsKey(bacteriaName)) {
                    bacteriaOccurrence.put(bacteriaName,
                            bacteriaOccurrence.get(bacteriaName) + 1);
                    addedBacteria.put(bacteriaName, true);
                }

                Long s = 0L;
                Long r = 0L;
                if (biogram.getSusceptibility().startsWith("S")) {
                    s = 1L;
                } else {
                    r = 1L;
                }

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

        FratTableResponse res = new FratTableResponse();

        List<String> antibiotics = new ArrayList<String>();
        antibioticsOccurrence.forEach((antibiotic, numberOfOccurrence) -> {
            antibiotics.add(antibiotic);
        });
        res.setAntibiotics(antibiotics);

        List<String> bacterias = new ArrayList<String>();
        List<Double> bacteriaFrequency = new ArrayList<Double>();  // in percent
        AtomicReference<Integer> sumOfOccurrence = new AtomicReference<>(0);
        bacteriaOccurrence.forEach((bacteria, numberOfOccurrence) -> {
            sumOfOccurrence.set((int) (sumOfOccurrence.get() + numberOfOccurrence));
        });

        bacteriaOccurrence.forEach((bacteria, numberOfOccurrence) -> {
            bacterias.add(bacteria);
            bacteriaFrequency.add((double) 100 * numberOfOccurrence / sumOfOccurrence.get());
        });
        res.setBacterias(bacterias);

        List<List<String>> rows = new ArrayList<>();

        HashMap<String, Double> resultSum = new HashMap<String, Double>();
        for (String antibio: antibiotics) {
            resultSum.put(antibio, 0.0);
        }

        for (int i = 0; i < bacterias.size(); i++) {
            String bacteria = bacterias.get(i);
            List<String> row = new ArrayList<String>();

            row.add(bacteria);
            row.add(String.format("%.2f", bacteriaFrequency.get(i)));

            for (String antibio: antibiotics) {
                String valS = "-";
                String valF = "-";

                BacteriaAntibiotic key = new BacteriaAntibiotic(bacteria, antibio);
                if (sucpectabilities.containsKey(key)) {
                    Long s = sucpectabilities.get(key).getSuspectible();
                    Long r = sucpectabilities.get(key).getResistant();

                    Double percent = Double.valueOf((100.0 * s / (r * 1.0 + s * 1.0)));
                    Double F = percent * bacteriaFrequency.get(i) / 100;
                    resultSum.put(antibio, resultSum.get(antibio) + F);

                    valS = (String.format("%.2f", percent));
                    valF = (String.format("%.2f", F));
                }
                row.add(valS);
                row.add(valF);

            }

            rows.add(row);
        }

        List<String> toAdd = new ArrayList<>();
        toAdd.add("Razem:");
        toAdd.add("100.00");
        for (Double v: resultSum.values()) {
            toAdd.add("");
            toAdd.add(String.format("%.2f", v));
        }
        rows.add(toAdd);

        res.setRows(rows);
        return res;
    }
}
