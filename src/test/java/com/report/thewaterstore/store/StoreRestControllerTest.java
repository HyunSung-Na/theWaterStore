package com.report.thewaterstore.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.report.thewaterstore.store.application.StoreService;
import com.report.thewaterstore.store.domain.Store;
import com.report.thewaterstore.store.dto.BusinessTimeDto;
import com.report.thewaterstore.store.dto.StoreCreateDto;
import com.report.thewaterstore.store.dto.StoreHolidayCreateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.report.thewaterstore.util.ApiDocumentUtils.getDocumentRequest;
import static com.report.thewaterstore.util.ApiDocumentUtils.getDocumentResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("????????? ?????? ?????? ??????")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs
public class StoreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreService storeService;

    private StoreCreateDto storeCreateDto;
    private Store newStore;

    @BeforeEach
    public void setUp() {
        //given
        storeCreateDto = createDto();
        newStore = storeService.saveStore(storeCreateDto);
    }

    @DisplayName("????????? ????????????")
    @Test
    void createStore() throws Exception {

        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/store")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(storeCreateDto)))
                .andExpect(status().is(201))
                .andDo(print())
                .andDo(document("store-generate-success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("owner").description("????????? ??????"),
                                fieldWithPath("description").description("?????? ??????"),
                                fieldWithPath("level").description("??????"),
                                fieldWithPath("address").description("??????"),
                                fieldWithPath("phoneNumber").description("?????? ??????"),
                                fieldWithPath("businessTimes.[].day").description("?????? ??????"),
                                fieldWithPath("businessTimes.[].open").description("?????? ????????????"),
                                fieldWithPath("businessTimes.[].close").description("?????? ????????????")
                        )))
                .andReturn();

        //then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(mvcResult.getResponse().getHeader("Location")).isNotBlank();
    }

    @DisplayName("????????? ????????? ????????????")
    @Test
    void addHolidays() throws Exception {

        // given
        List<String> holidays = Arrays.asList("2021-05-29", "2021-05-30");
        StoreHolidayCreateDto createHolidayDto = new StoreHolidayCreateDto(newStore.getId(), holidays);

        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/store/addHoliday")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createHolidayDto)))
                .andExpect(status().is(204))
                .andDo(print())
                .andDo(document("store-add-Holiday",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("id").description("?????? id"),
                                fieldWithPath("holidays.[]").description("?????? ??????")
                        )))
                .andReturn();
    }

    @DisplayName("????????? ??????????????? ????????????")
    @Test
    void findStoreDetails() throws Exception {

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/store/" + newStore.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andDo(print())
                .andDo(document("store-findDetail-success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("id").description("?????? id"),
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("description").description("?????? ??????"),
                                fieldWithPath("level").description("??????"),
                                fieldWithPath("address").description("??????"),
                                fieldWithPath("phone").description("?????? ??????"),
                                fieldWithPath("businessDays.[].day").description("?????? ??????"),
                                fieldWithPath("businessDays.[].open").description("?????? ????????????"),
                                fieldWithPath("businessDays.[].close").description("?????? ????????????"),
                                fieldWithPath("businessDays.[].status").description("?????? ?????? ??????")
                        )))
                .andReturn();
    }

    @DisplayName("???????????? ????????????")
    @Test
    void findStores() throws Exception {

        storeService.saveStore(createDto());

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/store")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andDo(print())
                .andDo(document("findAllStores-success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[].name").description("?????? ??????"),
                                fieldWithPath("[].description").description("?????? ??????"),
                                fieldWithPath("[].level").description("?????? ??????"),
                                fieldWithPath("[].businessStatus").description("?????? ?????? ??????")
                        )))
                .andReturn();
    }

    @DisplayName("????????? ????????????")
    @Test
    void deleteStore() throws Exception {

        // when
        MvcResult mvcResult = mockMvc.perform(delete("/api/store/" + newStore.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204))
                .andDo(print())
                .andDo(document("store-delete-success",
                        getDocumentRequest(),
                        getDocumentResponse()))
                .andReturn();
    }

    private StoreCreateDto createDto() {
        String name = "???????????????";
        String owner = "??????";
        String description = "?????? ????????? ???";
        int level = 1;
        String address = "????????? ??????";
        String phoneNumber = "010-9985-4937";
        List<BusinessTimeDto> businessTimes = new ArrayList<>();

        businessTimes.add(new BusinessTimeDto("Monday", "09:00", "18:00"));
        businessTimes.add(new BusinessTimeDto("Tuesday", "09:00", "18:00"));
        businessTimes.add(new BusinessTimeDto("Wednesday", "09:00", "18:00"));
        businessTimes.add(new BusinessTimeDto("Thursday", "09:00", "18:00"));
        businessTimes.add(new BusinessTimeDto("Friday", "09:00", "18:00"));

        return StoreCreateDto.builder()
                .name(name)
                .owner(owner)
                .description(description)
                .level(level)
                .address(address)
                .phoneNumber(phoneNumber)
                .businessTimes(businessTimes)
                .build();
    }
}
