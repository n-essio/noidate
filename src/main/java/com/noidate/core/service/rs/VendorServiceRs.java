package com.noidate.core.service.rs;

import com.noidate.api.service.RsRepositoryServiceV4;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import com.noidate.api.service.RsRepositoryServiceV3;
import com.noidate.core.model.Vendor;

import jakarta.inject.Singleton;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.noidate.core.management.AppConstants.VENDORS_PATH;

@Path(VENDORS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class VendorServiceRs extends RsRepositoryServiceV4<Vendor, String> {

    public VendorServiceRs() {
        super(Vendor.class);
    }

    @Override
    protected String getDefaultOrderBy() {
        return "surname asc";
    }

    @Override
    public Predicate[] query(CriteriaBuilder criteriaBuilder, Root<Vendor> root) throws Exception {
        return new Predicate[0];
    }
}
