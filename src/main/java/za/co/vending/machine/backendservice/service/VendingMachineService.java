package za.co.vending.machine.backendservice.service;


import za.co.vending.machine.backendservice.domain.ItemDto;
import za.co.vending.machine.backendservice.exceptions.ErrorHandler;

import java.math.BigDecimal;
import java.util.List;

public interface VendingMachineService {
    List<ItemDto> getAllProducts() throws ErrorHandler;

    List<BigDecimal> getCurrencyDenominator();

    List<BigDecimal> performPurchase(List<ItemDto> itemDto, BigDecimal credit) throws ErrorHandler;
}
