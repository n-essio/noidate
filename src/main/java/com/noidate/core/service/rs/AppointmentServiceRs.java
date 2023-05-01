package com.noidate.core.service.rs;

import com.noidate.api.service.RsRepositoryServiceV4;
import com.noidate.core.model.Appointment;
import jakarta.inject.Singleton;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.noidate.core.management.AppConstants.APPOINTMENTS_PATH;

@Path(APPOINTMENTS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class AppointmentServiceRs extends RsRepositoryServiceV4<Appointment, String> {

	public AppointmentServiceRs() {
		super(Appointment.class);
	}

	@Override
	protected String getDefaultOrderBy() {
		return "begin_date desc";
	}

	@Override
	public Predicate[] query(CriteriaBuilder criteriaBuilder, Root<Appointment> root) throws Exception {
		return new Predicate[0];
	}

}
