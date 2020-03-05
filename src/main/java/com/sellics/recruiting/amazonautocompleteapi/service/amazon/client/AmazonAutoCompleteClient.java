package com.sellics.recruiting.amazonautocompleteapi.service.amazon.client;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sellics.recruiting.amazonautocompleteapi.service.amazon.exception.AmazonResponseParserException;
import com.sellics.recruiting.amazonautocompleteapi.service.amazon.model.AmazonResponse;
import com.sellics.recruiting.amazonautocompleteapi.service.amazon.parser.AmazonResponseParser;

import lombok.extern.slf4j.Slf4j;


/**
 * Amazon API requesting client.
 * The search method runs aynchronously and returns a {@link CompletableFuture}
 * By using this service async, the anchestor service will not wait and will only follow the Future.
 * @author Mikail
 *
 */
@Service
@Slf4j
public class AmazonAutoCompleteClient {
	@Autowired 
	private AmazonResponseParser amazonResponseParser;
	
	@Value("${amazon.service.address}")
	private String amazonServiceAddress;
	
	@Value("${keyword.replacement}")
	private String keywordReplacement;
	

	private RestTemplate template = new RestTemplate();
	
	@Async
	public CompletableFuture<AmazonResponse> search(String keyword) {
		AmazonResponse amazonResponse = null;
		
		try {
			String url = this.amazonServiceAddress.replace(keywordReplacement, keyword);
			
			ResponseEntity<String> response = template.getForEntity(url, String.class);
			
			String body = response.getBody();
		
			amazonResponse = this.amazonResponseParser.parseResponse(body);
			
		} catch (AmazonResponseParserException e) {
			log.error(e.getMessage(), e);
		}
		
		return CompletableFuture.completedFuture(amazonResponse);
	}
}
