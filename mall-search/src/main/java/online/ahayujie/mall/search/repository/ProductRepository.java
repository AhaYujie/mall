package online.ahayujie.mall.search.repository;

import online.ahayujie.mall.search.bean.model.EsProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author aha
 * @since 2020/10/26
 */
public interface ProductRepository extends ElasticsearchRepository<EsProduct, Long> {
}
