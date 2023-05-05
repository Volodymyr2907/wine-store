package com.mentorship.vineservice.controller;


import static com.mentorship.vineservice.dto.enums.VineColor.RED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorship.vineservice.dto.response.ErrorResponse;
import com.mentorship.vineservice.dto.VineDto;
import com.mentorship.vineservice.dto.VinesDto;
import com.mentorship.vineservice.dto.enums.UserRole;
import com.mentorship.vineservice.dto.enums.VineColor;
import com.mentorship.vineservice.model.VinesQueryParameters;
import com.mentorship.vineservice.service.OrderService;
import com.mentorship.vineservice.service.PermissionValidator;
import com.mentorship.vineservice.service.VineService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = VineController.class)
public class VineControllerTest {

    private static final String GET_VINES_URL = "/api/vine-service/vines";
    private static final String CREATE_VINE_URL = "/api/vine-service/vine";
    private static final String X_TOTAL_COUNT_HEADER = "X-Total-Count";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String RANDOM_TOKEN = "random token";


    @MockBean
    PermissionValidator permissionValidationService;

    @MockBean
    VineService vineService;

    @MockBean
    OrderService orderService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private VinesDto listOfValidVineDtosWithCount;
    private List<VineDto> listOfValidVineDtos;
    private VineDto merlotVineDto;
    private VineDto primitivoVineDto;
    private VineDto vineDtoWithMissingName;
    private VineDto vineDtoWithMissingColor;
    private VineDto vineDtoWithNegativeYearValue;
    private VineDto zinfandelVineDto;

    @BeforeEach
    public void setupVines() {

        primitivoVineDto = createVineDto(1L, VineColor.RED, "DRY", "PRIMITIVO VINE", "PRIMITIVO", 2018);
        merlotVineDto = createVineDto(2L, VineColor.RED, "DRY", "MERLOT VINE", "MERLOT", 2017);
        zinfandelVineDto = createVineDto(2L, VineColor.ROSE, "SEMI-SWEET", "ZINFANDEL VINE", "ZINFANDEL", 2017);

        vineDtoWithMissingName = createVineDto(1L, VineColor.RED, "DRY", null, "PRIMITIVO", 2018);
        vineDtoWithMissingColor = createVineDto(1L, null, "DRY", "PRIMITIVO VINE", "PRIMITIVO", 2018);
        vineDtoWithNegativeYearValue = createVineDto(1L, VineColor.RED, "DRY", "PRIMITIVO VINE", "PRIMITIVO", -2018);

        listOfValidVineDtos = List.of(primitivoVineDto, merlotVineDto, zinfandelVineDto);

        listOfValidVineDtosWithCount = new VinesDto(3L, listOfValidVineDtos);

    }


    @Test
    public void shouldGetAllVinesWithOkStatusIfFilterAndPaginationMissing() throws Exception {

        when(vineService.getVinesWithFilterAndPagination(any())).thenReturn(listOfValidVineDtosWithCount);

        mockMvc.perform(get(GET_VINES_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(listOfValidVineDtos)))
            .andExpect(MockMvcResultMatchers.header()
                .stringValues(X_TOTAL_COUNT_HEADER, String.valueOf(listOfValidVineDtosWithCount.getTotalCount())));

    }

    @Test
    public void shouldReturnVinesBasedOnColorAndSugarFilterWithOkStatus() throws Exception {

        String vineSugar = "DRY";

        VinesQueryParameters queryParameters = VinesQueryParameters.builder()
            .page(0)
            .size(5)
            .sugar(vineSugar)
            .color(RED)
            .build();

        when(vineService.getVinesWithFilterAndPagination(eq(queryParameters))).thenReturn(
            new VinesDto(2L, List.of(primitivoVineDto, merlotVineDto)));

        mockMvc.perform(get(GET_VINES_URL)
                .param("color", RED.name())
                .param("sugar", vineSugar)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(primitivoVineDto, merlotVineDto))))
            .andExpect(MockMvcResultMatchers.header()
                .stringValues(X_TOTAL_COUNT_HEADER, String.valueOf(2L)));

    }

    @Test
    public void shouldFilterAndReturnVinesByGrapeNameAndYear() throws Exception {

        String grapeName = "MERLOT";
        Integer year = 2017;

        VinesQueryParameters queryParameters = VinesQueryParameters.builder()
            .page(0)
            .size(5)
            .grapeName(grapeName)
            .year(year)
            .build();

        when(vineService.getVinesWithFilterAndPagination(eq(queryParameters))).thenReturn(
            new VinesDto(1L, List.of(merlotVineDto)));

        mockMvc.perform(get(GET_VINES_URL)
                .param("grape", grapeName)
                .param("year", String.valueOf(year))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(merlotVineDto))))
            .andExpect(MockMvcResultMatchers.header()
                .stringValues(X_TOTAL_COUNT_HEADER, String.valueOf(1L)));

    }

    @Test
    public void shouldFilterAndReturnVinesByName() throws Exception {

        String vineName = "PRIMITIVO VINE";

        VinesQueryParameters queryParameters = VinesQueryParameters.builder()
            .page(0)
            .size(5)
            .name(vineName)
            .build();

        when(vineService.getVinesWithFilterAndPagination(eq(queryParameters))).thenReturn(
            new VinesDto(1L, List.of(primitivoVineDto)));

        mockMvc.perform(get(GET_VINES_URL)
                .param("name", vineName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(primitivoVineDto))))
            .andExpect(MockMvcResultMatchers.header()
                .stringValues(X_TOTAL_COUNT_HEADER, String.valueOf(1L)));

    }

    @Test
    public void shouldReturnBadRequestIfSearchVinesWithNotSupportedColor() throws Exception {

        String invalidColorType = "BLUE";

        String responseContent = mockMvc.perform(get(GET_VINES_URL)
                .param("color", invalidColorType)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(responseContent)).containsOnly(String.format(
            "Unknown enum type %s, Allowed values are [VineColor.WHITE, VineColor.RED, VineColor.ORANGE, VineColor.ROSE]",
            invalidColorType));

    }

    @Test
    public void shouldCreateVineAndReturnCreatedStatusWithVineId() throws Exception {

        doNothing().when(permissionValidationService).validateUserPermission(isA(String.class), isA(UserRole.class));

        when(vineService.saveVine(any())).thenReturn(1L);

        mockMvc.perform(post(CREATE_VINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, RANDOM_TOKEN)
                .content(objectMapper.writeValueAsString(primitivoVineDto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().string("1"));
    }

    @Test
    public void shouldNotCreateVineIfNameMissingAndReturnBadRequest() throws Exception {

        doNothing().when(permissionValidationService).validateUserPermission(isA(String.class), isA(UserRole.class));

        String responseContent = mockMvc.perform(post(CREATE_VINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, RANDOM_TOKEN)
                .content(objectMapper.writeValueAsString(vineDtoWithMissingName)))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(responseContent)).containsOnly("Name can not be empty or null");

    }

    @Test
    public void shouldNotCreateVineIfYearValueIsNegativeAndReturnBadRequest() throws Exception {

        doNothing().when(permissionValidationService).validateUserPermission(isA(String.class), isA(UserRole.class));

        String responseContent = mockMvc.perform(post(CREATE_VINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, RANDOM_TOKEN)
                .content(objectMapper.writeValueAsString(vineDtoWithNegativeYearValue)))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(responseContent)).containsOnly("Year must be greater then 0");

    }

    @Test
    public void shouldNotCreateVineIfColorIsMissingAndReturnBadRequest() throws Exception {

        doNothing().when(permissionValidationService).validateUserPermission(isA(String.class), isA(UserRole.class));

        String responseContent = mockMvc.perform(post(CREATE_VINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, RANDOM_TOKEN)
                .content(objectMapper.writeValueAsString(vineDtoWithMissingColor)))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(responseContent)).containsOnly("Color can not be null");

    }


    private VineDto createVineDto(Long vineId, VineColor vineColor, String sugar, String name, String grapeName,
        Integer year) {
        VineDto vineDto = new VineDto();
        vineDto.setId(vineId);
        vineDto.setColor(vineColor);
        vineDto.setSugar(sugar);
        vineDto.setName(name);
        vineDto.setGrapeName(grapeName);
        vineDto.setYear(year);
        vineDto.setCountry("UKRAINE");
        vineDto.setManufacturer("SHABO");
        return vineDto;
    }

    private List<String> getErrorListFromResponse(String responseAsString) throws JsonProcessingException {
        return objectMapper.readValue(responseAsString, ErrorResponse.class).getErrors();
    }


}
