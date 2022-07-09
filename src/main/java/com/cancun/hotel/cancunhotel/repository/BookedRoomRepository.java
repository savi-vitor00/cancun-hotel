package com.cancun.hotel.cancunhotel.repository;

import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface BookedRoomRepository extends JpaRepository<BookedRoom, Long> {

    List<BookedRoom> findAllByCustomer(Customer customer);

    @Query(value = "SELECT COUNT(b) FROM BookedRoom as b " +
            "WHERE (:startDate BETWEEN b.startDate and b.endDate) " +
            "OR (:endDate BETWEEN b.startDate and b.endDate)")
    Integer checkUnavailabilityByStartAndEndDates(@Param("startDate")LocalDate startDate,
                                                            @Param("endDate")LocalDate endDate);

    @Query(value = "SELECT b FROM BookedRoom as b " +
            "WHERE (:startDate BETWEEN b.startDate and b.endDate) " +
            "OR (:endDate BETWEEN b.startDate and b.endDate)")
    List<BookedRoom> listUnavailabilityByStartAndEndDates(@Param("startDate")LocalDate startDate,
                                                                @Param("endDate")LocalDate endDate);
}
