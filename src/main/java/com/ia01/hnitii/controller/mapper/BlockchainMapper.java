package com.ia01.hnitii.controller.mapper;

import com.ia01.hnitii.controller.dto.BlockDto;
import com.ia01.hnitii.controller.dto.TransactionDto;
import com.ia01.hnitii.model.Block;
import com.ia01.hnitii.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlockchainMapper {

	BlockDto toBlockDto(Block block);

	TransactionDto toTransactionDto(Transaction transaction);

	Block toBlock(BlockDto dto);

}
