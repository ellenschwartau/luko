package de.everyjob.service.webapi.resources;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.google.common.annotations.VisibleForTesting;

import de.everyjob.commons.amqp.MessageSenderException;
import de.everyjob.dropwizard.commons.exceptions.ProcessingException;
import de.everyjob.dropwizard.commons.exceptions.RequestForwardingException;
import de.everyjob.service.webapi.core.SearchQuery;
import de.everyjob.service.webapi.core.SearchResult;
import de.everyjob.service.webapi.messaging.SearchMessageSender;
import de.everyjob.service.webapi.search.SearchService;
import de.galan.commons.logging.Say;


/**
 * search resource
 */
@Path("/api/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchApiResource {

	private SearchService searchService;
	SearchMessageSender messageSender;


	public SearchApiResource(SearchService searchService, SearchMessageSender messageSender) {
		this.searchService = searchService;
		this.messageSender = messageSender;
	}


	@VisibleForTesting
	protected void sendMessage(@NotNull SearchQuery query) {
		try {
			messageSender.send(query.getTitle());
		}
		catch (MessageSenderException e) {
			Say.error("Unable to send search-query-message with query {}", query.toString());
		}
	}


	@POST
	@Timed
	public SearchResult search(@NotNull SearchQuery query) throws RequestForwardingException, ProcessingException {
		searchService.preprocessQuery(query);
		if (messageSender != null) {
			sendMessage(query);
		}
		return searchService.search(query);
	}

}
