
import java.util.ArrayList;
import java.util.List;

public class IPv4 {

	String ipv4;
	int mascara;
	ArrayList <String> ipv4Array=new ArrayList<String>();
	char[] binarioIP;
	char[] binarioMascara;
	char[] binarioSubred;

	
	/*@
	 * ipv4 recibe una ip del tipo 172.16.32.3
	 * la mascara es del tipo corta, ejemplo 28
	 * 
	 * */
	public ArrayList<String> rangoDeIp(String ipv4,String mask){
		
		ArrayList<String> rangoIp=new ArrayList<String>();
		
		int nroHosts=calcularNroHosts(mask);
		
		String subred=calcularSubred(ipv4,Integer.parseInt(mask));
		
		System.out.println("La subred es.."+subred+" y los host son "+nroHosts);
	//	rangoIp=sumarHostASubred(subred,nroHosts);
		
		return rangoIp;			
	
	}

	private String convertirMascaraAbinaria(int mascara) {
		String binario = null;
		int i=0;
		while(i<mascara) {
			binario=binario+"1";
			i++;
		}
		binario=rellenarCon0deAtrasParaAdelante(binario);
		
		return binario;
	}
	
	private ArrayList<String> convertirStringMascaraAarraySeparadoen4(String mascaraBinaria){
		ArrayList<String>mascaraBinariaSeparada=new ArrayList<String>();
		
		mascaraBinariaSeparada.add(mascaraBinaria.substring(0,7));
		mascaraBinariaSeparada.add(mascaraBinaria.substring(8,15));
		mascaraBinariaSeparada.add(mascaraBinaria.substring(16,23));
		mascaraBinariaSeparada.add(mascaraBinaria.substring(24,31));
		
		return mascaraBinariaSeparada;
	}
	
	
	/*@
	 * Devuelve un string con la ip de subred*/
	public String calcularSubred(String ipv4, int mask) {
		int indice=0;
		int contadorPuntos=0;
		ArrayList<String>IpSeparadaEnOctetos=new ArrayList<String>();
		ArrayList<String>IpSeparadaEnOctetosBinarios=new ArrayList<String>();
		ArrayList<String>mascaraSeparadaEnOctetosBinarios;
		ArrayList<String>IpSubred=new ArrayList<String>();
		
		
		mascaraSeparadaEnOctetosBinarios=convertirStringMascaraAarraySeparadoen4(
				convertirMascaraAbinaria(mask)
				);
		
		
		
		
		String subred=null;
				
		for(int i=0;i<ipv4.length();i++) {
			 
				if(contadorPuntos==3) {
					IpSeparadaEnOctetos.add(ipv4.substring(indice,ipv4.length()));
					i=ipv4.length();
			
			}else {
				
				if(ipv4.charAt(i)=='.') {
					IpSeparadaEnOctetos.add(ipv4.substring(indice,i));
					contadorPuntos++;
					indice=i+1;	
					
					}
			}
		}
		
		int j=0;
		while(j<IpSeparadaEnOctetos.size()) {
			IpSeparadaEnOctetosBinarios.add(
			crearBinario8Bits(Integer.parseInt(IpSeparadaEnOctetos.get(j)))
		);
		j++;
		}// aca está el error
		while(j<IpSeparadaEnOctetosBinarios.size()) {
			IpSubred.add(hacerAndDosNrosBinarios(
					IpSeparadaEnOctetosBinarios.get(j),
					mascaraSeparadaEnOctetosBinarios.get(j)));
					System.out.println(IpSeparadaEnOctetosBinarios.get(j));
			j++;
		}
		
				
		
		
		return subred;
	}





	public int calcularNroHosts(String mask) {
		int nroTotalBitsPosibles=32;
		int nroBitsIngresantes=Integer.parseInt(mask);
		int bitsParaHosts=nroTotalBitsPosibles-nroBitsIngresantes;
		int nroTotalHosts=1;
		
		for(int i=0;i<bitsParaHosts;i++) {
			nroTotalHosts=2*nroTotalHosts;		
		}		
		return nroTotalHosts-2;
	}



	private String resultadoAnd(String param1,String param2) {
		if(Integer.valueOf(param1)+Integer.valueOf(param2)==2) {
			return "1";
		}else {
			return "0";
		}		
	
	}

	public String  hacerAndDosNrosBinarios(String octetoIPv4Binario,String octetoMascaraBinario) {

		ArrayList<String> ipBool=new ArrayList<String>();
		ArrayList<String> maskBool=new ArrayList<String>();
		ArrayList<String> subredBool=new ArrayList<String>();
		
		String ipBinaria=rellenarCon0(octetoMascaraBinario);
		String mascara=rellenarCon0(octetoMascaraBinario);		
		
		this.binarioIP=ipBinaria.toCharArray();
		this.binarioMascara=mascara.toCharArray();
		
		
		int i=0;
		while(i<8) {
			if(this.binarioIP[i]=='1') {
				ipBool.add("1");
			}else 
				if(this.binarioIP[i]=='0'){
					ipBool.add("0");
				}

			if(this.binarioMascara[i]=='1') {
				maskBool.add("1");
			}else 
				if(this.binarioMascara[i]=='0'){
					maskBool.add("0");
				}
			i++;		
		}

		int j=0;
		while(j<8) {
			subredBool.add(resultadoAnd(ipBool.get(j),maskBool.get(j)));

			j++;
		}

		System.out.println("IP");
		System.out.println(ipBool);
		System.out.println("Mascara");
		System.out.println(maskBool);
		System.out.println("Subred a la que pertenece");
		System.out.println(subredBool);
		
		
		
		return subredBool.toString();



	}


	public String crearBinario8Bits(int nro) {
		if(nro>255) {
			return "Error, máximo nro permitido 255";
		}
		String nroBinario;
		int numero, exp, digito;
		int binario;

		numero = nro;
		while(numero<0);

		exp=0;
		binario=0;
		while(numero!=0){
			digito = numero % 2;            
			binario = (int) (binario + digito * Math.pow(10, exp));   
			exp++;
			numero = numero/2;
		}
		nroBinario=String.valueOf(binario);
		if(nroBinario.length()<8) {
			nroBinario=rellenarCon0(nroBinario);
		}

		return  nroBinario;
	}

	public String rellenarCon0(String nroBinario) {
		String convertido = nroBinario;
		while(convertido.length()<8) {
			convertido="0"+convertido;
		}

		return convertido;
	}
	
	public String rellenarCon0deAtrasParaAdelante(String nroBinario) {
		String convertido = nroBinario;
		while(convertido.length()<8) {
			convertido=convertido+"0";
		}

		return convertido;
	}






























}