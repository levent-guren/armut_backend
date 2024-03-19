package com.tobeto.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.SuccessResponseDTO;
import com.tobeto.dto.fruit.AcceptFruitRequestDTO;
import com.tobeto.dto.fruit.CreateFruitRequestDTO;
import com.tobeto.dto.fruit.DeleteFruitRequestDTO;
import com.tobeto.dto.fruit.FruitCountResponseDTO;
import com.tobeto.dto.fruit.FruitResponseDTO;
import com.tobeto.entity.Fruit;
import com.tobeto.service.FruitService;

@RestController
@RequestMapping("/api/v1/fruit")
public class FruitController {
	@Autowired
	private FruitService fruitService;
	@Autowired
	@Qualifier("requestMapper")
	private ModelMapper requestMapper;

	@Autowired
	@Qualifier("responseMapper")
	private ModelMapper responseMapper;

	@PostMapping("/create")
	public SuccessResponseDTO createFruit(@RequestBody CreateFruitRequestDTO dto) {
		Fruit fruit = requestMapper.map(dto, Fruit.class);
		fruitService.createFruit(fruit);
		return new SuccessResponseDTO();
	}

	@PostMapping("/delete")
	public SuccessResponseDTO deleteFruit(@RequestBody DeleteFruitRequestDTO dto) {
		fruitService.deleteFruit(dto.getId());
		return new SuccessResponseDTO();
	}

	@PostMapping("/accept")
	public SuccessResponseDTO acceptFruit(@RequestBody AcceptFruitRequestDTO dto) {
		fruitService.acceptFruit(dto.getFruitId(), dto.getCount());
		return new SuccessResponseDTO();
	}

	@PostMapping("/sale")
	public SuccessResponseDTO saleFruit(@RequestBody AcceptFruitRequestDTO dto) {
		fruitService.saleFruit(dto.getFruitId(), dto.getCount());
		return new SuccessResponseDTO();
	}

	@GetMapping("/")
	public List<FruitResponseDTO> getAllFruits() {
		List<Fruit> fruits = fruitService.getAllFruits();
		return fruits.stream().map(f -> responseMapper.map(f, FruitResponseDTO.class)).toList();
	}

	@GetMapping("/count/{fruitId}")
	public FruitCountResponseDTO getFruitCount(@PathVariable int fruitId) {
		int count = fruitService.getFruitCount(fruitId);
		return new FruitCountResponseDTO(count);
	}
}
