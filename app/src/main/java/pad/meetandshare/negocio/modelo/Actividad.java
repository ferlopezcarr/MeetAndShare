package pad.meetandshare.negocio.modelo;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pad.meetandshare.actividades.FechaUtil;

public class Actividad {

    public final static String ActivitiesDatabaseName = "activities";

    private String uid;

    private String nombre;
    private static final String NOMBRE_PATTERN = "^([a-zA-ZáéíóúñÁÉÍÓÚÑ ])*$";

    private Date fechaInicio;

    private Date fechaFin;

    private int maxParticipantes;
    //private static final String MAX_PARTICIPANTES_PATTERN = "^([0-9])*$";

    private String descripcion;

    private Ubicacion ubicacion;

    private String idAdministrador;

    private List<Categoria> categorias;

    private List<String> idUsuariosInscritos;

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
     * @param maxParticipantes
     * @param descripcion
     * @param ubicacion
     */
    public Actividad(String nombre, Date fechaInicio, Date fechaFin, int maxParticipantes, String descripcion, Place ubicacion, String idAdministrador) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.maxParticipantes = maxParticipantes;
        this.descripcion = descripcion;
        this.ubicacion = new Ubicacion(ubicacion);
        this.idAdministrador = idAdministrador;
        this.activa = true;
        this.finalizada = false;

        this.categorias = new ArrayList<Categoria>();
        this.idUsuariosInscritos = new ArrayList<String>();
        if(!this.idUsuariosInscritos.contains(idAdministrador)) {
            this.idUsuariosInscritos.add(idAdministrador);
        }
    }


    /* PARSERS */

    /**
     * Valida el nombre
     * @param nombre
     * @return
     */
    public static boolean isValidNombre(String nombre) {
        Pattern pattern = Pattern.compile(NOMBRE_PATTERN);
        Matcher matcher = pattern.matcher(nombre);
        return matcher.matches();
    }

    /**
     * Valida la fecha de inicio de la actividad
     * @param fechaIni
     * @return
     */
    public static boolean isValidFechaIni(Date fechaIni) {
        FechaUtil fechaUtil = new FechaUtil();
        Date today = new Date();
        today.setTime(0);
        int resCompareDates = fechaIni.compareTo(today);

        return (resCompareDates >= 0); //hoy o despues
    }

    public static boolean isValidHora(String horaIniString) {
        String hora[] = horaIniString.split(":");
        String horas = hora[0];
        String minutos = hora[1];

        return (Integer.parseInt(horas) < 24 && Integer.parseInt(horas) >= 0 ) &&
                (Integer.parseInt(minutos) < 60 && Integer.parseInt(minutos) >= 0);
    }

    /**
     * Valida la fecha de fin de la actividad
     * @param fechaIni
     * @param fechaFin
     * @return
     */
    public static boolean isValidFechaFin(Date fechaIni, Date fechaFin) {
        int resCompareToday = fechaFin.compareTo(new Date());
        int resCompareFechaIni = fechaFin.compareTo(fechaIni);

        return (resCompareToday >= 0 && resCompareFechaIni >= 0); //hoy o despues
    }

    /**
     * Valida el numero de participantes
     * @param maxParticipantesString
     * @return
     */
    public static boolean isValidMaxParticipantes(String maxParticipantesString) {
        return (Integer.parseInt(maxParticipantesString) > 1);
    }


    /* GETTERS Y SETTERS */

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getMaxParticipantes() {
        return this.maxParticipantes;
    }

    public void setMaxParticipantes(int maxParticipantes) {
        this.maxParticipantes = maxParticipantes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getIdAdministrador() {
        return this.idAdministrador;
    }

    public void setIdAdministrador(String idAdministrador) {
        this.idAdministrador = idAdministrador;
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

    public List<String> getIdUsuariosInscritos() {
        return this.idUsuariosInscritos;
    }

    public void setUsuariosInscritos(List<String> idUsuariosInscritos) {
        this.idUsuariosInscritos = idUsuariosInscritos;
    }

    public boolean addUsuario(String idUsuario) {
        if (idUsuariosInscritos == null) {
            idUsuariosInscritos = new ArrayList<String>();
        }

        if (idUsuariosInscritos.contains(idUsuario)) {
            return false;
        }
        else {
            idUsuariosInscritos.add(idUsuario);
            return true;
        }
    }

    public boolean deleteUsuario(String idUsuario) {
        if (idUsuariosInscritos == null || idUsuariosInscritos.isEmpty()) {
            return false;
        }

        if (idUsuariosInscritos.contains(idUsuario)) {
            idUsuariosInscritos.remove(idUsuario);
            return true;
        }
        else {
            return false;
        }
    }

}
