package com.mentorship.vineservice.repository;

import static com.mentorship.vineservice.specification.VineSpecification.equalsColor;
import static com.mentorship.vineservice.specification.VineSpecification.equalsGrape;
import static com.mentorship.vineservice.specification.VineSpecification.equalsName;
import static com.mentorship.vineservice.specification.VineSpecification.equalsSugar;
import static org.assertj.core.api.Assertions.assertThat;

import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.enums.VineColor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@DataJpaTest
public class VineRepositoryTest {

    @Autowired
    private VineRepository vineRepository;
    private Vine merlotVine1;
    private Vine zinfandelVine1;
    private Vine cabernetVine2;
    private Vine cabernetVine1;
    private Vine merlotVine2;
    private Vine merlotVine3;
    private Map<Vine, Vine> vineMap;
    private List<Vine> defaultListOfVines;


    @BeforeEach
    public void setUpData() {

        merlotVine1 = createVineObject("MERLOT_VINE", VineColor.RED, "MERLOT", "SWEET");
        zinfandelVine1 = createVineObject("ZINFANDEL_VINE", VineColor.ORANGE, "ZINFANDEL", "DRY");
        cabernetVine2 = createVineObject("CABERNET_VINE", VineColor.WHITE, "CABERNET SAUVIGNON", "SWEET");
        cabernetVine1 = createVineObject("CABERNET_VINE", VineColor.RED, "CABERNET SAUVIGNON", "SEMI-SWEET");
        merlotVine2 = createVineObject("MERLOT_VINE", VineColor.WHITE, "MERLOT", "SWEET");
        merlotVine3 = createVineObject("MERLOT_VINE", VineColor.ROSE, "MERLOT", "DRY");

        defaultListOfVines = List.of(
            merlotVine1,
            zinfandelVine1,
            cabernetVine2,
            cabernetVine1,
            merlotVine2,
            merlotVine3
        );

        vineMap = new HashMap<>();

        defaultListOfVines.forEach(vineObject -> {

            Vine createdVine = vineRepository.save(vineObject);

            vineMap.put(vineObject, createdVine);
        });

    }


    @Test
    public void shouldCreateVineAndReturnVineObject() {

        Vine vineToSave = createVineObject("CHARDONNAY_VINE", VineColor.RED, "Chardonnay", "SWEET");

        Vine createdVine = vineRepository.save(vineToSave);

        Vine expectedVine = vineRepository.findById(createdVine.getId()).get();

        assertThat(createdVine).isEqualTo(expectedVine);

    }


    @Test
    public void shouldReturnAllVinesWithoutFilterAndPagination() {

        List<Vine> listOfAllVines = vineRepository.findAll();

        assertThat(listOfAllVines).hasSize(defaultListOfVines.size());
    }


    @Test
    public void shouldReturnVinesByNameAndColorParameters() {

        var searchParameters = Specification.where(
                equalsName("MERLOT_VINE"))
            .and(equalsColor(VineColor.RED));

        List<Vine> listOfAllVines = vineRepository.findAll(searchParameters);

        assertThat(listOfAllVines).hasSize(1);
        assertThat(listOfAllVines).containsExactlyInAnyOrder(vineMap.get(merlotVine1));

    }


    @Test
    public void shouldReturnVinesFilteredByGrapeAndSugarParameters() {

        var searchParameters = Specification.where(
                equalsGrape("MERLOT"))
            .and(equalsSugar("SWEET"));

        List<Vine> listOfAllVines = vineRepository.findAll(searchParameters);

        assertThat(listOfAllVines).hasSize(2);
        assertThat(listOfAllVines).containsExactlyInAnyOrder(vineMap.get(merlotVine1), vineMap.get(merlotVine2));
    }

    @Test
    public void shouldReturnVinesFilteredByNameAndColorAndGrapeAndSugarParameters() {

        var searchParameters = Specification.where(
                equalsGrape("MERLOT"))
            .and(equalsName("MERLOT_VINE"))
            .and(equalsColor(VineColor.ROSE))
            .and(equalsSugar("DRY"));

        List<Vine> listOfAllVines = vineRepository.findAll(searchParameters);

        assertThat(listOfAllVines).hasSize(1);
        assertThat(listOfAllVines).containsOnly(vineMap.get(merlotVine3));
    }

    @Test
    public void shouldReturnEmptyListIfSearchCriteriaDidNotMatchAnyVine() {

        var searchParameters = Specification.where(
                equalsGrape("MERLOT"))
            .and(equalsName("MERLOT_VINE"))
            .and(equalsColor(VineColor.ROSE))
            .and(equalsSugar("SWEET"));

        List<Vine> listOfAllVines = vineRepository.findAll(searchParameters);

        assertThat(listOfAllVines).hasSize(0);
    }

    @Test
    public void shouldReturnVinesByPageableParametersAndSearchCriteria() {

        Pageable pageable = PageRequest.of(0, 2);

        var searchParameters = Specification.where(
            equalsSugar("SWEET"));

        Page<Vine> listOfAllVines = vineRepository.findAll(searchParameters, pageable);

        Integer listOfPages = listOfAllVines.getTotalPages();
        Long allElements = listOfAllVines.getTotalElements();

        assertThat(listOfPages).isEqualTo(2);
        assertThat(allElements).isEqualTo(3);

    }

    private Vine createVineObject(String name, VineColor vineColor, String grape, String sugar) {
        Vine vine = new Vine();

        vine.setName(name);
        vine.setColor(vineColor);
        vine.setCountry("UKRAINE");
        vine.setManufacturer("KOLONIST");
        vine.setYear(2019);
        vine.setSugar(sugar);
        vine.setGrapeName(grape);
        vine.setAmount(100);
        vine.setSoldWine(10);
        vine.setAbv(10.5);
        vine.setIsSparkling(true);
        vine.setPrice(1000.0);
        vine.setRegion("KHERSON");

        return vine;
    }
}
