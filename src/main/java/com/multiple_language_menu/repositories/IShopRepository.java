package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Shops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface IShopRepository  extends JpaRepository<Shops, String> {
    //TODO: QUERY LOGIC
    @Query("SELECT s FROM Shops s WHERE s.created_by = :createById")
    List<Shops> findByCreatedById(@Param("createById") String createdById);


    @Query("SELECT s FROM Shops s WHERE s.owner.id = :ownerId")
    List<Shops> findByOwnerId( @Param("ownerId") String ownerId);

    @Query("SELECT s FROM Shops s WHERE s.owner.id = :ownerId AND s.contractTerm BETWEEN :startDate AND :endDate")
    List<Shops> findByOwnerIdAndBetweenContractTerm(@Param("ownerId") String ownerId,
                                                    @Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate);

    @Query("SELECT s FROM Shops s WHERE s.created_by = :createById AND s.contractTerm BETWEEN :startDate AND :endDate")
    List<Shops> findByCreatedByIdAndBetweenContractTerm(@Param("createById") String createdById,
                                                        @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate);

    @Query("SELECT s FROM Shops s WHERE s.contractTerm BETWEEN :startDate AND :endDate")
    List<Shops> findBetweenContractTerm(@Param("startDate") Date startDate,
                                        @Param("endDate") Date endDate);

}
