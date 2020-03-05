package com.sellics.recruiting.amazonautocompleteapi.service.estimation.calculator;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sellics.recruiting.amazonautocompleteapi.service.amazon.model.AmazonResponse;


/**
 * AggregatedEstimationCalculator calculates the estimation based on the product name knowledge.
 * Consider that we are searching "iphone" keyword and Amazon API suggested various words related to iphone like "iphone 8, iphone 11, ..."
 * In this case the exact matching can not be found, but several iphone suggestions are returned.
 * So this means, as a product "iphone"'s popularity is high, but due to the missing exact matching, the rate would be lower.
 * This calculator tries to find the product popularity rate regarding products related to keyword.
 *  
 * @author Mikail
 *
 */
@Service
public class AggregatedEstimationCalculator implements EstimationCalculator{

	@Override
	public int calculate(String keyword, List<AmazonResponse> amazonResponses) {
		double total = 0;
		
		List<String> splittedKeywordList = Arrays.asList(keyword.split(" "));
		
		for(AmazonResponse amazonResponse : amazonResponses) {
			int suggestionSize = amazonResponse.getSuggestions().size();
			
			if(suggestionSize > 0 && amazonResponse.isParsingSuccessful()) {
				if(amazonResponse.getSuggestions().contains(keyword)) {		// If suggestion list contains the exact match
					int index = amazonResponse.getSuggestions().indexOf(keyword);
					
					int value = (10 - index) * 10;
					total += value;
					
				} else {	// If suggestion does not contain the exact match but has all the words in any suggestion, give it a half point to increase score
					double averageValue = 0d;
					double totalPossibleValue = 0d;
					
					int index = 0;
					for(String suggestion : amazonResponse.getSuggestions()) {
						List<String> splittedSuggestionList = Arrays.asList(suggestion.split(" "));
						
						double possibleValue = 10d * (suggestionSize - index);
						totalPossibleValue += possibleValue;
						
						if(splittedSuggestionList.size() > 1 && splittedSuggestionList.containsAll(splittedKeywordList)) {
							averageValue += possibleValue;
						}
						index++;
					}
					
					total += (averageValue / totalPossibleValue) * 100;
					
				}
			}
		}
		
		return (int)(total / keyword.length());
	}
	
}
