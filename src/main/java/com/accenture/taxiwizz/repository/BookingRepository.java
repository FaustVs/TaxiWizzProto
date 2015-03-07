package com.accenture.taxiwizz.repository;

import com.accenture.taxiwizz.domain.Booking;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Booking entity.
 */
public interface BookingRepository extends JpaRepository<Booking,Long> {

}
