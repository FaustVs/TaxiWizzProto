package com.accenture.taxiwizz.web.rest;

import com.accenture.taxiwizz.Application;
import com.accenture.taxiwizz.domain.Booking;
import com.accenture.taxiwizz.repository.BookingRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookingResource REST controller.
 *
 * @see BookingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BookingResourceTest {

    private static final String DEFAULT_DESTINATION = "SAMPLE_TEXT";
    private static final String UPDATED_DESTINATION = "UPDATED_TEXT";
    private static final String DEFAULT_DEPARTURE = "SAMPLE_TEXT";
    private static final String UPDATED_DEPARTURE = "UPDATED_TEXT";

    @Inject
    private BookingRepository bookingRepository;

    private MockMvc restBookingMockMvc;

    private Booking booking;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookingResource bookingResource = new BookingResource();
        ReflectionTestUtils.setField(bookingResource, "bookingRepository", bookingRepository);
        this.restBookingMockMvc = MockMvcBuilders.standaloneSetup(bookingResource).build();
    }

    @Before
    public void initTest() {
        booking = new Booking();
        booking.setDestination(DEFAULT_DESTINATION);
        booking.setDeparture(DEFAULT_DEPARTURE);
    }

    @Test
    @Transactional
    public void createBooking() throws Exception {
        // Validate the database is empty
        assertThat(bookingRepository.findAll()).hasSize(0);

        // Create the Booking
        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(1);
        Booking testBooking = bookings.iterator().next();
        assertThat(testBooking.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testBooking.getDeparture()).isEqualTo(DEFAULT_DEPARTURE);
    }

    @Test
    @Transactional
    public void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookings
        restBookingMockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(booking.getId().intValue()))
                .andExpect(jsonPath("$.[0].destination").value(DEFAULT_DESTINATION.toString()))
                .andExpect(jsonPath("$.[0].departure").value(DEFAULT_DEPARTURE.toString()));
    }

    @Test
    @Transactional
    public void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(booking.getId().intValue()))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION.toString()))
            .andExpect(jsonPath("$.departure").value(DEFAULT_DEPARTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Update the booking
        booking.setDestination(UPDATED_DESTINATION);
        booking.setDeparture(UPDATED_DEPARTURE);
        restBookingMockMvc.perform(put("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(1);
        Booking testBooking = bookings.iterator().next();
        assertThat(testBooking.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testBooking.getDeparture()).isEqualTo(UPDATED_DEPARTURE);
    }

    @Test
    @Transactional
    public void deleteBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc.perform(delete("/api/bookings/{id}", booking.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(0);
    }
}
