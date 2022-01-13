package com.top.antibiotic.controllers;

import com.top.antibiotic.dto.FratRequest;
import com.top.antibiotic.dto.FratTableResponse;
import com.top.antibiotic.repository.*;
import com.top.antibiotic.servcice.FratTableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/frat-table")
@AllArgsConstructor
@Slf4j
public class FratTableController {
    private final FratTableService fratTableService;

    @RequestMapping(path = "/{wardName}/{materialName}",
            method = RequestMethod.GET)
    public FratTableResponse getTable(
            @PathVariable String wardName, @PathVariable String materialName) {
        return fratTableService.getTable(FratRequest.builder()
                .ward(wardName)
                .material(materialName)
                .build());
    }
}
