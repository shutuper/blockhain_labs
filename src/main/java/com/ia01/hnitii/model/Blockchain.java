package com.ia01.hnitii.model;

import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Getter
@ToString
@Component
@FieldDefaults(makeFinal = true, level = PRIVATE)
public final class Blockchain {

	List<Block> chain = new ArrayList<>();
	List<Transaction> currentTransactions = new ArrayList<>();

	public static String HYO_MONTH_OF_BIRTH = "07";
	public static String THIS_NODE = UUID.randomUUID().toString();
	public static int HYO_DAY_OF_BIRTH = 18;

	public Blockchain() {
		newTransaction("0", THIS_NODE, HYO_DAY_OF_BIRTH);
		newBlock(18072003, "hnitii_f033b131e00564ab7f84abdf16f0df73faa06f809a54fffbadad80d07");
	}

	public Block lastBlock() {
		return CollectionUtils.isNotEmpty(this.chain) ?
				this.chain.get(this.chain.size() - 1) : null;
	}

	public int newTransaction(String sender, String recipient, int amount) {
		this.currentTransactions.add(new Transaction(sender, recipient, amount));
		return this.currentTransactions.size();
	}

	public Block newBlock(int proof, String previousHash) {
		List<Transaction> transactions = new ArrayList<>(this.currentTransactions);

		Block newBlock = Block.builder()
				.previousHash(previousHash)
				.transactions(transactions)
				.timestamp(System.nanoTime())
				.index(this.chain.size())
				.proof(proof)
				.build();

		this.currentTransactions.clear();

		this.chain.add(newBlock);

		newBlock.setHash(hash(newBlock));

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
				.concat(String.valueOf(hyoBlock.getTimestamp()))
				.concat(String.valueOf(hyoBlock.getProof()))
				.concat(hyoBlock.getPreviousHash());
	}
}
