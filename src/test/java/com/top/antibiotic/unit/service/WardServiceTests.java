package com.top.antibiotic.unit.service;

import com.top.antibiotic.dto.WardDto;
import com.top.antibiotic.entities.Ward;
import com.top.antibiotic.exceptions.AntibioticsException;
import com.top.antibiotic.mapper.WardMapper;
import com.top.antibiotic.repository.WardRepository;
import com.top.antibiotic.servcice.WardService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class WardServiceTests {

    @MockBean
    private WardRepository wardRepository;

    @Autowired
    private WardService sut;

    @Autowired
    private WardMapper wardMapper;

    @Test
    public void givenListOfWards_whenGetAll_returnAllWards() {
        // given
        List<Ward> wards = new ArrayList<>();
        wards.add(Ward.builder().name("ward name 1").build());
        wards.add(Ward.builder().name("ward name 2").build());

        List<WardDto> expected = wards.stream()
                .map(wardMapper::mapWardToDto)
                .collect(Collectors.toList());

        Mockito.when(wardRepository.findAll()).thenReturn(wards);

        // when
        List<WardDto> actual = sut.getAll();

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void givenListOfWards_whenGetWardById_returnWard() {
        // given
        Ward ward = Ward.builder().name("ward name 1").build();
        WardDto expected = wardMapper.mapWardToDto(ward);

        Mockito.when(wardRepository.findById(1L)).thenReturn(Optional.ofNullable(ward));

        // when
        WardDto actual = sut.getWard(1L);

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void givenEmptyListOfWards_whenGetWardById_throwException() {
        // given
        Mockito.when(wardRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        Assert.assertThrows(AntibioticsException.class, () -> sut.getWard(1L));
    }

    @Test
    public void givenListOfWards_whenGetWardByName_returnWard() {
        // given
        String wardName = "ward name 1";
        Ward ward = Ward.builder().name(wardName).build();
        WardDto expected = wardMapper.mapWardToDto(ward);

        Mockito.when(wardRepository.findByName(wardName)).thenReturn(Optional.ofNullable(ward));

        // when
        WardDto actual = sut.getWard(wardName);

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void givenEmptyListOfWards_whenGetWardByName_throwException() {
        // given
        Mockito.when(wardRepository.findByName("ward name")).thenReturn(Optional.empty());

        // when & then
        Assert.assertThrows(AntibioticsException.class, () -> sut.getWard("ward name"));
    }

}
