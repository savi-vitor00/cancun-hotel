package com.cancun.hotel.cancunhotel.repository;

import com.cancun.hotel.cancunhotel.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}
