package za.co.vending.machine.backendservice.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.vending.machine.backendservice.service.calculation.ChangeCalculator;
import za.co.vending.machine.backendservice.domain.ItemDto;
import za.co.vending.machine.backendservice.exceptions.ErrorHandler;
import za.co.vending.machine.backendservice.persistance.entity.MachineItemEntity;
import za.co.vending.machine.backendservice.persistance.repository.MachineItemRepository;
import za.co.vending.machine.backendservice.persistance.repository.CurrencyDenominatorRepository;
import za.co.vending.machine.backendservice.service.VendingMachineService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendingMachineServiceImpl implements VendingMachineService {

    private final MachineItemRepository machineItemRepository;

    private final CurrencyDenominatorRepository currencyDenominatorRepository;

    private final ChangeCalculator changeCalculator;

    @Override
    public List<ItemDto> getAllProducts() throws ErrorHandler {
        List<ItemDto> items = new ArrayList<>();
        List<MachineItemEntity> list = machineItemRepository.findAll();

        if (!list.isEmpty()) {
            for (MachineItemEntity item : list) {
                items.add(ItemDto.builder()
                        .id(item.getId().intValue())
                        .product(item.getProduct())
                        .price(item.getPrice())
                        .build());
            }

            log.info("Retrieved all machine items ");
        } else {
            log.info("All items sold out");
            throw new ErrorHandler("Items Sold Out");
        }

        return items;
    }

    @Override
    public List<BigDecimal> getCurrencyDenominator() {
        List<BigDecimal> currencyDenominator = new ArrayList<>();
        currencyDenominatorRepository.findAll().forEach((i)-> currencyDenominator.add(i.getCurrencyDenominator()));
        log.info("Retrieved the machine petty cash {}", currencyDenominator);
        return currencyDenominator;
    }

    @Override
    public List<BigDecimal> performPurchase(List<ItemDto> items, BigDecimal credit) throws ErrorHandler {
        double totalItemPrice = items.stream().mapToDouble(ItemDto::getPrice).sum();
        BigDecimal totalChange = credit.subtract(BigDecimal.valueOf(totalItemPrice));
        List<BigDecimal> change = new ArrayList<>();
        try {
            change = changeCalculator.calculateChange(totalChange, items);
            log.info("Successfully retrieved change {}", change);
        } catch (Exception e) {
            log.error("An error occured while processing a purchase", e);
            throw new ErrorHandler("An error occured while processing a purchase {}");
        }
        return change;
    }
}
