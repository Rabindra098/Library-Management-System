package com.rb.service;

import com.rb.model.Reservation;
import com.rb.paylode.dto.ReservationDTO;
import com.rb.paylode.request.ReservationRequest;
import com.rb.paylode.request.ReservationSearchRequest;
import com.rb.paylode.response.PageResponse;

public interface ReservationService {
    ReservationDTO createReservation(ReservationRequest reservationRequest) throws Exception;
    ReservationDTO createReservationForUser(ReservationRequest reservationRequest,Long usetId) throws Exception;
    ReservationDTO cancelReservation(Long reservationId) throws Exception;
    ReservationDTO fulfillReservation(Long reservationId) throws Exception;

    PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest searchRequest) throws Exception;
    PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest searchRequest);
}
