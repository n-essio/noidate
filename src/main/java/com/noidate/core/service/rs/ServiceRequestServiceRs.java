package com.noidate.core.service.rs;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import com.noidate.api.service.RsRepositoryServiceV3;
import com.noidate.core.model.ServiceRequest;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.noidate.core.management.AppConstants.SERVICE_REQUESTS_PATH;

@Path(SERVICE_REQUESTS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class ServiceRequestServiceRs extends RsRepositoryServiceV3<ServiceRequest, String> {

	public ServiceRequestServiceRs() {
		super(ServiceRequest.class);
	}

	@Override
	protected String getDefaultOrderBy() {
		return "begin_date desc";
	}

	@Override
	public PanacheQuery<ServiceRequest> getSearch(String orderBy) throws Exception {
		PanacheQuery<ServiceRequest> search;
		Sort sort = sort(orderBy);
		if (sort != null) {
			search = ServiceRequest.find(null, sort);
		} else {
			search = ServiceRequest.find(null);
		}
		return search;
	}
}
