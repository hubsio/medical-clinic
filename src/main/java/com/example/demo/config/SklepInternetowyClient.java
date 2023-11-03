package com.example.demo.config;

import com.example.demo.model.dto.OrderDTO;
import com.example.demo.model.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "sklep-internetowy-api", url = "http://localhost:8081/api")
public interface SklepInternetowyClient {

    @RequestMapping(method = RequestMethod.GET, value = "/products")
    List<ProductDTO> getProducts();

    @RequestMapping(method = RequestMethod.GET, value = "/products/{productId}", produces = "application/json")
    ProductDTO getProductById(@PathVariable("productId") Long productId);

    @RequestMapping(method = RequestMethod.POST, value = "/products", consumes = "application/json")
    void createProduct(@RequestBody ProductDTO product);

    @RequestMapping(method = RequestMethod.PUT, value = "/products/{productId}", consumes = "application/json")
    void updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductDTO product);

    @RequestMapping(method = RequestMethod.DELETE, value = "/products/{productId}")
    void deleteProduct(@PathVariable("productId") Long productId);

    @RequestMapping(method = RequestMethod.GET, value = "/orders")
    List<OrderDTO> getOrders();

    @RequestMapping(method = RequestMethod.GET, value = "/orders/{orderId}", produces = "application/json")
    OrderDTO getOrderById(@PathVariable("orderId") Long orderId);

    @RequestMapping(method = RequestMethod.POST, value = "/orders", consumes = "application/json")
    void createOrder(@RequestBody OrderDTO order);

    @RequestMapping(method = RequestMethod.PUT, value = "/orders/{orderId}", consumes = "application/json")
    void updateOrder(@PathVariable("orderId") Long orderId, @RequestBody OrderDTO order);

    @RequestMapping(method = RequestMethod.DELETE, value = "/orders/{orderId}")
    void cancelOrder(@PathVariable("orderId") Long orderId);
}
