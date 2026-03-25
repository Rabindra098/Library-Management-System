package com.rb.service;

import com.rb.domain.FineStatus;
import com.rb.domain.FineType;
import com.rb.paylode.dto.FineDTO;
import com.rb.paylode.request.CreateFineRequest;
import com.rb.paylode.request.WaiveFineRequest;
import com.rb.paylode.response.PageResponse;
import com.rb.paylode.response.PaymentInitiateResponse;

import java.util.List;

public interface FineService {
    FineDTO creatFine(CreateFineRequest CreateFineRequest);
    PaymentInitiateResponse payFine(Long fineId, String transactionId) throws Exception;
    void markFineAsPaid(Long fineId,Long amount,String transactionId);
    FineDTO waiveFine(WaiveFineRequest waiveFineRequest ) throws Exception;
    List<FineDTO> getMyFines(FineStatus status, FineType type) throws Exception;
    PageResponse<FineDTO> getAllFines(FineStatus status, FineType type,Long userId,int page,int size);
}
