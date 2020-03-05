package com.sellics.recruiting.amazonautocompleteapi.endpoint;

import java.util.EnumMap;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sellics.recruiting.amazonautocompleteapi.endpoint.dto.EstimationResponse;
import com.sellics.recruiting.amazonautocompleteapi.endpoint.dto.EstimationType;
import com.sellics.recruiting.amazonautocompleteapi.service.amazon.model.AmazonResponse;
import com.sellics.recruiting.amazonautocompleteapi.service.estimation.AmazonService;
import com.sellics.recruiting.amazonautocompleteapi.service.estimation.calculator.EstimationCalculator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EstimationEndpoint implements InitializingBean{
	
	@Autowired	private AmazonService amazonService;
	@Autowired	private EstimationCalculator simpleEstimationCalculator;
	@Autowired	private EstimationCalculator aggregatedEstimationCalculator;
	
	private EnumMap<EstimationType, EstimationCalculator> estimationCalculators;
	
	
	@GetMapping("estimate")
	public EstimationResponse estimate(@PathParam("keyword") String keyword, @PathParam("estimation") String estimation) {
		long start = System.currentTimeMillis();
		
		EstimationType selectedEstimationType = EstimationType.SIMPLE;
		try {
			selectedEstimationType = EstimationType.valueOf(estimation.toUpperCase());
		} catch(Exception e) {
			log.info("Unknown estimation type supplied :{}. Continue with default.", estimation);
		}
		
		log.info("Calculating estimation for keyword: {} as {}", keyword, selectedEstimationType);
		
		
		List<AmazonResponse> amazonSuggestions = this.amazonService.getSuggestions(keyword);
		
		int score = this.estimationCalculators.get(selectedEstimationType).calculate(keyword, amazonSuggestions);
		
		EstimationResponse response = new EstimationResponse();
		response.setKeyword(keyword);
		response.setScore(score);
		
		long end = System.currentTimeMillis();
		log.info("Calculating estimation for keyword: {} finished in {} ms", keyword, (end - start));
		
		return response;
	}
	
	

	@Override
	public void afterPropertiesSet() throws Exception {
		this.estimationCalculators = new EnumMap<EstimationType, EstimationCalculator>(EstimationType.class);
		this.estimationCalculators.put(EstimationType.SIMPLE, this.simpleEstimationCalculator);
		this.estimationCalculators.put(EstimationType.AGGREGATED, this.aggregatedEstimationCalculator);
	}
}
