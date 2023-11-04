package com.example.demo.config;

import com.example.demo.model.dto.OrderDTO;
import com.example.demo.model.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "sklep-internetowy-api", url = "http://sklep-internetowy.com/api")
public interface SklepInternetowyClient {

    @GetMapping("/products")
    List<ProductDTO> getProducts();

    @GetMapping("/products/{productId}")
    ProductDTO getProductById(@PathVariable("productId") Long productId);

    @PostMapping("/products")
    void createProduct(@RequestBody ProductDTO product);

    @PutMapping("/products/{productId}")
    void updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductDTO product);

    @DeleteMapping("/products/{productId}")
    void deleteProduct(@PathVariable("productId") Long productId);

    @GetMapping("/orders")
    List<OrderDTO> getOrders();

    @GetMapping("/orders/{orderId}")
    OrderDTO getOrderById(@PathVariable("orderId") Long orderId);

    @PostMapping("/orders")
    void createOrder(@RequestBody OrderDTO order);

    @PutMapping("/orders/{orderId}")
    void updateOrder(@PathVariable("orderId") Long orderId, @RequestBody OrderDTO order);

    @DeleteMapping("/orders/{orderId}")
    void cancelOrder(@PathVariable("orderId") Long orderId);
}
