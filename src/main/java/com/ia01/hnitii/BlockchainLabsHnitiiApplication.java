package com.ia01.hnitii;

import com.ia01.hnitii.model.Blockchain;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlockchainLabsHnitiiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockchainLabsHnitiiApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(Blockchain blockchain) {
		return args -> {
//			Block lastBlock = requireNonNull(blockchain.lastBlock());
//			for (int i = 0; i < 10; i++) {
//				System.out.println("Block N" + (i + 1) + "\n" + lastBlock);
//
//				int proof = blockchain.proofOfWork(lastBlock.getProof());
//				String hash = Blockchain.hash(lastBlock);
//
//				blockchain.newBlock(proof, hash);
//				lastBlock = requireNonNull(blockchain.lastBlock());
//			}
		};
	}

}
