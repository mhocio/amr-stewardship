package com.top.antibiotic.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public class AntibiogramRow {
    public AntibiogramRow(XSSFRow row) {
        DataFormatter formatter = new DataFormatter();
        for (int j = 0; j <= 26 ; j++) {
            String val = formatter.formatCellValue(row.getCell(j));
            switch (j) {
                case 0:
                    wardName = val;
                case 1:
                    patientFirstName = val;
                case 2:
                    patientSecondName = val;
                case 4:
                    dateInserted = val;
                case 5:
                    orderNumber = val;
                case 6:
                    materialName = val;
                case 7:
                    bacteriaName = val;
                case 8:
                    bacteriaSubtype = val;
                case 9:
                    antibioticName = val;
                case 10:
                    suspectability = val;
                case 11:
                    mic = val;
                case 12:
                    alert = val;
                case 13:
                    patogen = val;
                case 14:
                    growth = val;
                case 15:
                    firstIsolate = val;
                case 16:
                    hospitalInfection = val;
                case 17:
                    orderId = val;
                case 18:
                    testId = val;
                case 19:
                    isolationId = val;
                case 20:
                    isolationCode = val;
                case 21:
                    islolationNum = val;
                case 22:
                    antibioticCode = val;
                case 23:
                    result = val;
                case 24:
                    dailyNumber = val;
                case 25:
                    mode = val;
                case 26:
                    pryw = val;

            }
        }
    }

    private String wardName = "";  //0
    private String patientFirstName = "";  //1
    private String patientSecondName = "";  //2
    private String patientPesel = "";  //3
    private String dateInserted = "";  //4
    private String orderNumber = "";  //5
    private String materialName = "";  //6
    private String bacteriaName = "";  //7
    private String bacteriaSubtype = "";  //8
    private String antibioticName = "";  //9
    private String suspectability = "";  //10
    private String mic = "";  //11
    private String alert = "";  //12
    private String patogen = "";  //13
    private String growth = "";  //14
    private String firstIsolate = "";  //15
    private String hospitalInfection = "";  //16
    private String orderId = "";  //17
    private String testId = "";  //18
    private String isolationId = "";  //19
    private String isolationCode = "";  //20
    private String islolationNum = "";  //21
    private String antibioticCode = "";  //22
    private String result = "";  //23
    private String dailyNumber = "";  //24
    private String mode = "";  //25
    private String pryw = "";  //26

    public List<String> getListItems() {
        List<String> items = new ArrayList<>();

        items.add(wardName);
        items.add(patientFirstName);
        items.add(patientSecondName);
        items.add(patientPesel);
        items.add(dateInserted);
        items.add(orderNumber);
        items.add(materialName);
        items.add(bacteriaName);
        items.add(bacteriaSubtype);
        items.add(antibioticName);
        items.add(suspectability);
        items.add(mic);
        items.add(alert);
        items.add(patogen);
        items.add(growth);
        items.add(firstIsolate);
        items.add(hospitalInfection);
        items.add(orderId);
        items.add(testId);
        items.add(isolationId);
        items.add(isolationCode);
        items.add(islolationNum);
        items.add(antibioticCode);
        items.add(result);
        items.add(dailyNumber);
        items.add(mode);
        items.add(pryw);

        return items;
    }
}
