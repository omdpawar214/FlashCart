package com.FlashCart.FlashSaleSystem.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
