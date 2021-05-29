package com.report.thewaterstore.store.ui;

import com.report.thewaterstore.store.application.StoreService;
import com.report.thewaterstore.store.domain.Store;
import com.report.thewaterstore.store.dto.StoreCreateDto;
import com.report.thewaterstore.store.dto.StoreDetailResponseDto;
import com.report.thewaterstore.store.dto.StoreHolidayCreateDto;
import com.report.thewaterstore.store.dto.StoresResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "api/store", produces = MediaType.APPLICATION_JSON_VALUE)

public class StoreRestController {

    private final StoreService storeService;

    public StoreRestController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity createStore(@RequestBody StoreCreateDto storeCreateDto) {
        Store store = storeService.saveStore(storeCreateDto);
        return ResponseEntity.created(URI.create("/api/store/" + store.getId())).build();
    }

    @PostMapping("addHoliday")
    public ResponseEntity<Void> addHoliday(@RequestBody StoreHolidayCreateDto createDto) {
        storeService.addHolidayStore(createDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<StoresResponseDto>> stores() {
        return ResponseEntity.ok(storeService.stores());
    }

    @GetMapping("{storeId}")
    public ResponseEntity<StoreDetailResponseDto> storeDetail(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.findDetailStore(storeId));
    }

    @DeleteMapping("{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.noContent().build();
    }
}
