package com.top.antibiotic.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuspectibleResistant {
    private Long suspectible;
    private Long resistant;
}
