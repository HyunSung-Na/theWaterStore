package com.report.thewaterstore.store.application;

import com.report.thewaterstore.error.NotFoundException;
import com.report.thewaterstore.store.domain.Day;
import com.report.thewaterstore.store.domain.Store;
import com.report.thewaterstore.store.domain.StoreRepository;
import com.report.thewaterstore.store.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Store saveStore(StoreCreateDto storeCreateDto) {
        Store newStore = createProcess(storeCreateDto);
        newStore.addBusinessTime(storeCreateDto.getBusinessTimes());
        return storeRepository.save(newStore);
    }

    private Store createProcess(StoreCreateDto storeCreateDto) {
        Store newStore = Store.builder()
                .name(storeCreateDto.getName())
                .owner(storeCreateDto.getOwner())
                .description(storeCreateDto.getDescription())
                .address(storeCreateDto.getAddress())
                .phoneNumber(storeCreateDto.getPhoneNumber())
                .level(storeCreateDto.getLevel())
                .build();

        return storeRepository.save(newStore);
    }

    public void addHolidayStore(StoreHolidayCreateDto createDto) {
        Store store = findByStoreId(createDto.getId());
        store.addHolidays(createDto.getHolidays());
        storeRepository.save(store);
    }

    @Transactional(readOnly = true)
    public StoreDetailResponseDto findDetailStore(Long storeId) {
        Store store = findByStoreId(storeId);

        Calendar calendar = Calendar.getInstance();
        Day day = new Day(calendar);
        List<BusinessDayDto> businessDayDtoList = store.detailBusinessDays(day);

        return new StoreDetailResponseDto().of(store, businessDayDtoList);
    }

    @Transactional(readOnly = true)
    public List<StoresResponseDto> stores() {
        List<Store> stores = storeRepository.findAll();
        List<StoresResponseDto> responseDtoList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        Day day = new Day(calendar);

        stores.forEach(
                it -> {
                    String status = it.isHoliday(day);

                    if (status.equals("NO")) {
                        status = it.isBusiness(day);
                    }
                    responseDtoList.add(new StoresResponseDto().of(it, status));
                });
        Collections.sort(responseDtoList);
        return responseDtoList;
    }

    public void deleteStore(Long storeId) {
        storeRepository.delete(findByStoreId(storeId));
    }

    @Transactional(readOnly = true)
    public Store findByStoreId(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(storeId));
    }
}
