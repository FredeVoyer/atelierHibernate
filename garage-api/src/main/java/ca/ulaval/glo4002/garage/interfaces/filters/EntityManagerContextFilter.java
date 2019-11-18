package ca.ulaval.glo4002.garage.interfaces.filters;

import java.io.IOException;

import javax.servlet.*;

public class EntityManagerContextFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) {
		// TODO lab
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO lab
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO lab
	}
}
