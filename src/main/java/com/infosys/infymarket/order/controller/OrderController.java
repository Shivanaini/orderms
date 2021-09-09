package com.infosys.infymarket.order.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.infymarket.order.dto.OrderDTO;
import com.infosys.infymarket.order.dto.ProductsorderedDTO;
import com.infosys.infymarket.order.service.OrderService;
import com.infosys.infymarket.order.service.ProductOrderService;
import com.infosys.infymarket.user.exception.InfyMarketException;


@RestController

public class OrderController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
@Autowired
Environment environment;
@Autowired
private OrderService orderservice;

@Autowired
ProductOrderService proser;


//GET BY ORDERID-->GET
@RequestMapping(value = "/api/orders/{orderid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<List<OrderDTO>> getSpecificOrder(@PathVariable String orderid) throws InfyMarketException {
		logger.info("Order details {}", orderid);
		List<OrderDTO> orders = orderservice.getSpecificOrder(orderid);
		return new ResponseEntity<>(orders, HttpStatus.OK);

}  
   //GET ALL ORDERS-->GET
	@GetMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderDTO>> getAllOrder() throws InfyMarketException {
		
			logger.info("Fetching all products");
			List<OrderDTO> orderdto = orderservice.getAllOrder();
			return new ResponseEntity<>(orderdto, HttpStatus.OK);
	}
	//PLACEORDER-->POST
	@RequestMapping(value = "/orders/placeorders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> searchBuyer(@RequestBody OrderDTO orderDTO) throws InfyMarketException{
			String orderid = orderservice.saveOrder(orderDTO);
			String successMessage = environment.getProperty("API.ORDER_SUCCESS") + orderid;
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
	}
	//REORDER-->POST
	@PostMapping(value = "/orders/reorder/{orderid}/{orderid}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login(@RequestBody OrderDTO orderDTO) throws InfyMarketException{
	
			boolean order =  orderservice.Order(orderDTO);
			String successMessage = environment.getProperty("API.ORDER_SUCCESS")+order;
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
	}
	//PRODUCTSORDERED BY ORDERID
	@RequestMapping(value = "/api/productsorders/{prodid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductsorderedDTO>> getProductById(@PathVariable String prodid) throws InfyMarketException{
		
			logger.info("product details request for ordered product {}", prodid);
			List<ProductsorderedDTO> orders =  proser.getProductById(prodid);
			return new ResponseEntity<>(orders, HttpStatus.CREATED);
	}

	
	
	//DELETEORDER-->DELETE
	@DeleteMapping(value = "/order/{orderid}")
	public ResponseEntity<String> deleteOrder(@PathVariable String orderid) throws InfyMarketException {
	
			orderservice.deleteOrder(orderid);
			String successMessage = environment.getProperty("API.DELETE_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		
	}

}
