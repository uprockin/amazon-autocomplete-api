package com.sellics.recruiting.amazonautocompleteapi.service.estimation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sellics.recruiting.amazonautocompleteapi.service.amazon.client.AmazonAutoCompleteClient;
import com.sellics.recruiting.amazonautocompleteapi.service.amazon.model.AmazonResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AmazonService {
	
	@Autowired
	private AmazonAutoCompleteClient amazonAutoCompleteClient;
	
	
	/**
	 * This method receives a <b>keyword</b> as an input and brute forces Amazon API by creating prefixes with all sequential letters.
	 * Request's the Amazon API as asynchronously and tries to decrease the total service response time.
	 * Returns an {@link AmazonResponse} covering the requested keyword an suggestions.
	 * 
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AmazonResponse> getSuggestions(String keyword) {
		CompletableFuture<AmazonResponse>[] amazonResponseFutureArray =  new CompletableFuture[keyword.length()];
		
		StringBuilder word = new StringBuilder();
		
		for(int i = 0; i < keyword.length(); i++) {
			word.append(keyword.charAt(i));
			
			CompletableFuture<AmazonResponse> amazonResponseFuture = this.amazonAutoCompleteClient.search(word.toString());
			amazonResponseFutureArray[i] = amazonResponseFuture;
		}
		
		CompletableFuture.allOf(amazonResponseFutureArray).join();		// Wait for all futures to complete
		
		List<AmazonResponse> amazonResponses = new ArrayList<>();
		Arrays.asList(amazonResponseFutureArray).forEach(future -> {
			try {
				amazonResponses.add(future.get());
			} catch (Exception e) {
				log.error("Exception occured while getting future of CompletableFuture:" + future, e);
			}
		});
		
		return amazonResponses;
	}
}
