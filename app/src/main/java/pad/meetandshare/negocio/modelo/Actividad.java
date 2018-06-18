package pad.meetandshare.negocio.modelo;


import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Actividad implements Serializable {

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

    public static String rootDataBase(String userUid) {
        return Usuario.UsersDataBaseName + "/" + userUid + "/" + ActivitiesDatabaseName;
    }


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
    public Actividad(String nombre, Date fechaInicio, Date fechaFin, int maxParticipantes, String descripcion, Ubicacion ubicacion, ArrayList<Categoria> lista, String idAdministrador) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.maxParticipantes = maxParticipantes;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.idAdministrador = idAdministrador;
        this.activa = true;
        this.finalizada = false;
        this.categorias = lista;
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
        return (fechaIni.after(new Date())); //despues de ahora
    }

    public static boolean isOnlyFechaIniLaterThanToday(Date fechaIni) {

        Date todayWithHour = new Date();
        String todayStr = FechaUtil.getDateFormat().format(todayWithHour);

        Date today = FechaUtil.dateCorrectFormat(todayStr, "00:00");

        return (fechaIni.compareTo(today) >= 0);//hoy o despues
    }

    public static boolean isValidHora(String horaString) {
        String hora[] = horaString.split(":");
        String horas = hora[0];
        String minutos = hora[1];

        return (Integer.parseInt(horas) < 24 && Integer.parseInt(horas) >= 0 ) &&
                (Integer.parseInt(minutos) < 60 && Integer.parseInt(minutos) >= 0);
    }

    public static boolean isOnlyFechaFinLaterThanFechaIni(String fechaIniString, String fechaFinString) throws ParseException {

        Date fechaIniWithOutHour = FechaUtil.getDateFormat().parse(fechaIniString);
        Date fechaFinWithOutHour = FechaUtil.getDateFormat().parse(fechaFinString);

        return (fechaFinWithOutHour.compareTo(fechaIniWithOutHour) >= 0);
    }

    public static boolean isValidFechaFin(Date fechaIni, Date fechaFin) {
        return fechaFin.after(fechaIni); //despues de ahora
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
        return fechaFin;
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

    public boolean addUsuario(String uidUsuario) {
        if (idUsuariosInscritos == null) {
            idUsuariosInscritos = new ArrayList<String>();
        }

        if (idUsuariosInscritos.contains(uidUsuario)) {
            return false;
        }
        else {
            idUsuariosInscritos.add(uidUsuario);
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

    public static Actividad checkInputActividad(
            Activity activity,
            String[] listItems,
            boolean[] checkedItems,
            Ubicacion ubicacionSeleccionada,
            String uid,
            EditText etNombre,
            EditText etFechaIni,
            EditText etHoraIni,
            EditText etFechaFin,
            EditText etHoraFin,
            EditText etMaxParticipantes,
            EditText etDescripcion
    ) {

        Actividad act = null;

        //OBTENER ELEMENTOS DE LA VISTA
        String nombre = etNombre.getText().toString();
        String fechaIniString = etFechaIni.getText().toString();
        String horaIniString = etHoraIni.getText().toString();
        String fechaFinString = etFechaFin.getText().toString();
        String horaFinString = etHoraFin.getText().toString();
        String maxParticipantesString = etMaxParticipantes.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        int maxParticipantes = 0;
        Date fechaIni = null;
        long horaIni = 0;
        Date fechaFin = null;
        long horaFin = 0;

        ArrayList<Categoria> intereses = new ArrayList<>();

        for (int i = 0; i < checkedItems.length; ++i) {
            if (checkedItems[i]) {
                Categoria cat = Categoria.getCategoria(listItems[i]);
                intereses.add(cat);
            }
        }

        boolean nombreOk = false;
        boolean fechaIniOK = false;
        boolean horaIniOK = false;
        boolean fechaFinOK = false;
        boolean horaFinOK = false;
        boolean maxParticipantesOK = false;
        View focusView = null;

        //QUITAR ESPACIOS AL PRINCIPIO Y FINAL DE CADA INPUT
        nombre = nombre.trim();
        fechaIniString = fechaIniString.trim();
        horaIniString = horaIniString.trim();
        fechaFinString = fechaFinString.trim();
        horaFinString = horaFinString.trim();
        descripcion = descripcion.trim();

        final String campoObligatorio = "Por favor, rellene todos los campos";

        //NOMBRE
        if (nombre == null || nombre.isEmpty()) {
            etNombre.setError(campoObligatorio);
            if(focusView != null)
                focusView = etNombre;
        } else if (!isValidNombre(nombre)) {
            etNombre.setError("El nombre introducido no es válido, sólo puede contener letras");
            if(focusView != null)
                focusView = etNombre;
        } else {
            nombreOk = true;
        }

        //FECHA INI
        if (fechaIniString == null || fechaIniString.isEmpty()) {
            etFechaIni.setError(campoObligatorio);
            if(focusView != null)
                focusView = etFechaIni;
        } else {
            try {
                fechaIni = FechaUtil.getDateFormat().parse(fechaIniString);

                if(!isOnlyFechaIniLaterThanToday(fechaIni)) {
                    etFechaIni.setError("La fecha de inicio debe ser hoy o posterior");
                    if(focusView != null)
                        focusView = etFechaIni;
                }
                else {
                    fechaIniOK = true;
                }
            } catch (ParseException e) {
                etFechaIni.setError("Formato de fecha incorrecto");
                if(focusView != null)
                    focusView = etFechaIni;
            }
        }

        //HORA INI
        if (horaIniString == null || horaIniString.isEmpty()) {
            etHoraIni.setError(campoObligatorio);
            if(focusView != null)
                focusView = etHoraIni;
        } else {
            horaIniString = FechaUtil.horaCorrectFormat(horaIniString);

            if (!Actividad.isValidHora(horaIniString)) {
                etHoraIni.setError("Formato de hora incorrecto");
                if(focusView != null)
                    focusView = etHoraIni;
            } else {
                if (fechaIniOK) {
                    fechaIni = FechaUtil.dateCorrectFormat(fechaIniString, horaIniString);

                    horaIniOK = Actividad.isValidFechaIni(fechaIni);
                    if (!horaIniOK) {
                        etHoraIni.setError("La hora de inicio debe ser ahora o posterior");
                        if(focusView != null)
                            focusView = etHoraIni;
                    }
                } else {
                    etHoraIni.setError("Introduce una fecha de inicio correcta");
                    if(focusView != null)
                        focusView = etFechaIni;
                }
            }
        }

        if (fechaIniOK && horaIniOK) {
            //FECHA FIN
            if (fechaFinString == null || fechaFinString.isEmpty()) {
                etFechaFin.setError(campoObligatorio);
                if(focusView != null)
                    focusView = etFechaFin;
            } else {
                try {
                    fechaFin = FechaUtil.getDateFormat().parse(fechaFinString);
                } catch (ParseException e) {
                    fechaFinOK = true;
                    etFechaFin.setError("Formato de fecha incorrecto");
                    if(focusView != null)
                        focusView = etFechaFin;
                }
            }

            //HORA FIN
            if (horaFinString == null || horaFinString.isEmpty()) {
                etHoraFin.setError(campoObligatorio);
                if(focusView != null)
                    focusView = etHoraFin;
            } else {
                horaFinString = FechaUtil.horaCorrectFormat(horaFinString);

                try {
                    if (!isOnlyFechaFinLaterThanFechaIni(fechaIniString, fechaFinString)) {
                        etFechaFin.setError("La fecha de inicio debe ser igual o posterior a la fecha de fin");
                        if(focusView != null)
                            focusView = etFechaFin;
                    }
                    else {
                        fechaFinOK = true;

                        if (!Actividad.isValidHora(horaFinString)) {
                            etHoraFin.setError("Formato de hora incorrecto");
                            if(focusView != null)
                                focusView = etHoraFin;
                        }
                        else {
                            fechaFin = FechaUtil.dateCorrectFormat(fechaFinString, horaFinString);

                            if (!Actividad.isValidFechaFin(fechaIni, fechaFin)) {
                                etHoraFin.setError("La hora de fin debe ser posterior a la hora de inicio");
                                if(focusView != null)
                                    focusView = etHoraFin;
                            } else {
                                horaFinOK = true;
                            }
                        }
                    }
                } catch(ParseException e) {
                    fechaFinOK = false;
                    etFechaFin.setError("Formato de fecha incorrecto");
                    if(focusView != null)
                        focusView = etFechaFin;
                }
            }
        } else {
            etFechaFin.setError("Introduce una fecha de inicio correcta");
            if(focusView != null)
                focusView = etFechaFin;
        }

        //MAX PARTICIPANTES
        if (maxParticipantesString == null || maxParticipantesString.isEmpty()) {
            etMaxParticipantes.setError(campoObligatorio);
            if(focusView != null)
                focusView = etMaxParticipantes;
        } else if (!Actividad.isValidMaxParticipantes(maxParticipantesString)) {
            etMaxParticipantes.setError("La actividad debe permitir almenos 2 participantes");
            if(focusView != null)
                focusView = etMaxParticipantes;
        } else {
            maxParticipantes = Integer.parseInt(maxParticipantesString);
            maxParticipantesOK = true;
        }

        //INTERESES
        int i = 0;
        boolean unlessOneInteres = false;
        while (i < checkedItems.length && !unlessOneInteres) {
            unlessOneInteres = checkedItems[i];
            i++;
        }

        if (!unlessOneInteres) {
            Toast toast2 = Toast.makeText(activity, "Debes seleccionar al menos un interés", Toast.LENGTH_SHORT);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }

        boolean ubicacionOk = false;
        //UBICACION
        if (ubicacionSeleccionada == null) {
            Toast toast3 = Toast.makeText(activity, "Selecciona una ubicación", Toast.LENGTH_SHORT);
            toast3.setGravity(Gravity.CENTER, 0, 0);
            toast3.show();
        } else {
            ubicacionOk = true;
        }

        //DESCRIPCION
        if (descripcion == null) {
            descripcion = "";
        }

        if(focusView != null)
            focusView.setFocusable(true);

        if(nombreOk && fechaIniOK && horaIniOK && fechaFinOK && horaFinOK && maxParticipantesOK && ubicacionOk && unlessOneInteres)
            act = new Actividad(nombre, fechaIni, fechaFin, maxParticipantes, descripcion, ubicacionSeleccionada, intereses, uid);

        return act;
    }

    public static Actividad checkInputActividadModificar(
            Activity activity,
            String[] listItems,
            boolean[] checkedItems,
            Date fechaInicioAnt,
            int numUsuariosInscritos,
            Ubicacion ubicacionSeleccionada,
            String uid,
            EditText etNombre,
            EditText etFechaIni,
            EditText etHoraIni,
            EditText etFechaFin,
            EditText etHoraFin,
            EditText etMaxParticipantes,
            EditText etDescripcion
    ) {

        Actividad act = null;

        //OBTENER ELEMENTOS DE LA VISTA
        String nombre = etNombre.getText().toString();
        String fechaIniString = etFechaIni.getText().toString();
        String horaIniString = etHoraIni.getText().toString();
        String fechaFinString = etFechaFin.getText().toString();
        String horaFinString = etHoraFin.getText().toString();
        String maxParticipantesString = etMaxParticipantes.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        int maxParticipantes = 0;
        Date fechaIni = null;
        long horaIni = 0;
        Date fechaFin = null;
        long horaFin = 0;

        ArrayList<Categoria> intereses = new ArrayList<>();

        for (int i = 0; i < checkedItems.length; ++i) {
            if (checkedItems[i]) {
                Categoria cat = Categoria.getCategoria(listItems[i]);
                intereses.add(cat);
            }
        }

        boolean nombreOk = false;
        boolean fechaIniOK = false;
        boolean horaIniOK = false;
        boolean fechaFinOK = false;
        boolean horaFinOK = false;
        boolean maxParticipantesOK = false;
        View focusView = null;

        //QUITAR ESPACIOS AL PRINCIPIO Y FINAL DE CADA INPUT
        nombre = nombre.trim();
        fechaIniString = fechaIniString.trim();
        horaIniString = horaIniString.trim();
        fechaFinString = fechaFinString.trim();
        horaFinString = horaFinString.trim();
        descripcion = descripcion.trim();

        final String campoObligatorio = "Por favor, rellene todos los campos";

        //NOMBRE
        if (nombre == null || nombre.isEmpty()) {
            etNombre.setError(campoObligatorio);
            if(focusView != null)
                focusView = etNombre;
        } else if (!isValidNombre(nombre)) {
            etNombre.setError("El nombre introducido no es válido, sólo puede contener letras");
            if(focusView != null)
                focusView = etNombre;
        } else {
            nombreOk = true;
        }

        //FECHA INI
        if (fechaIniString == null || fechaIniString.isEmpty()) {
            etFechaIni.setError(campoObligatorio);
            if(focusView != null)
                focusView = etFechaIni;
        } else {
            try {
                fechaIni = FechaUtil.getDateFormat().parse(fechaIniString);

                fechaIniOK = true;
            } catch (ParseException e) {
                etFechaIni.setError("Formato de fecha incorrecto");
                if(focusView != null)
                    focusView = etFechaIni;
            }
        }

        //HORA INI
        if (horaIniString == null || horaIniString.isEmpty()) {
            etHoraIni.setError(campoObligatorio);
            if(focusView != null)
                focusView = etHoraIni;
        } else {
            horaIniString = FechaUtil.horaCorrectFormat(horaIniString);

            if (!Actividad.isValidHora(horaIniString)) {
                etHoraIni.setError("Formato de hora incorrecto");
                if(focusView != null)
                    focusView = etHoraIni;
            } else {
                if (fechaIniOK) {
                    fechaIni = FechaUtil.dateCorrectFormat(fechaIniString, horaIniString);

                    if(!fechaInicioAnt.equals(fechaIni)) {
                        horaIniOK = Actividad.isValidFechaIni(fechaIni);
                        if (!horaIniOK) {
                            etHoraIni.setError("La hora de inicio debe ser ahora o posterior");
                            if (focusView != null)
                                focusView = etHoraIni;
                        }
                    }
                    else {
                        horaIniOK = true;
                    }
                } else {
                    etHoraIni.setError("Introduce una fecha de inicio correcta");
                    if(focusView != null)
                        focusView = etFechaIni;
                }
            }
        }

        if (fechaIniOK && horaIniOK) {
            //FECHA FIN
            if (fechaFinString == null || fechaFinString.isEmpty()) {
                etFechaFin.setError(campoObligatorio);
                if(focusView != null)
                    focusView = etFechaFin;
            } else {
                try {
                    fechaFin = FechaUtil.getDateFormat().parse(fechaFinString);
                } catch (ParseException e) {
                    fechaFinOK = true;
                    etFechaFin.setError("Formato de fecha incorrecto");
                    if(focusView != null)
                        focusView = etFechaFin;
                }
            }

            //HORA FIN
            if (horaFinString == null || horaFinString.isEmpty()) {
                etHoraFin.setError(campoObligatorio);
                if(focusView != null)
                    focusView = etHoraFin;
            } else {
                horaFinString = FechaUtil.horaCorrectFormat(horaFinString);

                try {
                    if (!isOnlyFechaFinLaterThanFechaIni(fechaIniString, fechaFinString)) {
                        etFechaFin.setError("La fecha de inicio debe ser igual o posterior a la fecha de fin");
                        if(focusView != null)
                            focusView = etFechaFin;
                    }
                    else {
                        fechaFinOK = true;

                        if (!Actividad.isValidHora(horaFinString)) {
                            etHoraFin.setError("Formato de hora incorrecto");
                            if(focusView != null)
                                focusView = etHoraFin;
                        }
                        else {
                            fechaFin = FechaUtil.dateCorrectFormat(fechaFinString, horaFinString);

                            if (!Actividad.isValidFechaFin(fechaIni, fechaFin)) {
                                etHoraFin.setError("La hora de fin debe ser posterior a la hora de inicio");
                                if(focusView != null)
                                    focusView = etHoraFin;
                            } else {
                                horaFinOK = true;
                            }
                        }
                    }
                } catch(ParseException e) {
                    fechaFinOK = false;
                    etFechaFin.setError("Formato de fecha incorrecto");
                    if(focusView != null)
                        focusView = etFechaFin;
                }
            }
        } else {
            etFechaFin.setError("Introduce una fecha de inicio correcta");
            if(focusView != null)
                focusView = etFechaFin;
        }

        //MAX PARTICIPANTES
        if (maxParticipantesString == null || maxParticipantesString.isEmpty()) {
            etMaxParticipantes.setError(campoObligatorio);
            if(focusView != null)
                focusView = etMaxParticipantes;
        } else if (!Actividad.isValidMaxParticipantes(maxParticipantesString)) {
            etMaxParticipantes.setError("La actividad debe permitir almenos 2 participantes");
            if(focusView != null)
                focusView = etMaxParticipantes;
        } else {
            maxParticipantes = Integer.parseInt(maxParticipantesString);
            if(numUsuariosInscritos > maxParticipantes) {
                etMaxParticipantes.setError("Debes admitir como mínimo a toda la gente inscrita");
                if(focusView != null)
                    focusView = etMaxParticipantes;
            }
            else {
                maxParticipantesOK = true;
            }
        }

        //INTERESES
        int i = 0;
        boolean unlessOneInteres = false;
        while (i < checkedItems.length && !unlessOneInteres) {
            unlessOneInteres = checkedItems[i];
            i++;
        }

        if (!unlessOneInteres) {
            Toast toast2 = Toast.makeText(activity, "Debes seleccionar al menos un interés", Toast.LENGTH_SHORT);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }

        boolean ubicacionOk = false;
        //UBICACION
        if (ubicacionSeleccionada == null) {
            Toast toast3 = Toast.makeText(activity, "Selecciona una ubicación", Toast.LENGTH_SHORT);
            toast3.setGravity(Gravity.CENTER, 0, 0);
            toast3.show();
        } else {
            ubicacionOk = true;
        }

        //DESCRIPCION
        if (descripcion == null) {
            descripcion = "";
        }

        if(focusView != null)
            focusView.setFocusable(true);

        if(nombreOk && fechaIniOK && horaIniOK && fechaFinOK && horaFinOK && maxParticipantesOK && ubicacionOk && unlessOneInteres)
            act = new Actividad(nombre, fechaIni, fechaFin, maxParticipantes, descripcion, ubicacionSeleccionada, intereses, uid);

        return act;
    }
}
