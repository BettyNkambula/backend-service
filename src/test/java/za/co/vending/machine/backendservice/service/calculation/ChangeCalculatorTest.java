package za.co.vending.machine.backendservice.service.calculation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.vending.machine.backendservice.domain.ItemDto;
import za.co.vending.machine.backendservice.exceptions.ErrorHandler;
import za.co.vending.machine.backendservice.persistance.repository.CurrencyDenominatorRepository;
import za.co.vending.machine.backendservice.persistance.repository.MachineItemRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class ChangeCalculatorTest {
    @Mock
    private MachineItemRepository machineItemRepository;

    @Mock
    private CurrencyDenominatorRepository currencyDenominatorRepository;

    @InjectMocks
    private ChangeCalculator changeCalculator;

    @Test
    void testCalculateChange_success() throws ErrorHandler {
        List<ItemDto> itemDto = Arrays.asList(ItemDto.builder().id(1).product("Lays").price(15).build());
        BigDecimal change = new BigDecimal(5);

        Mockito.when(currencyDenominatorRepository.findAllCurrencyDenominator()).thenReturn(Arrays.asList(new BigDecimal(5)));

        List<BigDecimal> items = changeCalculator.calculateChange(change, itemDto);
        Assertions.assertEquals(new BigDecimal(5), items.get(0));
    }

    @Test
    void testCalculateChange_changeNotAvailble_failure() throws ErrorHandler {
        List<ItemDto> itemDto = Arrays.asList(ItemDto.builder().id(1).product("Lays").price(15).build());
        BigDecimal change = new BigDecimal(5);

        Mockito.when(currencyDenominatorRepository.findAllCurrencyDenominator()).thenReturn(Arrays.asList(new BigDecimal(10)));

        Assertions.assertThrows(ErrorHandler.class, () -> changeCalculator.calculateChange(change, itemDto));
    }

    @Test
    void testCalculateChange_notAllowedDenominator_failure() throws ErrorHandler {
        List<ItemDto> itemDto = Arrays.asList(ItemDto.builder().id(1).product("Lays").price(15).build());
        BigDecimal change = new BigDecimal(5);

        Mockito.when(currencyDenominatorRepository.findAllCurrencyDenominator()).thenReturn(Arrays.asList(new BigDecimal(7)));

        Assertions.assertThrows(ErrorHandler.class, () -> changeCalculator.calculateChange(change, itemDto));
    }

}
