package com.ia01.hnitii;

import com.ia01.hnitii.model.HYOBlock;
import com.ia01.hnitii.model.HYOBlockchain;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static java.util.Objects.requireNonNull;

@SpringBootApplication
public class Lab1HnitiiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Lab1HnitiiApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(HYOBlockchain blockchain) {
		return args -> {
			HYOBlock lastBlock = requireNonNull(blockchain.lastBlock());
			for (int i = 0; i < 10; i++) {
				System.out.println("Block N" + (i + 1) + "\n" + lastBlock);

				int proof = blockchain.proofOfWork(lastBlock.getProof());
				String hash = HYOBlockchain.hash(lastBlock);

				blockchain.newBlock(proof, hash);
				lastBlock = requireNonNull(blockchain.lastBlock());
			}

		};
	}

}
