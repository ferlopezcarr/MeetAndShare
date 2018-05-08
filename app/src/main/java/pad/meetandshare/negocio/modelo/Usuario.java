package pad.meetandshare.negocio.modelo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {

    private Integer id;

    private String email;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private String nombre;

    private static final String NOMBRE_PATTERN = "^([a-zA-ZáéíóúñÁÉÍÓÚÑ ])*$";

    private Date fechaNacimiento;

    private String descripcion;

    private String foto;

    private List<Categoria> categorias;

    private boolean activo;


    /**
     * Constructora por defecto de Usuario
     */
    public Usuario() {}

    /**
     * Constructora con argumentos de Usuario
     * @param email
     * @param nombre
     * @param fechaNacimiento
     * @param descripcion
     */
    public Usuario(String email, String nombre , Date fechaNacimiento, String descripcion, ArrayList<Categoria> lista) {
        this.email = email;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.descripcion = descripcion;
        this.foto = foto; // foto por defecto
        this.activo = true;
        categorias = lista;
    }

    public Usuario(String email, String nombre,Date fecha,ArrayList<Categoria> lista, String descripcion  ){
        this.email = email;
        this.nombre = nombre;
        this.fechaNacimiento = fecha;
        this.categorias = lista;
        this.activo = true;
        this.descripcion = descripcion;
    }

    /**
     * Constructora con argumentos de Usuario
     * @param email
     * @param nombre
     * @param fechaNacimiento
     * @param descripcion
     * @param foto
     */
    public Usuario(String email, String nombre, Date fechaNacimiento, String descripcion, String foto) {
        this.email = email;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.descripcion = descripcion;
        this.foto = foto;
        this.activo = true;
        categorias = new ArrayList<Categoria>();
    }


    /* PARSERS */

    /**
     * Valida el email
     * @param email
     * @return
     */
    private boolean isValidEmail(String email) {
        //pattern de gps "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$"
        if(email != null && email.length() > 5) { //"x@x.x".length() = 5
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        else
            return false;
    }

    /**
     * Valida el nombre
     * @param nombre
     * @return
     */
    private boolean isValidNombre(String nombre) {
        if(nombre != null && nombre.length() > 0) {
            Pattern pattern = Pattern.compile(NOMBRE_PATTERN);
            Matcher matcher = pattern.matcher(nombre);
            return matcher.matches();
        }
        else
            return false;
    }

    /**
     * Valida la contraseña
     * @param password
     * @return
     */
    private boolean isValidPassword(String password) {
        return (password != null && password.length() >=4);
    }


    /* GETTERS Y SETTERS */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /* --- Categorias --- */

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public boolean addCategoria(Categoria categoria) {
        if (categorias == null) {
            categorias = new ArrayList<Categoria>();
        }

        if (categorias.contains(categoria)) {
            return false;
        }
        else {
            categorias.add(categoria);
            return true;
        }
    }

    public boolean deleteCategoria(Categoria categoria) {
        if (categorias == null || categorias.isEmpty()) {
            return false;
        }

        if (categorias.contains(categoria)) {
            categorias.remove(categoria);
            return true;
        }
        else {
            return false;
        }
    }

    public void replaceAllCategorias(List<Categoria> categorias) {
        this.categorias.clear();
        this.categorias.addAll(categorias);
    }

}

