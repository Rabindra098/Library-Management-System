package com.rb.controller;

import com.rb.domain.FineStatus;
import com.rb.domain.FineType;
import com.rb.paylode.dto.FineDTO;
import com.rb.paylode.request.CreateFineRequest;
import com.rb.paylode.request.WaiveFineRequest;
import com.rb.paylode.response.PageResponse;
import com.rb.paylode.response.PaymentInitiateResponse;
import com.rb.service.FineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fines")
public class FineController {
    private final FineService fineService;
    @PostMapping
    public ResponseEntity<?> createFine(@RequestBody CreateFineRequest fineRequest) throws Exception{
        FineDTO fineDTO =fineService.creatFine(fineRequest);
        return ResponseEntity.ok(fineDTO);
    }
    @PostMapping("/{id}/pay")
    public ResponseEntity<?> payFine(@PathVariable Long id, @RequestParam(required = false) String transactionId) throws Exception{
        PaymentInitiateResponse res =fineService.payFine(id,transactionId);
        return ResponseEntity.ok(res);
    }
    @PostMapping("/waive")
    public ResponseEntity<?> waiveFine(@Valid @RequestBody WaiveFineRequest waiveFineRequest) throws Exception{
        FineDTO fineDTO =fineService.waiveFine(waiveFineRequest);
        return ResponseEntity.ok(fineDTO);
    }
    @GetMapping("/my")
    public ResponseEntity<?> getMyFines(
            @RequestParam(required = false) FineStatus status,
            @RequestParam(required = false) FineType type) throws Exception{
       List<FineDTO>  listFineDTO =fineService.getMyFines(status,type);
        return ResponseEntity.ok(listFineDTO);
    }
    @GetMapping
    public ResponseEntity<?> getAllFines(
            @RequestParam(required = false) FineStatus status,
            @RequestParam(required = false) FineType type,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        PageResponse<FineDTO> fines = fineService
                .getAllFines(status, type, userId, page, size);

        return ResponseEntity.ok(fines);
    }

}
