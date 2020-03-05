package com.sellics.recruiting.amazonautocompleteapi.service.amazon.parser;

import java.util.ArrayList;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.stereotype.Component;

import com.sellics.recruiting.amazonautocompleteapi.service.amazon.exception.AmazonResponseParserException;
import com.sellics.recruiting.amazonautocompleteapi.service.amazon.model.AmazonResponse;

import lombok.extern.slf4j.Slf4j;


/**
 * Amazon API returns a JavaScript array containing the requested keyword, and suggestions.
 * This parser class is for parsing the data and converting it to a plain old java object
 * @author Mikail
 *
 */
@Component
@Slf4j
public class AmazonResponseParser {
	private ScriptEngineManager manager = new ScriptEngineManager();
	
	public AmazonResponse parseResponse(String response) throws AmazonResponseParserException {
		AmazonResponse amazonResponse = new AmazonResponse();
		
		try {
			ScriptEngine engine = this.manager.getEngineByName("JavaScript");
			
			engine.eval("var response = " + response + ";");
			engine.eval("function getKeyword() { return " + response + "[0] };");
			engine.eval("function getSuggestionLength() { return " + response + "[1].length };");
			engine.eval("function getSuggestion(index) { return " + response + "[1][index] };");
			
			Invocable invocable = (Invocable) engine;
			Object keyword = invocable.invokeFunction("getKeyword");
			
			amazonResponse.setKeyword(keyword.toString());
			amazonResponse.setSuggestions(new ArrayList<String>());
			
			int suggestionLength = (int)invocable.invokeFunction("getSuggestionLength");
			for(int i = 0; i < suggestionLength; i++) {
				Object suggestion = invocable.invokeFunction("getSuggestion", i);
				
				amazonResponse.getSuggestions().add(suggestion.toString());
			}
		
			amazonResponse.setParsingSuccessful(true);
		} catch (Exception e) {
			log.error("Exception occured while parsing Amazon response: " + response, e);
			throw new AmazonResponseParserException("Exception occured while parsing Amazon response: " + response, e);
			
		}
		
		return amazonResponse;
	}
}
