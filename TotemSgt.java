import java.util.ArrayList;

import javax.swing.JOptionPane;

import MetodosSql.Credenciales;
import MetodosSql.MetodosSql;

public class TotemSgt {
	private String ipDelTotem;// Solo para uso informativo;
	//_id	Incremental	No
	private String name;//	[OjoHalconOperativo].[InformacionCliente].direccion	SI
	private int clientNumber;//	Default 1. Luego Argentina lo Edita.	SI
	private String contractNumber;//	[OjoHalconOperativo].[InformacionCliente].abonado	SI
	private int serviceStatusadministrativeId;//	= 1 (Activo) | 9 (Fuera de Horario). Este dato se toma de [OjoHalconOperativo].[CalenServParcial] (si está en la Tabla es 12 Hs).	SI
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
		this.serviceStatusadministrativeId=1; // Siempre en uno, activo
		this.address=name; 		
		this.isDeleted=0;
		this.createdDate=MetodosSql.dameFechaDeHoy();
		this.createdUserId	= 1021;
		this.workSpaceId=1;// 1 por default hasta que se defina si se debe	averiguar el operador que se usa o no.
		this.ipRangeId=obtenerIpRangeId(); // Crear metodo con IPV4 // Si tiene -1 o la ip ya existe o hay un problema con ese contrato.
		this.preAddedDate=this.createdDate;
		this.countryId=1;
		this.recordingServer=obtenerIdRecordingServer();
		this.activationDate=this.createdDate;
		this.maitenanceMode=0;
		this.Updating=0;
		this.vpn=generarNroVpn();
		this.managementServer=1;		
		/*Se pone a lo ultimo porque obtenerIpRangeId() se encargará de averigüar la ip, asi no se llama 2 veces =) */
		this.servicePlanId=obtenerServicePlanid(this.ipDelTotem);// Cuando creen los service plans id vemos que valor lleva --> Lo debe crear Pablo.
		
	}
	/**
	 * 
	 * @param ipDelTotem 
	 * @return devuelve el serviceplanid, si llegase a ser -1, tuvo un problema en traerlo.
	 * y hay que revisar los serviceplandid de calenservparcial si no encontró nada en calenservparcial
	 * quiere decir que es un 24 hs.
	 */
	private int obtenerServicePlanid(String ipDelTotem) {
		int serviceplanId=-1;
		String resultado="";
		String prefijo="T#";
		String sufijo=":9291";
		String totemId=prefijo+ipDelTotem+sufijo;
		String query="SELECT servicePlanId\r\n" + 
				"  FROM [OjoHalconOperativo].[dbo].[CalenServParcial]\r\n" + 
				"  where TOTEM_ID ='"+totemId+"'    \r\n" + 
				"  group by servicePlanId";
		resultado=baseOjoHalconOperativo.consultarUnaCelda(query);
		if(resultado.isEmpty()&&esIpValida(ipDelTotem))
			return 1;
		
		try {
			serviceplanId=Integer.parseInt(resultado);
		}catch (Exception e) {
			System.out.println(e.getCause());
			return -1;
		}
		return serviceplanId;
		
		
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
		}else if (result.isEmpty()) {
			return 0;
		}
		else {
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
			//BuscadorErrores.errores.add("Ya existe IpRangeId Reasignalo o borralo -> select * from ips where ipnumber="+ipDelTotem+" =>"+IpRangeId);
			
			return IpRangeId;
			
		}else {		
			//No existe la ip en el rango, hay que agregarla en ipRanges.
			if(esIpValida(ipDelTotem)) {
			IpRangeId=agregarIpenTablaIpRanges(ipDelTotem);		
			if(IpRangeId!=-1) {
			agregarRangodeIpTablaIps(IpRangeId,ipDelTotem);
			}else {
				BuscadorErrores.errores.add("Ya existe totem no voy a generar otro Rango de Ip... Revisa eso. ");
				
			}
			
		return IpRangeId;
			}else {
				return -1;
			}
	}
	}
	
	private boolean esIpValida(String ip) {
		if(ip.contains("172.")) {//fucking genius =)
			return true;
		}else {
			return false;
		}
		
	}
	/**
	 * 
	 * @param unaIpDelRango
	 * @return true o false en función de si hay al menos un registro en la tabla ips que coincida con el criterio.
	 */
	private boolean existeRangoIp(String unaIpDelRango) {
		String SentenciaSql="select count(1) from ips where ipnumber='"+unaIpDelRango+"';";
		
		if(baseSGT.consultarUnaCelda(SentenciaSql).equals("0")) {
			return false;
		}else {
			return true;
		}
		
	}
	
	
	/**
	 * 
	 * @param ipRangeId es el id que viene de ipranges
	 * @param ipDelTotem
	 * @return
	 */

	private boolean agregarRangodeIpTablaIps(int ipRangeId, String ipDelTotem) {
		if(existeRangoIp(ipDelTotem)) {
			BuscadorErrores.errores.add("Ya existe la ip \"+ipDelTotem+\" en la tabla IPS, no voy a generar otro rango de forma automática");
			
			return false;
		}else {			
		
		IPv4 managerIps=new IPv4();
		ArrayList<String>IpsDelRango;
		//Obtengo el rango de ip para ser ingresado en la tabla [EOH_SGT].[dbo].[Ips]
		IpsDelRango=managerIps.rangoDeIp(ipDelTotem, "28");	
		
		int i=0;
		while(i<IpsDelRango.size()) {
			baseSGT.insertarOmodif("insert into ips(IpRangeId,IpNumber)values("+ipRangeId+",'"+IpsDelRango.get(i)+"');");
			i++;
		}		
			
		
		if(IpsDelRango.size()==i) {
			System.out.println("Registros insertados ok en la tabla IPS");
			return true;
		}else {
			return false;
		}		
		}
		
		
	}
	
	/**
	 * 
	 * @param ip Recibe la Ip de un totem, ingresa los datos en ipranges
	 * @return Devuelve el id generado por el insert de ipranges que servirá para crear un service
	 * Si la ip es vacía retorna un -1;
	 */
	private int agregarIpenTablaIpRanges(String ip) {
		if(!ip.isEmpty() && !BuscadorErrores.totemExisteenBaseSgt(ip)) {
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
		baseSGT.insertarOmodif(query);
		String queryIdIpRages="select max(id) from ipranges where name='"+this.name+"'";
		String resultado=baseSGT.consultarUnaCelda(queryIdIpRages);
		try {
		id_ipranges=Integer.parseInt(resultado);
		
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return id_ipranges;		
		}else {
			BuscadorErrores.errores.add("Totem ya existe o la ip no es válida");
			/*System.out.println("Totem ya existe o la ip no es válida");
			System.out.println("IP->"+getIP());
			System.out.println("Nombre->"+getName());*/
			return-1;
		}
	}

	
	
	
	
	
	private String obtenerIpdelTotem() {
		
		String query="  SELECT  TOTEMID      \r\n" + 
				"  FROM CLIENTES_EOH_NEW_CUSTOM\r\n" + 
				"  where CONTRATO='"+this.contractNumber+"'";
		String ip=null;
		ip=baseOjoHalconOperativo.consultarUnaCelda(query);
		ip=ip.replaceAll("T#","");
		ip=ip.replaceAll(":9291","");
		this.ipDelTotem=ip;
		return ip;
	}

	private String generarNroVpn() {
		String cadenaEjemplo="EOHa000025";
		String SentenciaSql = "SELECT substring(max(name),5,9)+1  FROM [EOH_SGT].[dbo].[VPN]";
		String maximoNroVpnMasUno=baseSGT.consultarUnaCelda(SentenciaSql);
		String prefijo="EOHa";
		int cerosArellenarEntreCadenas=cadenaEjemplo.length()-maximoNroVpnMasUno.length()-prefijo.length();
		String ceros="0";
		int i=1;
		while(i<cerosArellenarEntreCadenas) {
			ceros=ceros+"0";
			i++;
		}
		return prefijo+ceros+maximoNroVpnMasUno;
	}
	
	/**
	 * 
	 * @param vpnDevuelve true si existe vpn en tabla services
	 * @return
	 *
	
	private boolean existeVPNenServices(String vpn) {
		String query="SELECT VPN FROM Services where vpn='"+vpn+"';";
		String resultado="";
		resultado=baseSGT.consultarUnaCelda(query);
		if(resultado.isEmpty()) {
			return false;
		}else {
			return true;
		}
		
	}*/
	private String obtenerIdRecordingServer() {
		ArrayList<String>listaRecordingServers; // debe ser 1 si es más, hay que avisar y que el negocio mueva las cam a un solo recording
		String query=
				 "select idrecorder from hardware where name like '%"+this.getContractNumber()+"%'\r\n" + 
				"group by idrecorder;";
		listaRecordingServers=baseSurveillance.consultarUnaColumna(query);
		
		if(listaRecordingServers.size()>1) {
			BuscadorErrores.errores.add("Alerta!!! este contrato tiene las camaras en mas de un recording "+this.getContractNumber());
		
			return null;
		}else {
			if(listaRecordingServers.isEmpty()) {
				return "";
			}else {
			return listaRecordingServers.get(0);
			}
		}
		
		
	}

	public void mostrarAtributos() {
		System.out.println("IP->"+getIP());
		System.out.println("name->"+getName());
		System.out.println("clientNumber->"+getClientNumber());
		System.out.println("contractNumber->"+getContractNumber());
		System.out.println("ServiceStatusadministrativeId->"+getServiceStatusAdministrativeId());
		System.out.println("address->"+getAddress());
		System.out.println("servicePlanId->"+getServicePlanId());
		System.out.println("isDeleted->"+getIsDeleted());
		System.out.println("createdDate->"+getCreatedDate());
		System.out.println("createdUserId->"+getCreatedUserId());
		System.out.println("workSpaceId->"+getWorkSpaceId());
		System.out.println("ipRangeId->"+getIpRangeId());
		System.out.println("preAddedDate"+getPreAddedDate());
		System.out.println("countryId->"+getCountryId());
		System.out.println("recordingServer->"+getRecordingServer());
		System.out.println("activationDate->"+getActivationDate());
		System.out.println("maitenanceMode->"+getMaitenanceMode());
		System.out.println("Updating->"+getUpdating());
		System.out.println("vpn->"+getVpn());
		System.out.println("managementServer->"+getManagementServer());		
	}


	public String getIP() {
		return this.ipDelTotem;
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
	public int getServiceStatusAdministrativeId() {
		return serviceStatusadministrativeId;
	}
	public void setServiceStatusAdministrativeId(int serviceStatusadministrativeId) {
		this.serviceStatusadministrativeId = serviceStatusadministrativeId;
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
