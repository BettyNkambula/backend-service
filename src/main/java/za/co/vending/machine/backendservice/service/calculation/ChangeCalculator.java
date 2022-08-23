package za.co.vending.machine.backendservice.service.calculation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import za.co.vending.machine.backendservice.domain.CurrencyDenominationEnum;
import za.co.vending.machine.backendservice.domain.ItemDto;
import za.co.vending.machine.backendservice.exceptions.ErrorHandler;
import za.co.vending.machine.backendservice.persistance.entity.CurrencyDenominatorEntity;
import za.co.vending.machine.backendservice.persistance.repository.CurrencyDenominatorRepository;
import za.co.vending.machine.backendservice.persistance.repository.MachineItemRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChangeCalculator {

    private final CurrencyDenominatorRepository currencyDenominatorRepository;
    private final MachineItemRepository machineItemRepository;

    public List<BigDecimal> calculateChange(BigDecimal totalChange, List<ItemDto> itemDtoList) throws ErrorHandler {
        List<BigDecimal> changeDenominators = new ArrayList<>();
        List<CurrencyDenominationEnum> change = getChange(totalChange);
        updateCurrencyDenominator(change);
        updateMachineItems(itemDtoList);

        change.forEach((i)-> changeDenominators.add(new BigDecimal(i.getDenomination())));
        return changeDenominators;
    }

    public BigDecimal calculateTotalPettyCash() {
        int pettyCash = 0;
        for (CurrencyDenominatorEntity currencyDenominatorEntity : currencyDenominatorRepository.findAll()) {
            pettyCash += currencyDenominatorEntity.getCurrencyDenominator().intValue();
        }
        return new BigDecimal(pettyCash);
    }

    private void updateCurrencyDenominator(List<CurrencyDenominationEnum> denominationEnumList) {
        denominationEnumList.forEach((i)-> currencyDenominatorRepository.deleteCurrencyDenomination(new BigDecimal(i.getDenomination())));
    }

    private void updateMachineItems(List<ItemDto> denominationEnumList) {
        denominationEnumList.forEach((i)-> machineItemRepository.deleteBroughtItems(new Long(i.getId())));
    }

    private List<CurrencyDenominationEnum> getChange(BigDecimal amount) throws ErrorHandler {
        List<CurrencyDenominationEnum> changes = Collections.EMPTY_LIST;
        List<BigDecimal> pettyCashEntityList = currencyDenominatorRepository.findAllCurrencyDenominator();
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            changes = new ArrayList<CurrencyDenominationEnum>();
            BigDecimal balance = amount;
            while (balance.compareTo(BigDecimal.ZERO) > 0) {
                if (balance.compareTo(new BigDecimal(CurrencyDenominationEnum.TWENTYRAND.getDenomination()))>=0
                        && pettyCashEntityList.contains(new BigDecimal(CurrencyDenominationEnum.TWENTYRAND.getDenomination()))) {
                    changes.add(CurrencyDenominationEnum.TWENTYRAND);
                    balance = balance.subtract(new BigDecimal(CurrencyDenominationEnum.TWENTYRAND.getDenomination()));

                } else if (balance.compareTo(new BigDecimal(CurrencyDenominationEnum.TENRAND.getDenomination())) >= 0
                        && pettyCashEntityList.contains(new BigDecimal(CurrencyDenominationEnum.TENRAND.getDenomination()))) {
                    changes.add(CurrencyDenominationEnum.TENRAND);
                    balance = balance.subtract(new BigDecimal(CurrencyDenominationEnum.TENRAND.getDenomination()));

                } else if (balance.compareTo(new BigDecimal(CurrencyDenominationEnum.FIVERAND.getDenomination())) >= 0
                        && pettyCashEntityList.contains(new BigDecimal(CurrencyDenominationEnum.FIVERAND.getDenomination()))) {
                    changes.add(CurrencyDenominationEnum.FIVERAND);
                    balance = balance.subtract(new BigDecimal(CurrencyDenominationEnum.FIVERAND.getDenomination()));

                } else {
                    throw new ErrorHandler("NotSufficientChange, Please try another product");
                }
            }
        }

        return changes;
    }
}
