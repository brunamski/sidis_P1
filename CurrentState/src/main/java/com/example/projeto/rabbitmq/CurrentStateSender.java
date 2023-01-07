package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.*;
import com.example.projeto.domain.repositories.ReviewRepository;
import com.example.projeto.domain.repositories.VoteRepository;
import com.example.projeto.domain.services.CurrentStateService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentStateSender {

    @Autowired
    private CurrentStateService service;

    @RabbitListener(queues = "rpc_prod_receiver")
    public List<ProductDTO> sendProd() throws IOException {
        return service.getProductList();
    }

    @RabbitListener(queues = "rpc_rev_receiver")
    public List<ReviewDTO> sendRev(){
        return service.getReviewList();
    }

    @RabbitListener(queues = "rpc_vote_receiver")
    public List<VoteDTO> sendVote(){
        return service.getVoteList();
    }
}
