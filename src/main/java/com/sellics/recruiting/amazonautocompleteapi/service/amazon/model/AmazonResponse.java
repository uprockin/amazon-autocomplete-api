package com.sellics.recruiting.amazonautocompleteapi.service.amazon.model;

import java.util.List;

import lombok.Data;

/**
 * Wrapper POJO class for collecting Amazon API
 * 
 * @author Mikail
 *
 */

@Data
public class AmazonResponse {
	private boolean parsingSuccessful;
	private String keyword;
	private List<String> suggestions;
}
