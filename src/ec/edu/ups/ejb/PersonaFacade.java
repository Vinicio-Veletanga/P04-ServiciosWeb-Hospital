package ec.edu.ups.ejb;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ec.edu.ups.model.Persona;

@Stateless
public class PersonaFacade extends AbstractFacade<Persona> {
	
	@PersistenceContext(unitName = "HospitalServices")
	private EntityManager entityManager;

	public PersonaFacade() {
		super(Persona.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return entityManager;
	}
	
	public Persona getPersona(String correo, String password) {
		String query = "SELECT p FROM Persona p WHERE p.correo = :correo AND p.password = :password";
		Persona persona = null;
		
		try {
			persona = (Persona) entityManager.createQuery(query)
											 .setParameter("correo", correo)
											 .setParameter("password", password)
											 .getSingleResult();
			
		} catch (Exception e) {
			System.out.println("--> ERROR Persona.getPersona" + e.getMessage());
		}
		return persona;
	}
	

	public List<Persona> getPersonabyEspecialidad(String especialidad) {
		String query = "SELECT * \n"
				+ "From public.\"Persona\" pe, public.\"Doctor_Especialidad\" doce, public.\"Especialidad\" e\n"
				+ "WHERE doce.id_doctor = pe.id_persona and doce.id_especialidad = e.id_especialidad\n"
				+ "and e.nombre_especialidad='"+ especialidad +"'";
		List<Persona> personas = new ArrayList<Persona>();
	
		try {
			personas = entityManager.createNativeQuery(query).getResultList();
			System.out.println("Personas------->:" + personas);
			
		} catch (Exception e) {
			System.out.println("--> ERROR Persona.getPersonabyEspecialidad" + e.getMessage());
		}
				
		return personas;
	}
		
	

	public List<Persona> getPersonabyCedula(String cedula) {
		String query = "SELECT *\r\n" + 
				"FROM public.\"Persona\" where cedula ='"+cedula+"';";
		List<Persona> personas = new ArrayList<Persona>();
	
		try {
			personas = entityManager.createNativeQuery(query).getResultList();
			System.out.println("Personas por cedula------->:" + personas);
			
		} catch (Exception e) {
			System.out.println("--> ERROR Persona.getPersonabyEspecialidad" + e.getMessage());
		}
				
		return personas;
	}
	
	public List<Persona> getPersonaE(String cedula,String estado) {
		String query = "" + 
				"UPDATE public.\"Persona\"\r\n" + 
				"SET estado='"+estado+"' where cedula ='"+cedula+"';" + 
				"" + 
				"";
		List<Persona> personas = new ArrayList<Persona>();
	
		try {
			personas = entityManager.createNativeQuery(query).getResultList();
			System.out.println("Personas------->:" + personas);
			
		} catch (Exception e) {
			System.out.println("--> ERROR Persona.getPersonabyEspecialidad" + e.getMessage());
		}
				
		return personas;
	}

}
