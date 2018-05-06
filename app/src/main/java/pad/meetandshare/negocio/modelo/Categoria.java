package pad.meetandshare.negocio.modelo;

import java.util.Arrays;

public enum Categoria {

    //Mantenerlo ordenado para facilitar la busqueda
    Aventura("Aventura"),
    CienciaFiccion("Ciencia Ficción"),
    Cine("Cine"),
    Deportes("Deportes"),
    Mascotas("Mascotas");
    //hay que ir rellenando

    private final String displayName;

    Categoria(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }



    public static String[] getArray() {
        String[] array =new String[Categoria.values().length];
        int i=0;
        for (Categoria cat : Categoria.values()) {

            array[i]=cat.displayName;
            ++i;
        }
        return array;
    }

}
