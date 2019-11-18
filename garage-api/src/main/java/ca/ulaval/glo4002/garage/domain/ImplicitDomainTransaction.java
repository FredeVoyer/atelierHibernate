package ca.ulaval.glo4002.garage.domain;

import org.glassfish.jersey.internal.util.Producer;

public class ImplicitDomainTransaction implements DomainTransaction {
	@Override
	public <T> T wrapInTransaction(Producer<T> call) {
		return call.call();
	}
}
