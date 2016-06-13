package de.everyjob.service.searcher.core;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableSet;


/**
 * definition of search facets.
 */
public enum SearchFacetType {
	WORKINGTIMES("workingTimes", "jobitems.workingTimes"),
	INDUSTRIES("industries", "jobitems.industries"),
	EMPLOYMENTTYPES("employmentTypes", "jobitems.employmentTypes"),
	COMPANIES("companies", "jobitems.company.raw"),
	COMPANYTYPE("companyType", "jobitems.tags"),
	JOBSPERCITY("jobsPerCity", "jobitems.locations.city.raw");

	private static final ImmutableSet<SearchFacetType> TAGS = ImmutableSet.of(COMPANYTYPE);

	private String facetName;
	private String field;


	private SearchFacetType(String facetName, String field) {
		this.facetName = facetName;
		this.field = field;
	}


	@JsonValue
	public String getFacetName() {
		return facetName;
	}


	public boolean isTag() {
		return TAGS.contains(this);
	}


	public String getField() {
		return field;
	}
}
