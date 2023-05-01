package com.noidate.core.service.rs;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import com.noidate.api.service.RsRepositoryServiceV3;
import com.noidate.core.model.Appointment;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.noidate.core.management.AppConstants.APPOINTMENTS_PATH;

@Path(APPOINTMENTS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class AppointmentServiceRs extends RsRepositoryServiceV3<Appointment, String> {

	public AppointmentServiceRs() {
		super(Appointment.class);
	}

	@Override
	protected String getDefaultOrderBy() {
		return "begin_date desc";
	}

	@Override
	public PanacheQuery<Appointment> getSearch(String orderBy) throws Exception {
		PanacheQuery<Appointment> search;
		Sort sort = sort(orderBy);
		if (sort != null) {
			search = Appointment.find(null, sort);
		} else {
			search = Appointment.find(null);
		}
		return search;
	}
}
