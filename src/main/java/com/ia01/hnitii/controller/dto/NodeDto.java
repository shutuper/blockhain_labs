package com.ia01.hnitii.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class NodeDto {

	@NotEmpty
	List<String> nodes = new ArrayList<>();

}
