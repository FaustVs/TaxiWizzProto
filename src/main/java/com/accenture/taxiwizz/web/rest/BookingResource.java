package com.accenture.taxiwizz.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.accenture.taxiwizz.domain.Booking;
import com.accenture.taxiwizz.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Booking.
 */
@RestController
@RequestMapping("/api")
public class BookingResource {

    private final Logger log = LoggerFactory.getLogger(BookingResource.class);

    @Inject
    private BookingRepository bookingRepository;

    /**
     * POST  /bookings -> Create a new booking.
     */
    @RequestMapping(value = "/bookings",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", booking);
        if (booking.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new booking cannot already have an ID").build();
        }
        bookingRepository.save(booking);
        return ResponseEntity.created(new URI("/api/bookings/" + booking.getId())).build();
    }

    /**
     * PUT  /bookings -> Updates an existing booking.
     */
    @RequestMapping(value = "/bookings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", booking);
        if (booking.getId() == null) {
            return create(booking);
        }
        bookingRepository.save(booking);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /bookings -> get all the bookings.
     */
    @RequestMapping(value = "/bookings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Booking> getAll() {
        log.debug("REST request to get all Bookings");
        return bookingRepository.findAll();
    }

    /**
     * GET  /bookings/:id -> get the "id" booking.
     */
    @RequestMapping(value = "/bookings/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Booking> get(@PathVariable Long id) {
        log.debug("REST request to get Booking : {}", id);
        return Optional.ofNullable(bookingRepository.findOne(id))
            .map(booking -> new ResponseEntity<>(
                booking,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bookings/:id -> delete the "id" booking.
     */
    @RequestMapping(value = "/bookings/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Booking : {}", id);
        bookingRepository.delete(id);
    }
}
