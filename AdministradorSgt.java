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
	System.out.println("Probando acceso a OjoHalconOperativo");
	OjoHalconOperativo=baseOjoHalconOperativo.consultarUnaCelda(SentenciaSql,null);
	System.out.println("Probando acceso a SGT");
	SGT=baseSGT.consultarUnaCelda(SentenciaSql,null);
	System.out.println("Probando acceso a Surveillance");
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
	private static void imprimirErrores(TotemSgt totem,JTextArea txtrLogs){
		int errores=BuscadorErrores.errores.size();
		System.out.println("************************ Comenzando impresion de errores ************************");
		for(int i=0;i<errores;i++) {
			System.out.println(BuscadorErrores.errores.get(i));
			if(txtrLogs!=null)
			txtrLogs.setText(txtrLogs.getText()+"\n"+BuscadorErrores.errores.get(i));
		
		}
		
	}
public static void guardarTotem(TotemSgt totem, JTextArea txtrLogs) {
	if(txtrLogs!=null)
	txtrLogs.setText(txtrLogs.getText()+"\n"+"Comenzando intento de guardado");
	String queryVpn="insert into vpn (name) values('"+totem.getVpn()+"');";
	
	
	int errores=BuscadorErrores.erroresTotem(totem).size();
	if(errores!=0) {
		imprimirErrores(totem,txtrLogs);
	}else {
		System.out.println("No se encontraron errores...");
		//totem.setIpRangeId(totem.obtenerIpRangeId());//VER ESTE ULTIMO AGREGADO SI IRIA ACA O NO
		if(totem.getIpRangeId()==0) {
			BuscadorErrores.errores.add("erroresTotem erroresTotem IpRangeId Inválido -> "+totem.getIpRangeId());
			
			//ACÁ SE DEBERÍA HACER ROLLBACK DE LA INSERCIÓN
			
			imprimirErrores(totem,txtrLogs);
			return;
			
		}else {		
		
		baseSGT.insertarOmodif(queryVpn);		
		baseSGT.insertarOmodif(crearQueryTotem(totem));
		
		
		}
		
		
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

/**
 * Requiere que esté cargado el totem y las camaras, es el último paso....
 * @param totem
 */
public static void guardarServiceDevices(TotemSgt totem) {
	ArrayList<ServiceDevices> serviceDevices=obtenerServiceDevices(totem);
	int serviceId=serviceDevices.get(0).getServiceId();
	String queryControlDuplicados="select count(1) from serviceDevices where serviceId="+serviceId;
	int cantRegistros=Integer.parseInt(baseSGT.consultarUnaCelda(queryControlDuplicados,null));
	
	if(cantRegistros==0) {
	
	
	int ipId=0;
	int categoryDeviceId=0;
	
	String query=null;
	
	for(int i=0;i<serviceDevices.size();i++) {
		serviceId=serviceDevices.get(i).getServiceId();
		ipId=serviceDevices.get(i).getIpId();
		categoryDeviceId=serviceDevices.get(i).getCategoryDeviceId();
		query="insert into serviceDevices(serviceid,ipid,categorydeviceid)values("+serviceId+","+ipId+","+categoryDeviceId+");";
		baseSGT.insertarOmodif(query);
		System.out.println("*******************Insertanto en serviceDevices*******************");
		System.out.println(query);
	}
	}else {
		System.out.println("Ya existen registros ingresados para ServiceDevices para el ServiceId "+serviceId);
		BuscadorErrores.errores.add("Ya existen registros ingresados para ServiceDevices para el ServiceId "+serviceId);
	}
	
}


private static void  mostrarServiceCamaras(ServiceCameras servicecam) {
	System.out.println("CategoryCameraId:"+servicecam.getCategoryCameraId());
    System.out.println("ServiceId:"+servicecam.getServiceId());
    System.out.println("HardwareID:"+servicecam.getHardwareID());
    System.out.println("HardwareName:"+servicecam.getHardwareName());
    System.out.println("GroupName:"+servicecam.getGroupName());
    System.out.println("CameraName:"+servicecam.getCameraName());
    System.out.println("CamaraIp:"+servicecam.getCamaraIp());
    System.out.println("User:"+servicecam.getUser());
    System.out.println("Password:"+servicecam.getPassword());
    System.out.println("IndexCamera:"+servicecam.getIndexCamera());
    System.out.println("IpId:"+servicecam.getIpId());
}

private static String generaQueryGuardadoServiceCamaras(ServiceCameras servicecam) {
	String query=" insert into ServiceCameras" + 			
			"(ServiceId\r\n" + 
			",DeviceId\r\n" + 
			",IpId\r\n" + 
			",Port\r\n" + 
			",\"User\"\r\n" + // Raro pero real, sin las comillas dobles, no anda. Es palabra reservada =()
			",Password\r\n" + 
			",Milestone\r\n" + 
			",CategoryCameraId\r\n" + 
			",GroupName\r\n" + 
			",HardwareName\r\n" + 
			",HardwareID\r\n" + 
			",CameraName\r\n" + 
			",IndexCamera\r\n" + 
			",Name\r\n" + 
			",StreamId\r\n" + 
			")\r\n" + 
			"values(\r\n" + 
			"'"+servicecam.getServiceId()+"',"+ 
			"'"+servicecam.getDeviceId()+"',"+ 
			"'"+servicecam.getIpId()+"',"+ 
			"'"+servicecam.getPort()+"',"+ 
			"'"+servicecam.getUser()+"',"+ 
			"'"+servicecam.getPassword()+"',"+ 
			"'"+servicecam.getMilestone()+"',"+ 
			"'"+servicecam.getCategoryCameraId()+"',"+ 
			"'"+servicecam.getGroupName()+"',"+ 
			"'"+servicecam.getHardwareName()+"',"+ 
			"'"+servicecam.getHardwareID()+"',"+ 
			"'"+servicecam.getCameraName()+"',"+ 
			"'"+servicecam.getIndexCamera()+"',"+ 
			"'"+servicecam.getCameraName()+"',"+ 
			"'"+servicecam.getStreamId()+"');";
			  
	return query;
	
}

/**
 * 
 * @param serviceCamList
 * Guarda en la BBDD la lista de camaras generadas por el método obtenerServiceCameras(String contrato) 
 */
public static void guardarServiceCameras(TotemSgt totem) {
	if(BuscadorErrores.errores.size()==0) {
	ArrayList <ServiceCameras> serviceCamList=obtenerServiceCameras(totem.getContractNumber());
	String sentenciaSql="";
	ServiceCameras servicecam = null;
	System.out.println("Comenzando guardado de cámaras...");
	for(int i=0;i<serviceCamList.size();i++) {
	servicecam=serviceCamList.get(i);
	//mostrarServiceCamaras(servicecam);
	sentenciaSql=generaQueryGuardadoServiceCamaras(servicecam);
	
	baseSGT.insertarOmodif(sentenciaSql);
	System.out.println("Finalizado guardado de cámara ->"+serviceCamList.get(i).getCameraName());
	}
	
    
	}
	else {
		System.out.println("Lo siento, hay errores, no puedo guardar camaras, vea el log de errores.");
		System.out.println("Usa el parámetro Rollback para deshacer el contrato de la BBDD SGT y vuelve a cargarlo");
		
	}
	
	
	
	
	
}
/**
 * Método que genera los datos necesarios para guardar en [EOH_SGT].[dbo].[ServiceDevices]
 * Devuelve una lista que guardará otro método en la BBDD.
 * @param contrato
 * @return
 */
public static ArrayList <ServiceDevices> obtenerServiceDevices(TotemSgt totem) {
	String contrato=totem.getContractNumber();
	int ipRangeid=totem.getIpRangeId();
	ArrayList<String>idsPrimerasIpdeIPS;
	int cantCamaras=obtenerServiceCameras(contrato).size();
	
	
	ArrayList<ServiceDevices>serviceDevices=new ArrayList<ServiceDevices>();
	ServiceDevices serviceDevice;
	String sentenciaSqlServiceId=
			  "SELECT  Id     \r\n" + 
			"  FROM [EOH_SGT].[dbo].[Services]\r\n" + 
			"  where ContractNumber='"+contrato+"';";
	String SentenciaSqlidsPrimeraNIpdeIPS = 
			"  select top "+cantCamaras+" id from ips where IpRangeId="+ipRangeid+"\r\n" + 
			"  order by ipnumber asc";
	
	String serviceidString=baseSGT.consultarUnaCelda(sentenciaSqlServiceId,null);
	int serviceid=Integer.parseInt(serviceidString);		
	idsPrimerasIpdeIPS=baseSGT.consultarUnaColumna(SentenciaSqlidsPrimeraNIpdeIPS);
	
	for(int i=1;i<=cantCamaras;i++) {
		
		serviceDevice=new ServiceDevices();		
		serviceDevice.setIpId(Integer.parseInt(idsPrimerasIpdeIPS.get(i-1)));
		serviceDevice.setServiceId(serviceid);
		serviceDevice.setCategoryDeviceId(i);	
		serviceDevices.add(serviceDevice);
		
	}	
	
	return serviceDevices;
	
}

/**
 * Busca en Surveillance las cámaras asociadas al contrato, en el nombre de la cam debe figurar el contrato. 
 * @param contrato
 * @return
 */

public static ArrayList obtenerServiceCameras(String contrato)  {
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
	return servicecameras;
	
	
	
	
	
}

public static void hacerRollback(String contrato) {
	
	String queryRollBack="declare @contrato as varchar(max)\r\n" + 
			"\r\n" + 
			"set @contrato='"+contrato+"'\r\n" + 
			"\r\n" + 
			"declare @name as varchar(max)\r\n" + 
			"declare @ipRangesId as varchar(max)\r\n" + 
			"declare @vpn as varchar(max)\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"  set @name=(select name from services where ContractNumber=@contrato);\r\n" + 
			"  set @ipRangesId=(select id from ipranges where name=@name);\r\n" + 
			"  set @vpn=(select vpn from services where ContractNumber=@contrato);\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			" \r\n" + 
			"  delete FROM [EOH_SGT].[dbo].[ServiceCameras]\r\n" + 
			"  where name like '%'+@contrato+'%';\r\n" + 
			"\r\n" + 
			"  \r\n" + 
			"  \r\n" + 
			"  delete from  [EOH_SGT].[dbo].[ServiceDevices]\r\n" + 
			"  where serviceid=(select id from services where name=@name)\r\n" + 
			"  \r\n" + 
			"\r\n" + 
			" \r\n" + 
			"  delete\r\n" + 
			"  FROM [EOH_SGT].[dbo].[Services]\r\n" + 
			"  where name=@name;\r\n" + 
			"\r\n" + 
			"   \r\n" + 
			"  delete\r\n" + 
			"  FROM [EOH_SGT].[dbo].[Ips] \r\n" + 
			"  where iprangeid=@ipRangesId\r\n" + 
			"  \r\n" + 
			"    delete\r\n" + 
			"  FROM [EOH_SGT].[dbo].[IpRanges]\r\n" + 
			"  where name=@name\r\n" + 
			"  \r\n" + 
			"    \r\n" + 
			"  delete\r\n" + 
			"  FROM [EOH_SGT].[dbo].[vpn]\r\n" + 
			"  where name=@vpn";
	baseSGT.insertarOmodif(queryRollBack);
	System.out.println("Rollback finalizado.");
	
	
}








}
