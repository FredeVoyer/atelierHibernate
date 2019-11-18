package ca.ulaval.glo4002.garage.context;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import ca.ulaval.glo4002.garage.context.application.ApplicationContext;
import ca.ulaval.glo4002.garage.context.persistence.HibernatePersistenceContext;
import ca.ulaval.glo4002.garage.context.persistence.InMemoryPersistenceContext;

public class GarageContext extends AbstractBinder {
	private final PersistenceType persistenceType;

	public GarageContext(PersistenceType persistenceType) {
		this.persistenceType = persistenceType;
	}

	@Override
	protected void configure() {
		install(new ApplicationContext());
		switch (persistenceType) {
			case HIBERNATE:
				System.out.println("Using HIBERNATE for persistence");
				install(new HibernatePersistenceContext());
				break;
			case MEMORY:
				System.out.println("Using IN-MEMORY for persistence");
				install(new InMemoryPersistenceContext());
				break;
		}
	}
}
