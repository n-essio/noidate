package com.noidate.core.service.rs;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import com.noidate.api.service.RsRepositoryServiceV3;
import com.noidate.core.model.PriceList;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.noidate.core.management.AppConstants.PRICELIST_PATH;

@Path(PRICELIST_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class PriceListServiceRs extends RsRepositoryServiceV3<PriceList, String> {

    public PriceListServiceRs() {
        super(PriceList.class);
    }

    @Override
    protected String getDefaultOrderBy() {
        return "name asc";
    }

    @Override
    public PanacheQuery<PriceList> getSearch(String orderBy) throws Exception {
        PanacheQuery<PriceList> search;
        Sort sort = sort(orderBy);
        if (sort != null) {
            search = PriceList.find(null, sort);
        } else {
            search = PriceList.find(null);
        }
        return search;
    }
}
