package de.everyjob.mini.microservices.caching;

import static com.google.common.cache.CacheBuilder.*;
import static java.util.concurrent.TimeUnit.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.everyjob.mini.microservices.data.Statistic;
import de.galan.commons.logging.Say;


/**
 * Object to access and cache statistics.
 */
public class StatisticsCache {

	/**
	 * cache
	 */
	private LoadingCache<String, List<? extends Statistic>> cache;
	/**
	 * number of saved statistics
	 */
	private static final int SAVED_STATISTICS = 2;
	/**
	 * unit for the refresh interval
	 */
	private static final TimeUnit REFRESH_UNIT = HOURS;
	/**
	 * service for requesting statistical data
	 */
	private StatisticsService statisticsService;
	/**
	 * requests the statistical data or reads them out of the cache
	 */
	private CacheLoader<String, List<? extends Statistic>> loader = new CacheLoader<String, List<? extends Statistic>>() {

		@Override
		public List<? extends Statistic> load(String key) throws Exception {
			return statisticsService.get(key);
		}
	};


	/**
	 * Initialized cache and service
	 *
	 * @param statisticsService service for requesting statistical data
	 */
	public StatisticsCache(StatisticsService statisticsService, Integer refreshInterval) {
		this.statisticsService = statisticsService;
		cache = newBuilder()
			.maximumSize(SAVED_STATISTICS)
			.refreshAfterWrite(refreshInterval, REFRESH_UNIT)
			.build(loader);
	}


	/**
	 * @param keys specifies which statistics should be loaded
	 * @return list of statistics
	 */
	public Map<String, List<? extends Statistic>> getStatistics(List<String> keys) {
		Map<String, List<? extends Statistic>> res = new HashMap<>();
		keys.forEach(key -> {
			try {
				res.put(key, cache.get(key));
			}
			catch (ExecutionException ex) {
				Say.error("Could not load statistics with key {} from cache", ex, key);
			}
		});
		return res;
	}


	/**
	 * Invalidates the cache, so that the next request reloads the data.
	 */
	public void invalidate() {
		cache.invalidateAll();
	}

}
