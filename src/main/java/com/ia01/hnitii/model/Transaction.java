package com.ia01.hnitii.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Transaction {

	String sender;

	String recipient;

	int amount;

	long timestamp = System.nanoTime();

	public Transaction(String sender, String recipient, int amount) {
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
	}
}
