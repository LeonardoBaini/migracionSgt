
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
		String ipDelRango;
		ArrayList<String> rangoIp=new ArrayList<String>();
		ArrayList<Integer>subred;
		
		int nroHosts=calcularNroHosts(mask);
		
		subred=calcularSubred(ipv4,Integer.parseInt(mask));		
		
		//System.out.println("La subred es.."+subred+" y los host son "+nroHosts);
		int i=1;
		int j=1;
		
		while(i<=nroHosts) {
			ipDelRango=subred.get(0)+"."+subred.get(1)+"."+subred.get(2)+"."+sum(subred.get(3),j);
			rangoIp.add(ipDelRango);
			System.out.println();
			if(
				   sum(subred.get(3),j)>254){
				   System.out.println("Epppaaaa");
				   break;
			}
				
			i++;
			j++;
		}
	
		
		return rangoIp;			
	
	}
	private int sum(int a, int b) {
		return a+b;
	}
	private String convertirMascaraAbinaria(int mascara) {
		String binario = "1";
		int i=1;
		while(i<mascara) {
			binario="1"+binario;
			i++;
		}
		binario=rellenarCon0deAtrasParaAdelante(binario);
		
		return binario;
	}
	
	private ArrayList<String> convertirStringMascaraAarraySeparadoen4(String mascaraBinaria){
		ArrayList<String>mascaraBinariaSeparada=new ArrayList<String>();
		
		mascaraBinariaSeparada.add(mascaraBinaria.substring(0,8));
		mascaraBinariaSeparada.add(mascaraBinaria.substring(8,16));
		mascaraBinariaSeparada.add(mascaraBinaria.substring(16,24));
		mascaraBinariaSeparada.add(mascaraBinaria.substring(24,32));
		
		return mascaraBinariaSeparada;
	}
	
	
	/*@
	 * Devuelve un string con la ip de subred*/
	public ArrayList<Integer> calcularSubred(String ipv4, int mask) {
		int indice=0;
		int contadorPuntos=0;
		ArrayList<String>IpSeparadaEnOctetos=new ArrayList<String>();
		ArrayList<String>IpSeparadaEnOctetosBinarios=new ArrayList<String>();
		ArrayList<String>mascaraSeparadaEnOctetosBinarios;
		ArrayList<Integer>IpSubred=new ArrayList<Integer>();
		
		
		mascaraSeparadaEnOctetosBinarios=convertirStringMascaraAarraySeparadoen4(
				convertirMascaraAbinaria(mask)
				);	
		
		
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
		}
		j=0;
		while(j<IpSeparadaEnOctetosBinarios.size()) {
			
			IpSubred.add(
					
					binarioaDecimal(Integer.parseInt(
					
					hacerAndDosNrosBinarios(
					IpSeparadaEnOctetosBinarios.get(j),
					mascaraSeparadaEnOctetosBinarios.get(j)))
					
					));
			
			
					
			j++;
			
		}
		
		
				
		
		
		return IpSubred;
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

	
	public String  hacerAndDosNrosBinarios(String octetoIPv4Binario,String octetoMascaraBinario) {

		String subred=null;
				
		int i=0;
		while(i<octetoIPv4Binario.length()) {
			
			if(octetoIPv4Binario.charAt(i)=='1' && octetoMascaraBinario.charAt(i)=='1') {
				
				if(subred==null) {subred="1";}else{subred=subred+"1";}	;
				
				}else {
					
				if(subred==null) {subred="0";}else{subred=subred+"0";}	;
			}
			i++;
	}
		return subred;
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
		while(convertido.length()<32) {
			convertido=convertido+"0";
		}

		return convertido;
	}
	
	
	public static int binarioaDecimal(int number) {
        int decimal = 0;
        int binary = number;
        int power = 0;
 
        while (binary != 0) {
            int lastDigit = binary % 10;
            decimal += lastDigit * Math.pow(2, power);
            power++;
            binary = binary / 10;
        }
        return decimal;
    }






























}