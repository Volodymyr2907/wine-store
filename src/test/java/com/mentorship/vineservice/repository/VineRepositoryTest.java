package com.mentorship.vineservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mentorship.vineservice.domain.DeliveryDetails;
import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.PostOffice;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.enums.VineColor;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
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
    private PostOffice ps3;


    @BeforeEach
    public void setUpData() {

        merlotVine1 = createVines("MERLOT_VINE", VineColor.RED, "MERLOT", "SWEET", 2021);
        zinfandelVine1 = createVines("ZINFANDEL_VINE", VineColor.ORANGE, "ZINFANDEL", "DRY", 2000);
        cabernetVine2 = createVines("CABERNET_VINE", VineColor.WHITE, "CABERNET SAUVIGNON", "SWEET", 2020);
        cabernetVine1 = createVines("CABERNET_VINE", VineColor.RED, "CABERNET SAUVIGNON", "SEMI-SWEET", 2013);
        merlotVine2 = createVines("MERLOT_VINE", VineColor.WHITE, "MERLOT", "SWEET", 2024);
        merlotVine3 = createVines("MERLOT_VINE", VineColor.ROSE, "MERLOT", "DRY", 2001);
        defaultListOfVines = List.of(merlotVine1, zinfandelVine1, cabernetVine2, cabernetVine1, merlotVine2,
            merlotVine3);

        PostOffice ps1 = createPostOffice("Lviv", "Pakova, 9", 1);
        PostOffice ps2 = createPostOffice("Kyiv", "Naukova, 2", 10);
        ps3 = createPostOffice("Ternopil", "Opilska, 4", 4);

        DeliveryDetails dd1 = new DeliveryDetails();
        dd1.setHomeAddress("Lviv, Kopernyka str, 90");
        DeliveryDetails dd2 = new DeliveryDetails();
        dd2.setHomeAddress("Lutsk, Shevchenko str, 14");
        DeliveryDetails dd3 = new DeliveryDetails();
        dd3.setPostOffice(ps2);
        DeliveryDetails dd4 = new DeliveryDetails();
        dd4.setPostOffice(ps1);

        Order order1 = createOrder(merlotVine1, 10, createDeliveryDetails("Lviv, Kopernyka str, 90", null));
        Order order2 = createOrder(zinfandelVine1, 5, createDeliveryDetails("Lutsk, Shevchenko str, 14", null));
        Order order3 = createOrder(merlotVine1, 7, createDeliveryDetails(null, ps1));
        Order order4 = createOrder(merlotVine1, 1, createDeliveryDetails(null, ps2));


        entityManager.flush();

    }

    @Test
    void test() {
        List<Vine> result = vineRepository.getAllVines();
        assertThat(result).hasSameElementsAs(defaultListOfVines);
    }

    @Test
    void test2() {
        Long result = vineRepository.getVineIdWithFiltering(1L);
        assertThat(result).isEqualTo(merlotVine1.getId());
    }

    @Test
    void test3() {
        Long result = vineRepository.getVineIdFilteredByJoinId();
    }

    @Test
    void test4() {
        var result = vineRepository.getAllOrdersIdWithDeliveryInfo();
        assertThat(result).isEmpty();
    }

    @Test
    void test5() {
        var result = vineRepository.getVinesWithFilteringInSubQuery();
        assertThat(result).isEmpty();

    }

    @Test
    void test6() {
        var result = vineRepository.getAllOrdersWithDeliveryDetails();
        assertThat(result).isEmpty();

    }

    @Test
    void test7() {
        var result = vineRepository.getCountedSoldWines();
        assertThat(result).isEmpty();
    }

    @Test
    void test8() {
        var result = vineRepository.getVinesWithFilteringInCte();
        assertThat(result).isEmpty();

    }

    @Test
    void test9() {
        vineRepository.updateVineNameById("zalupa", 1L);
        entityManager.clear();
        entityManager.flush();
        var result = vineRepository.getById(1L);
        assertThat(result.getName()).isEqualTo("zalupa");
    }

    @Test
    void test10() {
        Long id = ps3.getId();
        vineRepository.deletePostOfficeById(id);
        entityManager.clear();
        entityManager.flush();
        var result = postOfficeRepository.existsById(id);
        assertThat(result).isFalse();
    }


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
        return vineRepository.saveAndFlush(vine);
    }

    private Order createOrder(Vine vine, Integer vineAmount, DeliveryDetails deliveryDetails) {
        Order order = new Order();
        order.addVine(vine, vineAmount);
        order.setDatetime(LocalDateTime.MAX);
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

}
