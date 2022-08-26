package za.co.vending.machine.backendservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.vending.machine.backendservice.domain.ItemDto;
import za.co.vending.machine.backendservice.exceptions.ErrorHandler;
import za.co.vending.machine.backendservice.persistance.entity.CurrencyDenominatorEntity;
import za.co.vending.machine.backendservice.persistance.entity.MachineItemEntity;
import za.co.vending.machine.backendservice.persistance.repository.CurrencyDenominatorRepository;
import za.co.vending.machine.backendservice.persistance.repository.MachineItemRepository;
import za.co.vending.machine.backendservice.service.Impl.VendingMachineServiceImpl;
import za.co.vending.machine.backendservice.service.calculation.ChangeCalculator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class VendingMachineServiceTest {
    @Mock
    private MachineItemRepository machineItemRepository;

    @Mock
    private CurrencyDenominatorRepository currencyDenominatorRepository;

    @Mock
    private ChangeCalculator changeCalculator;

    @InjectMocks
    private VendingMachineServiceImpl vendingMachineService;

    @Test
    void getAllProducts_Success() throws ErrorHandler {
        MachineItemEntity itemDto = MachineItemEntity.builder().id(1L).product("Lays").price(12.4).build();
        Mockito.when(machineItemRepository.findAll()).thenReturn(Arrays.asList(itemDto));

        List<ItemDto> items = vendingMachineService.getAllProducts();
        Assertions.assertEquals(itemDto.getId(), items.get(0).getId());
        Assertions.assertEquals(itemDto.getProduct(), items.get(0).getProduct());
        Assertions.assertEquals(itemDto.getPrice(), items.get(0).getPrice());
    }

    @Test
    void getAllProducts_empty() {
        Assertions.assertThrows(ErrorHandler.class, () ->  vendingMachineService.getAllProducts());

    }

    @Test
    void testCurrencyDenominator_Success() throws ErrorHandler {
        CurrencyDenominatorEntity itemDto = CurrencyDenominatorEntity.builder().id(1L).currencyDenominator(new BigDecimal(5)).build();
        Mockito.when(currencyDenominatorRepository.findAll()).thenReturn(Arrays.asList(itemDto));

        List<BigDecimal> items = vendingMachineService.getCurrencyDenominator();
        Assertions.assertEquals(itemDto.getCurrencyDenominator(), items.get(0));
    }

    @Test
    void testCurrencyDenominator_empty() {
        List<BigDecimal> items = vendingMachineService.getCurrencyDenominator();
        Assertions.assertEquals(0, items.size());

    }



    @Test
    void testPurchase_Success() throws ErrorHandler {
        List<ItemDto> itemDto = Arrays.asList(ItemDto.builder().id(1).product("Lays").price(15).build());
        BigDecimal credit = new BigDecimal(20);

        Mockito.when(changeCalculator.calculateChange(any(), any())).thenReturn(Arrays.asList(new BigDecimal(5)));
        List<BigDecimal> items = vendingMachineService.performPurchase(itemDto, credit);
        Assertions.assertEquals(new BigDecimal(5), items.get(0));
    }

    @Test
    void testPurchase_no_insufficient() throws ErrorHandler {
        List<ItemDto> itemDto = Arrays.asList(ItemDto.builder().id(1).product("Lays").price(7).build());
        BigDecimal credit = new BigDecimal(10);

        Mockito.when(changeCalculator.calculateChange(any(), any())).thenThrow(new ErrorHandler("NoSufficient change"));
        Assertions.assertThrows(ErrorHandler.class, () ->  vendingMachineService.performPurchase(itemDto, credit));

    }
}
