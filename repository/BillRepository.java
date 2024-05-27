package com.example.iHome.repository;

import com.example.iHome.model.entity.Bill;
import com.example.iHome.model.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByHouse(House house);

    @Query(value = "SELECT b.* FROM bill AS b LEFT JOIN house AS h ON b.house_id = h.id WHERE h.owner_id = :ownerId", nativeQuery = true)
    List<Bill> findByOwner(long ownerId);

    @Query(value = "SELECT b.* FROM bill AS b LEFT JOIN house AS h ON b.house_id = h.id WHERE h.owner_id = :ownerId AND create_date BETWEEN :startDate AND :endDate order by create_date DESC", nativeQuery = true)
    List<Bill> findByReport(long ownerId, String startDate, String endDate);

    @Query(value = "SELECT b.* FROM bill AS b WHERE create_date BETWEEN :startDate AND :endDate order by create_date DESC", nativeQuery = true)
    List<Bill> findReportAndRoleIsAdmin(String startDate, String endDate);

    @Query(value = "SELECT b.* FROM bill AS b LEFT JOIN house AS h ON b.house_id = h.id WHERE b.type LIKE %:type% AND h.owner_id = :ownerId AND create_date BETWEEN :startDate AND :endDate order by create_date DESC", nativeQuery = true)
    List<Bill> findReportAndType(String type, long ownerId, String startDate, String endDate);

    @Query(value = "SELECT b.* FROM bill AS b WHERE b.type LIKE %:type% AND create_date BETWEEN :startDate AND :endDate order by create_date DESC", nativeQuery = true)
    List<Bill> findReportAndTypeAndRoleIsAdmin(String type, String startDate, String endDate);

}
