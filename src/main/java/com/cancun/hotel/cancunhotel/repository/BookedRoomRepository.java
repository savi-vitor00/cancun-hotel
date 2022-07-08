package com.cancun.hotel.cancunhotel.repository;

import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import com.cancun.hotel.cancunhotel.domain.Costumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookedRoomRepository extends JpaRepository<BookedRoom, Long> {

    List<BookedRoom> findAllByCostumer(Costumer costumer);
}
