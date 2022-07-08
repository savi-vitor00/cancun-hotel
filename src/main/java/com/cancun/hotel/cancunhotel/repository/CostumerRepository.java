package com.cancun.hotel.cancunhotel.repository;

import com.cancun.hotel.cancunhotel.domain.Costumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostumerRepository extends JpaRepository<Costumer, Long> {
}
