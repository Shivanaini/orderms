package com.infosys.infymarket.order.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infosys.infymarket.order.dto.OrderDTO;
import com.infosys.infymarket.order.entity.Order;
import com.infosys.infymarket.order.repository.OrderRepository;
import com.infosys.infymarket.order.repository.ReorderRepository;
import com.infosys.infymarket.user.exception.InfyMarketException;


@Service
@Transactional
public class OrderService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OrderRepository orderrepo;
	@Autowired
	ReorderRepository reorderRepo;
	
	
	//GET BY ORDERID
	public List<OrderDTO> getSpecificOrder(String orderid)  throws InfyMarketException{

		logger.info("Order details of Id {}", orderid);

		Iterable<Order> order = orderrepo.findByOrderid(orderid);
		List<OrderDTO> orderDTO = new ArrayList<OrderDTO>();

		order.forEach(ord -> {
			orderDTO.add(OrderDTO.valueOf(ord));
		});
		if (orderDTO.isEmpty())
			throw new InfyMarketException("Service.ORDERS_NOT_FOUND");
		logger.info("{}", orderDTO);
		return orderDTO;
	}
	//GET ALL ORDERS
	public List<OrderDTO> getAllOrder()  throws InfyMarketException{

		Iterable<Order> orders = orderrepo.findAll();
		List<OrderDTO> orderDTOs = new ArrayList<>();

		orders.forEach(order -> {
			OrderDTO orderDTO = OrderDTO.valueOf(order);
			orderDTOs.add(orderDTO);
		});
		if (orderDTOs.isEmpty())
		throw new InfyMarketException("Service.ORDERS_NOT_FOUND");
		logger.info("Order Details : {}", orderDTOs);
		return orderDTOs;
	}
	public String saveOrder(OrderDTO orderDTO) throws InfyMarketException{

		Order order = orderrepo.getOrderByBuyerIdAndAddress(orderDTO.getBuyerid(), orderDTO.getAddress());
		if (order != null) {
			return order.getOrderid();
		}else {
		throw new InfyMarketException("Services.ORDER_NOT_PLACED");
		}

	}
	public boolean Order(OrderDTO orderDTO) throws InfyMarketException{
		Order ord = reorderRepo.findByOrderid(orderDTO.getOrderid());
		if (ord != null && ord.getOrderid().equals(orderDTO.getOrderid())) {
			return true;
		}else {
			throw new InfyMarketException("Services.ORDER_NOT_PLACED");
		}
		
	}

 public void deleteOrder(String orderid) throws InfyMarketException{
	 if(orderid!= null) {
		Optional<Order> ord = orderrepo.findById(orderid);
		orderrepo.deleteById(orderid);
	}
	 else
	 {
		 throw new InfyMarketException("Service.ORDERS_NOT_FOUND");
	 }
}
}