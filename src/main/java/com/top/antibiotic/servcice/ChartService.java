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
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public SusceptibilityChartResponse getSusceptibilityChartData(SusceptibilityChartRequest susceptibilityChartRequest) throws ParseException {
        Bacteria bacteria = bacteriaRepository.findByName(susceptibilityChartRequest.getBacteria())
                .orElseThrow(() -> new AntibioticsException("no bacteria found with name: " +
                        susceptibilityChartRequest.getBacteria()));

        SusceptibilityChartResponse res = new SusceptibilityChartResponse();

        List<String> keys = new ArrayList<String>();
        keys.add("Nazwa");

        String pattern = "yy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        List<Date> dates = new ArrayList<>();
        for (Integer i = 2019; i <= 2021; i++) {
            dates.add(simpleDateFormat.parse(i + "-01-01"));
            dates.add(simpleDateFormat.parse(i + "-12-31"));
            keys.add(i.toString());
        }
        res.setKeys(keys);

        HashMap<String, List<String>> antibioResultsByYear = new HashMap<>();

        for (int i = 0; i < dates.size(); i+=2) {

            HashMap<String, Long> antibioticsOccurrence = new HashMap<String, Long>();
            HashMap<BacteriaAntibiotic, SuspectibleResistant> sucpectabilities =
                    new HashMap<BacteriaAntibiotic, SuspectibleResistant>();

            //List<Antibiogram> antibiograms = antibiogramRepository.findByBacteria(bacteria);
            List<Antibiogram> antibiograms = antibiogramRepository.findByBacteriaAndOrderDateBetween(
                    bacteria, dates.get(i), dates.get(i+1));

            for (Antibiogram biogram : antibiograms) {

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
                    sucpectabilities.put(key, new SuspectibleResistant(0L + s, 0L + r));
                }
            }

            List<String> antibiotics = new ArrayList<String>();
            antibioticsOccurrence.forEach((antibiotic, numberOfOccurrence) -> {
                antibiotics.add(antibiotic);
            });

            List<String> row = new ArrayList<String>();

            for (String antibio : antibiotics) {
                String valS = "-";

                BacteriaAntibiotic key = new BacteriaAntibiotic(bacteria.getName(), antibio);
                if (sucpectabilities.containsKey(key)) {
                    Long s = sucpectabilities.get(key).getSuspectible();
                    Long r = sucpectabilities.get(key).getResistant();

                    Double percent = Double.valueOf((100.0 * s / (r * 1.0 + s * 1.0)));

                    valS = (String.format("%.2f", percent));
                }

                if (!antibioResultsByYear.containsKey(antibio)) {
                    antibioResultsByYear.put(antibio, new ArrayList<>());
                }
                antibioResultsByYear.get(antibio).add(valS);

                //row.add(valS);
            }

            for (String a : antibioResultsByYear.keySet()) {
                if (!antibiotics.contains(a)) {
                    antibioResultsByYear.get(a).add("0");
                }
            }
        }

        List<List<String>> values = new ArrayList<>();
        for (String names: antibioResultsByYear.keySet()) {
            List<String> antibioticData = new ArrayList<>();
            antibioticData.add(names);
            values.add(antibioticData);
        }
        int i = 0;
        for (List<String> vals: antibioResultsByYear.values()) {
            values.get(i).addAll(vals);
            i++;
        }

//        for (int i = 0; i < antibiotics.size(); i++) {
//            List<String> antibioticData = new ArrayList<>();
//            antibioticData.add(antibiotics.get(i));
//            antibioticData.add(row.get(i));
//            values.add(antibioticData);
//        }

        res.setResults(values);

        return res;
    }
}