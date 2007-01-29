/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.ConnectionManager;
import edu.uoregon.cs.p2presenter.DefaultRequestHandlerMapping;
import edu.uoregon.cs.p2presenter.authentication.LoginRequestHandler;
import edu.uoregon.cs.p2presenter.authentication.LogoutRequestHandler;
import edu.uoregon.cs.p2presenter.authentication.SecurityContextIntegrationFilter;
import edu.uoregon.cs.p2presenter.philosopher.Philosopher;
import edu.uoregon.cs.p2presenter.philosopher.SimplePhilosopherInterface;
import edu.uoregon.cs.p2presenter.server.P2PresenterServerPortListener;
import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;

public class DemoInteractivityServer {
	public static void main(String[] args) throws Exception {
		final ActiveInteractivityController activeInteractivityController = new ActiveInteractivityController();
		
		ConnectionManager server = new ConnectionManager() {
			private int connectionCount = 0;
			@Override
			protected synchronized void connectionCreatedInternal(Connection connection) {
				if (connectionCount == 0) {
					// use dao
					activeInteractivityController.addActiveInteractivity(0, new ActiveInteractivity<Philosopher>(connection, new InteractivityDefinition(0, SimplePhilosopherInterface.class.getName(), Philosopher.class.getName())));
				}
				connectionCount++;
			}
		
		};
		DefaultRequestHandlerMapping requestHandlerMapping = server.getRequestHandlerMapping();
		
		requestHandlerMapping.mapFilter(DefaultRequestHandlerMapping.DEFAULT_URL, new SecurityContextIntegrationFilter());
		requestHandlerMapping.mapHandler("/login", new LoginRequestHandler());
		requestHandlerMapping.mapHandler("/logout", new LogoutRequestHandler());
		
		JoinInteractivityRequestHandler joinInteractivityController = new JoinInteractivityRequestHandler(activeInteractivityController);
		ProxyInteractivityRequestHandler controllerProxy = new ProxyInteractivityRequestHandler(activeInteractivityController);
		requestHandlerMapping.mapHandler(new InteractivityRequestMatcher("controller"), controllerProxy);
		requestHandlerMapping.mapHandler(new InteractivityRequestMatcher("join"), joinInteractivityController);
		
		P2PresenterServerPortListener portListener = new P2PresenterServerPortListener(9000, server);
		portListener.run();
	}
}
