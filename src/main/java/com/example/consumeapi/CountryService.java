package com.example.consumeapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CountryService {

	public List<Country> listCountry(){
		List<Country> countries = new ArrayList<>();
		String uri = "http://localhost:9000/rest/v2/all";
		RestTemplate restTemplate = new RestTemplate();
		Country[] result = restTemplate.getForObject(uri,Country[].class);
		for(Country itr : result) {
			countries.add(itr);
		}
		
		return countries;
	}
}
