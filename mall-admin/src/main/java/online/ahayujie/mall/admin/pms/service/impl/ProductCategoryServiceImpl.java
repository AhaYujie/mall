package online.ahayujie.mall.admin.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.pms.bean.dto.CreateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.dto.ProductCategoryTree;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateProductCategoryMessageDTO;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;
import online.ahayujie.mall.admin.pms.exception.IllegalProductCategoryException;
import online.ahayujie.mall.admin.pms.mapper.ProductCategoryMapper;
import online.ahayujie.mall.admin.pms.service.ProductCategoryService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品分类 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-07-10
 */
@Slf4j
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final ProductCategoryMapper productCategoryMapper;

    public ProductCategoryServiceImpl(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate,
                                      ProductCategoryMapper productCategoryMapper) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public void create(CreateProductCategoryParam param) throws IllegalProductCategoryException {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(param, productCategory);
        validate(productCategory);
        productCategory.setLevel(getLevel(productCategory.getParentId()));
        productCategory.setCreateTime(new Date());
        productCategoryMapper.insert(productCategory);
    }

    @Override
    public void update(Long id, UpdateProductCategoryParam param) throws IllegalProductCategoryException {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);
        BeanUtils.copyProperties(param, productCategory);
        validate(productCategory);
        if (productCategoryMapper.selectById(id) == null) {
            throw new IllegalProductCategoryException("商品分类不存在");
        }
        if (productCategory.getParentId() != null) {
            productCategory.setLevel(getLevel(productCategory.getParentId()));
        }
        productCategory.setUpdateTime(new Date());
        productCategoryMapper.updateById(productCategory);
        // 发送更新商品分类的消息
        try {
            UpdateProductCategoryMessageDTO messageDTO = new UpdateProductCategoryMessageDTO(id);
            String message = objectMapper.writeValueAsString(messageDTO);
            rabbitTemplate.convertAndSend(RabbitmqConfig.PRODUCT_CATEGORY_UPDATE_EXCHANGE, "", message);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public CommonPage<ProductCategory> getPageByParentId(Long parentId, Integer pageNum, Integer pageSize) {
        Page<ProductCategory> page = new Page<>(pageNum, pageSize);
        IPage<ProductCategory> productCategoryPage = productCategoryMapper.selectByParentId(page, parentId);
        return new CommonPage<>(productCategoryPage);
    }

    @Override
    public ProductCategory getById(Long id) {
        return productCategoryMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        ProductCategory productCategory = productCategoryMapper.selectById(id);
        if (productCategory == null) {
            return;
        }
        productCategoryMapper.deleteById(id);
        // TODO:发送删除商品分类消息
    }

    @Override
    public void updateNavStatus(List<Long> ids, Integer isNav) throws IllegalProductCategoryException {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setIsNav(isNav);
        validate(productCategory);
        for (Long id : ids) {
            UpdateProductCategoryParam param = new UpdateProductCategoryParam();
            param.setIsNav(isNav);
            try {
                update(id, param);
            } catch (IllegalProductCategoryException e) {
                // do nothing
            }
        }
    }

    @Override
    public void updateShowStatus(List<Long> ids, Integer isShow) throws IllegalProductCategoryException {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setIsNav(isShow);
        validate(productCategory);
        for (Long id : ids) {
            UpdateProductCategoryParam param = new UpdateProductCategoryParam();
            param.setIsShow(isShow);
            try {
                update(id, param);
            } catch (IllegalProductCategoryException e) {
                // do nothing
            }
        }
    }

    @Override
    public List<ProductCategoryTree> listWithChildren() {
        List<ProductCategory> parents = productCategoryMapper.selectAllByParentId(ProductCategory.NON_PARENT_ID);
        List<ProductCategoryTree> trees = parents.stream()
                .map(parent -> new ProductCategoryTree(parent, null))
                .collect(Collectors.toList());
        for (ProductCategoryTree tree : trees) {
            ProductCategory parent = tree.getProductCategory();
            List<ProductCategory> children = productCategoryMapper.selectAllByParentId(parent.getId());
            List<ProductCategoryTree> childrenTrees = children.stream()
                    .map(child -> new ProductCategoryTree(child, null))
                    .collect(Collectors.toList());
            tree.setChildren(childrenTrees);
        }
        return trees;
    }

    /**
     * 根据上级分类获取分类级别
     * 若parentId为null则默认返回一级分类，即0
     * @param parentId 上级分类id
     * @return 分类级别
     */
    private Integer getLevel(Long parentId) {
        if (parentId == null || parentId == 0) {
            return 0;
        }
        ProductCategory parent = productCategoryMapper.selectById(parentId);
        return parent == null ? 0 : (parent.getLevel() + 1);
    }

    /**
     * 检查商品分类信息合法性，若某一字段为null则不检查该字段
     * @param productCategory 商品分类信息
     * @throws IllegalProductCategoryException 商品分类不合法
     */
    private void validate(ProductCategory productCategory) throws IllegalProductCategoryException {
        Long parentId = productCategory.getParentId();
        if (parentId != null && parentId < ProductCategory.NON_PARENT_ID) {
            throw new IllegalProductCategoryException("上级分类不合法");
        }
        Integer isNav = productCategory.getIsNav();
        if (isNav != null && !Arrays.stream(ProductCategory.NavStatus.values())
            .map(ProductCategory.NavStatus::getValue)
            .collect(Collectors.toList()).contains(isNav)) {
            throw new IllegalProductCategoryException("显示在导航栏状态不合法");
        }
        Integer isShow = productCategory.getIsShow();
        if (isShow != null && !Arrays.stream(ProductCategory.ShowStatus.values())
                .map(ProductCategory.ShowStatus::getValue)
                .collect(Collectors.toList()).contains(isShow)) {
            throw new IllegalProductCategoryException("显示在移动端状态不合法");
        }
    }
}
