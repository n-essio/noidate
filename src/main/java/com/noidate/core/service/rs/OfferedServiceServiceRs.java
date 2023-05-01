package com.noidate.core.service.rs;

import com.noidate.api.service.RsRepositoryServiceV3;
import com.noidate.core.model.OfferedService;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.noidate.core.management.AppConstants.OFFERED_SERVICES_PATH;

@Path(OFFERED_SERVICES_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class OfferedServiceServiceRs extends RsRepositoryServiceV3<OfferedService, String> {

	public OfferedServiceServiceRs() {
		super(OfferedService.class);
	}

	@Override
	protected String getDefaultOrderBy() {
		return "name asc";
	}

	@Override
	public PanacheQuery<OfferedService> getSearch(String orderBy) throws Exception {
		PanacheQuery<OfferedService> search;
		Sort sort = sort(orderBy);
		if (sort != null) {
			search = OfferedService.find(null, sort);
		} else {
			search = OfferedService.find(null);
		}
		return search;
	}
}
