package com.example.iHome.model.mapper;

import com.example.iHome.model.dto.BookingDTO;
import com.example.iHome.model.entity.Booking;

import java.util.List;

public interface BookingMapper {

    BookingDTO toDTO(Booking booking);

    List<BookingDTO> toListDTO(List<Booking> bookings);

    Booking toEntity(BookingDTO bookingDTO);

}
