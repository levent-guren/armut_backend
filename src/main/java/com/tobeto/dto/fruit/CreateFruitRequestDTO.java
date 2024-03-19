package com.tobeto.dto.fruit;

import lombok.Data;

@Data
public class CreateFruitRequestDTO {
	private int id;
	private String name;
	private int minimum;
	private String image;
}
