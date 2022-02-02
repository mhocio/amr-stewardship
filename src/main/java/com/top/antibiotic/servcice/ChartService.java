package com.top.antibiotic.servcice;

import com.top.antibiotic.data.BacteriaAntibiotic;
import com.top.antibiotic.data.SuspectibleResistant;
import com.top.antibiotic.dto.*;
import com.top.antibiotic.entities.*;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.repository.AntibiogramRepository;
import com.top.antibiotic.repository.AntibioticRepository;
import com.top.antibiotic.repository.BacteriaRepository;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.KeyValue;
import org.javatuples.Pair;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ChartService {

    private final AntibiogramRepository antibiogramRepository;
    private final BacteriaRepository bacteriaRepository;
    private final AntibioticRepository antibioticRepository;

    private final FratTableService fratTableService;

    private Pair<List<String>, List<Date>> generateResponseKeys(
            String firstValue, int startYear, int endYear) throws ParseException {
        if (startYear > endYear) {
            throw new AntibioticsException("wrong dates");
        }
        List<String> keys = new ArrayList<>();
        keys.add(firstValue);

        String pattern = "yy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        List<Date> dates = new ArrayList<>();
        for (Integer i = startYear; i <= endYear; i++) {
            dates.add(simpleDateFormat.parse(i + "-01-01"));
            dates.add(simpleDateFormat.parse(i + "-12-31"));
            keys.add(i.toString());
        }

        return new Pair<>(keys, dates);
    }

    private List<List<String>> generateValuesFromHashMap(
            HashMap<String, List<String>> antibioResultsByYear
    ) {
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

        return values;
    }

    public SusceptibilityChartResponse getSusceptibilityChartData(
            SusceptibilityChartRequest susceptibilityChartRequest) throws ParseException {
        Bacteria bacteria = bacteriaRepository.findByName(susceptibilityChartRequest.getBacteria())
                .orElseThrow(() -> new AntibioticsException("no bacteria found with name: " +
                        susceptibilityChartRequest.getBacteria()));

        SusceptibilityChartResponse res = new SusceptibilityChartResponse();

        var k = generateResponseKeys("Nazwa", 2018, 2021);
        List<Date> dates = k.getValue1();
        res.setKeys(k.getValue0());

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

        res.setResults(generateValuesFromHashMap(antibioResultsByYear));

        return res;
    }

    private List<List<String>> generateValuesFromHashMapGraphChart(
            HashMap<String, List<String>> antibioResultsByYear, List<String> antibiotics,
            int noOfYears, Integer startYear
    ) {
        List<List<String>> values = new ArrayList<>();
        if (noOfYears <= 0) {
            return values;
        }

        for (int i = 0; i < noOfYears; i++) {
            values.add(new ArrayList<>());
            values.get(i).add(String.valueOf(startYear + i));
        }

        for (int i = 0; i < noOfYears; i++) {
            for (String a : antibiotics) {
                values.get(i).add(antibioResultsByYear.get(a).get(i));
            }
        }

        return values;
    }

    public FRATChartsResponse getSusceptibilityFRATChartData(
            SusceptibilityFRATChartRequest request) throws ParseException {

        SusceptibilityChartResponse responseBarChart = new SusceptibilityChartResponse();
        SusceptibilityChartResponse responseGraphChart = new SusceptibilityChartResponse();
        responseGraphChart.setKeys(new ArrayList<>());
        responseGraphChart.getKeys().add("Nazwa");

        var k = generateResponseKeys("Nazwa",
                request.getStartYear(),
                request.getEndYear());
        List<Date> dates = k.getValue1();
        responseBarChart.setKeys(k.getValue0());

        HashMap<String, List<String>> antibioResultsByYear = new HashMap<>();

        List<Antibiotic> antibiotics = antibioticRepository.findAll();
        List<String> antibioticsNames = new ArrayList<>();
        List<List<String>> results = new ArrayList<>();

        for (Antibiotic antibiotic : antibiotics) {
            antibioResultsByYear.put(antibiotic.getName(),
                    new ArrayList<>());
            responseGraphChart.getKeys().add(antibiotic.getName());
            antibioticsNames.add(antibiotic.getName());
        }

        for (int i = 0; i < dates.size(); i+=2) {
            FratRequest fratRequest = FratRequest.builder()
                    .startDate(dates.get(i))
                    .endDate(dates.get(i+1))
                    .material(request.getMaterial())
                    .ward(request.getWard())
                    .build();

            FratTableResponse FRAT = fratTableService.getTable(fratRequest, false).getFirstTable();

            for (String antibioticsName : antibioticsNames) {
                if (FRAT.getResults().containsKey(antibioticsName)) {
                    antibioResultsByYear.get(antibioticsName).add(
                            String.valueOf(FRAT.getResults().get(antibioticsName)));
                } else {
                    antibioResultsByYear.get(antibioticsName).add("0");
                }
            }
        }

        responseBarChart.setResults(
                generateValuesFromHashMap(antibioResultsByYear));

        responseGraphChart.setResults(
                generateValuesFromHashMapGraphChart(antibioResultsByYear,
                        antibioticsNames, request.getEndYear() - request.getStartYear() + 1,
                        request.getStartYear())
        );

        return new FRATChartsResponse(responseBarChart, responseGraphChart);
    }
}