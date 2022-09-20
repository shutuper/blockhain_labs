package com.ia01.hnitii.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class HYOBlock {

	List<HYOTransaction> HYOTransactions;

	String previousHash;

	String hash;

	long timestamp;

	int index;

	int proof;

}
