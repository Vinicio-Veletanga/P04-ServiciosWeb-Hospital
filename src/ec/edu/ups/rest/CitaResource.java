package ec.edu.ups.rest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ec.edu.ups.ejb.CitaFacade;
import ec.edu.ups.ejb.PersonaFacade;
import ec.edu.ups.model.Cita;
import ec.edu.ups.model.Persona;

@Path("/cita")
public class CitaResource {
	
	@EJB
	private CitaFacade citaFacade;
	
	@EJB
	private PersonaFacade personaFacade;
	
	@POST()
	@Path("/crear")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCitaMedica(@FormParam("comentario") String comentarioCita, @FormParam("costo") Integer costo,
									 @FormParam("estado") String estadoCita,@FormParam("fecha") String fechaCita,@FormParam("sintomas") String sintomatologia,
									 @FormParam("doctor") Integer doctorEspecialidad,@FormParam("paciente") Integer pacientePersona) throws ParseException {
		
		
		Persona doctor = personaFacade.find(doctorEspecialidad);
		Persona paciente = personaFacade.find(pacientePersona);
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(fechaCita);
		
		
		Cita cita = new Cita(comentarioCita, BigDecimal.valueOf(costo), estadoCita, date, sintomatologia, doctor, paciente);
		citaFacade.create(cita);
		return Response.status(Response.Status.CREATED).entity(cita).build();
	}
	
	@PUT()
	@Path("/actualizarEstado")
	@Produces(MediaType.APPLICATION_JSON)
	public Response actualizarcita(@FormParam("estado") String newEstadoCita, @FormParam("id") Integer id) {
		 Cita cita = citaFacade.find(id);
		 	 
		 try {
			 if(cita == null) {
				 return Response.status(Response.Status.NOT_MODIFIED).build();
			 }else {
				 cita.setEstadoCita(newEstadoCita);
				 citaFacade.edit(cita);
				 return Response.ok(cita).build();	
			 }
			 
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(500).build();
		}
	}
	
	@PUT()
	@Path("/cancelarCitaMedica")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelarCitaMedica(@FormParam("id") int id) {
		 Cita cita = citaFacade.find(id);
		 
		 try {
			 if(cita==null) {
				 return Response.status(Response.Status.NOT_MODIFIED).build();
			 }else {
				 cita.setEstadoCita("Cancelar");
				 citaFacade.edit(cita);
				 return Response.ok(cita).build();	
			 }
			 
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(500).build();
		}
		 
	}
	
	@GET
	@Path("/listarEstado")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListasEstado(@QueryParam("estado") String estado) {
		
		try {
			Jsonb jsonb = JsonbBuilder.create();
			List<Cita> citas = citaFacade.getCitasbyEstado(estado);
			
			if (citas == null) {
				return Response.status(Response.Status.NOT_FOUND).entity(404).build();
			}else {
				return Response.ok(jsonb.toJson(citaFacade.getCitasbyEstado(estado))).build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(500).build();
		}
		
	}
	
	@GET
	@Path("/listarFecha")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListasFecha(@QueryParam("fecha") String fecha) {
		
		try {
			Jsonb jsonb = JsonbBuilder.create();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(fecha);
			List<Cita> citas = citaFacade.getCitasbyDate(date);
			
			if (citas == null) {
				return Response.status(Response.Status.NOT_FOUND).entity(404).build();
			}else {
				return Response.ok(jsonb.toJson(citaFacade.getCitasbyDate(date))).build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(500).build();
		}
		
	}
	
	

}
