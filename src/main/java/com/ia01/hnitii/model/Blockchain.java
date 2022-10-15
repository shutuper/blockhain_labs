package com.ia01.hnitii.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.ia01.hnitii.common.dto.PageResponse;
import com.ia01.hnitii.common.exception.BusinessException;
import com.ia01.hnitii.controller.dto.BlockDto;
import com.ia01.hnitii.controller.mapper.BlockchainMapper;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.ObjectUtils.notEqual;

@Getter
@ToString
@Component
@FieldDefaults(makeFinal = true, level = PRIVATE)
public final class Blockchain {

	@NonFinal
	List<Block> chain = Collections.synchronizedList(new ArrayList<>());
	Set<String> nodes = Collections.synchronizedSet(new HashSet<>());

	BlockchainMapper blockchainMapper;
	Mempool mempool;

	public final static String THIS_NODE = "914dc579-9ef6-489a-b59d-b52c9f3c6fd2";
	public final static String HYO_MONTH_OF_BIRTH = "07";
	public final static String GENESIS_ADDRESS = "0";
	public final static int HYO_DAY_OF_BIRTH = 18;

	public Blockchain(BlockchainMapper blockchainMapper, Mempool mempool) {
		this.blockchainMapper = blockchainMapper;
		this.mempool = mempool;
		newTransaction(GENESIS_ADDRESS, THIS_NODE, HYO_DAY_OF_BIRTH);
		newBlock(18072003, "hnitii_f033b131e00564ab7f84abdf16f0df73faa06f809a54fffbadad80d07");
	}

	public void registerNode(String node) {
		this.nodes.add(node);
	}

	public void resolveConflicts() {
		List<Block> newChain = this.chain;

		newChain = consensusAlgorithm(newChain);

		if (newChain != this.chain) {
			this.chain = newChain;
		}
	}

	public static boolean validChain(List<Block> chain) {
		for (int i = 1; i < chain.size(); i++) {
			Block lastBlock = chain.get(i - 1);
			Block currentBlock = chain.get(i);

			if (notEqual(currentBlock.getPreviousHash(), hash(lastBlock))) {
				System.out.println("Hash don't match");
				return false;
			}
		}

		return true;
	}

	private List<Block> consensusAlgorithm(List<Block> newChain) {
		int maxLen = newChain.size();

		for (String host : this.nodes) {
			String path = "/api/v1/blockchain/chain";
			String getURL = host + path;

			Request request = new Request.Builder()
					.url(getURL)
					.get()
					.build();

			try (Response response = new OkHttpClient().newCall(request).execute()) {
				if (!response.isSuccessful()) {
					throw new BusinessException("Failed to GET " + getURL);
				}

				String responseBody = Objects.requireNonNull(response.body()).string();
				PageResponse<BlockDto> remoteChainResponse = new ObjectMapper().readValue(
						responseBody, new TypeReference<>() {
						});

				List<Block> remoteChain = remoteChainResponse.getResult()
						.stream()
						.map(blockchainMapper::toBlock)
						.toList();

				if (remoteChain.size() > maxLen && validChain(remoteChain)) {
					maxLen = remoteChain.size();
					newChain = remoteChain;
				}

			} catch (IOException e) {
				throw new BusinessException("Failed to GET: " + getURL + ". IOException: " + e.getMessage());
			}
		}
		return newChain;
	}

	public Block lastBlock() {
		return CollectionUtils.isNotEmpty(this.chain) ?
				this.chain.get(this.chain.size() - 1) : null;
	}

	public int newTransaction(String sender, String recipient, int amount) {
		Transaction transaction = new Transaction(sender, recipient, amount);

		if (notEqual(GENESIS_ADDRESS, sender)) {
			validateTransaction(transaction);
		}

		this.mempool.add(transaction);
		return this.mempool.getAmount();
	}

	public Map<String, Long> getAllBalances() {
		Map<String, Long> walletToBalance = new HashMap<>();

		this.chain.stream()
				.flatMap(block -> block.getTransactions().stream())
				.forEach(transaction -> {
					String recipient = transaction.getRecipient();
					String sender = transaction.getSender();
					int amount = transaction.getAmount();
					walletToBalance.put(recipient, (walletToBalance.getOrDefault(recipient, 0L) + amount));
					walletToBalance.put(sender, (walletToBalance.getOrDefault(sender, 0L) - amount));
				});

		return walletToBalance;
	}

	public Block newBlock(int proof, String previousHash) {
		List<Transaction> transactions = this.mempool.getAllAndClear();

		Block newBlock = Block.builder()
				.previousHash(previousHash)
				.transactions(transactions)
				.timestamp(System.nanoTime())
				.index(this.chain.size())
				.proof(proof)
				.build();

		newBlock.setHash(hash(newBlock));

		this.chain.add(newBlock);

		return newBlock;
	}

	public static String hash(Block block) {
		String guessHash = Hashing.sha256()
				.hashString(getHashingInput(block), StandardCharsets.UTF_8)
				.toString();

		while (isBlockHashNotValid(guessHash)) {
			block.setProof(block.getProof() + 1);
			guessHash = Hashing.sha256()
					.hashString(getHashingInput(block), StandardCharsets.UTF_8)
					.toString();
		}

		return guessHash;
	}

	public int proofOfWork(int lastProofOfWork) {
		int proof = 0;
		while (isNotProofValid(lastProofOfWork, proof)) {
			proof++;
		}
		return proof;
	}

	private boolean isNotProofValid(int lastProof, int proof) {
		String guessString = Integer.toString(lastProof) + proof;
		return !Hashing.sha256()
				.hashString(guessString, StandardCharsets.UTF_8)
				.toString()
				.endsWith(HYO_MONTH_OF_BIRTH);
	}

	private static boolean isBlockHashNotValid(String guessHash) {
		return !guessHash.endsWith(HYO_MONTH_OF_BIRTH);
	}

	@NotNull
	private static String getHashingInput(Block hyoBlock) {
		return String.valueOf(hyoBlock.getIndex())
				.concat(Arrays.toString(hyoBlock.getTransactions().toArray()))
				.concat(String.valueOf(hyoBlock.getTimestamp()))
				.concat(String.valueOf(hyoBlock.getProof()))
				.concat(hyoBlock.getPreviousHash());
	}

	private void validateTransaction(Transaction transaction) {
		boolean isTransactionValid = getWalletBalance(transaction.getSender()) >= transaction.getAmount();

		if (!isTransactionValid) {
			throw new BusinessException("The sender doesn't have enough tokens to complete the transaction");
		}
	}

	private long getWalletBalance(String walletAddress) {
		return Stream.concat(
				this.chain.stream().flatMap(block -> block.getTransactions().stream()),
				this.mempool.getTransactions().stream()
		).mapToLong(transaction -> {
			if (Objects.equals(transaction.getRecipient(), walletAddress)) {
				return transaction.getAmount();

			} else if (Objects.equals(transaction.getSender(), walletAddress)) {
				return -transaction.getAmount();

			}
			return 0;
		}).sum();
	}
}
