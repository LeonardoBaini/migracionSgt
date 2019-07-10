
public class TotemSgt {
	
	//_id	Incremental	No
	String name;//	[OjoHalconOperativo].[InformacionCliente].direccion	SI
	int clientNumber=1;//	Default 1. Luego Argentina lo Edita.	SI
	String contractNumber;//	[OjoHalconOperativo].[InformacionCliente].abonado	SI
	int serviceStatus=1;
	int administrativeId;//	= 1 (Activo) | 9 (Fuera de Horario). Este dato se toma de [OjoHalconOperativo].[CalenServParcial] (si está en la Tabla es 12 Hs).	SI
	String address;//	[OjoHalconOperativo].[InformacionCliente].direccion	SI
	// geoLattitude=null;//	Null	NO
	// geoLongitud=null;// 	Null	NO
	int servicePlanId;//	Se hará un matcheo entre [OjoHalconOperativo].[CalenServParcial] y los Id de Planes 12 Hs en SGT. Hace falta crear planes en esta Tabla (ServicePlanId) de algunos planes que faltan.	SI
	//InternetProvidedId	Null. Lo cargará Argentina.	NO
	//ContractISPNumber	Null.	NO
	//TechnologyAccessId	Null. Lo cargará Argentina.	NO
	//BandwidthId	Null. Lo cargará Argentina.	NO
	int isDeleted	= 0;// siempre	SI
	String createdDate;//	A revisar de donde sacamos el dato. Default fecha actual. Pedir a Argentina	SI
	int createdUserId	= 1021;// (admin)	SI
	//LastUpdatedDate	Null	NO
	//DeletedDate	Null	NO
	//DeletedUserId	Null	NO
	int workSpaceId;//	Hay que machear la tabla [EOH_SGT].[workspaces] con [OjoHalconOperativo].[operador]. En esta última hay que agregar un campo nuevo a mano incluyendo la IP y luego hacer un join con la del SGT.	SI
	int ipRangeId;//	Previamente se debe crear la entrada a esta Tabla correspondiente al Servicio que se está creando. Para esto se utilizará la IP de la tabla [OjoHalconOperativo].[WDog_Totems_Info].TOTEMID	SI
	String preAddedDate;//	Misma fecha que CreatedUserId.	SI
	int countryId	= 1;//Argentina) siempre. Luego lo modificará Argentina.	SI
	//StateId	Null. Lo cargará Argentina.	NO
	//ZoneId	Null. Lo cargará Argentina.	NO
	String recordingServer;//	Para todas las cámaras del Servicio, se buscará en la Tabla [Surveillance].[dbo].[Hardware].IDRecorder el ID del RS de cada cámara. En el SGT, siempre es el mismo Recording para todas las cámaras por lo tanto hay que segurarse que este de esa manera el Milestone. El IDRecorder se cargará en este campo (RecordingServer) en el SGT.
	//Si hay camaras de un mismo servicio a más de un RS, se pedirá al equipo de Argentina que las migren todas al mismo RS.	SI
	String activationDate;//	Misma fecha que CreatedUserId.	SI
	int maitenanceMode	= 0;// (fuera del modo mantenimiento)	SI
	//TotemVersion	Null.	NO
	//ResourceVersion	Null.	NO
	//MilestoneLogin	Null.	NO
	//MaitenanceModeShowing	Null.	NO
	int Updating=0; //	SI
	//UpdateTotemSchedule	Null.	NO
	//UpdateResourcesSchedule	Null.	NO
	String vpn;//	Incremental con formato “EOHa000009”. Buscar el último y de ahí incrementar.	SI
	int managementServer=1;// (todas las cámaras están hoy en día en el Mgt#1 ARDC1CSPWA098)	SI
	
	
	public TotemSgt() {
		super();
		
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
