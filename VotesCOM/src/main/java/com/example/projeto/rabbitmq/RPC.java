package com.example.projeto.rabbitmq;

import com.example.projeto.domain.models.ProductDTO;
import com.example.projeto.domain.models.ReviewDTO;
import com.example.projeto.domain.models.VoteDTO;
import com.example.projeto.domain.services.VoteService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class RPC {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private VoteService voteService;

    public void helper() throws IOException {

        List<ProductDTO> productDTOList = (List<ProductDTO>) amqpTemplate.convertSendAndReceive("rpc_prod", "key", "");
        for (ProductDTO p : productDTOList){
            voteService.create(p);
        }

        List<ReviewDTO> reviewDTOList = (List<ReviewDTO>) amqpTemplate.convertSendAndReceive("rpc_rev", "key", "");
        for (ReviewDTO r : reviewDTOList){
            voteService.create(r);
        }

        List<VoteDTO> voteDTOList = (List<VoteDTO>) amqpTemplate.convertSendAndReceive("rpc_vote", "key", "");
        for (VoteDTO v : voteDTOList){
            voteService.create(v);
        }
    }
}
