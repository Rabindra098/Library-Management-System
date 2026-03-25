package com.rb.service;

import com.rb.paylode.dto.WishListDTO;
import com.rb.paylode.response.PageResponse;

public interface WishListService {
    WishListDTO addToWishList(Long bookId,String notes) throws Exception;
    void removeFromWishList(Long bookId) throws Exception;
    PageResponse<WishListDTO> getMyWishList(int page,int size) throws Exception;
}
