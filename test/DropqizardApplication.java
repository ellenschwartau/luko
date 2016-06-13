package de.everyjob.mini.microservices.application;

import javax.ws.rs.client.Client;

import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import de.everyjob.commons.amqp.MessagingFactory;
import de.everyjob.dropwizard.amqp.bundle.AmqpBundle;
import de.everyjob.dropwizard.commons.application.BaseDropwizardApplication;
import de.everyjob.dropwizard.commons.util.BootstrapUtil;
import de.everyjob.dropwizard.hibernate.bundle.DatabaseInitializationBundle;
import de.everyjob.dropwizard.hibernate.bundle.HibernateCoreBundle;
import de.everyjob.mini.microservices.data.JobStatistic;
import de.everyjob.mini.microservices.data.SearchStatistic;
import de.everyjob.mini.microservices.request.RequestStatisticsService;
import de.everyjob.mini.microservices.caching.StatisticsCache;
import de.everyjob.mini.microservices.messaging.StatisticMessageListener;
import de.everyjob.mini.microservices.resources.StatisticsResource;
import de.everyjob.mini.microservices.caching.StatisticsService;
import de.everyjob.mini.microservices.storage.SearchStatisticDAO;
import de.galan.commons.logging.Say;
import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;


/**
 * Application to provide statistic information. This appllication class is the entry point for providing the statistic
 * information. It gets the configuration file as parameter and registers resources and health checks.
 */
public class DropwizardApplication extends BaseDropwizardApplication<StatisticsProviderConfiguration> {

	private AmqpBundle<StatisticsProviderConfiguration> amqpBundle;
	private HibernateBundle<StatisticsProviderConfiguration> hibernateBundle;


	/**
	 * Applications entry point
	 *
	 * @param args program arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new DropwizardApplication().run(args);
	}


	@Override
	public String getName() {
		return "statistics-provider";
	}


	@Override
	public void initialize(Bootstrap<StatisticsProviderConfiguration> bootstrap) {
		// initialize prometheus monitoring
		BootstrapUtil.initialize(bootstrap);
		// allow env variables in config
		bootstrap.addBundle(new TemplateConfigBundle());
		// initialize the startup-environment
		// Messaging
		amqpBundle = new AmqpBundle<>(conf -> conf.getAmqpConnectionDefinition());
		bootstrap.addBundle(amqpBundle);
		// Initializes the database and schema, must be added prior the hibernateBundle
		bootstrap.addBundle(new DatabaseInitializationBundle<>());
		// add entities to enabled database interaction
		hibernateBundle = new HibernateCoreBundle<>(JobStatistic.class, SearchStatistic.class);
		bootstrap.addBundle(hibernateBundle);
	}


	@Override
	public void run(StatisticsProviderConfiguration configuration, Environment environment) throws Exception {
		// create Jersey Client
		super.run(configuration, environment);
		final Client client = newClient(configuration.getJerseyClientConfiguration(), environment);

		// Provide Services and DAOs
		SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
		SearchStatisticDAO searchStatisticDAO = new SearchStatisticDAO(sessionFactory);
		RequestStatisticsService requestStatisticsService = new RequestStatisticsService(client, configuration.getSearchEndpoint());
		StatisticsService statisticsService = new UnitOfWorkAwareProxyFactory(hibernateBundle).create(
			StatisticsService.class,
			new Class<?>[] {RequestStatisticsService.class, SearchStatisticDAO.class},
			new Object[] {requestStatisticsService, searchStatisticDAO});
		StatisticsCache statisticsCache = new StatisticsCache(statisticsService, configuration.getRefreshCacheInterval());
		// Listen for messages
		listen(configuration, amqpBundle.getMessagingFactory(), statisticsService);

		// register the StatisticsResource
		final StatisticsResource resource = new StatisticsResource(statisticsCache);
		environment.jersey().register(resource);
	}


	private void listen(StatisticsProviderConfiguration configuration, MessagingFactory messagingFactory, StatisticsService statisticsService) {
		StatisticMessageListener listener = new StatisticMessageListener(statisticsService);
		messagingFactory.createConsumer(configuration.getStatisticsProviderQueue()).retries(5L, "2m").listen(listener);
		Say.info("Listening on queue {queueName} for incoming statistic-messages.", configuration.getStatisticsProviderQueue());
	}
}
