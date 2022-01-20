package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.FratRequest;
import com.top.antibiotic.dto.FratTableResponse;
import com.top.antibiotic.repository.*;
import com.top.antibiotic.servcice.FratTableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/frat-table")
@AllArgsConstructor
@Slf4j
public class FratTableController {
    private final FratTableService fratTableService;

    @RequestMapping(path = "/{wardName}/{materialName}",
            method = RequestMethod.GET)
    public FratTableResponse getTable(
            @PathVariable String wardName, @PathVariable String materialName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) Long id2) {

        String pattern = "yy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date sDate = null;
        Date eDate = null;
        try {
            if (startDate != null && endDate != null) {
                sDate = simpleDateFormat.parse(startDate);
                eDate = simpleDateFormat.parse(endDate);
            }
        } catch (ParseException e) {
            sDate = null;
            eDate = null;
        }

        return fratTableService.getTable(FratRequest.builder()
                .ward(wardName)
                .material(materialName)
                .startDate(sDate)
                .endDate(eDate)
                .build());
    }
}
