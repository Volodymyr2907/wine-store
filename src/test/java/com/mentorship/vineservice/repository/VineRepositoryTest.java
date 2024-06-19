package com.mentorship.vineservice.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.mentorship.vineservice.config.RepositoryTestBase;
import com.mentorship.vineservice.domain.DeliveryDetails;
import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.OrderVine;
import com.mentorship.vineservice.domain.PostOffice;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.OrderPostOfficeDto;
import com.mentorship.vineservice.dto.enums.VineColor;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class VineRepositoryTest {

    @Autowired
    private VineRepository vineRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PostOfficeRepository postOfficeRepository;
    @Autowired
    private EntityManager entityManager;

    private Vine merlotVine1;
    private Vine zinfandelVine1;
    private Vine cabernetVine2;
    private Vine cabernetVine1;
    private Vine merlotVine2;
    private Vine merlotVine3;
    private List<Vine> defaultListOfVines;
    private PostOffice ternopilPostOffice;
    private PostOffice lvivPostOffice;
    private PostOffice kyivPostOffice;
    private Order order1;
    private Order order2;
    private Order order3;
    private Order order4;


    @BeforeEach
    public void setUpData() {

        // create listOfVines
        merlotVine1 = createVines("MERLOT_VINE", VineColor.RED, "MERLOT", "SWEET", 2021);
        zinfandelVine1 = createVines("ZINFANDEL_VINE", VineColor.ORANGE, "ZINFANDEL", "DRY", 2000);
        cabernetVine2 = createVines("CABERNET_VINE", VineColor.WHITE, "CABERNET SAUVIGNON", "SWEET", 2020);
        cabernetVine1 = createVines("CABERNET_VINE", VineColor.RED, "CABERNET SAUVIGNON", "SEMI-SWEET", 2013);
        merlotVine2 = createVines("MERLOT_VINE", VineColor.WHITE, "MERLOT", "SWEET", 2024);
        merlotVine3 = createVines("MERLOT_VINE", VineColor.ROSE, "MERLOT", "DRY", 2001);
        defaultListOfVines = List.of(merlotVine1, zinfandelVine1, cabernetVine2, cabernetVine1, merlotVine2,
            merlotVine3);


        // create postOffice
        lvivPostOffice = createPostOffice("Lviv", "Pakova, 9", 1);
        kyivPostOffice = createPostOffice("Kyiv", "Naukova, 2", 10);
        ternopilPostOffice = createPostOffice("Ternopil", "Opilska, 4", 4);


        // create orders
        order1 = createOrder(merlotVine1, 10, createDeliveryDetails("Lviv, Kopernyka str, 90", null));
        order2 = createOrder(zinfandelVine1, 5, createDeliveryDetails("Lutsk, Shevchenko str, 14", null));
        order3 = createOrder(zinfandelVine1, 7, createDeliveryDetails(null, lvivPostOffice));
        order4 = createOrder(merlotVine1, 1, createDeliveryDetails(null, kyivPostOffice));

        flushAndClear();
    }

    @Test
    void testGetAllVines() {
        List<Vine> result = vineRepository.getAllVines();
        assertThat(result).hasSize(6);
    }

    @Test
    void testGetVineIdWithFilterByName() {
        List<Long> result = vineRepository.getVineIdsWithFilterByName("MERLOT_VINE");
        assertThat(result).containsExactlyInAnyOrder(merlotVine1.getId(), merlotVine2.getId(), merlotVine3.getId());
    }

    @Test
    void testGetVineNameWithFilterByJoinedOrderId() {
        String vineName = vineRepository.getVineNameWithFilterByJoinedOrderId(order1.getId());

        assertThat(vineName).isEqualTo(merlotVine1.getName());
    }

    @Test
    void testGetOrderPostOfficeDto() {
        List<OrderPostOfficeDto> result = vineRepository.getOrderPostOfficeDto();

        List<OrderPostOfficeDto> expectedList = new ArrayList<>();
        expectedList.add(new OrderPostOfficeDto(order3.getId(), "Lviv", 1));
        expectedList.add(new OrderPostOfficeDto(order4.getId(), "Kyiv", 10));

        assertThat(result).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedList);
    }

    @Test
    void testGetOrderVinesWithFilteringInSubQuery() {

        List<OrderVine> result = vineRepository.getOrderVinesWithFilteringInSubQuery(2012, 2021);
        assertThat(result).hasSize(2);

    }

    @Test
    void testGetAllOrdersWithFetchedDeliveryDetails() {
        List<Order> result = vineRepository.getAllOrdersWithFetchedDeliveryDetails();

        PostOffice postOfficeFromOrder3 = result.stream()
            .filter(order -> order.getId().equals(order3.getId()))
            .map(order -> order.getDeliveryDetails().getPostOffice())
            .findFirst().get();

        assertThat(result).hasSize(4);
        assertThat(postOfficeFromOrder3).isEqualToComparingFieldByFieldRecursively(lvivPostOffice);


    }

    @Test
    void testGetMapVineNameToSoldVine() {
        Map<String, Long> result = vineRepository.getMapVineNameToSoldVine();

        Map<String, Long> expectedMap = new HashMap<>();
        expectedMap.put("MERLOT_VINE", 2L);
        expectedMap.put("ZINFANDEL_VINE", 2L);
        expectedMap.put("CABERNET_VINE", 0L);

        assertThat(result).isEqualTo(expectedMap);
    }

    @Test
    void testGetOrderVinesWithFilteringInCte() {

        List<OrderVine> result = vineRepository.getOrderVinesWithFilteringInCte(2012, 2021);
        assertThat(result).hasSize(2);

    }

    @Test
    void testUpdateVineNameById() {
        vineRepository.updateVineNameById("Bordeaux", merlotVine1.getId());
        flushAndClear();
        Optional<Vine> result = vineRepository.findById(merlotVine1.getId());
        assertThat(result.get().getName()).isEqualTo("Bordeaux");
    }

    @Test
    void testDeletePostOfficeById() {
        vineRepository.deletePostOfficeById(ternopilPostOffice.getId());
        flushAndClear();
        boolean result = postOfficeRepository.existsById(ternopilPostOffice.getId());
        assertThat(result).isFalse();
    }


////////////////////////////////////////////////////
    private Vine createVines(String name, VineColor vineColor, String grape, String sugar, Integer year) {
        Vine vine = new Vine();
        vine.setName(name);
        vine.setColor(vineColor);
        vine.setCountry("UKRAINE");
        vine.setManufacturer("KOLONIST");
        vine.setYear(year);
        vine.setSugar(sugar);
        vine.setGrapeName(grape);
        vine.setAmount(100);
        vine.setSoldWine(10);
        vine.setAbv(10);
        vine.setIsSparkling(true);
        vine.setPrice(1000);
        vine.setRegion("KHERSON");
        return vineRepository.save(vine);
    }

    private Order createOrder(Vine vine, Integer vineAmount, DeliveryDetails deliveryDetails) {
        Order order = new Order();
        order.addVine(vine, vineAmount);
        order.setUserId(1L);
        order.setDeliveryDetails(deliveryDetails);
        return orderRepository.saveAndFlush(order);
    }

    private PostOffice createPostOffice(String city, String address, Integer number) {
        PostOffice postOffice = new PostOffice();
        postOffice.setCity(city);
        postOffice.setOfficeAddress(address);
        postOffice.setOfficeNumber(number);

        return postOfficeRepository.saveAndFlush(postOffice);
    }

    private  DeliveryDetails createDeliveryDetails(String homeAddress, PostOffice postOffice) {
        DeliveryDetails deliveryDetails = new DeliveryDetails();
        deliveryDetails.setHomeAddress(homeAddress);
        deliveryDetails.setPostOffice(postOffice);
        deliveryDetails.setUserEmail("colombo@gmail.com");
        deliveryDetails.setPhoneNumber("+38056893456");
        deliveryDetails.setUserLastName("Bybko");
        deliveryDetails.setUserFirstName("John");
        return deliveryDetails;
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

}
