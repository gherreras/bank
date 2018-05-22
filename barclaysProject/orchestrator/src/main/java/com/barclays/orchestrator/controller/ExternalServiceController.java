package com.barclays.orchestrator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.orchestrator.clientservice.RESTClient;
import com.barclays.orchestrator.message.external.PaymentResponseType;
import com.barclays.orchestrator.message.external.PaymentType;
import com.barclays.orchestrator.message.internal.InternalRequestType;
import com.barclays.orchestrator.message.internal.InternalServiceRQType;
import com.barclays.orchestrator.message.internal.InternalServiceRSType;
import com.barclays.orchestrator.message.internal.ProviderType;
import com.barclays.orchestrator.message.internal.RestType;

@RestController
@RequestMapping(value = { "/paymentService" })
public class ExternalServiceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExternalServiceController.class);

	@Autowired
	private RESTClient restClient;

	@Value("${enpoint.routing}")
	private String endpointRouting;

	@Value("${enpoint.dispatcher}")
	private String endpointDispatcher;

	@RequestMapping(value = { "/query/{idFactura}" }, method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<PaymentType> queryService(@PathVariable(required = true) Integer idFactura) {
		LOGGER.info("Recibiendo petición para pago de servicios " + idFactura);
		ResponseEntity<PaymentType> response = null;

		try {

			ProviderType routing = new ProviderType();
			routing.setRest(new RestType());

			routing.getRest().setEndPoint(String.format("%s%s%s%s", endpointRouting, "/", idFactura, "/consulta"));
			routing.getRest().setMethod("GET");

			// Llamar al routing
			LOGGER.info(String.format("Llamando routing %s ", routing.getRest().getEndPoint()));
			ProviderType responseRouting = restClient.callService(routing, null, ProviderType.class);

			// Llamar al dispatcher
			ProviderType dispatcher = new ProviderType();
			dispatcher.setRest(new RestType());
			dispatcher.getRest().setEndPoint(endpointDispatcher);
			dispatcher.getRest().setMethod("POST");

			InternalServiceRQType internalServiceRQ = new InternalServiceRQType();
			internalServiceRQ.setInternalRequest(new InternalRequestType());
			internalServiceRQ.getInternalRequest().setMassageType("request");
			internalServiceRQ.getInternalRequest().setMessage(idFactura.toString());
			internalServiceRQ.getInternalRequest().setOperation("consulta");
			internalServiceRQ.getInternalRequest().setProvider(responseRouting);
			internalServiceRQ.setServiceType("consulta");

			LOGGER.info(String.format("Llamando dispatcher %s ", dispatcher.getRest().getEndPoint()));

			InternalServiceRSType internalServiceRS = restClient.callService(dispatcher, internalServiceRQ,
					InternalServiceRSType.class);
		} catch (Exception e) {
			LOGGER.info("Recibiendo petición para pago de servicios " + idFactura);
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	@RequestMapping(value = { "/payment" }, method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<PaymentResponseType> paymentService(@RequestBody(required = true) PaymentType payment) {

		LOGGER.info("Recibiendo petición para pago de servicios");
		ResponseEntity<PaymentResponseType> response = null;

		return response;
	}

}
