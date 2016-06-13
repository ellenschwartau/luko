package de.everyjob.mini.microservices.request;

import static de.everyjob.mini.microservices.data.JobStatisticsQuery.createJobStatisticsQuery;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.concurrent.ExecutionException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;

import de.everyjob.dropwizard.commons.exceptions.RequestForwardingException;
import de.everyjob.dropwizard.commons.util.RequestForwardingUtil;
import de.everyjob.mini.microservices.data.JobStatisticsResult;


/**
 * Requests the job statistics and transforms the result.
 */
public class RequestStatisticsService {

	/** configuration */
	private String endpoint;
	/** Client to execute requests */
	private Client client;


	/**
	 * @param endpoint search endpoint
	 */
	public RequestStatisticsService(Client client, String endpoint) {
		this.endpoint = endpoint;
		this.client = client;
	}


	/**
	 * curl -i -XPOST -H "Content-Type: application/json" localhost:31134/statistic -d '{"facetTypes":["jobsPerCity"]}'
	 *
	 * @return statistical data for jobs
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public JobStatisticsResult getJobStatistics() throws ExecutionException, InterruptedException, RequestForwardingException {
		return RequestForwardingUtil.performAndPassOn(() -> client.target(endpoint)
			.request(APPLICATION_JSON)
			.post(Entity.entity(createJobStatisticsQuery(), APPLICATION_JSON)), JobStatisticsResult.class);
	}

}
