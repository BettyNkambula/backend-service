package za.co.vending.machine.backendservice.service.calculation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import za.co.vending.machine.backendservice.domain.ItemDto;
import za.co.vending.machine.backendservice.exceptions.ErrorHandler;
import za.co.vending.machine.backendservice.persistance.entity.CurrencyDenominatorEntity;
import za.co.vending.machine.backendservice.persistance.repository.CurrencyDenominatorRepository;
import za.co.vending.machine.backendservice.persistance.repository.MachineItemRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChangeCalculator {

    private final CurrencyDenominatorRepository currencyDenominatorRepository;
    private final MachineItemRepository machineItemRepository;

    public List<BigDecimal> calculateChange(BigDecimal totalChange, List<ItemDto> itemDtoList) throws ErrorHandler {
        List<BigDecimal> change = getChange(totalChange);
        updateCurrencyDenominator(change);
        updateMachineItems(itemDtoList);

        return change;
    }

    public BigDecimal calculateTotalPettyCash() {
        int pettyCash = 0;
        for (CurrencyDenominatorEntity currencyDenominatorEntity : currencyDenominatorRepository.findAll()) {
            pettyCash += currencyDenominatorEntity.getCurrencyDenominator().intValue();
        }
        return new BigDecimal(pettyCash);
    }

    private void updateCurrencyDenominator(List<BigDecimal> denominationEnumList) {
        denominationEnumList.forEach(currencyDenominatorRepository::deleteCurrencyDenomination);
    }

    private void updateMachineItems(List<ItemDto> denominationEnumList) {
        denominationEnumList.forEach((i)-> machineItemRepository.deleteBroughtItems((long) i.getId()));
    }

    private List<BigDecimal> getChange(BigDecimal amount) throws ErrorHandler {
        final List<BigDecimal> changes = new ArrayList<BigDecimal>();
        ArrayList<BigDecimal> pettyCashEntityList = new ArrayList<>(currencyDenominatorRepository.findAllCurrencyDenominator());
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            for (BigDecimal denom : pettyCashEntityList) {
                if (amount.compareTo(denom)>=0) {
                    changes.add(denom);
                    amount = amount.subtract(denom);
                } else {
                    throw new ErrorHandler("NotSufficientChange, Please try another product");
                }
            }
        }

        return changes;
    }
}
