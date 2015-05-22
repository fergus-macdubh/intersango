package com.luxoft.alpha.intersango.controllers;

import com.luxoft.alpha.intersango.domain.*;
import com.luxoft.alpha.intersango.repository.DealRepository;
import com.luxoft.alpha.intersango.repository.OrderRepository;
import com.luxoft.alpha.intersango.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Objects;

@Controller
public class DealController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DealRepository dealRepository;
    @Autowired
    private ControllerUtils utils;
    @Autowired
    private MailService mailService;

    @RequestMapping("/deal/")
    public ModelAndView deals() {
        User user = utils.getCurrentUser();

        ModelAndView model = new ModelAndView("deals");
        model.addObject("myDeals", dealRepository.findDealsByAcceptor(user));
        model.addObject("requestedDeals", dealRepository.findDealsByUserOrders(user));
        utils.setUserData(model);

        return model;
    }

    @RequestMapping("/deal/{id}/confirm/")
    public ModelAndView confirm(@PathVariable Long id) {
        Deal deal = dealRepository.findOneWithOrder(id);

        if (deal.getOrder().getAmount() < deal.getAmount()) {
            return utils.generateError("Ordered amount is less than amount in this deal");
        }

        deal.setState(DealState.CONFIRMED);
        dealRepository.save(deal);

        deal.getOrder().setAmount(deal.getOrder().getAmount() - deal.getAmount());

        Order order = orderRepository.findOneWithDeals(deal.getOrder().getId());

        if (order.getAmount() <= 0) {
            order.setState(OrderState.CLOSED);

            for (Deal d : order.getDeals()) {
                if (d.getState() == DealState.REQUESTED) {
                    d.setState(DealState.CANCELLED);
                    dealRepository.save(d);
                }
            }
        }

        orderRepository.saveAndFlush(order);
        dealRepository.flush();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("deal", deal);

        mailService.sendMail(utils.getCurrentUser().getEmail(),
                "Deal is confirmed",
                "/velocity/deal-confirm-owner.vm",
                map);

        mailService.sendMail(deal.getAcceptor().getEmail(),
                "Deal is confirmed",
                "/velocity/deal-confirm-acceptor.vm",
                map);

        return new ModelAndView("redirect:/deal/");
    }

    @RequestMapping("/deal/{id}/reject/")
    public ModelAndView reject(@PathVariable Long id) {
        Deal deal = dealRepository.findOne(id);

        deal.setState(DealState.REJECTED);
        dealRepository.saveAndFlush(deal);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("deal", deal);

        mailService.sendMail(utils.getCurrentUser().getEmail(),
                "Deal is rejected",
                "/velocity/deal-reject.vm",
                map);

        return new ModelAndView("redirect:/deal/");
    }

    @RequestMapping("/deal/add/")
    public ModelAndView accept(@RequestParam Long orderId, @RequestParam Integer amount) {
        Order order = orderRepository.findOneWithDeals(orderId);

        if (amount > order.getAmount()) {
            return utils.generateError("Order amount is less than requested.");
        }

        if (Objects.equals(order.getUser().getId(), utils.getCurrentUser().getId())) {
            return utils.generateError("You cannot accept your own order.");
        }

        Deal deal = new Deal(amount, utils.getCurrentUser(), order);
        dealRepository.saveAndFlush(deal);

        order.getDeals().add(deal);
        orderRepository.saveAndFlush(order);

        HashMap<String, Object> map = new HashMap<>();
        map.put("deal", deal);

        mailService.sendMail(utils.getCurrentUser().getEmail(),
                "Your order is accepted",
                "/velocity/order-accept.vm",
                map);

        return new ModelAndView("redirect:/deal/");
    }
}
