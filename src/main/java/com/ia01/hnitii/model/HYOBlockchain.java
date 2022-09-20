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

import static lombok.AccessLevel.PRIVATE;

@Getter
@ToString
@Component
@FieldDefaults(makeFinal = true, level = PRIVATE)
public final class HYOBlockchain {

	List<HYOBlock> hyoChain = new ArrayList<>();
	List<HYOTransaction> hyoCurrentTransactions = new ArrayList<>();

	static String HYO_MONTH_OF_BIRTH = "07";

	public HYOBlockchain() {
		newBlock(18072003, "hnitii_f033b131e00564ab7f84abdf16f0df73faa06f809a54fffbadad80d07");
	}

	public HYOBlock lastBlock() {
		return CollectionUtils.isNotEmpty(this.hyoChain) ?
				this.hyoChain.get(this.hyoChain.size() - 1) : null;
	}

	public int newTransaction(String sender, String recipient, int amount) {
		this.hyoCurrentTransactions.add(new HYOTransaction(sender, recipient, amount));
		return this.hyoCurrentTransactions.size();
	}

	public HYOBlock newBlock(int proof, String previousHash) {
		List<HYOTransaction> hyoTransactions = new ArrayList<>(this.hyoCurrentTransactions);

		HYOBlock newHyoBlock = HYOBlock.builder()
				.previousHash(previousHash)
				.HYOTransactions(hyoTransactions)
				.timestamp(System.nanoTime())
				.index(this.hyoChain.size())
				.proof(proof)
				.build();

		this.hyoCurrentTransactions.clear();

		this.hyoChain.add(newHyoBlock);

		newHyoBlock.setHash(hash(newHyoBlock));

		return newHyoBlock;
	}

	public static String hash(HYOBlock hyoBlock) {
		String guessHash = Hashing.sha256()
				.hashString(getHashingInput(hyoBlock), StandardCharsets.UTF_8)
				.toString();

		while (isHashNotValid(guessHash)) {
			hyoBlock.setProof(hyoBlock.getProof() + 1);
			guessHash = Hashing.sha256()
					.hashString(getHashingInput(hyoBlock), StandardCharsets.UTF_8)
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
		String guessHash = Hashing.sha256()
				.hashString(guessString, StandardCharsets.UTF_8)
				.toString();

		boolean guessed = guessHash.endsWith(HYO_MONTH_OF_BIRTH);

		if (guessed) {
			System.out.println("Guessed hash: " + guessHash + "\n");
		}

		return !guessed;
	}

	@NotNull
	private static String getHashingInput(HYOBlock hyoBlock) {
		return String.valueOf(hyoBlock.getIndex())
				.concat(String.valueOf(hyoBlock.getTimestamp()))
				.concat(String.valueOf(hyoBlock.getProof()))
				.concat(hyoBlock.getPreviousHash());
	}

	private static boolean isHashNotValid(String guessHash) {
		return !guessHash.endsWith(HYO_MONTH_OF_BIRTH);
	}
}
