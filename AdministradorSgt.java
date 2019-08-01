package Clases;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTextArea;

import MetodosSql.Credenciales;
import MetodosSql.MetodosSql;

public class AdministradorSgt {
	static MetodosSql baseOjoHalconOperativo=new MetodosSql(
			Credenciales.ip_OjoHalconOperativo,
			Credenciales.base_OjoHalconOperativo,
			Credenciales.usuario_OjoHalconOperativo,
			Credenciales.password_OjoHalconOperativo
			);
	static MetodosSql baseSurveillance=new MetodosSql(
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
public static boolean testearConexionBases() {
	boolean todofunciona=true;
	String SentenciaSql = "select getdate()";
	String OjoHalconOperativo;
	String SGT;
	String Surveillance;
	OjoHalconOperativo=baseOjoHalconOperativo.consultarUnaCelda(SentenciaSql,null);
	SGT=baseSGT.consultarUnaCelda(SentenciaSql,null);
	Surveillance=baseSurveillance.consultarUnaCelda(SentenciaSql,null);
	
	if(OjoHalconOperativo.isEmpty()) {
		System.out.println("No se puede conectar a OjoHalconOperativo");
		todofunciona=false;
	}if(SGT.isEmpty()) {
		System.out.println("No se puede conectar a SGT");
		todofunciona=false;
		
	}if(Surveillance.isEmpty()) {
		System.out.println("No se puede conectar a Surveillance");
		todofunciona=false;
		
	}
	return todofunciona;
}
	
public static void guardarTotem(TotemSgt totem, JTextArea txtrLogs) {
	if(txtrLogs!=null)
	txtrLogs.setText(txtrLogs.getText()+"\n"+"Comenzando intento de guardado");
	String queryVpn="insert into vpn (name) values('"+totem.getVpn()+"');";
	
	
	int errores=BuscadorErrores.erroresTotem(totem).size();
	if(errores!=0) {
		for(int i=0;i<errores;i++) {
			System.out.println(BuscadorErrores.erroresTotem(totem).get(i));
			if(txtrLogs!=null)
			txtrLogs.setText(txtrLogs.getText()+"\n"+BuscadorErrores.erroresTotem(totem).get(i));
		
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


public static void obtenerServiceCameras(String contrato)  {
	String queryServiceid=" select id from Services where contractNumber='"+contrato+"';";
	String queryIpId;//va cambiando en función de la ip de la cámara
	String serviceId=baseSGT.consultarUnaCelda(queryServiceid, null);
	String auxIp=null;//Sirve para no hacer la conversion a int directamente y que no de error
	int ipId=-1;//pongo -1 para luego hacer control de errores sobre eso antes de insertar
	int categoryCameraId;// la primera es totem 1 y las demás 3
	int flagCategoryCameraId=0;//para saber si ya se uso la categorycameraid=1
	ArrayList<ServiceCameras>servicecameras=new ArrayList<ServiceCameras>();
	ServiceCameras servicecam = null;
	String query=
			"select upper(idhardware)idhardware,\r\n" + 
			"name,\r\n" + 
			"replace(replace(uri,'http:',''),'/','') as ip,\r\n" + 
			"loginid,ROW_NUMBER() OVER(ORDER BY uri ASC) AS indice \r\n" + 
			"from hardware where name like '%"+contrato+"%'"; 
	/*matriz=baseSurveillance.consultar(query);
	
	for(int i=0;i<matriz.size();i++) {
		for(int j=0;j<matriz.get(i).size();j++)
		System.out.println(matriz.get(i).get(j));
	}*/
	ResultSet res=baseSurveillance.consultarResultSet(query);
	try {
		while(res.next()) {
			if(flagCategoryCameraId==0) {
				categoryCameraId=1;
				flagCategoryCameraId++;
			}else {
				categoryCameraId=3;//si ya se asignó el 1 a la primera, al resto le ponemos 3
			}
			
				
			servicecam = new ServiceCameras();
			servicecam.setCategoryCameraId(categoryCameraId);	
			servicecam.setServiceId(serviceId);
			servicecam.setHardwareID(res.getString("idhardware"));
			servicecam.setHardwareName(res.getString("name"));// el name es igual en las 3 variables
			servicecam.setGroupName(res.getString("name"));
			servicecam.setCameraName(res.getString("name"));
			servicecam.setCamaraIp(res.getString("ip"));
			servicecam.setUser(res.getString("loginid"));
			servicecam.setPassword(servicecam.getUser());//Password igual que el usuario por default
			servicecam.setIndexCamera(res.getInt("indice"));
			queryIpId="SELECT Id FROM Ips where ipnumber='"+servicecam.getCamaraIp()+"'";
			auxIp=baseSGT.consultarUnaCelda(queryIpId, null);
			if(!auxIp.isEmpty()) {//para evitar errores de ejecución si no está cargada la ip en ips
				ipId=Integer.parseInt(auxIp);
			}
			servicecam.setIpId(ipId);
			
			servicecameras.add(servicecam);			
			
		}
		res.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println(e.getCause());
	}
	
	
	
	
	
}









}
