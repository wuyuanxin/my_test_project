package com.my.mapper;

import com.my.domain.WebDto;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface WebDtoMapper extends Mapper<WebDto>, MySqlMapper<WebDto> {
    public Integer selectTotal();
}