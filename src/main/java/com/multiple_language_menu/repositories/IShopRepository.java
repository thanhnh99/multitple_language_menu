package com.multiple_language_menu.repositories;

import com.multiple_language_menu.models.entities.Shops;
import com.multiple_language_menu.models.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IShopRepository  extends JpaRepository<Shops, String> {
//    @Query("SELECT s FROM Shops s WHERE s.owner.id = :ownerId")
    Page<Shops> findByOwner(Users owner, Pageable pageable);
    Shops findByOwner(Users owner);

//    @Query("SELECT s FROM Shops s WHERE s.owner.id = :ownerId AND s.contractTerm BETWEEN :startDate AND :endDate ORDER BY s.contractTerm DESC ")
    Page<Shops> findByOwnerAndContractTermBetween(Users owner,
                                                  Date startDate,
                                                  Date endDate,
                                                  Pageable pageable);

//    @Query("SELECT s FROM Shops s WHERE s.owner.id = :ownerId AND s.contractTerm < :endDate")
    Page<Shops> findByOwnerAndContractTermBefore(Users owner,
                                                 Date endDate,
                                                 Pageable pageable);
//    @Query("SELECT s FROM Shops s WHERE s.owner.id = :ownerId AND s.contractTerm > :startDate")
    Page<Shops> findByOwnerAndContractTermAfter(Users owner,
                                                Date startDate,
                                                Pageable pageable);

//    @Query("SELECT s FROM Shops s WHERE s.created_by = :createById")
    Page<Shops> findByCreatedBy(String createdById,
                                 Pageable pageable);

    List<Shops> findByCreatedBy(String createdById);

//    @Query("SELECT s FROM Shops s WHERE s.created_by = :createById AND s.contractTerm BETWEEN :startDate AND :endDate")
    Page<Shops> findByCreatedByAndContractTermBetween(String createdById,
                                                       Date startDate,
                                                       Date endDate,
                                                       Pageable pageable);

//    @Query("SELECT s FROM Shops s WHERE s.created_by = :createById AND s.contractTerm > :startDate limit 15, 10")
    Page<Shops> findByCreatedByAndContractTermAfter(String createdById,
                                                     Date startDate,
                                                     Pageable pageable);

//    @Query("SELECT s FROM Shops s WHERE s.created_by = :createById AND s.contractTerm < :endDate")
    Page<Shops> findByCreatedByAndContractTermBefore(String createdById,
                                                      Date endDate,
                                                      Pageable pageable);

//    @Query("SELECT s FROM Shops s WHERE s.contractTerm BETWEEN :startDate AND :endDate")
    Page<Shops> findByContractTermBetween(Date startDate,
                                        Date endDate,
                                        Pageable pageable);


//    @Query("SELECT s FROM Shops s WHERE s.contractTerm > :startDate")
    Page<Shops> findByContractTermAfter(Date startDate,
                                        Pageable pageable);

//    @Query("SELECT s FROM Shops s WHERE s.contractTerm > :endDate")
    Page<Shops> findByContractTermBefore(Date endDate,
                                         Pageable pageable);

    Page<Shops> findAll(Pageable pageable);


}
