package Clases;
import java.util.ArrayList;

import MetodosSql.Credenciales;
import MetodosSql.MetodosSql;

public class AdministradorSgt {
	MetodosSql baseOjoHalconOperativo=new MetodosSql(
			Credenciales.ip_OjoHalconOperativo,
			Credenciales.base_OjoHalconOperativo,
			Credenciales.usuario_OjoHalconOperativo,
			Credenciales.password_OjoHalconOperativo
			);
	MetodosSql baseSurveillance=new MetodosSql(
			Credenciales.ip_Surveillance,
			Credenciales.base_Surveillance,
			Credenciales.usuario_Surveillance,
			Credenciales.password_Surveillance
			);
	
	static MetodosSql baseSGT=new MetodosSql(
			Credenciales.ip_SGT,
			Credenciales.base_SGT,
			Credenciales.usuario_SGT,
			Credenciales.password_SGT
			);
	
public static void guardarTotem(TotemSgt totem) {
	String queryVpn="insert into vpn (name) values('"+totem.getVpn()+"');";
	
	
	int errores=BuscadorErrores.erroresTotem(totem).size();
	if(errores!=0) {
		for(int i=0;i<errores;i++) {
			System.out.println(BuscadorErrores.erroresTotem(totem).get(i));
		
		}
	}else {
		System.out.println("No se encontraron errores...");
		baseSGT.insertarOmodif(queryVpn);
		baseSGT.insertarOmodif(crearQueryTotem(totem));
		
		
	}
	
		
}

public static String crearQueryTotem(TotemSgt totem) {

	String query="insert into services "+
			"(name,\r\n" +
		    "clientNumber,\r\n" + 
			"contractNumber,\r\n" + 
			"ServiceStatusadministrativeId,\r\n" + 
			"address,\r\n" + 
			"servicePlanId,\r\n" + 
			"isDeleted,\r\n" + 
			"createdDate,\r\n" + 
			"createdUserId,\r\n" + 
			"workSpaceId,\r\n" + 
			"ipRangeId,\r\n" + 
			"preAddedDate,\r\n" + 
			"countryId,\r\n" + 
			"recordingServer,\r\n" + 
			"activationDate,\r\n" + 
			"MaintenanceMode,\r\n" + 
			"Updating,\r\n" + 
			"vpn,\r\n" + 
			"managementServer)"
			+ "values("+
			"'"+totem.getName()+"',"+
			totem.getClientNumber()+","+
			"'"+totem.getContractNumber()+"',"+
			totem.getServiceStatusAdministrativeId()+","+
			"'"+totem.getAddress()+"',"+
			totem.getServicePlanId()+","+
			totem.getIsDeleted()+","+
			"'"+totem.getCreatedDate()+"',"+
			totem.getCreatedUserId()+","+
			totem.getWorkSpaceId()+","+
			totem.getIpRangeId()+","+
			"'"+totem.getPreAddedDate()+"',"+
			totem.getCountryId()+","+
			"'"+totem.getRecordingServer()+"',"+
			"'"+totem.getActivationDate()+"',"+
			totem.getMaitenanceMode()+","+
			totem.getUpdating()+","+
			"'"+totem.getVpn()+"',"+
			totem.getManagementServer()
			+")"
			;
	
	
	
	
	return query;
}


private void guardarServiceDevices() {
	
}
private void guardarServiceCameras() {
	
}









}
