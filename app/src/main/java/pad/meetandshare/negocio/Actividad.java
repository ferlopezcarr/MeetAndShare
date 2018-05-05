package pad.meetandshare.negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Actividad {

    private Integer id;

    private String nombre;

    private Date fechaInicio;

    private Date fechaFin;

    private int numParticipantesMax;

    private String descripcion;

    private String ubicacion;

    private List<Categoria> categorias;

    private List<Usuario> usuariosInscritos;

    private boolean activa;

    private boolean finalizada;


    /**
     * Constructoria por defecto de Actividad
     */
    public Actividad() {}

    /**
     * Constructora con argumentos de Actividad
     * @param nombre
     * @param fechaInicio
     * @param fechaFin
     * @param numParticipantesMax
     * @param descripcion
     * @param ubicacion
     */
    public Actividad(String nombre, Date fechaInicio, Date fechaFin, int numParticipantesMax, String descripcion, String ubicacion) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numParticipantesMax = numParticipantesMax;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion; // foto por defecto
        this.activa = true;
        this.finalizada = false;

        this.categorias = new ArrayList<Categoria>();
        this.usuariosInscritos = new ArrayList<Usuario>();
    }

    /*
     * Constructora con argumentos de Actividad
     * @param nombre
     * @param fechaInicio
     * @param fechaFin
     * @param numParticipantesMax
     * @param descripcion
     */
    public Actividad(String nombre, Date fechaInicio, Date fechaFin, int numParticipantesMax, String descripcion) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numParticipantesMax = numParticipantesMax;
        this.descripcion = descripcion;
        this.ubicacion = null; // foto por defecto
        this.activa = true;
        this.finalizada = false;

        this.categorias = new ArrayList<Categoria>();
        this.usuariosInscritos = new ArrayList<Usuario>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getNumParticipantesMax() {
        return this.numParticipantesMax;
    }

    public void setNumParticipantesMax(int numParticipantesMax) {
        this.numParticipantesMax = numParticipantesMax;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
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

    /* --- Usuarios inscritos --- */

    public List<Usuario> getUsuariosInscritos() {
        return this.usuariosInscritos;
    }

    public void setUsuariosInscritos(List<Usuario> usuariosInscritos) {
        this.usuariosInscritos = usuariosInscritos;
    }

    public boolean addUsuario(Usuario usuario) {
        if (usuariosInscritos == null) {
            usuariosInscritos = new ArrayList<Usuario>();
        }

        if (usuariosInscritos.contains(usuario)) {
            return false;
        }
        else {
            usuariosInscritos.add(usuario);
            return true;
        }
    }

    public boolean deleteCategoria(Usuario usuario) {
        if (usuariosInscritos == null || usuariosInscritos.isEmpty()) {
            return false;
        }

        if (usuariosInscritos.contains(usuario)) {
            usuariosInscritos.remove(usuario);
            return true;
        }
        else {
            return false;
        }
    }

}
