package com.ia01.hnitii.controller.dto;

import com.ia01.hnitii.model.Block;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ChainDto {

	List<Block> chain = new ArrayList<>();

	int length = 0;

}
