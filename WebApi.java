package de.everyjob.service.webapi.application;

import javax.ws.rs.client.Client;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import de.everyjob.dropwizard.amqp.bundle.AmqpBundle;
import de.everyjob.dropwizard.commons.application.BaseDropwizardApplication;
import de.everyjob.dropwizard.commons.util.BootstrapUtil;
import de.everyjob.service.webapi.messaging.SearchMessageSender;
import de.everyjob.service.webapi.resources.JobalertApiResource;
import de.everyjob.service.webapi.resources.SearchApiResource;
import de.everyjob.service.webapi.resources.SitemapResource;
import de.everyjob.service.webapi.resources.StatisticApiResource;
import de.everyjob.service.webapi.resources.TagResource;
import de.everyjob.service.webapi.search.SearchService;


/**
 * web-api application
 */
public class DropwizardApplication extends BaseDropwizardApplication<WebApiConfiguration> {

	private AmqpBundle<WebApiConfiguration> amqpBundle;


	public static void main(String[] args) throws Exception {
		new DropwizardApplication().run(args);
	}


	@Override
	public String getName() {
		return "web-api";
	}


	@Override
	public void initialize(Bootstrap<WebApiConfiguration> bootstrap) {
		BootstrapUtil.initialize(bootstrap);
		amqpBundle = new AmqpBundle<>(conf -> conf.getAmqpConnectionDefinition());
		bootstrap.addBundle(amqpBundle);
	}


	@Override
	public void run(WebApiConfiguration configuration, Environment environment) throws Exception {
		super.run(configuration, environment);
		final Client client = newClient(configuration.getJerseyClientConfiguration(), environment);

		SearchService searchService = new SearchService(client, configuration.getSearchEndpoint());
		environment.jersey().register(
			new JobalertApiResource(configuration.getJobAlertEndpoint(), searchService, client));
		environment.jersey().register(new StatisticApiResource(configuration.getStatisticEndpoint(), searchService,
			client));
		SearchMessageSender searchMessageSender = null;
		if (configuration.getStatisticsExchangeEnabled()) {
			searchMessageSender = new SearchMessageSender(
				amqpBundle.getMessagingFactory().createSender(),
				configuration.getStatisticsExchange());
		}
		environment.jersey().register(new SearchApiResource(searchService, searchMessageSender));
		environment.jersey().register(new TagResource());
		environment.jersey().register(new SitemapResource());
	}

}
