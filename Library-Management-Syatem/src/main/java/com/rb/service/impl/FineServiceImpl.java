package com.rb.service.impl;

import com.rb.domain.FineStatus;
import com.rb.domain.FineType;
import com.rb.domain.PaymentGateway;
import com.rb.domain.PaymentType;
import com.rb.mapper.FineMapper;
import com.rb.model.BookLoan;
import com.rb.model.Fine;
import com.rb.model.User;
import com.rb.paylode.dto.FineDTO;
import com.rb.paylode.request.CreateFineRequest;
import com.rb.paylode.request.PaymentInitiateRequest;
import com.rb.paylode.request.WaiveFineRequest;
import com.rb.paylode.response.PageResponse;
import com.rb.paylode.response.PaymentInitiateResponse;
import com.rb.repository.BookLoanRepository;
import com.rb.repository.FineRepository;
import com.rb.service.FineService;
import com.rb.service.PaymentService;
import com.rb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FineServiceImpl implements FineService {

    private final BookLoanRepository bookLoanRepository;
    private final FineRepository fineRepository;
    private final FineMapper fineMapper;
    private final UserService userService;
    private final PaymentService paymentService;

    @Override
    public FineDTO creatFine(CreateFineRequest CreateFineRequest) {
//        1. validate Book Loan is exist or not
        BookLoan bookLoan = bookLoanRepository.findById(CreateFineRequest.getBookLoanId()).orElseThrow(() -> new RuntimeException("Book loan does not exist"));
//        2. create fine
        Fine fine = Fine.builder().bookLoan(bookLoan).user(bookLoan.getUser()).type(CreateFineRequest.getType()).amount(CreateFineRequest.getAmount()).status(FineStatus.PENDING).reason(CreateFineRequest.getReason()).notes(CreateFineRequest.getNotes()).build();
        Fine savedFine = fineRepository.save(fine);
        return fineMapper.toDTO(savedFine);
    }

    @Override
    public PaymentInitiateResponse payFine(Long fineId, String transactionId) throws Exception {
//        1. validate fine exist
        Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new RuntimeException("Fine does not exist"));
//        2. check if fine is paid
        if (fine.getStatus() == FineStatus.PAID) {
            throw new RuntimeException("Fine already paid");
        }
        if (fine.getStatus() == FineStatus.WAIVED) {
            throw new RuntimeException("Fine already waived");
        }
//        3. initiate payment
        User user = userService.getCurrentUser();
        PaymentInitiateRequest request = PaymentInitiateRequest.builder().userId(user.getId()).fineId(fine.getId()).paymentType(PaymentType.FINE).gateway(PaymentGateway.RAZORPAY).amount(fine.getAmount()).description("Library fine Payment").build();
        return paymentService.initiatePayemnt(request);
    }

    @Override
    public void markFineAsPaid(Long fineId, Long amount, String transactionId) {
        Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new RuntimeException("Fine does not exist with id " + fineId));
        fine.applyPayment(amount);
        fine.setNotes(transactionId);
        fine.setStatus(FineStatus.PAID);
        fine.setUpdatedAt(LocalDateTime.now());
        fineRepository.save(fine);
    }


    @Override
    public FineDTO waiveFine(WaiveFineRequest waiveFineRequest) throws Exception {

        Fine fine = fineRepository.findById(waiveFineRequest.getFineId()).orElseThrow(() -> new Exception("Fine not found with id"));

        // 2. Check if already waived or paid
        if (fine.getStatus() == FineStatus.WAIVED) {
            throw new Exception("Fine has already been waived");
        }

        if (fine.getStatus() == FineStatus.PAID) {
            throw new Exception("Fine has already been paid and cannot be waived");
        }

        // 3. Waive the fine
        User currentAdmin = userService.getCurrentUser();
        fine.waive(currentAdmin, waiveFineRequest.getReason());

        // 4. Save and return
        Fine savedFine = fineRepository.save(fine);
        return fineMapper.toDTO(savedFine);
    }

    public List<FineDTO> getMyFines(FineStatus status, FineType type) throws Exception {
        User currentUser = userService.getCurrentUser();
        List<Fine> fines;

        // Apply filters based on parameters
        if (status != null && type != null) {
            // Both filters
            fines = fineRepository.findByUserId(currentUser.getId()).stream().filter(f -> f.getStatus() == status && f.getType() == type).collect(Collectors.toList());

        } else if (status != null) {
            // Status filter only
            fines = fineRepository.findByUserId(currentUser.getId()).stream().filter(f -> f.getStatus() == status).collect(Collectors.toList());

        } else if (type != null) {
            // Type filter only
            fines = fineRepository.findByUserIdAndType(currentUser.getId(), type);

        } else {
            // No filter - all fines for user
            fines = fineRepository.findByUserId(currentUser.getId());
        }
        return fines.stream().map(fineMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public PageResponse<FineDTO> getAllFines(FineStatus status, FineType type, Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Fine> finePage = fineRepository.findAllWithFilters(userId, status, type, pageable);

        return convertToPageResponse(finePage);
    }

    private PageResponse<FineDTO> convertToPageResponse(Page<Fine> finePage) {

        List<FineDTO> fineDTOS = finePage.getContent()
                .stream()
                .map(fineMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                fineDTOS,
                finePage.getNumber(),
                finePage.getSize(),
                finePage.getTotalElements(),
                finePage.getTotalPages(),
                finePage.isLast(),
                finePage.isFirst(),
                finePage.isEmpty()
        );
    }


}
