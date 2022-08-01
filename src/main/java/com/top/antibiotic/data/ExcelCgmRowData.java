package com.top.antibiotic.data;

import com.top.antibiotic.exceptions.ImportExcelRowException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public class ExcelCgmRowData {
    public ExcelCgmRowData (List<String> items) throws ImportExcelRowException {
        try {
            examinationNumber = Long.parseLong(items.get(3));
        } catch (Exception e) {
            throw new ImportExcelRowException("examinationNumber error", e, items.get(3), "Long", 3);
        }

        wardName = items.get(1);
        if (wardName.isEmpty())
            throw new ImportExcelRowException("wardName error", items.get(1), "String", 1);

        patientFirstName = items.get(19);
        if (patientFirstName.isEmpty())
            throw new ImportExcelRowException("patientFirstName error", items.get(19), "String", 19);

        patientSecondName = items.get(18);
        if (patientSecondName.isEmpty())
            throw new ImportExcelRowException("patientSecondName error", items.get(18), "String", 18);

        patientPesel = items.get(21);
        if (patientPesel.isEmpty())
            throw new ImportExcelRowException("patientPesel error", items.get(21), "String", 21);

        String pattern = "yy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            dateInserted = simpleDateFormat.parse(items.get(2));
        } catch (ParseException e) {
            throw new ImportExcelRowException("dateInserted error", e, items.get(2), "yy-MM-dd", 2);
        }
        bacteriaName = items.get(10);
        if (bacteriaName.isEmpty())
            throw new ImportExcelRowException("bacteriaName error", items.get(10), "String", 10);

        materialName = items.get(14);
        if (materialName.isEmpty())
            throw new ImportExcelRowException("materialName error", items.get(14), "String", 14);

        resistantAntibiotics = generateListOfItems(items.get(15));
        intermediateAntibiotics = generateListOfItems(items.get(16));
        susceptibleAntibiotics = generateListOfItems(items.get(17));
    }
    private Long examinationNumber;
    private String wardName;
    private String patientFirstName;
    private String patientSecondName;
    private String patientPesel;
    private Date dateInserted;
    private String bacteriaName;
    private String materialName;
    private List<String> resistantAntibiotics;
    private List<String> intermediateAntibiotics;
    private List<String> susceptibleAntibiotics;

    private List<String> generateListOfItems(String s) {
        return Arrays.asList(s.replaceAll("\\[", "")
                .replaceAll("\\]", "").replaceAll(" ", "").split(","));
    }
}
