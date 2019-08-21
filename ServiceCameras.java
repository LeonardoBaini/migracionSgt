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
