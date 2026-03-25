package com.rb.service.impl;

import com.rb.mapper.WishListMapper;
import com.rb.model.Book;
import com.rb.model.User;
import com.rb.model.WishList;
import com.rb.paylode.dto.WishListDTO;
import com.rb.paylode.response.PageResponse;
import com.rb.repository.BookRepository;
import com.rb.repository.WishListRepository;
import com.rb.service.UserService;
import com.rb.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WishListServiceImpl implements WishListService {


    private final UserService userService;
    private final BookRepository bookRepository;
    private final WishListRepository wishListRepository;
    private final WishListMapper wishListMapper;

    @Override
    public WishListDTO addToWishList(Long bookId, String notes) throws Exception {
        User user=userService.getCurrentUser();

//        1. validate book exist
        Book book=bookRepository.findById(bookId).orElseThrow(()->new Exception("Book not found"));

//        2. check if book is already in wishlist
        if(wishListRepository.existsByUserIdAndBookId(user.getId(),book.getId())){
            throw new Exception("Book is already in your wishlist");
        }
//        3. create wishlist
        WishList wishList=new WishList();
        wishList.setUser(user);
        wishList.setBook(book);
        wishList.setNotes(notes);
        WishList saved =wishListRepository.save(wishList);
        return wishListMapper.toDto(saved);
    }

    @Override
    public void removeFromWishList(Long bookId) throws Exception {

        User user=userService.getCurrentUser();

        WishList wishList=wishListRepository.findByUserIdAndBookId(user.getId(),bookId);
        if(wishList==null){
            throw new Exception("book is not in your wishlist");
        }
        wishListRepository.delete(wishList);
    }

    @Override
    public PageResponse<WishListDTO> getMyWishList(int page, int size) throws Exception {
        Long userId=userService.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("addedAt").descending());
        Page<WishList> wishListPage = wishListRepository.findByUserId(userId,pageable);
        return convertToPageResponse(wishListPage);
    }

    private PageResponse<WishListDTO> convertToPageResponse(Page<WishList> wishListPage) {
        List<WishListDTO> wishListDTOS=wishListPage.getContent()
                .stream()
                .map(wishListMapper::toDto)
                .collect(Collectors.toList());
        return new PageResponse<>(
                wishListDTOS,
                wishListPage.getNumber(),
                wishListPage.getSize(),
                wishListPage.getTotalElements(),
                wishListPage.getTotalPages(),
                wishListPage.isLast(),
                wishListPage.isFirst(),
                wishListPage.isEmpty()

        );
    }
}
