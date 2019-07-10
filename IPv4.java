
import java.util.ArrayList;
import java.util.List;

public class IPv4 {

	String ipv4;
	int mascara;
	ArrayList <String> ipv4Array=new ArrayList<String>();
	char[] binarioIP;
	char[] binarioMascara;
	char[] binarioSubred;

	public void rangoDeIp(int ipv4){

		this.binarioIP=crearBinario8Bits(ipv4).toCharArray();
		System.out.println(this.binarioIP);		
	}

	public void hacerAndDosNros(int ipv4,int mascara) {

		ArrayList<Boolean> ipBool=new ArrayList<Boolean>();
		ArrayList<Boolean> maskBool=new ArrayList<Boolean>();
		ArrayList<Boolean> subredBool=new ArrayList<Boolean>();
		this.binarioIP=crearBinario8Bits(ipv4).toCharArray();
		this.binarioMascara=crearBinario8Bits(mascara).toCharArray();
		int i=0;
		while(i<8) {
			if(this.binarioIP[i]=='1') {
				ipBool.add(true);
			}else 
				if(this.binarioIP[i]=='0'){
					ipBool.add(false);
				}

			if(this.binarioMascara[i]=='1') {
				maskBool.add(true);
			}else 
				if(this.binarioMascara[i]=='0'){
					maskBool.add(false);
				}
			i++;		
		}

		int j=0;
		while(j<8) {
			subredBool.add(ipBool.get(j)&&maskBool.get(j));

			j++;
		}

		System.out.println("IP");
		System.out.println(ipBool);
		System.out.println("Mascara");
		System.out.println(maskBool);
		System.out.println("Subred a la que pertenece");
		System.out.println(subredBool);



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






























}