package com.baeldung.endpoint;

import com.baeldung.repository.CountryRepository;
import com.baeldung.springsoap.gen.GetCountryRequest;
import com.baeldung.springsoap.gen.GetCountryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

@Endpoint
public class CountryEndpoint {

	private static final Logger log = LoggerFactory.getLogger(CountryEndpoint.class);
	private static final String NAMESPACE_URI = "http://www.baeldung.com/springsoap/gen";

	private CountryRepository countryRepository;

	@Autowired
	public CountryEndpoint(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	@ResponsePayload
	@SoapAction(value = "http://www.baeldung.com/springsoap/gen/getCountryRequest")
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
	public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request, MessageContext messageContext) {
		WebServiceMessage webServiceMessage = messageContext.getRequest();
		SoapMessage soapMessage = (SoapMessage) webServiceMessage;
		log.info("SOAPAction: '{}'", soapMessage.getSoapAction());

		GetCountryResponse response = new GetCountryResponse();
		response.setCountry(countryRepository.findCountry(request.getName()));
		return response;
	}
}
