package com.workcheng.weiya.service;

import com.workcheng.weiya.common.dto.BarrAgerModel;
import com.workcheng.weiya.common.domain.WsMessage;
import com.workcheng.weiya.common.exception.ServerInternalException;
import com.workcheng.weiya.common.utils.RandomUtil;
import com.workcheng.weiya.repository.WsMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andy
 * @date 2017/1/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final WsMessageRepository wsMessageRepository;
    private List<String> colors = new ArrayList() {
        {
            add("#CC3333");
            add("#996699");
            add("#FF6600");
            add("#0099CC");
            add("#3366CC");
            add("#000000");
            add("#FF6600");
            add("#339933");
            add("#9999FF");
            add("#660066");
            add("#FF33CC");
            add("#CCFF66");
        }
    };

    public WsMessage save(WsMessage zoeMessage) {
        return wsMessageRepository.save(zoeMessage);
    }

    public WsMessage get() {
        final Iterable<WsMessage> all = wsMessageRepository.findAll();
        if (all.iterator().hasNext()) {
            return all.iterator().next();
        }
        return null;
    }

    public BarrAgerModel getBarrAgerModel() throws ServerInternalException {
        BarrAgerModel barrAgerModel = new BarrAgerModel();
        WsMessage zoeMessage = get();
        if (null == zoeMessage) {
            throw new ServerInternalException("消息池为空");
        }
        barrAgerModel.setImg(zoeMessage.getHeadImgUrl());
        barrAgerModel.setMax(15);
        barrAgerModel.setSpeed(RandomUtil.getRandomInt(10));
        barrAgerModel.setInfo(zoeMessage.getContent());
        barrAgerModel.setColor(colors.get(RandomUtil.getRandomInt(11)));
        return barrAgerModel;
    }

    public Long count() {
        return wsMessageRepository.count();
    }

    public Iterable<WsMessage> getAll() {
        return wsMessageRepository.findAll();
    }
}
