package pad.meetandshare.negocio.modelo;

public enum Category {

    //Mantenerlo ordenado para facilitar la busqueda
    Aventura("Aventura", "fab fa-fort-awesome"),//hiking
    Baile("Baile", "fas fa-child"),
    Belleza("Belleza", "fas fa-heart"),//heartbeat
    Cine("Cine", "fas fa-film"),
    Cocina("Cocina", "fas fa-utensils"),
    Comida("Comida", "fas fa-drumstick-bite"),
    Deportes("Deportes", "fas fa-futbol"),
    Fotografia("Fotografía", "fas fa-camera-retro"),
    Friki("Friki", "fab fa-rebel"),
    Idiomas("Idiomas", "fas fa-language"),
    Libros("Libros", "fas fa-book-open"),
    Mascotas("Mascotas", "fas fa-paw"),
    Miedo("Miedo", "fas fa-ghost"),
    Moda("Moda", "fas fa-tshirt"),
    Musica("Música", "fas fa-music"),
    Naturaleza("Naturaleza", "fas fa-tree"),
    Television("Televisión", "fas fa-tv"),
    Viajes("Viajes", "fas fa-plane"),
    Videojuegos("Videojuegos", "fas fa-gamepad");

    //hay que ir rellenando

    private final String displayName;
    private final String iconName;

    Category(String displayName, String iconName) {
        this.displayName = displayName;
        this.iconName = iconName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIconName() {
        return iconName;
    }

    public static Category getCategory(String displayName) {
        Category res = null;
        Category categoryEnumValues[] = Category.values();

        int i = 0;
        boolean found = false;
        while(i < categoryEnumValues.length && !found) {
            found = (categoryEnumValues[i].getDisplayName().equalsIgnoreCase(displayName));
            i++;
        }

        if(found)
            res = categoryEnumValues[i-1];

        return res;
    }

    public static String getDisplayNameFromStringValue(String valueStr) {
        Category res = null;
        Category categoryEnumValues[] = Category.values();

        int i = 0;
        boolean found = false;
        while(i < categoryEnumValues.length && !found) {
            found = (categoryEnumValues[i].toString().equalsIgnoreCase(valueStr));
            i++;
        }

        if(found)
            res = categoryEnumValues[i-1];

        return res.getDisplayName();
    }

    public static String getIconNameFromStringValue(String valueStr) {
        Category res = null;
        Category categoryEnumValues[] = Category.values();

        int i = 0;
        boolean found = false;
        while(i < categoryEnumValues.length && !found) {
            found = (categoryEnumValues[i].toString().equalsIgnoreCase(valueStr));
            i++;
        }

        if(found)
            res = categoryEnumValues[i-1];

        return res.getIconName();
    }


    public static String[] getArray() {
        String[] array =new String[Category.values().length];
        int i=0;
        for (Category cat : Category.values()) {

            array[i]=cat.displayName;
            ++i;
        }
        return array;
    }

}
