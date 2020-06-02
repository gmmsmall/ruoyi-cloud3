package com.ruoyi.acad.documnet;

import com.ruoyi.acad.client.ClientAcad;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cwenao
 * @version $Id ElasticAccountInfoRepository.java, v 0.1 2017-02-06 10:26 cwenao Exp $$
 */
@Repository
public interface ElasticClientAcadRepository extends ElasticsearchRepository<ClientAcad,String> {

}