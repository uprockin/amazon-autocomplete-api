package com.sellics.recruiting.amazonautocompleteapi.endpoint.dto;

import lombok.Data;

@Data
public class EstimationResponse {
	private String keyword;
	private int score;
}
