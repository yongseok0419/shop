package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    /**
     *  하나의 상품은 여러 주문 상품으로 들어갈 수 있으므로 주문 상품
     *  기준으로 다대일 단방향 매핑을 설정합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    /**
     *  한 번의 주문에 여러 개의 상품을 주문할 수 있으므로 주문 상품 엔티티와
     *  주문 엔티티를 다대일
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격

    private int count;  //수량

    /**
     *  BaseEntity를 상속받기때문에 중복되는 소스코드 주석처리
     */
//    private LocalDateTime regTime;

//    private LocalDateTime updateTime;

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice*count;
    }

    /**
     *  주문 취소 시 주문 수량만큼 상품의 재고를 더해줍니다.
     */
    public void cancel(){
        this.getItem().addStock(count);
    }

}
