package za.co.vending.machine.backendservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.vending.machine.backendservice.domain.ItemDto;
import za.co.vending.machine.backendservice.exceptions.ErrorHandler;
import za.co.vending.machine.backendservice.service.VendingMachineService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/vending")
@RequiredArgsConstructor
public class VendingMachineController {

    private final VendingMachineService vendingMachineService;

    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> getAllItems() throws ErrorHandler {
        List<ItemDto> items = vendingMachineService.getAllProducts();
        return new ResponseEntity<List<ItemDto>>(items, HttpStatus.OK);
    }

    @GetMapping("/currency/denominator")
    public ResponseEntity<List<BigDecimal>> getCurrenctyDenominatorEndpoint() {
        List<BigDecimal> currencyDenominator = vendingMachineService.getCurrencyDenominator();
        return new ResponseEntity<List<BigDecimal>>(currencyDenominator, HttpStatus.OK);
    }

    @PostMapping(path = "/purchase/{credit}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BigDecimal>> purchase(
            @PathVariable(value = "credit", required = true) BigDecimal credit,
            @RequestBody List<ItemDto> itemDto) {
        log.info("Started purchasing {}", itemDto);
        List<BigDecimal> change = new ArrayList<>();
        try {
            change = vendingMachineService.performPurchase(itemDto, credit);
        } catch (Exception e) {
            return new ResponseEntity<List<BigDecimal>>(change, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<BigDecimal>>(change, HttpStatus.OK);
    }
}
