package com.info.mongo.repository;

import com.info.mongo.document.ShuJuMoHeMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Phi on 2018/1/24.
 */
@Component
public interface IShuJuMoHeMessageRepository extends MongoRepository<ShuJuMoHeMessage, String>{
    List<ShuJuMoHeMessage> findByIdentityCardAndUserPhone(String identityCard, String userPhone);
}
