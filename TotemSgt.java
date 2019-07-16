import java.util.ArrayList;

import MetodosSql.Credenciales;
import MetodosSql.MetodosSql;

public class TotemSgt {
	
	//_id	Incremental	No
	private String name;//	[OjoHalconOperativo].[InformacionCliente].direccion	SI
	private int clientNumber;//	Default 1. Luego Argentina lo Edita.	SI
	private String contractNumber;//	[OjoHalconOperativo].[InformacionCliente].abonado	SI
	private int serviceStatus;
	private int administrativeId;//	= 1 (Activo) | 9 (Fuera de Horario). Este dato se toma de [OjoHalconOperativo].[CalenServParcial] (si está en la Tabla es 12 Hs).	SI
	private String address;//	[OjoHalconOperativo].[InformacionCliente].direccion	SI
	// geoLattitude=null;//	Null	NO
	// geoLongitud=null;// 	Null	NO
	private int servicePlanId;//	Se hará un matcheo entre [OjoHalconOperativo].[CalenServParcial] y los Id de Planes 12 Hs en SGT. Hace falta crear planes en esta Tabla (ServicePlanId) de algunos planes que faltan.	SI
	//InternetProvidedId	Null. Lo cargará Argentina.	NO
	//ContractISPNumber	Null.	NO
	//TechnologyAccessId	Null. Lo cargará Argentina.	NO
	//BandwidthId	Null. Lo cargará Argentina.	NO
	private int isDeleted;// siempre	SI
	private String createdDate;//	A revisar de donde sacamos el dato. Default fecha actual. Pedir a Argentina	SI
	private int createdUserId;// (admin)	SI
	//LastUpdatedDate	Null	NO
	//DeletedDate	Null	NO
	//DeletedUserId	Null	NO
	private int workSpaceId;//	Hay que machear la tabla [EOH_SGT].[workspaces] con [OjoHalconOperativo].[operador]. En esta última hay que agregar un campo nuevo a mano incluyendo la IP y luego hacer un join con la del SGT.	SI
	private int ipRangeId;//	Previamente se debe crear la entrada a esta Tabla correspondiente al Servicio que se está creando. Para esto se utilizará la IP de la tabla [OjoHalconOperativo].[WDog_Totems_Info].TOTEMID	SI
	private String preAddedDate;//	Misma fecha que CreatedUserId.	SI
	private int countryId;//Argentina) siempre. Luego lo modificará Argentina.	SI
	//StateId	Null. Lo cargará Argentina.	NO
	//ZoneId	Null. Lo cargará Argentina.	NO
	private String recordingServer;//	Para todas las cámaras del Servicio, se buscará en la Tabla [Surveillance].[dbo].[Hardware].IDRecorder el ID del RS de cada cámara. En el SGT, siempre es el mismo Recording para todas las cámaras por lo tanto hay que segurarse que este de esa manera el Milestone. El IDRecorder se cargará en este campo (RecordingServer) en el SGT.
	//Si hay camaras de un mismo servicio a más de un RS, se pedirá al equipo de Argentina que las migren todas al mismo RS.	SI
	private String activationDate;//	Misma fecha que CreatedUserId.	SI
	private int maitenanceMode;// (fuera del modo mantenimiento)	SI
	//TotemVersion	Null.	NO
	//ResourceVersion	Null.	NO
	//MilestoneLogin	Null.	NO
	//MaitenanceModeShowing	Null.	NO
	private int Updating; //	SI
	//UpdateTotemSchedule	Null.	NO
	//UpdateResourcesSchedule	Null.	NO
	private String vpn;//	Incremental con formato “EOHa000009”. Buscar el último y de ahí incrementar.	SI
	private int managementServer=1;// (todas las cámaras están hoy en día en el Mgt#1 ARDC1CSPWA098)	SI
	
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
	
	MetodosSql baseSGT=new MetodosSql(
			Credenciales.ip_SGT,
			Credenciales.base_SGT,
			Credenciales.usuario_SGT,
			Credenciales.password_SGT
			);
	
	public TotemSgt(String contrato) {
		
		prepararTotemParaSGT(contrato);
		mostrarAtributos();
		
	}
	
	/**
	 * Metodo que completa todos los campos necesarios para crear el objeto y luego otro método se encargará de guardarlo.
	 * @param contrato 
	 */
	
	private void prepararTotemParaSGT(String contrato) {
				
		this.name=buscarNombreTotem(contrato);		
		this.clientNumber=1; 
		this.contractNumber=contrato;
		this.serviceStatus=1; 
		this.administrativeId=1111111111; // Consulta, si está en 12 hs, que valor debe ponerse?.
		this.address=name; 
		this.servicePlanId=1111111111;// Cuando creen los service plans id vemos que valor lleva --> Lo debe crear Pablo.
		this.isDeleted=0;
		this.createdDate=MetodosSql.dameFechaDeHoy();
		this.createdUserId	= 1021;
		this.workSpaceId=1111111111;	
		this.ipRangeId=obtenerIpRangeId(); // Crear metodo con IPV4
		this.preAddedDate=this.createdDate;
		this.countryId=1;
		this.recordingServer=obtenerIdRecordingServer();
		this.activationDate=this.createdDate;
		this.maitenanceMode=0;
		this.Updating=0;
		this.vpn=generarNroVpn();
		this.managementServer=1;		
		
	}
	
	public String consultarSiExisteTotem(String contrato) {
		return baseSGT.consultarUnaCelda("select name from services where contractNumber='"+contrato+"';");
	}
	/**
	 * 
	 * @param ip recibe una Ip de un totem
	 * @return devuelve 0 si no existe la ip en la tabla ips, si existe devuelve el iprangeid.
	 */
	private int existeIpSgt(String ip) {
		String result=null;
		String query="select IpRangeId from Ips where ipNumber='"+ip+"'";
		result=baseSGT.consultarUnaCelda(query);
		if(result==null) {
			return 0;
		}else {
			return Integer.parseInt(result);
		}		
	}
	/**
	 * 
	 * @return Obtiene el ipRangeId
	 * Si la ip ya existía, entonces obtiene el iprangeId y sino, inserta los datos
	 * Correspondientes en IpRanges y en Ips y devuelve el valor correspondiente
	 */
	public int obtenerIpRangeId() {
		String ipDelTotem=obtenerIpdelTotem();
		int IpRangeId=existeIpSgt(ipDelTotem);
		
		
		 // verificar si existe la ip antes de agregar
		
		if(IpRangeId!=0) {
			//si existe la ip, obtener el IpRangeId y asignárselo al totemSgt
			return IpRangeId;
			
		}else {		
			//No existe la ip en el rango, hay que agregarla en ipRanges.
			
			IpRangeId=agregarIpenTablaIpRanges(ipDelTotem);			
			agregarRangodeIpTablaIps(IpRangeId,ipDelTotem);
			
		return IpRangeId;
	}
	}
	
	
	

	public boolean agregarRangodeIpTablaIps(int ipRangeId, String ipDelTotem) {
		IPv4 managerIps=new IPv4();
		managerIps.calcularSubred(ipDelTotem, 28);
		String query="insert into ips(IpRangeId,IpNumber)values("+ipRangeId+",'200.59.13.90');";
		
		rangoDeIp();
		
		baseSGT.insertarOmodif(query);
		
		
		return true;
		
		
	}
	
	/**
	 * 
	 * @param ip Recibe la Ip de un totem, ingresa los datos en ipranges
	 * @return Devuelve el id generado por el insert de ipranges que servirá para crear un service
	 */
	private int agregarIpenTablaIpRanges(String ip) {
		int id_ipranges=0;
		String query= 
				"insert into IpRanges\r\n" + 
				"(name,ipaddress,mask,available,isdeleted,createddate,createduserid,lastUpdatedDate,lastUpdatedUserId,DeletedDate,DeletedUserId,CountryId,AssignedService)\r\n" + 
				"values(\r\n" + 
				"'"+this.name+"'--name,\r\n" + 
				",'"+ip+"'--ipaddress,\r\n" + 
				",28--mask,\r\n" + 
				",1--available,\r\n" + 
				",0--isdeleted,\r\n" + 
				",getdate()--createddate,\r\n" + 
				",1021--createduserid,\r\n" + 
				",getdate()--lastUpdatedDate,\r\n" + 
				",1021--lastUpdateUserId,\r\n" + 
				",null--DeletedDate,\r\n" + 
				",null--DeletedUserId,\r\n" + 
				",1--CountryId,\r\n" + 
				",1--AssignedService\r\n" + 
				")";
		String queryIdIpRages="select max(id) from ipranges where name='"+this.name+"'";
		id_ipranges=Integer.parseInt(baseSGT.consultarUnaCelda(queryIdIpRages));
		
		return id_ipranges;		
		
	}

	private String obtenerIpdelTotem() {
		String query="  SELECT  TOTEMID      \r\n" + 
				"  FROM CLIENTES_EOH_NEW_CUSTOM\r\n" + 
				"  where CONTRATO='"+this.contractNumber+"'";
		String ip=null;
		ip=baseOjoHalconOperativo.consultarUnaCelda(query);
		ip=ip.replaceAll("T#","");
		ip=ip.replaceAll(":9291","");
		
		return ip;
	}

	private String generarNroVpn() {
		// TODO Auto-generated method stub
		return null;
	}

	private String obtenerIdRecordingServer() {
		// TODO Auto-generated method stub
		return null;
	}

	public void mostrarAtributos() {
		System.out.println("name->"+getName());
		System.out.println("clientNumber->"+getClientNumber());
		System.out.println("contractNumber->"+getContractNumber());
		System.out.println("ServiceStatus->"+getServiceStatus());
		System.out.println("administrativeId->"+getAdministrativeId());
		System.out.println("address->"+getAddress());
		System.out.println("servicePlanId->"+getServicePlanId());
		System.out.println("isDeleted->"+getIsDeleted());
		System.out.println("createdDate->"+getCreatedDate());
		System.out.println("createdUserId->"+getCreatedUserId());
		System.out.println("workSpaceId->"+getWorkSpaceId());
		System.out.println("ipRangeId->"+getIpRangeId());
		System.out.println("preAddedDate"+getPreAddedDate());
		System.out.println("countryId->"+getCountryId());
		System.out.println("recordingServer"+getRecordingServer());
		System.out.println("activationDate->"+getActivationDate());
		System.out.println("maitenanceMode->"+getMaitenanceMode());
		System.out.println("Updating->"+getUpdating());
		System.out.println("vpn->"+getVpn());
		System.out.println("managementServer->"+getManagementServer());		
	}


	private String buscarNombreTotem(String contrato) {
		
		return baseOjoHalconOperativo.consultarUnaCelda(
				"SELECT top 1 Direccion\r\n" + 
				"  FROM InformacionCliente\r\n" + 
				"  where Abonado='"+contrato+"'");
				
	}

	/*
	 * @Este método guarda el totem y devuelve true o false en función del si pudo o no  guardar.
	 * 
	 * 
	 * */
	public boolean guardarTotemSgt() {
		return true;
	}
	
	public boolean totemExisteenBaseSgt() {
		return true;
	}
	
	
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getClientNumber() {
		return clientNumber;
	}
	public void setClientNumber(int clientNumber) {
		this.clientNumber = clientNumber;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public int getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(int serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public int getAdministrativeId() {
		return administrativeId;
	}
	public void setAdministrativeId(int administrativeId) {
		this.administrativeId = administrativeId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getServicePlanId() {
		return servicePlanId;
	}
	public void setServicePlanId(int servicePlanId) {
		this.servicePlanId = servicePlanId;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public int getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(int createdUserId) {
		this.createdUserId = createdUserId;
	}
	public int getWorkSpaceId() {
		return workSpaceId;
	}
	public void setWorkSpaceId(int workSpaceId) {
		this.workSpaceId = workSpaceId;
	}
	public int getIpRangeId() {
		return ipRangeId;
	}
	public void setIpRangeId(int ipRangeId) {
		this.ipRangeId = ipRangeId;
	}
	public String getPreAddedDate() {
		return preAddedDate;
	}
	public void setPreAddedDate(String preAddedDate) {
		this.preAddedDate = preAddedDate;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getRecordingServer() {
		return recordingServer;
	}
	public void setRecordingServer(String recordingServer) {
		this.recordingServer = recordingServer;
	}
	public String getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}
	public int getMaitenanceMode() {
		return maitenanceMode;
	}
	public void setMaitenanceMode(int maitenanceMode) {
		this.maitenanceMode = maitenanceMode;
	}
	public int getUpdating() {
		return Updating;
	}
	public void setUpdating(int updating) {
		Updating = updating;
	}
	public String getVpn() {
		return vpn;
	}
	public void setVpn(String vpn) {
		this.vpn = vpn;
	}
	public int getManagementServer() {
		return managementServer;
	}
	public void setManagementServer(int managementServer) {
		this.managementServer = managementServer;
	}


}
