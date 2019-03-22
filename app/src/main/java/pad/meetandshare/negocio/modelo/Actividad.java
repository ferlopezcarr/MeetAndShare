package pad.meetandshare.negocio.modelo;


import android.app.Activity;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pad.meetandshare.actividades.ParserObjects.ParserActividad;

public class Actividad implements Serializable {

    // -------- ATRIBUTOS -------- //
    private String uid;

    private String name;

    private Date startDate;

    private Date endDate;

    private int maxRegistered;

    private String description;

    private Ubication ubication;

    private String adminUid;

    private List<Category> categories;

    private List<String> registeredUserIds;

    private boolean active;

    // -- Datos para integracion -- //
    public final static String ActivitiesDatabaseName = "activities";

    public static String rootDataBase(String userUid) {
        return User.UsersDataBaseName + "/" + userUid + "/" + ActivitiesDatabaseName;
    }

    // -------- METODOS -------- //
    /**
     * Constructoria por defecto de Actividad
     */
    public Actividad() {}

    /**
     * Constructora con argumentos de Actividad
     * @param name
     * @param startDate
     * @param endDate
     * @param maxRegistered
     * @param description
     * @param ubication
     */
    public Actividad(String name, Date startDate, Date endDate, int maxRegistered, String description, Ubication ubication, ArrayList<Category> categories, String adminUid) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxRegistered = maxRegistered;
        this.description = description;
        this.ubication = ubication;
        this.adminUid = adminUid;
        this.active = true;
        this.categories = categories;
        this.registeredUserIds = new ArrayList<String>();
        if(!this.registeredUserIds.contains(adminUid)) {
            this.registeredUserIds.add(adminUid);
        }
    }



    // -- GETTERS Y SETTERS -- //

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getMaxRegistered() {
        return this.maxRegistered;
    }

    public void setMaxRegistered(int maxRegistered) {
        this.maxRegistered = maxRegistered;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Ubication getUbication() {
        return ubication;
    }

    public void setUbication(Ubication ubication) {
        this.ubication = ubication;
    }

    public String getAdminUid() {
        return this.adminUid;
    }

    public void setAdminUid(String adminUid) {
        this.adminUid = adminUid;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean activityInProgess() {
        Date ahora = new Date();
        return (startDate.after(ahora) && endDate.before(ahora));
    }

    public boolean activityFinished() {
        return (endDate.before(new Date()));
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


    // -- Usuarios inscritos -- //

    public List<String> getRegisteredUserIds() {
        return this.registeredUserIds;
    }

    public void setRegisteredUserIds(List<String> registeredUserIds) {
        this.registeredUserIds = registeredUserIds;
    }

    public boolean addRegisteredUser(String uidUsuario) {
        if (registeredUserIds == null) {
            registeredUserIds = new ArrayList<String>();
        }

        if (registeredUserIds.contains(uidUsuario)) {
            return false;
        }
        else {
            registeredUserIds.add(uidUsuario);
            return true;
        }
    }

    public boolean deleteRegisteredUser(String idUsuario) {
        if (registeredUserIds == null || registeredUserIds.isEmpty()) {
            return false;
        }

        if (registeredUserIds.contains(idUsuario)) {
            registeredUserIds.remove(idUsuario);
            return true;
        }
        else {
            return false;
        }
    }



    // -- CHECK INPUT ACTIVIDAD -- //



    /*
    public static Actividad checkInputActividadModificar(
            Activity activity,
            Actividad actividad,
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
            etNombre.setError("El nombre contiene carácteres inválidos");
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
        }
        else {
            maxParticipantes = Integer.parseInt(maxParticipantesString);

            if (!Actividad.isValidMaxParticipantes(maxParticipantes)) {
                etMaxParticipantes.setError("La actividad debe permitir almenos 2 participantes");
                if(focusView != null)
                    focusView = etMaxParticipantes;
            }
            else if(numUsuariosInscritos > maxParticipantes) {
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

        if(nombreOk && fechaIniOK && horaIniOK && fechaFinOK && horaFinOK && maxParticipantesOK && ubicacionOk && unlessOneInteres) {
            actividad.setNombre(nombre);
            actividad.setFechaInicio(fechaIni);
            actividad.setFechaFin(fechaFin);
            actividad.setMaxParticipantes(maxParticipantes);
            actividad.setUbicacion(ubicacionSeleccionada);
            actividad.setCategorias(intereses);
        }

        return actividad;
    }
    */
}
