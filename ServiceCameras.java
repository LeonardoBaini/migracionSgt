package Clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import MetodosSql.Credenciales;
import MetodosSql.MetodosSql;

public class ServiceCameras {	
/**
 * Clase que en función de un contrato busca los servicescameras correspondientes y los entrega en una lista.
 */

//_id	Incremental	No
String camaraIp;//informativo
String serviceId;//	[EOH_SGT].[Services]._id	SI

int deviceId=1;// si es la primer cámara=1 sino =4
/* Cambio pedido por pbaustian
	Modificar definición				vendor ProductId de hardware 
	Campo DeviceId de la tabla ServiceCameras				
	Id	Name	Brand	Model	
	1	VB600-Totem	Sony	SNC-VB600	Si es la primer cámara (Totem) y es Sony SNC-VB6xx/VM6xx/EM6xx Series en Milestone -> va VB600-Totem en SGT.
	2	SNC-CH140-Totem	Sony	SNC-CH140	Si es Sony SNC-CH140/CH180/CH240/CH280/DH240/DH280 en Milestone -> va SNC-CH140-Totem en SGT.
	3	Operador EB600	SONY	EB600	
	4	DOMO SNC-EM602R	SONY	SNC-EM602R	Si es una cámara distinta a la primera (Totem) y es Sony SNC-VB6xx/VM6xx/EM6xx Series en Milestone -> va DOMO SNC-EM602R en SGT.
	5	Domo HIKVISION	HIKVISION	DS-2CD2725FWD-IZS	
	6	Operador HIKVISION	HIKVISION	DS-2CD4C26FWD	
	7	SNC-DH160	Sony	SNC-DH160	Si es Sony SNC-CH160/DH160/CH260/DH260/CH220/DH120T/DH220T en Milestone -> va SNC-DH160 en SGT.
	8	Axis	Axis	P1354	Todo lo que es Axis en Milestone -> va Axis en SGT.
	9	Operador CH140	SONY	SNC-CH140	
		salir por error			Si es HikVisionGeneric en Milestone -> este caso exceptúalo porque no esta homologada esta cámara. Sacalo por error para que no migre.
		salir por error			Cualquier otro valor de Milestone, salgamos por error y lo analizamos casos a caso.*/

//(por ahora le pongo 1 para que guarde pero ,-->	Es mandatorio. Hay que revisar si es necesario que este correctamente cargado para poder sincronizar.	SI
int ipId;//	[EOH_SGT].[Ips]._id hay que machearla con la IP real de la cámara que esta en [Surveillance].[Hardware].URI	SI
int port=80;//	SI
String user;// User	[Surveillance].[Hardware].LoginId En la base Surviellance solo hay dos tipos: admin y root.	SI
String password;//	Argentina deberá proveer los passwords asociados a cada user.	SI
int milestone	= 3;	//SI
//DeviceProfileId	Null	NO
int categoryCameraId;//	Para la IP más chica es siempre = 1 (Totem)//Hay que crear una nueva categoría que se llame “Apoyo” para el resto de las cámaras.	SI
String groupName;//	[Surveillance].[Hardware].Name	SI
String hardwareName;   //	[Surveillance].[Hardware].Name	SI
String hardwareID;//	[Surveillance].[Hardware].IDHardware	SI
String cameraName;//	[Surveillance].[Hardware].Name	SI
int indexCamera;//	Se ordena de menor a mayor con las IP. Va en número entero: 1, 2, 3, 4, etc.	SI
//Name	Null	NO
int streamId=1;//. Argentina debe confirmar si tiene cámaras con Streams = 2.	SI
//StreamMilestone	Null.	NO

public ServiceCameras(){
	
}

public String getServiceId() {
	return serviceId;
}

public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
}

public int getDeviceId() {
	return deviceId;
}

public void setDeviceId(int deviceId) {
	this.deviceId = deviceId;
}

public int getIpId() {
	return ipId;
}

public void setIpId(int ipId) {
	this.ipId = ipId;
}

public int getPort() {
	return port;
}

public void setPort(int port) {
	this.port = port;
}

public String getUser() {
	return user;
}

public void setUser(String user) {
	this.user = user;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public int getMilestone() {
	return milestone;
}

public void setMilestone(int milestone) {
	this.milestone = milestone;
}

public int getCategoryCameraId() {
	return categoryCameraId;
}

public void setCategoryCameraId(int categoryCameraId) {
	this.categoryCameraId = categoryCameraId;
}

public String getGroupName() {
	return groupName;
}

public void setGroupName(String groupName) {
	this.groupName = groupName;
}

public String getHardwareName() {
	return hardwareName;
}

public void setHardwareName(String hardwareName) {
	this.hardwareName = hardwareName;
}

public String getCamaraIp() {
	return camaraIp;
}

public void setCamaraIp(String camaraIp) {
	this.camaraIp = camaraIp;
}

public String getHardwareID() {
	return hardwareID;
}

public void setHardwareID(String hardwareID) {
	this.hardwareID = hardwareID;
}

public String getCameraName() {
	return cameraName;
}

public void setCameraName(String cameraName) {
	this.cameraName = cameraName;
}

public int getIndexCamera() {
	return indexCamera;
}

public void setIndexCamera(int indexCamera) {
	this.indexCamera = indexCamera;
}

public int getStreamId() {
	return streamId;
}

public void setStreamId(int streamId) {
	this.streamId = streamId;
}





}
