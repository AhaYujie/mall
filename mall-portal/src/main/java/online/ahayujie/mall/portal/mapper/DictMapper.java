package online.ahayujie.mall.portal.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import online.ahayujie.mall.portal.bean.model.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-12
 */
@Mapper
@Repository
public interface DictMapper extends BaseMapper<Dict> {
    /**
     * 根据code和dictKey查询
     * @param code 字典码
     * @param DictKey 字典值
     * @return 数据字典
     */
    Dict selectByCodeAndDictKey(@Param("code") String code, @Param("dictKey") String DictKey);

    /**
     * 根据code和dictKey更新
     * @param dict 数据字典
     * @return 更新数量
     */
    int updateByCodeAndDictKey(Dict dict);

    /**
     * 根据code查询，按照sort从大到小排序。
     * @param code 字典码
     * @return 数据字典
     */
    List<Dict> selectByCode(String code);
}
