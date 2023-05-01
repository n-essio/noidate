package com.noidate.core.service.rs;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import com.noidate.api.service.RsRepositoryServiceV3;
import com.noidate.core.model.Vendor;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.noidate.core.management.AppConstants.VENDORS_PATH;

@Path(VENDORS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class VendorServiceRs extends RsRepositoryServiceV3<Vendor, String> {

    public VendorServiceRs() {
        super(Vendor.class);
    }

    @Override
    protected String getDefaultOrderBy() {
        return "surname asc";
    }

    @Override
    public PanacheQuery<Vendor> getSearch(String orderBy) throws Exception {
        PanacheQuery<Vendor> search;
        Sort sort = sort(orderBy);
        if (sort != null) {
            search = Vendor.find(null, sort);
        } else {
            search = Vendor.find(null);
        }
        return search;
    }
}
