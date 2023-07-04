package com.shop.repository;

import com.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * 로그인한 회원의 Cart 엔티티를 찾기 위해서 CartRepository에 쿼리 메서드를 추가합니다.
     */
    Cart findByMemberId(Long memberId);

}
