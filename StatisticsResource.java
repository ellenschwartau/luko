package de.everyjob.mini.microservices.resources;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import de.everyjob.mini.microservices.data.Statistic;
import de.everyjob.mini.microservices.caching.StatisticsCache;


/**
 * Jersey resources are the meat-and-potatoes of a Dropwizard application. Each resource class is associated with a URI
 * template.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=utf-8")
public class StatisticsResource {

	/**
	 * Display string for Base Url
	 */
	public static final String DISPLAY_IS_ALIVE = "Statistics Provider is alive!";
	public static final String CACHE_INVALIDATED = "Cache invalidated.";
	/**
	 * Counts the connections
	 */
	private final AtomicLong counter;
	/**
	 * Cache to access statistics
	 */
	private final StatisticsCache statisticsCache;


	/**
	 * Constructor
	 *
	 * @param statisticsCache service for working with the statistical data
	 */
	public StatisticsResource(StatisticsCache statisticsCache) {
		counter = new AtomicLong();
		this.statisticsCache = statisticsCache;
	}


	/**
	 * @return Data-Object to be displayed
	 */
	@GET
	@Timed
	public StatusData display() {
		return new StatusData(counter.incrementAndGet(), DISPLAY_IS_ALIVE);
	}


	/**
	 * Example request: http://localhost:30210/statistics?keys=jobs&keys=search
	 *
	 * @return list of statistical data for search
	 */
	@GET
	@Path("statistics")
	@Timed
	public Map<String, List<? extends Statistic>> getStatistics(@QueryParam("keys") List<String> keys) {
		return statisticsCache.getStatistics(keys);
	}


	/**
	 * @return list of statistical data for search
	 */
	@POST
	@Path("invalidate")
	@Timed
	public StatusData invalidateCache() {
		statisticsCache.invalidate();
		return new StatusData(counter.incrementAndGet(), CACHE_INVALIDATED);
	}

}
