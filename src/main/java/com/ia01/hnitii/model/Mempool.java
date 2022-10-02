package com.ia01.hnitii.model;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Component
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class Mempool {

	List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());

	public void add(Transaction transaction) {
		this.transactions.add(transaction);
	}

	public List<Transaction> getAllAndClear() {
		List<Transaction> currentTransactions = Collections.synchronizedList(new ArrayList<>(this.transactions));
		this.transactions.clear();
		return currentTransactions;
	}

	public int getAmount() {
		return this.transactions.size();
	}

}
