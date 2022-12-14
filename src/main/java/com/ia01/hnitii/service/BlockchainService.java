package com.ia01.hnitii.service;

import com.ia01.hnitii.controller.dto.*;

import java.util.List;

public interface BlockchainService {


	List<BlockDto> getChain();

	BlockDto mine();

	int createTransaction(TransactionDto transaction);

	List<BalanceDto> getBalances();

	List<TransactionDto> getMempool();

	NodeDto registerNodes(NodeDto nodeDto);

	NodeDto getNodes();

	List<BlockDto> resolveConflicts();

}
