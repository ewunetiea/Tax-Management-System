package com.afr.fms.Common.Recommendation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Payload.response.AGPResponse;


@RestController
@RequestMapping("/api/common/recommendation")

public class RecommendationController {
	@Autowired
	private RecommendationService recommendationService;


	@GetMapping("/recommendation/{user_id}")
	public ResponseEntity<List<Recommendation>> getRecommendation(@PathVariable Long user_id,
			HttpServletRequest request) {

			try {
				return new ResponseEntity<>(recommendationService.getRecommendation(user_id), HttpStatus.OK);
			} catch (Exception ex) {

				System.out.println(ex);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		

	}

	@GetMapping("/recommendations/{identifier}")
	public ResponseEntity<List<Recommendation>> getRecommendations(HttpServletRequest request, @PathVariable String identifier) {

			try {
				return new ResponseEntity<>(recommendationService.getRecommendations(identifier), HttpStatus.OK);
			} catch (Exception ex) {

				System.out.println(ex);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteRecommendation(@PathVariable Long id,
			HttpServletRequest request) {

		try {
			recommendationService.deleteRecommendation(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {

			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
			

	}

	@PostMapping(path = "/create")
	public ResponseEntity<AGPResponse> createRecommendation(HttpServletRequest request,
			@RequestBody Recommendation recommendation) {

		try {
			if (recommendation.getId() != null) {

				recommendationService.updateRecommendation(recommendation);
				return AGPResponse.success("SUCCESS");
			} else {

				recommendationService.createRecommendation(recommendation);
				return AGPResponse.success("SUCCESS");
			}

		} catch (Exception ex) {
			System.out.println(ex);

			return AGPResponse.error("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}

			
	}

	
	@PostMapping("/delete/recommendations")
	public ResponseEntity<HttpStatus> deleteRecommendations(@RequestBody List<Recommendation> recommendations,
			HttpServletRequest request) {
	
		try {
			for (Recommendation recommendation : recommendations) {
				recommendationService.deleteRecommendation(recommendation.getId());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}

}
