package com.luxoft.alpha.intersango.controllers;

import com.luxoft.alpha.intersango.domain.*;
import com.luxoft.alpha.intersango.repository.DealRepository;
import com.luxoft.alpha.intersango.repository.OrderRepository;
import com.luxoft.alpha.intersango.services.MailService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
public class OrderController {
    @Autowired
    private DealRepository dealRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ControllerUtils utils;
    @Autowired
    private MailService mailService;

    @RequestMapping(value = "/")
    public ModelAndView exchange(@RequestParam(required = false) String currency) {
        ModelAndView model = new ModelAndView("orders");
        utils.setUserData(model);

        try {
            Currency c = Currency.valueOf(currency);
            model.addObject("orders", orderRepository.findOrdersByCurrencyAndState(c, OrderState.OPEN));
        } catch (IllegalArgumentException | NullPointerException e) {
            model.addObject("orders", orderRepository.findOrdersByState(OrderState.OPEN));
        }

        return model;
    }

    @RequestMapping(value = "/order/add/", method = RequestMethod.POST)
    public String addOrder(@RequestParam OrderType type,
                           @RequestParam Currency currency,
                           @RequestParam Integer amount,
                           @RequestParam Double price) {
        Order order = new Order();
        order.setType(type);
        order.setCurrency(currency);
        order.setAmount(amount);
        order.setPrice(price);
        order.setUser(utils.getCurrentUser());
        orderRepository.save(order);

        return "redirect:/";
    }

    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
    public ModelAndView orderDetails(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("details");

        Order order = orderRepository.findOne(id);

        if (order == null)
            return new ModelAndView("redirect:/");

        model.addObject("order", order);

        return model;
    }

    @RequestMapping("/order/{id}/cancel/")
    public ModelAndView cancel(@PathVariable Long id) {
        Order order = orderRepository.findOneWithDeals(id);

        if (order.getState() != OrderState.OPEN) {
            ModelAndView model = new ModelAndView("error");
            model.addObject("message", "Order is in inappropriate state");
            return model;
        }

        if (order.getUser().equals(utils.getCurrentUser())) {
            return utils.generateError("You have no permissions to modify this order");
        }

        for (Deal deal : order.getDeals()) {
            if (deal.getState() == DealState.REQUESTED) {
                deal.setState(DealState.CANCELLED);
                dealRepository.save(deal);

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("order", order);

                mailService.sendMail(
                        deal.getAcceptor().getEmail(),
                        "Order is cancelled",
                        "/velocity/order-cancel-acceptor.vm",
                        map);
            }
        }

        order.setState(OrderState.CANCELLED);
        orderRepository.saveAndFlush(order);

        return new ModelAndView("redirect:/");
    }
}
