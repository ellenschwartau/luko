package de.everyjob.service.webapi.messaging;

import de.everyjob.commons.amqp.MessageProperties;
import de.everyjob.commons.amqp.MessageSender;
import de.everyjob.commons.amqp.MessageSenderException;


/**
 * amqp sender for search statistics
 */
public class SearchMessageSender {

	public static final String SEARCH_QUERY_PROP = "search_query";
	private MessageSender sender;
	private String exchange;


	public SearchMessageSender(MessageSender sender, String exchange) {
		this.sender = sender;
		this.exchange = exchange;
	}


	public void send(String queue) throws MessageSenderException {
		MessageProperties props = new MessageProperties();
		props.put(SEARCH_QUERY_PROP, queue);
		sender.send("").properties(props).to(exchange);
	}

}
