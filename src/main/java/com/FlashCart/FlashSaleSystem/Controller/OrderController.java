package com.FlashCart.FlashSaleSystem.Controller;

import com.FlashCart.FlashSaleSystem.DTOs.OrderDTO;
import com.FlashCart.FlashSaleSystem.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    //method to purchase the order
    @RequestMapping("/flashSale/{saleId}/user/{userId}")
    public ResponseEntity<OrderDTO> purchaseTheItemFromSale(@PathVariable Long saleId,
                                                            @PathVariable Long userId,
                                                            @RequestParam Integer quantity){
        return new ResponseEntity<>(orderService.purchase(saleId,userId,quantity), HttpStatus.OK);
    }

    //method to pay for the order
    @RequestMapping("/{orderId}/pay")
    public ResponseEntity<String> payForOrder(@PathVariable Long orderId,
                                              @RequestParam Boolean paymentSuccess){
        return new ResponseEntity<>(orderService.pay(orderId,paymentSuccess),HttpStatus.OK);
    }

    //method to get all the orders
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAll(){
        return new ResponseEntity<>(orderService.getAll(),HttpStatus.OK);
    }

}
