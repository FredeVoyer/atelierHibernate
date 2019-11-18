package ca.ulaval.glo4002.garage.infrastructure.persistence.hibernate;

import org.glassfish.jersey.internal.util.Producer;

import ca.ulaval.glo4002.garage.domain.DomainTransaction;

public class HibernateDomainTransaction implements DomainTransaction {
	@Override
	public <T> T wrapInTransaction(Producer<T> call) {
		// TODO lab create/commit/rollback transaction
		return null;
	}
}
