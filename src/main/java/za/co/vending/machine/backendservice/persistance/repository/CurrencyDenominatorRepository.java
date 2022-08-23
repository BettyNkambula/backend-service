package za.co.vending.machine.backendservice.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.vending.machine.backendservice.persistance.entity.CurrencyDenominatorEntity;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CurrencyDenominatorRepository extends JpaRepository<CurrencyDenominatorEntity, Long> {

    @Query("select currencyDenominator from CurrencyDenominatorEntity")
    List<BigDecimal> findAllCurrencyDenominator();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from currency_denominator_table where currency_denominator in (select currency_denominator from currency_denominator_table where currency_denominator = :currencyDenominator limit 1)", nativeQuery = true)
    void deleteCurrencyDenomination(@Param("currencyDenominator") BigDecimal currencyDenominator);
}
