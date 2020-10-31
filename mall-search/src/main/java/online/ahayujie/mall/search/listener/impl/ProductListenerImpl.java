package online.ahayujie.mall.search.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import online.ahayujie.mall.search.bean.dto.ProductCreateMsgDTO;
import online.ahayujie.mall.search.bean.dto.ProductUpdateMsgDTO;
import online.ahayujie.mall.search.config.RabbitmqConfig;
import online.ahayujie.mall.search.listener.ProductListener;
import online.ahayujie.mall.search.service.ProductService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author aha
 * @since 2020/10/31
 */
@Service
public class ProductListenerImpl implements ProductListener {
    private ProductService productService;

    private final ObjectMapper objectMapper;

    public ProductListenerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @RabbitListener(queues = RabbitmqConfig.PRODUCT_CREATE_QUEUE_ES_SAVE)
    public void listenProductCreate(Channel channel, Message message) throws IOException {
        ProductCreateMsgDTO msgDTO = objectMapper.readValue(message.getBody(), ProductCreateMsgDTO.class);
        productService.saveEsProduct(msgDTO.getId());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Override
    @RabbitListener(queues = RabbitmqConfig.PRODUCT_UPDATE_QUEUE_ES_UPDATE)
    public void listenProductUpdate(Channel channel, Message message) throws IOException {
        ProductUpdateMsgDTO msgDTO = objectMapper.readValue(message.getBody(), ProductUpdateMsgDTO.class);
        productService.updateEsProduct(msgDTO.getId());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
