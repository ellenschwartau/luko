package de.everyjob.mini.microservices.storage;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;

import de.everyjob.mini.microservices.data.SearchStatistic;


/**
 * Object to access saved search query statistics.
 */
public class SearchStatisticDAO extends AbstractDAO<SearchStatistic> {

	public SearchStatisticDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}


	/**
	 * Inserts a new search statistic entry or updates the counter for the existing one.
	 *
	 * @param query search query
	 */
	public void incOrInsert(String query) {
		SearchStatistic entity = find(query);
		if (entity != null) {
			entity.incCount();
		}
		else {
			entity = new SearchStatistic(query, 1);
		}
		persist(entity);
	}


	/**
	 * @param query seach query
	 * @return statistical data for the query
	 */
	public SearchStatistic find(String query) {
		Criteria criteria = criteria()
			.add(Restrictions.eq(SearchStatistic.COL_STATISTIC_SEARCH_QUERY, query));
		return uniqueResult(criteria);
	}


	/**
	 * @return list of all search statistic entries
	 */
	public List<SearchStatistic> getAll() {
		return list(criteria());
	}
}
