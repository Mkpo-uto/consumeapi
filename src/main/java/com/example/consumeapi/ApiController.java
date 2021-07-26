package com.example.consumeapi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import jdk.nashorn.internal.runtime.Context;

@Component
@RestController
//@EnableFeignClients(basePackageClasses = apiServiceProxy.class)
public class ApiController implements apiService {

	
	//private apiServiceProxy proxy;
	
	
	
	//@Autowired
	//private ThreadPoolTaskScheduler taskScheduler;
	
		

	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private CountryService countryService;
	
//	public ApiController(apiServiceProxy proxy, CountryService countryService) {
	public ApiController( CountryService countryService) {
		super();
		//this.proxy = proxy;
		this.countryService = countryService;
		
	}

	@RequestMapping(value = "/simpleemail", method = RequestMethod.GET)
	@ResponseBody
	String home() {
		try {
			sendEmail();
			return "Email Sent!";
		}catch(Exception ex) {
			return "Error in sending email: " + ex;
		}
	}
	
	//@Scheduled(cron = "0 0 8,12,16 * * *")
	@Scheduled(cron = "30 07,08,09 06 * * * ")
	private String sendEmail() throws Exception{
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("edmkpouto@gmail.com");
			helper.setTo("mkpoutoe@hotmail.com");
			helper.setSubject("Test email for Calibre Financial Technology Junior Java Developer");
			helper.setText("Testing attach CSV file functionality for Calibre Financial Technology Junior Java Developer");
			DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd_HHmm");
			String currentDateTime = dateFormatter.format(new Date());
			
			FileSystemResource file = new FileSystemResource("C:\\Users\\mkpou\\Downloads\\obsval_" + currentDateTime + ".csv");
			helper.addAttachment(file.getFilename(), file);
		}catch(MessagingException e) {
			throw new MailParseException(e);
		}
		sender.send(message);
		return "Email sent!";
//		SimpleMailMessage mail = new SimpleMailMessage();
//		mail.setFrom("edmkpouto@gmail.com");
//		mail.setTo("edmkpouto@gmail.com");
//		mail.setSubject("Test email for Calibre Final Technology Junior Java Developer");
//		mail.setText("Testing send email functionality for Calibre Final Technology Junior Java Developer");
//		sender.send(mail);
	}
	
	
	//CronTrigger cronTrigger = new CronTrigger("0 43,44,45 15 * * *");
	//public void generateCSV(HttpServletResponse response) {
	//	taskScheduler.schedule(new ExportCSV(response), cronTrigger);
	//}
	
	
	
	//@Scheduled(cron = "0 54,55,56 13 * * * ")
	@GetMapping("/export-country")
	public void exportCSV(HttpServletResponse response) throws Exception {
		 
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd_HHmm");
		String currentDateTime = dateFormatter.format(new Date());
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=obsval_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);
		
		List<Country> listCountries = countryService.listCountry();
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"Name","Alpha2Code","Alpha3Code","Capital","Region","Subregion","Population", "Denonym"};
		String[] nameMapping = {"name","alpha2Code","alpha3Code","capital","region","subregion","population","demonym"};
		
		csvWriter.writeHeader(csvHeader);
		for(Country country : listCountries) {
			csvWriter.write(country, nameMapping);
		}
		
		csvWriter.close();
		
		//return "obsval_" + currentDateTime + ".csv downloaded";
		
//		StatefulBeanToCsv<Country> writer = new StatefulBeanToCsvBuilder<Country>(response.getWriter())
//				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
//				.withSeparator(CSVWriter.DEFAULT_SEPARATOR)
//				.withOrderedResults(false)
//				.build();
//		
//		writer.write(countryService.listCountry());
	}
	
	
//	@Override
//	@GetMapping("/rest/v2/all")
//	public List<Object> getCountries() {
//		
//		return proxy.getCountries();
//	}
//
//	@Override
//	@GetMapping("/products")
//	public List<Object> getProducts() {
//		
//		return proxy.getProducts();
//	}
//	
//	@Override
//	@GetMapping("/rest/v2/currency/{currency}")
//	public List<Object> getCurrency(@PathVariable String currency){
//		return proxy.getCurrency(currency);
//	}

	
	
	

//	@Autowired
//	private RestTemplate restTemplate;
//	
//	private static String url = "https://restcountries.eu/rest/v2/all";
//	
//	
//	@GetMapping("/countries")
//	public List<Object> getCountries(){
//		Object[] countries = restTemplate.getForObject(url,Object[].class);
//	    return Arrays.asList(countries);
//	}
}
