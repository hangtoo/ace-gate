package com.github.wxiaoqi.gate.back.mapper;

import com.github.wxiaoqi.gate.back.entity.GateClient;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface GateClientMapper extends Mapper<GateClient> {
    public void insertClientServiceById(@Param("clientId") int clientId, @Param("serviceId") int serviceId);

    public void deleteClientServiceById(@Param("clientId") int clientId);
}