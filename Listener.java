package de.everyjob.mini.microservices.messaging;

import de.everyjob.commons.amqp.MessageListener;
import de.everyjob.commons.amqp.MessageProperties;
import de.everyjob.dropwizard.commons.exceptions.ProcessingException;
import de.everyjob.mini.microservices.caching.StatisticsService;
import de.galan.commons.logging.Say;


/**
 * Listens for messages sent via RabbitMQ and saves the statistic in the database
 */
public class StatisticMessageListener implements MessageListener {

	public static final String SEARCH_QUERY_KEY = "search_query";
	private final StatisticsService statisticsService;


	public StatisticMessageListener(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}


	@Override
	public void onMessage(MessageProperties messageProperties, String s) {
		Say.debug("Received search-statistics-message");
		String searchQuery = (String)messageProperties.getHeaders().get(SEARCH_QUERY_KEY);
		try {
			statisticsService.insert(searchQuery);
		}
		catch (ProcessingException ex) {
			Say.error("Search-statistics-message with query {} could not be saved.", ex, searchQuery);
		}
	}
}
