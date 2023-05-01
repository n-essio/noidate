package com.noidate.core.service.rs;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import com.noidate.api.service.RsRepositoryServiceV3;
import com.noidate.core.model.ServiceCategory;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.noidate.core.management.AppConstants.SERVICE_CATEGORIES_PATH;

@Path(SERVICE_CATEGORIES_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class ServiceCategoryServiceRs extends RsRepositoryServiceV3<ServiceCategory, String> {

	public ServiceCategoryServiceRs() {
		super(ServiceCategory.class);
	}

	@Override
	protected String getDefaultOrderBy() {
		return "name asc";
	}

	@Override
	public PanacheQuery<ServiceCategory> getSearch(String orderBy) throws Exception {
		PanacheQuery<ServiceCategory> search;
		Sort sort = sort(orderBy);
		if (sort != null) {
			search = ServiceCategory.find(null, sort);
		} else {
			search = ServiceCategory.find(null);
		}
		return search;
	}
}
