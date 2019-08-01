package Clases;

import java.sql.SQLException;

public class main {

	public static void main(String[] args)  {
		//1242594
		//args[0]="1182447";


if(AdministradorSgt.testearConexionBases()) {
	TotemSgt totem=new TotemSgt("1182447"); 
	AdministradorSgt.guardarTotem(totem, null);
	AdministradorSgt.obtenerServiceCameras("1182447");
};


		
 }
}


