package online.ahayujie.mall.admin.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.*;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;
import online.ahayujie.mall.admin.pms.exception.IllegalProductCategoryException;
import online.ahayujie.mall.admin.pms.mapper.ProductCategoryMapper;
import online.ahayujie.mall.admin.pms.publisher.ProductCategoryPublisher;
import online.ahayujie.mall.admin.pms.service.ProductCategoryService;
import online.ahayujie.mall.common.api.CommonPage;
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
    private ProductCategoryPublisher productCategoryPublisher;

    private final ProductCategoryMapper productCategoryMapper;

    public ProductCategoryServiceImpl(ProductCategoryMapper productCategoryMapper) {
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public void create(CreateProductCategoryParam param) throws IllegalProductCategoryException {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(param, productCategory);
        validate(productCategory);
        Long parentId = param.getParentId();
        if (parentId != ProductCategory.NON_PARENT_ID && productCategoryMapper.selectById(parentId) == null) {
            throw new IllegalProductCategoryException("上级分类不存在");
        }
        productCategory.setCreateTime(new Date());
        productCategoryMapper.insert(productCategory);
    }

    @Override
    public void update(Long id, UpdateProductCategoryParam param) throws IllegalProductCategoryException {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);
        BeanUtils.copyProperties(param, productCategory);
        validate(productCategory);
        if (id.equals(param.getParentId())) {
            throw new IllegalProductCategoryException("上级分类不能是自身");
        }
        if (productCategoryMapper.selectById(id) == null) {
            throw new IllegalProductCategoryException("商品分类不存在");
        }
        Long parentId = productCategory.getParentId();
        if (parentId != null && ProductCategory.NON_PARENT_ID != parentId && productCategoryMapper.selectById(parentId) == null) {
            throw new IllegalProductCategoryException("上级分类不存在");
        }
        List<Long> subIds = new ArrayList<>();
        helpGetSub(id, subIds);
        if (subIds.contains(parentId)) {
            throw new IllegalProductCategoryException("更新的上级分类不能是该商品分类的下级分类");
        }
        productCategory.setUpdateTime(new Date());
        productCategoryMapper.updateById(productCategory);
        // 发送更新商品分类的消息
        productCategoryPublisher.publishUpdateMsg(id);
    }

    /**
     * 递归获取所有下级分类id
     * @param parentId 上级分类id
     * @param ids 下级分类id
     */
    private void helpGetSub(Long parentId, List<Long> ids) {
        List<Long> tmp = productCategoryMapper.selectIdsByParentId(parentId);
        for (Long id : tmp) {
            helpGetSub(id, ids);
        }
        ids.addAll(tmp);
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
        List<Long> subCategoryIds = productCategoryMapper.selectIdsByParentId(id);
        subCategoryIds.forEach(this::delete);
        // 发送删除商品分类消息
        DeleteProductCategoryMessageDTO messageDTO = new DeleteProductCategoryMessageDTO();
        BeanUtils.copyProperties(productCategory, messageDTO);
        productCategoryPublisher.publishDeleteMsg(messageDTO);
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
                // 抛出异常说明这个商品分类不存在，则忽略
            }
        }
    }

    @Override
    public List<ProductCategoryTree> listWithChildren(Long parentId) {
        if (ProductCategory.NON_PARENT_ID == parentId) {
            return listAllWithChildren();
        }
        List<ProductCategory> roots = productCategoryMapper.selectAllByParentId(parentId);
        List<ProductCategoryTree> trees = new ArrayList<>();
        for (ProductCategory root : roots) {
            ProductCategoryTree tree = new ProductCategoryTree();
            tree.setProductCategory(root);
            tree.setChildren(listWithChildren(root.getId()));
            trees.add(tree);
        }
        return trees;
    }

    /**
     * 树形结构递归查询所有子分类
     * @return 商品分类
     */
    private List<ProductCategoryTree> listAllWithChildren() {
        List<ProductCategory> all = productCategoryMapper.selectAll();
        return helpListAllWithChildren(ProductCategory.NON_PARENT_ID, all);
    }

    private List<ProductCategoryTree> helpListAllWithChildren(Long parentId, List<ProductCategory> all) {
        List<ProductCategoryTree> trees = new ArrayList<>();
        for (ProductCategory each : all) {
            if (parentId.equals(each.getParentId())) {
                ProductCategoryTree tree = new ProductCategoryTree();
                tree.setProductCategory(each);
                tree.setChildren(helpListAllWithChildren(each.getId(), all));
                trees.add(tree);
            }
        }
        trees.sort((o1, o2) -> o2.getProductCategory().getSort() - o1.getProductCategory().getSort());
        return trees;
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
    }

    @Autowired
    public void setProductCategoryPublisher(ProductCategoryPublisher productCategoryPublisher) {
        this.productCategoryPublisher = productCategoryPublisher;
    }
}
