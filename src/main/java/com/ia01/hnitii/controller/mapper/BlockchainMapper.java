package com.ia01.hnitii.controller.mapper;

import com.ia01.hnitii.controller.dto.BlockDto;
import com.ia01.hnitii.model.Block;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlockchainMapper {

	BlockDto toBlockDto(Block block);

}
