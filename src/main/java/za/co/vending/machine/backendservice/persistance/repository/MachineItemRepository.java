package za.co.vending.machine.backendservice.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.vending.machine.backendservice.persistance.entity.MachineItemEntity;

@Repository
public interface MachineItemRepository extends JpaRepository<MachineItemEntity, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from MachineItemEntity where id = :id")
    void deleteBroughtItems(@Param("id") Long id);
}
