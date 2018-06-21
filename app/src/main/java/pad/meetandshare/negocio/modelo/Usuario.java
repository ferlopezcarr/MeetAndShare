package pad.meetandshare.negocio.modelo;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {

    private String uid;

    private String email;

    private String nombre;

    private Date fechaNacimiento;

    private String descripcion;

    private String foto;

    private List<Categoria> categorias;

    private boolean activo;

    // -- Datos para integracion -- //
    public static final String UsersDataBaseName = "users";

    // -------- METODOS -------- //
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
        this.foto = null; // foto por defecto
        categorias = lista;
        this.activo = true;
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


    // -- Categorias -- //

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

