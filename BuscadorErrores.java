import java.util.ArrayList;

import MetodosSql.Credenciales;
import MetodosSql.MetodosSql;

public class BuscadorErrores {
	static ArrayList<String>errores=new ArrayList<String>();	
	
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

	public static ArrayList<String>erroresTotem(TotemSgt totem){
			
		
		if(!esIpValida(totem.getIP()))errores.add("IP Inválida -> "+totem.getIP());
		if(totem.getName().isEmpty())errores.add("Name Inválido Vacío -> "+totem.getName());
		if(totem.getAddress().isEmpty())errores.add("Address Inválido Vacío -> "+totem.getAddress());
		if(servicePlanInvalido(totem))errores.add("ServicePlanId Inválido -> "+totem.getServicePlanId());
		if(totem.getIpRangeId()==0)errores.add("IpRangeId Inválido -> "+totem.getIpRangeId());
		if(totem.getRecordingServer().length()<20)errores.add("RecordingServer Inválido -> "+totem.getRecordingServer());
		if(totem.getVpn().isEmpty())errores.add("Vpn Inválida -> "+totem.getVpn());	
		
		
		return errores;
		
	}
	private String tieneCamposVacios() {
		return null;		
	}
	
	private static boolean esIpValida(String ip) {
		if(ip.contains("172.")) {//fucking genius =)
			return true;
		}else {
			return false;
		}
		
	}
	private static boolean servicePlanInvalido(TotemSgt totem) {
		if(totem.getServicePlanId()==-1) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean totemExisteenBaseSgt(String ip) {
		int flag=0;
		String SentenciaSql=
				 "select count(1) from IpRanges"
				+ " where ipAddress='"+ip+"'";
		flag=Integer.parseInt(baseSGT.consultarUnaCelda(SentenciaSql));
		if(flag>0) {
		return true;
		}else {
		return false;
		}
	}
}
