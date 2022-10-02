package com.ia01.hnitii.service;

import com.ia01.hnitii.controller.dto.BalanceDto;
import com.ia01.hnitii.controller.dto.BlockDto;
import com.ia01.hnitii.controller.dto.TransactionDto;

import java.util.List;

public interface BlockchainService {


	List<BlockDto> getChain();

	BlockDto mine();

	int createTransaction(TransactionDto transaction);

	List<BalanceDto> getBalances();

	List<TransactionDto> getMempool();

}
