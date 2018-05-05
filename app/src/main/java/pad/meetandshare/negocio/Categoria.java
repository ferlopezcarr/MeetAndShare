package pad.meetandshare.negocio;

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
}
