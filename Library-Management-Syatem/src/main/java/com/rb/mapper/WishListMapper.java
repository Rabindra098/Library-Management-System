package com.rb.mapper;

import com.rb.model.WishList;
import com.rb.paylode.dto.WishListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishListMapper {

    private final BookMapper bookMapper;

    public WishListDTO toDto(WishList wishList){
          if(wishList == null){
              return null;
          }
          WishListDTO dto = new WishListDTO();
          dto.setId(wishList.getId());
          if(wishList.getUser() != null) {
              dto.setUserId(wishList.getUser().getId());
              dto.setUserFullName(wishList.getUser().getFullName());
            }
          if(wishList.getBook() != null) {
              dto.setBook(bookMapper.toDTO(wishList.getBook()));
          }
          dto.setAddedAt(wishList.getAddedAt());
          dto.setNotes(wishList.getNotes());

        return dto;
      }
}
