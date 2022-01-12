package com.top.antibiotic.servcice;

import com.top.antibiotic.dto.FratRequest;
import com.top.antibiotic.dto.FratTableResponse;
import com.top.antibiotic.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class FratTableService {

    private final AntibiogramRepository antibiogramRepository;
    private final WardRepository wardRepository;
    private final PatientRepository patientRepository;
    private final MaterialRepository materialRepository;
    private final BacteriaRepository bacteriaRepository;
    private final AntibioticRepository antibioticRepository;

    public FratTableResponse getTable(FratRequest fratRequest) {
        return new FratTableResponse();
    }
}
