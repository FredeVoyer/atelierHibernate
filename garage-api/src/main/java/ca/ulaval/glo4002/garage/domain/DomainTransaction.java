package ca.ulaval.glo4002.garage.domain;

import org.glassfish.jersey.internal.util.Producer;

public interface DomainTransaction {
	<T> T wrapInTransaction(Producer<T> call);
}
