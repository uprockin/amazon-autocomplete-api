package com.sellics.recruiting.amazonautocompleteapi.service.estimation.calculator;

import java.util.List;

import com.sellics.recruiting.amazonautocompleteapi.service.amazon.model.AmazonResponse;

public interface EstimationCalculator {
	public int calculate(String keyword, List<AmazonResponse> amazonResponses);
}
