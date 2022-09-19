package com.ia01.hnitii.service.impl;

import com.ia01.hnitii.controller.dto.BlockDto;
import com.ia01.hnitii.controller.dto.TransactionDto;
import com.ia01.hnitii.controller.mapper.BlockchainMapper;
import com.ia01.hnitii.model.Block;
import com.ia01.hnitii.model.Blockchain;
import com.ia01.hnitii.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ia01.hnitii.model.Blockchain.HYO_DAY_OF_BIRTH;
import static com.ia01.hnitii.model.Blockchain.THIS_NODE;
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

		blockchain.newTransaction("0", THIS_NODE, HYO_DAY_OF_BIRTH);

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

}
