package online.ahayujie.mall.search.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.search.bean.dto.ProductSpecificationDTO;
import online.ahayujie.mall.search.bean.model.EsProduct;
import online.ahayujie.mall.search.bean.model.Product;
import online.ahayujie.mall.search.bean.model.ProductParam;
import online.ahayujie.mall.search.mapper.ProductMapper;
import online.ahayujie.mall.search.mapper.ProductParamMapper;
import online.ahayujie.mall.search.mapper.ProductSpecificationMapper;
import online.ahayujie.mall.search.repository.ProductRepository;
import online.ahayujie.mall.search.service.*;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author aha
 * @since 2020/10/26
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private ProductParamValueStrategyFactory productParamValueStrategyFactory;
    private SpecificationValueStrategyFactory specificationValueStrategyFactory;

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductParamMapper productParamMapper;
    private final ProductSpecificationMapper productSpecificationMapper;

    public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository,
                              ProductParamMapper productParamMapper, ProductSpecificationMapper productSpecificationMapper) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.productParamMapper = productParamMapper;
        this.productSpecificationMapper = productSpecificationMapper;
    }

    @Override
    public void saveEsProduct(Long id) {
        EsProduct esProduct = getEsProduct(id);
        if (esProduct != null) {
            productRepository.save(esProduct);
        }
    }

    @Override
    public void saveEsProducts(List<Long> ids) {
        List<EsProduct> esProducts = ids.stream().map(this::getEsProduct).filter(Objects::nonNull).collect(Collectors.toList());
        productRepository.saveAll(esProducts);
    }

    @Override
    public void updateEsProduct(Long id) {
        saveEsProduct(id);
    }

    @Override
    public void updateEsProducts(List<Long> ids) {
        saveEsProducts(ids);
    }

    @Override
    public void deleteEsProduct(Long id) {
        deleteEsProducts(Collections.singletonList(id));
    }

    @Override
    public void deleteEsProducts(List<Long> ids) {
        ids.forEach(productRepository::deleteById);
    }

    @Override
    public EsProduct getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * 从数据库获取EsProduct信息，如果商品不存在则返回null。
     * @param id 商品id
     * @return EsProduct
     */
    private EsProduct getEsProduct(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return null;
        }
        List<ProductParam> productParams = productParamMapper.selectByProductId(id);
        String paramString = productParamToString(productParams);
        List<ProductSpecificationDTO> specificationDTOS = productSpecificationMapper.selectDTOByProductId(id);
        String specificationString = productSpecificationToString(specificationDTOS);
        EsProduct esProduct = new EsProduct();
        BeanUtils.copyProperties(product, esProduct);
        esProduct.setParams(paramString);
        esProduct.setSpecifications(specificationString);
        return esProduct;
    }

    /**
     * 转换商品参数列表为json数组。
     * 如果某一个商品参数为null则忽略。
     *
     * @param productParams 商品参数列表
     * @return 商品参数json数组
     */
    private String productParamToString(List<ProductParam> productParams) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < productParams.size(); i++) {
            ProductParam productParam = productParams.get(i);
            if (productParam == null) {
                continue;
            }
            stringBuilder.append("{\"").append(productParam.getName()).append("\": ");
            ProductParamValueStrategy strategy = productParamValueStrategyFactory.getStrategy(productParam.getType());
            stringBuilder.append("\"").append(strategy.getTextValue(productParam.getValue())).append("\"}");
            if (i != productParams.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /**
     * 转换商品规格列表为json数组。
     * 如果某一个商品规格为null则忽略。
     *
     * @param specificationDTOS 商品规格列表
     * @return 商品规格json数组
     */
    private String productSpecificationToString(List<ProductSpecificationDTO> specificationDTOS) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int size = specificationDTOS.size();
        for (int i = 0; i < size; i++) {
            ProductSpecificationDTO specificationDTO = specificationDTOS.get(i);
            sb.append("{\"").append(specificationDTO.getName()).append("\": ");
            sb.append("[");
            int valueSize = specificationDTO.getValues().size();
            for (int j = 0; j < valueSize; j++) {
                ProductSpecificationDTO.SpecificationValueDTO valueDTO = specificationDTO.getValues().get(j);
                SpecificationValueStrategy strategy = specificationValueStrategyFactory.getStrategy(valueDTO.getType());
                String value = strategy.getTextValue(valueDTO.getValue());
                sb.append("\"").append(value).append("\"");
                if (j != valueSize - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]}");
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Autowired
    public void setProductParamValueStrategyFactory(ProductParamValueStrategyFactory productParamValueStrategyFactory) {
        this.productParamValueStrategyFactory = productParamValueStrategyFactory;
    }

    @Autowired
    public void setSpecificationValueStrategyFactory(SpecificationValueStrategyFactory specificationValueStrategyFactory) {
        this.specificationValueStrategyFactory = specificationValueStrategyFactory;
    }
}
