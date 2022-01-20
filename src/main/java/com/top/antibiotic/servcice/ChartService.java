package com.top.antibiotic.servcice;

import com.top.antibiotic.data.BacteriaAntibiotic;
import com.top.antibiotic.data.SuspectibleResistant;
import com.top.antibiotic.dto.FratTableResponse;
import com.top.antibiotic.dto.SusceptibilityChartRequest;
import com.top.antibiotic.dto.SusceptibilityChartResponse;
import com.top.antibiotic.entities.Antibiogram;
import com.top.antibiotic.entities.Bacteria;
import com.top.antibiotic.entities.Examination;
import com.top.antibiotic.entities.Ward;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.repository.AntibiogramRepository;
import com.top.antibiotic.repository.BacteriaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ChartService {

    private final AntibiogramRepository antibiogramRepository;
    private final BacteriaRepository bacteriaRepository;

    public SusceptibilityChartResponse getSusceptibilityChartData(SusceptibilityChartRequest susceptibilityChartRequest) {
        Bacteria bacteria = bacteriaRepository.findByName(susceptibilityChartRequest.getBacteria())
                .orElseThrow(() -> new AntibioticsException("no bacteria found with name: " +
                        susceptibilityChartRequest.getBacteria()));

        HashMap<String, Long> antibioticsOccurrence = new HashMap<String, Long>();
        HashMap<String, Long> bacteriaOccurrence = new HashMap<String, Long>();
        HashMap<BacteriaAntibiotic, SuspectibleResistant> sucpectabilities =
                new HashMap<BacteriaAntibiotic, SuspectibleResistant>();

        List<Antibiogram> antibiograms = antibiogramRepository.findByBacteria(bacteria);

        for (Antibiogram biogram: antibiograms) {

            String antibioName = biogram.getAntibiotic().getName();
            String bacteriaName = biogram.getBacteria().getName();

            if (!antibioticsOccurrence.containsKey(antibioName)) {
                antibioticsOccurrence.put(antibioName, 0L);
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

        SusceptibilityChartResponse res = new SusceptibilityChartResponse();

        List<String> antibiotics = new ArrayList<String>();
        antibioticsOccurrence.forEach((antibiotic, numberOfOccurrence) -> {
            antibiotics.add(antibiotic);
        });

        List<String> row = new ArrayList<String>();

        for (String antibio: antibiotics) {
            String valS = "-";

            BacteriaAntibiotic key = new BacteriaAntibiotic(bacteria.getName(), antibio);
            if (sucpectabilities.containsKey(key)) {
                Long s = sucpectabilities.get(key).getSuspectible();
                Long r = sucpectabilities.get(key).getResistant();

                Double percent = Double.valueOf((100.0 * s / (r * 1.0 + s * 1.0)));

                valS = (String.format("%.2f", percent));

            }

            row.add(valS);
        }

        List<String> keys = new ArrayList<String>();
        keys.add("Nazwa");
        keys.add("2022");
        res.setKeys(keys);

        List<List<String>> values = new ArrayList<>();
        for (int i = 0; i < antibiotics.size(); i++) {
            List<String> antibioticData = new ArrayList<>();
            antibioticData.add(antibiotics.get(i));
            antibioticData.add(row.get(i));
            values.add(antibioticData);
        }

        res.setResults(values);

        return res;
    }
}