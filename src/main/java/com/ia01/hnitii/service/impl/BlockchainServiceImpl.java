package com.ia01.hnitii.service.impl;

import com.ia01.hnitii.controller.dto.BalanceDto;
import com.ia01.hnitii.controller.dto.BlockDto;
import com.ia01.hnitii.controller.dto.TransactionDto;
import com.ia01.hnitii.controller.mapper.BlockchainMapper;
import com.ia01.hnitii.model.Block;
import com.ia01.hnitii.model.Blockchain;
import com.ia01.hnitii.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ia01.hnitii.model.Blockchain.*;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class BlockchainServiceImpl implements BlockchainService {

	BlockchainMapper blockchainMapper;
	Blockchain blockchain;

	@Override
	public List<BlockDto> getChain() {
		return blockchain
				.getChain()
				.stream()
				.map(blockchainMapper::toBlockDto)
				.toList();
	}

	@Override
	public BlockDto mine() {
		Block lastBlock = requireNonNull(blockchain.lastBlock());

		int proof = blockchain.proofOfWork(lastBlock.getProof());

		blockchain.newTransaction(GENESIS_ADDRESS, THIS_NODE, HYO_DAY_OF_BIRTH);

		String lastHash = Blockchain.hash(lastBlock);

		Block newBlock = blockchain.newBlock(proof, lastHash);

		return blockchainMapper.toBlockDto(newBlock);
	}

	@Override
	public int createTransaction(TransactionDto transaction) {
		return blockchain.newTransaction(
				transaction.getSender(), transaction.getRecipient(), transaction.getAmount()
		);
	}

	@Override
	public List<BalanceDto> getBalances() {
		return blockchain.getAllBalances()
				.entrySet()
				.stream()
				.map(walletToBalance -> BalanceDto.builder()
						.wallet(walletToBalance.getKey())
						.amount(walletToBalance.getValue())
						.build()
				)
				.filter(dto -> ObjectUtils.notEqual(dto.getWallet(), GENESIS_ADDRESS))
				.toList();
	}

	@Override
	public List<TransactionDto> getMempool() {
		return blockchain.getMempool()
				.getTransactions()
				.stream()
				.map(blockchainMapper::toTransactionDto)
				.toList();
	}

}
