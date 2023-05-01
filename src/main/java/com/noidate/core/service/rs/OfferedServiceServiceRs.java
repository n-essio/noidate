package com.noidate.core.service.rs;

import com.noidate.api.service.RsRepositoryServiceV4;
import com.noidate.core.model.OfferedService;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Singleton;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.noidate.core.management.AppConstants.OFFERED_SERVICES_PATH;

@Path(OFFERED_SERVICES_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class OfferedServiceServiceRs extends RsRepositoryServiceV4<OfferedService, String> {

    public OfferedServiceServiceRs() {
        super(OfferedService.class);
    }

    @Override
    protected String getDefaultOrderBy() {
        return "name asc";
    }

    @Override
    public Predicate[] query(CriteriaBuilder criteriaBuilder, Root<OfferedService> root) throws Exception {
        return new Predicate[0];
    }

}
