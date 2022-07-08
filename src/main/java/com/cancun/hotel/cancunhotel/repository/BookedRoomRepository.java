package com.cancun.hotel.cancunhotel.repository;

import com.cancun.hotel.cancunhotel.domain.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedRoomRepository extends JpaRepository<BookedRoom, Long> {
}
