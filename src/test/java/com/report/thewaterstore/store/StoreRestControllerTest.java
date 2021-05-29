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

@DisplayName("수산물 상점 관련 기능")
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

    @DisplayName("상점을 등록한다")
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
                                fieldWithPath("name").description("가게 이름"),
                                fieldWithPath("owner").description("사장님 성함"),
                                fieldWithPath("description").description("가게 설명"),
                                fieldWithPath("level").description("등급"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("phoneNumber").description("가게 전화"),
                                fieldWithPath("businessTimes.[].day").description("영업 요일"),
                                fieldWithPath("businessTimes.[].open").description("영업 오픈시간"),
                                fieldWithPath("businessTimes.[].close").description("영업 마감시간")
                        )))
                .andReturn();

        //then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(mvcResult.getResponse().getHeader("Location")).isNotBlank();
    }

    @DisplayName("상점의 휴일을 등록한다")
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
                                fieldWithPath("id").description("가게 id"),
                                fieldWithPath("holidays.[]").description("가게 휴일")
                        )))
                .andReturn();
    }

    @DisplayName("상점의 상세정보를 조회한다")
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
                                fieldWithPath("id").description("가게 id"),
                                fieldWithPath("name").description("가게 이름"),
                                fieldWithPath("description").description("가게 설명"),
                                fieldWithPath("level").description("등급"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("phone").description("가게 전화"),
                                fieldWithPath("businessDays.[].day").description("영업 요일"),
                                fieldWithPath("businessDays.[].open").description("영업 오픈시간"),
                                fieldWithPath("businessDays.[].close").description("영업 마감시간"),
                                fieldWithPath("businessDays.[].status").description("현재 영업 상태")
                        )))
                .andReturn();
    }

    @DisplayName("상점들을 조회한다")
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
                                fieldWithPath("[].name").description("가게 이름"),
                                fieldWithPath("[].description").description("가게 설명"),
                                fieldWithPath("[].level").description("가게 등급"),
                                fieldWithPath("[].businessStatus").description("현재 영업 상태")
                        )))
                .andReturn();
    }

    @DisplayName("상점을 삭제한다")
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
        String name = "물고기수산";
        String owner = "인어";
        String description = "제일 맛있는 집";
        int level = 1;
        String address = "인천시 서구";
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
