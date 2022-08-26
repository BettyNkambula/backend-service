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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
public class ChangeCalculatorTest {
    @Mock
    private MachineItemRepository machineItemRepository;

    @Mock
    private CurrencyDenominatorRepository currencyDenominatorRepository;

    @InjectMocks
    private ChangeCalculator changeCalculator;

    @Test
    void testCalculateChangeCount_success() throws ErrorHandler {
        List<ItemDto> itemDto = Arrays.asList(ItemDto.builder().id(1).product("Lays").price(15).build());
        BigDecimal change = new BigDecimal(100);
        Mockito.when(currencyDenominatorRepository.findAllCurrencyDenominator()).thenReturn(Arrays.asList(new BigDecimal(20),new BigDecimal(20),new BigDecimal(20),new BigDecimal(20),new BigDecimal(10),new BigDecimal(10)));
        List<BigDecimal> items = changeCalculator.calculateChange(change, itemDto);
        Map map = countFrequencies(items);
        Assertions.assertEquals(4, map.get(new BigDecimal(20)));
        Assertions.assertEquals(2, map.get(new BigDecimal(10)));
    }

    @Test
    void testCalculateChangeCount_success2() throws ErrorHandler {
        List<ItemDto> itemDto = Arrays.asList(ItemDto.builder().id(1).product("Lays").price(15).build());
        BigDecimal change = new BigDecimal(100);
        Mockito.when(currencyDenominatorRepository.findAllCurrencyDenominator()).thenReturn(Arrays.asList(new BigDecimal(20),new BigDecimal(20),new BigDecimal(20),new BigDecimal(20),new BigDecimal(10),new BigDecimal(5), new BigDecimal(5)));
        List<BigDecimal> items = changeCalculator.calculateChange(change, itemDto);
        Map map = countFrequencies(items);
        Assertions.assertEquals(4, map.get(new BigDecimal(20)));
        Assertions.assertEquals(1, map.get(new BigDecimal(10)));
        Assertions.assertEquals(2, map.get(new BigDecimal(5)));
    }

    public  Map countFrequencies(List<BigDecimal> list)
    {
        // hashmap to store the frequency of element
        Map<BigDecimal, Integer> hm = new HashMap<BigDecimal, Integer>();
        for (BigDecimal i : list) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }
        return hm;
    }

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
