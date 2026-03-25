package com.rb.controller;

import com.rb.model.WishList;
import com.rb.paylode.dto.WishListDTO;
import com.rb.paylode.response.APIResponse;
import com.rb.paylode.response.PageResponse;
import com.rb.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {
    private final WishListService wishListService;
    @PostMapping("/add/{bookId}")
    public ResponseEntity<?> addToWishList(
            @PathVariable Long bookId,
            @RequestParam(required = false) String notes) throws Exception {
        WishListDTO wishListDTO=wishListService.addToWishList(bookId,notes);
        return ResponseEntity.ok(wishListDTO);
    }

    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<APIResponse> removeFromWishList(
            @PathVariable Long bookId) throws Exception {
        wishListService.removeFromWishList(bookId);
        return ResponseEntity.ok(new APIResponse("Book removed from wishlist Succefully",true));
    }

    @GetMapping("/my-wishlist")
    public ResponseEntity<PageResponse<WishListDTO>> getMyWishlist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {

        PageResponse<WishListDTO> wishList =
                wishListService.getMyWishList(page, size);
        return ResponseEntity.ok(wishList);
    }

}
