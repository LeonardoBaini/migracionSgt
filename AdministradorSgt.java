import java.util.ArrayList;

public class AdministradorSgt {
	
	
public static void guardarTotem(TotemSgt totem) {
	int errores=BuscadorErrores.erroresTotem(totem).size();
	if(errores!=0) {
		for(int i=0;i<errores;i++) {
			System.out.println(BuscadorErrores.erroresTotem(totem).get(i));
		}
	}else {
		System.out.println("No se encontraron errores...");
	}
		
}


private void guardarServiceDevices() {
	
}
private void guardarServiceCameras() {
	
}









}
