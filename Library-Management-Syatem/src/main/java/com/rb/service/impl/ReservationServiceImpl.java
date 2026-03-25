package com.rb.service.impl;

import com.rb.domain.BookLoanStatus;
import com.rb.domain.ReservationStatus;
import com.rb.domain.UserRole;
import com.rb.mapper.ReservationMapper;
import com.rb.model.Book;
import com.rb.model.Reservation;
import com.rb.model.User;
import com.rb.paylode.dto.ReservationDTO;
import com.rb.paylode.request.CheckoutRequest;
import com.rb.paylode.request.ReservationRequest;
import com.rb.paylode.request.ReservationSearchRequest;
import com.rb.paylode.response.PageResponse;
import com.rb.repository.BookLoanRepository;
import com.rb.repository.BookRepository;
import com.rb.repository.ReservationRepository;
import com.rb.service.BookLoanService;
import com.rb.service.ReservationService;
import com.rb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final BookLoanService  bookLoanService;

    int MAX_RESERVATION=5;

    @Override
    public ReservationDTO createReservation(ReservationRequest reservationRequest) throws Exception {
        User user=userService.getCurrentUser();
        return createReservationForUser(reservationRequest,user.getId());
    }

    @Override
    public ReservationDTO createReservationForUser(ReservationRequest reservationRequest, Long userId) throws Exception {
        boolean alreadyHasLoan=bookLoanRepository.existsByUserIdAndBookIdAndStatus(
                userId,reservationRequest.getBookId(), BookLoanStatus.CHECKED_OUT
        );
        if(alreadyHasLoan){
            throw new Exception("You already have loan on this book");
        }
//        1. validate user exist
        User user=userService.getCurrentUser();
//        2. validate book exist
        Book book=bookRepository.findById(reservationRequest.getBookId())
                .orElseThrow(() -> new Exception("Book not found"));
//        3.
        if(reservationRepository.hasActiveReservation(userId,book.getId()))
            throw new Exception("You have already reservation on this book");
//        4. check if book is already avalable
        if(book.getAvailableCopies()>0){
            throw new Exception("Book is already available");
        }
//        5. check user's active reservation limit
        long activeReservation=reservationRepository
                .countActiveReservationsByUser(userId);
        if(activeReservation>MAX_RESERVATION){
            throw new Exception("You have reserved "+MAX_RESERVATION+" times");
        }
//        6. create reservation
        Reservation reservation=new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setNotificationSend(false);
        reservation.setNotes(reservationRequest.getNotes());

        long pendingCount=reservationRepository.countPendingReservationsByBook(book.getId());

        reservation.setQueuePosition((int)pendingCount+1);

        Reservation savedreservation= reservationRepository.save(reservation);

        return reservationMapper.toDTO(savedreservation);
    }

    @Override
    public ReservationDTO cancelReservation(Long reservationId) throws Exception {
        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()-> new Exception("Reservation is not found with id "+reservationId));
        User currentUser=userService.getCurrentUser();
        if(!reservation.getUser().getId().equals(currentUser.getId())
                && currentUser.getRole() != UserRole.ROLE_ADMIN){
            throw new Exception("You are not allowed to cancel reservation");
        }
        if(!reservation.canBeCancelled()){
            throw new Exception("Reservation can not be cancelled (current user )"+reservation.getUser().getFullName());
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());

        Reservation savedreservation=reservationRepository.save(reservation);
        return reservationMapper.toDTO(savedreservation);
    }

    @Override
    public ReservationDTO fulfillReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new Exception("Reservation not found with ID: " + reservationId)
                );

        if (reservation.getBook().getAvailableCopies() <= 0) {
            throw new Exception(
                    "Reservation is not available for pickup (current status: "
                            + reservation.getStatus() + ")"
            );
        }

        reservation.setStatus(ReservationStatus.FULFILLED);
        reservation.setFulfilledAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        CheckoutRequest request = new CheckoutRequest();
        request.setBookId(reservation.getBook().getId());
        request.setNotes("Assign Booked by Admin");

        // Assuming checkoutService is available
        bookLoanService.checkoutBookForUser(savedReservation.getUser().getId(), request);

        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest searchRequest) throws Exception {
        User user=userService.getCurrentUser();
        searchRequest.setUserId(user.getId());
        return searchReservations(searchRequest);
    }

    @Override
    public PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest searchRequest) {

        Pageable pageable = createPageable(searchRequest);

        Page<Reservation> reservationPage =
                reservationRepository.searchReservationsWithFilters(
                        searchRequest.getUserId(),
                        searchRequest.getBookId(), // FIX: getter, not setter
                        searchRequest.getStatus(),
                        Boolean.TRUE.equals(searchRequest.getActiveOnly()),
                        pageable
                );
        return buildPageResponse(reservationPage);
    }


    private PageResponse<ReservationDTO> buildPageResponse(Page<Reservation> reservationPage) {

        List<ReservationDTO> dtos = reservationPage.getContent()
                .stream()
                .map(reservationMapper::toDTO)
                .toList();

        PageResponse<ReservationDTO> response = new PageResponse<>();
        response.setContent(dtos);
        response.setPageNumber(reservationPage.getNumber());
        response.setPageSize(reservationPage.getSize());
        response.setTotalElements(reservationPage.getTotalElements());
        response.setTotalPages(reservationPage.getTotalPages());
        response.setLast(reservationPage.isLast());
        return response;
    }
    private Pageable createPageable(ReservationSearchRequest searchRequest) {

        Sort sort = "ASC".equalsIgnoreCase(searchRequest.getSortDirection())
                ? Sort.by(searchRequest.getSortBy()).ascending()
                : Sort.by(searchRequest.getSortBy()).descending();

        return PageRequest.of(
                searchRequest.getPage(),
                searchRequest.getSize(),
                sort
        );
    }


}
