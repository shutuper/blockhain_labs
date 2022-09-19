package com.ia01.hnitii.controller;

import com.ia01.hnitii.controller.dto.BlockDto;
import com.ia01.hnitii.common.dto.PageResponse;
import com.ia01.hnitii.controller.dto.TransactionDto;
import com.ia01.hnitii.service.BlockchainService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "blockchain")
@RestController
@RequestMapping("/api/v1/blockchain")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BlockchainController {

	BlockchainService blockchainService;

	@GetMapping("/chain")
	public ResponseEntity<PageResponse<BlockDto>> getChain() {
		List<BlockDto> chain = blockchainService.getChain();
		return ResponseEntity.ok(
				PageResponse.of(chain, chain.size())
		);
	}

	@GetMapping("/mine")
	public ResponseEntity<BlockDto> mine() {
		BlockDto block = blockchainService.mine();
		return ResponseEntity.ok(block);
	}

	@PostMapping("/transactions")
	public ResponseEntity<String> createTransaction(@RequestBody @Valid
	                                                TransactionDto transaction) {
		int index = blockchainService.createTransaction(transaction);
		return ResponseEntity.ok("New transaction created, index: " + index);
	}

}