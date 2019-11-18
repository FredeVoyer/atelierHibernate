package ca.ulaval.glo4002.garage;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import ca.ulaval.glo4002.garage.context.GarageContext;
import ca.ulaval.glo4002.garage.context.PersistenceType;
import ca.ulaval.glo4002.garage.interfaces.filters.EntityManagerContextFilter;

public class GarageApiServer implements Runnable {
	private static final int PORT = 8282;
	public static final String GARAGE_PERSISTENCE_PROPERTY_NAME = "garage.persistence";

	public static void main(String[] args) {
		new GarageApiServer().run();
	}

	public void run() {
		Server server = new Server(PORT);
		ServletContextHandler contextHandler = new ServletContextHandler(server, "/");

		PersistenceType persistenceType = getPersistenceTypeOfThrow();

		AbstractBinder rootBinder = getRootBinderFromPersistenceType(persistenceType);
		ResourceConfig resourceConfig = new ResourceConfig()
				.packages("ca.ulaval.glo4002.garage.interfaces")
				.register(rootBinder);

		if (persistenceType == PersistenceType.HIBERNATE) {
			contextHandler.addFilter(EntityManagerContextFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		}

		ServletContainer container = new ServletContainer(resourceConfig);
		ServletHolder servletHolder = new ServletHolder(container);

		contextHandler.addServlet(servletHolder, "/*");

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.destroy();
		}
	}

	private PersistenceType getPersistenceTypeOfThrow() {
		String persistenceProperty = System.getProperty(GARAGE_PERSISTENCE_PROPERTY_NAME);
		if (persistenceProperty == null) {
			throw new RuntimeException("You must specify -D" + GARAGE_PERSISTENCE_PROPERTY_NAME + "=memory or hibernate in the VM options");
		}

		return PersistenceType.valueOf(persistenceProperty.toUpperCase());
	}

	private AbstractBinder getRootBinderFromPersistenceType(PersistenceType persistenceType) {
		return new AbstractBinder() {
			@Override
			protected void configure() {
				install(new GarageContext(persistenceType));
			}
		};
	}
}
