package com.top.antibiotic.unit.repository;

import com.top.antibiotic.entities.Material;
import com.top.antibiotic.repository.MaterialRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class MaterialRepositoryTests {

    @Autowired
    MaterialRepository materialRepository;

    @Test
    public void givenMaterialName_whenFindByMaterialName_thenReturnMaterial() {
        // given
        String materialName = "material test name";
        materialRepository.save(Material.builder().name(materialName).build());

        // when
        Optional<Material> foundMaterial = materialRepository.findByName(materialName);

        // then
        Assert.assertEquals(materialName, foundMaterial.get().getName());
    }

    @Test
    public void givenMaterialName_whenFindById_thenReturnMaterial() {
        // given
        Long id = 1L;
        String materialName = "material test name";
        materialRepository.save(Material.builder().name(materialName).build());

        // when
        Optional<Material> foundMaterial = materialRepository.findById(id);

        // then
        Assert.assertEquals(id, foundMaterial.get().getMaterialId());
    }

    @Test
    public void givenArbitraryMaterialId_whenFindById_thenDontReturnMaterial() {
        // given
        Long id = 999L;
        String materialName = "material test name";
        materialRepository.save(Material.builder().name(materialName).materialId(id).build());

        // when
        Boolean exists999 = materialRepository.existsById(id);
        Boolean exists1 = materialRepository.existsById(1L);

        // then
        Assert.assertFalse(exists999);
        Assert.assertTrue(exists1);
    }

    @Test
    public void givenMaterialNames_whenFindAll_thenReturnMaterials() {
        // given
        String materialName1 = "material test name1";
        String materialName2 = "material test name2";

        materialRepository.save(Material.builder().name(materialName1).build());
        materialRepository.save(Material.builder().name(materialName2).build());

        // when
        List<Material> foundMaterials = materialRepository.findAll();

        // then
        Assert.assertEquals(Optional.of(1L), Optional.ofNullable(foundMaterials.get(0).getMaterialId()));
        Assert.assertEquals(Optional.of(2L), Optional.ofNullable(foundMaterials.get(1).getMaterialId()));

    }


}
