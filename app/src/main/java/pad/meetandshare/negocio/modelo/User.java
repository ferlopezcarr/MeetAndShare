package pad.meetandshare.negocio.modelo;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

    private String uid;

    private String email;

    private String name;

    private Date birthday;

    private String description;

    private String photoUrl;

    private List<Category> categories;

    private List<String> registerActivities;

    private List<String> createdActivities;

    private String type="person";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private boolean active;

    // -- Datos para integracion -- //
    public static final String UsersDataBaseName = "users";

    // -------- METODOS -------- //
    /**
     * Constructora por defecto de Usuario
     */
    public User() {}

    /**
     * Constructora con argumentos de Usuario
     * @param email
     * @param name
     * @param birthday
     * @param description
     */
    public User(String email, String name , Date birthday, String description, ArrayList<Category> lista) {
        this.email = email;
        this.name = name;
        this.birthday = birthday;
        this.description = description;
        this.photoUrl = null; // foto por defecto
        categories = lista;
        this.active = true;
    }

    /**
     * Constructora con argumentos de Usuario
     * @param email
     * @param name
     * @param birthday
     * @param description
     * @param foto
     */
    public User(String email, String name, Date birthday, String description, String foto) {
        this.email = email;
        this.name = name;
        this.birthday = birthday;
        this.description = description;
        this.photoUrl = foto;
        this.active = true;
        categories = new ArrayList<Category>();
        this.registerActivities = new ArrayList<String>();
        this.createdActivities = new ArrayList<String>();
    }


    // -- GETTERS Y SETTERS -- //

    public String getUid() { return uid; }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void SetPhotoUrl(String foto) {
        this.photoUrl = foto;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    // -- Categorias -- //

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public boolean addCategory(Category category) {
        if (categories == null) {
            categories = new ArrayList<Category>();
        }

        if (categories.contains(category)) {
            return false;
        }
        else {
            categories.add(category);
            return true;
        }
    }

    public boolean deleteCategory(Category category) {
        if (categories == null || categories.isEmpty()) {
            return false;
        }

        if (categories.contains(category)) {
            categories.remove(category);
            return true;
        }
        else {
            return false;
        }
    }

    public void replaceAllCategories(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
    }


    // -- Actividades Inscritas -- //

    public List<String> getRegisteredActivityIds() {
        return registerActivities;
    }

    public void setRegisteredActivityIds(List<String> actividades) {
        this.registerActivities = actividades;
    }

    public boolean addRegisteredActivityId(String actividad) {
        if (registerActivities == null) {
            registerActivities = new ArrayList<String>();
        }

        if (registerActivities.contains(actividad)) {
            return false;
        }
        else {
            registerActivities.add(actividad);
            return true;
        }
    }

    public boolean deleteRegisteredActivityId(String actividad) {
        if (registerActivities == null || registerActivities.isEmpty()) {
            return false;
        }

        if (registerActivities.contains(actividad)) {
            registerActivities.remove(actividad);
            return true;
        }
        else {
            return false;
        }
    }

    public void replaceAllRegisteredActivityIds(List<String> actividades) {
        this.registerActivities.clear();
        this.registerActivities.addAll(actividades);
    }

    // -- Actividades Creadas -- //

    public List<String> getCreatedActivityIds() {
        return createdActivities;
    }

    public void setCreatedActivityIds(List<String> actividades) {
        this.createdActivities = actividades;
    }

    public boolean addCreatedActivityId(String actividad) {
        if (createdActivities == null) {
            createdActivities = new ArrayList<String>();
        }

        if (createdActivities.contains(actividad)) {
            return false;
        }
        else {
            createdActivities.add(actividad);
            return true;
        }
    }

    public boolean deleteCreatedActivityId(String actividad) {
        if (createdActivities == null || createdActivities.isEmpty()) {
            return false;
        }

        if (createdActivities.contains(actividad)) {
            createdActivities.remove(actividad);
            return true;
        }
        else {
            return false;
        }
    }

    public void replaceAllCreatedActivityIds(List<String> actividades) {
        this.createdActivities.clear();
        this.createdActivities.addAll(actividades);
    }
}

