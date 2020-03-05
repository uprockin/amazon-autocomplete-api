package com.sellics.recruiting.amazonautocompleteapi.service.estimation.calculator;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sellics.recruiting.amazonautocompleteapi.service.amazon.model.AmazonResponse;


/**
 * SimpleEstimationCalculator calculates the estimation based on the indexes of keyword.
 * Gives a value between 0 - 100 regarding the index and divides to the total search value
 *  
 * @author Mikail
 *
 */
@Service
public class SimpleEstimationCalculator implements EstimationCalculator{

	@Override
	public int calculate(String keyword, List<AmazonResponse> amazonResponses) {
		double total = 0;
		
		for(AmazonResponse amazonResponse : amazonResponses) {
			if(amazonResponse.isParsingSuccessful() && amazonResponse.getSuggestions().contains(keyword)) {
				int index = amazonResponse.getSuggestions().indexOf(keyword);
				
				int value = (10 - index) * 10;
				total += value;
			}
		}
		return (int)(total / keyword.length());
	}
	
}
