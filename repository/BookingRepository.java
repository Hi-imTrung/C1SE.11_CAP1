package com.example.iHome.repository;

import com.example.iHome.model.entity.Booking;
import com.example.iHome.model.entity.Customer;
import com.example.iHome.model.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByCustomer(Customer customer, Pageable pageable);

    @Query(value = "SELECT b.* FROM booking AS b JOIN house AS h ON b.house_id = h.id WHERE h.owner_id = :ownerId", nativeQuery = true)
    List<Booking> findByOwner(long ownerId);

    List<Booking> findByHouse(House house);

    List<Booking> findByHouseAndProgress(House house, String progress);

    List<Booking> findTop10ByProgressOrderByCreatedOnDesc(String progress);

    @Query(value = "SELECT b.* FROM booking AS b LEFT JOIN house AS h ON b.house_id = h.id WHERE  b.progress = :progress AND h.owner_id = :ownerId", nativeQuery = true)
    List<Booking> findByProgressAndOwner(String progress, long ownerId);

    @Query(value = "SELECT b.* FROM booking AS b LEFT JOIN house AS h ON b.house_id = h.id WHERE h.owner_id = :ownerId AND date BETWEEN :startDate AND :endDate order by date DESC", nativeQuery = true)
    List<Booking> findReport(String startDate, String endDate, long ownerId);

    @Query(value = "SELECT b.* FROM booking AS b WHERE date BETWEEN :startDate AND :endDate order by date DESC", nativeQuery = true)
    List<Booking> findReportAndRoleIsAdmin(String startDate, String endDate);
}
