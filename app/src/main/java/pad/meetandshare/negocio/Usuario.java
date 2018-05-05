package pad.meetandshare.negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {

    private Integer id;

    private String email;

    private String nombre;

    private String password;

    private Date fechaNacimiento;

    private String descripcion;

    private String foto;

    private List<Categoria> categorias;

    public Usuario() {}

    public Usuario(String email, String nombre, String password, Date fechaNacimiento, String descripcion) {
        this.email = email;
        this.nombre = nombre;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.descripcion = descripcion;
        this.foto = foto; // foto por defecto

        categorias = new ArrayList<Categoria>();
    }

    public Usuario(String email, String nombre, String password, Date fechaNacimiento, String descripcion, String foto) {
        this.email = email;
        this.nombre = nombre;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.descripcion = descripcion;
        this.foto = foto;

        categorias = new ArrayList<Categoria>();
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

