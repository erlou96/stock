package com.binzaijun.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.binzaijun.stock.domain.StockChange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface StockChangeMapper extends BaseMapper<StockChange> {

    boolean saveOrUpdateBatch(@Param("entities") Collection<StockChange> hhChainCustomerParams);
}
