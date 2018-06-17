package pad.meetandshare.negocio.modelo;

import java.util.Arrays;

public enum Categoria {

    //Mantenerlo ordenado para facilitar la busqueda
    Aventura("Aventura"),
    Baile("Baile"),
    Belleza("Belleza"),
    CienciaFiccion("Ciencia Ficción"),
    Cine("Cine"),
    Cocina("Cocina"),
    Comida("Comida"),
    Deportes("Deportes"),
    Fotografia("Fotografía"),
    Friki("Friki"),
    Idiomas("Idiomas"),
    Libros("Libros"),
    Mascotas("Mascotas"),
    Miedo("Miedo"),
    Moda("Moda"),
    Musica("Música"),
    Naturaleza("Naturaleza"),
    Series("Series"),
    Television("Televisión"),
    Viajes("Viajes"),
    Videojuegos("Videojuegos");

    //hay que ir rellenando

    private final String displayName;

    Categoria(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Categoria getCategoria(String displayName) {
        Categoria res = null;
        Categoria categoriasEnumValues[] = Categoria.values();

        int i = 0;
        boolean found = false;
        while(i < categoriasEnumValues.length && !found) {
            found = (categoriasEnumValues[i].getDisplayName().equalsIgnoreCase(displayName));
            i++;
        }

        if(found)
            res = categoriasEnumValues[i-1];

        return res;
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
