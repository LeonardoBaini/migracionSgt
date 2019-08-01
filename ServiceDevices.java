package Clases;

public class ServiceDevices {

	
	//_id	Incremental	No
	int serviceId;//	[EOH_SGT].[Services]._id	SI
	int ipId;//	[EOH_SGT].[IPs]._id y a su vez viene de [EOH_SGT].[IPRanges] Como siempre son 3 dispositivos Router, PC y UPS, se toman las 3 primeras IPs del Rango.	SI
	//Decription	Va en null	NO
	//Serial	Va en null	NO
	int categoryDeviceId;//	Va siempre 1, 2, 3 para los tres dispositivos por Service	SI
	
	
	public ServiceDevices() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getServiceId() {
		return serviceId;
	}


	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}


	public int getIpId() {
		return ipId;
	}


	public void setIpId(int ipId) {
		this.ipId = ipId;
	}


	public int getCategoryDeviceId() {
		return categoryDeviceId;
	}


	public void setCategoryDeviceId(int categoryDeviceId) {
		this.categoryDeviceId = categoryDeviceId;
	}

	
	
	
}
