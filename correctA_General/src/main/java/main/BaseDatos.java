/*
 * Copyright (C) 2024 Jesus del Buey Jiménez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: GPL-3.0
 */

package main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jesus.delBuey
 */
public class BaseDatos {

    // Idioma
    static ResourceBundle idioma = Procesador.idioma;

    /**
    * Log general de la aplicación
    */
    static protected Loguero log = Procesador.getLog();

    // String de conexión a la base de datos
    private static final String URL_BASE_DATOS = "jdbc:sqlite:" + Config.getFicheroBaseDatos();

    // Fecha y hora para guardar
    private static String fechaHora;

    // Variables de acceso a la Base de Datos
    private static PreparedStatement preStmt, preStmt2;
    private static Statement stmt;
    private static ResultSet resul, resul2;
    private static String sql, sql2; // String para los comandos SQL

    /**
     * Inicializa la Base de datos
     */
    public static void InicializarBaseDatos() {

        // Compruebo si existe ya el fichero de la base de datos, antes.
        if (!new File(Config.getFicheroBaseDatos()).exists()) {
            try (Connection con = DriverManager.getConnection(URL_BASE_DATOS)) {
                ///////// CREAR TABLA Examenes
                sql = "CREATE TABLE IF NOT EXISTS Examenes ("
                        + "id integer primary key autoincrement,"
                        + "fecha_Hora text,"
                        + "Nombre_Examen text,"
                        + "descripcion text,"
                        + "NumPreguntas integer,"
                        + "NumTipos integer"
                        + ");";

                stmt = con.createStatement();
                stmt.execute(sql);

                ///////// CREAR TABLA Tests
                sql = "CREATE TABLE IF NOT EXISTS Tests ("
                        + "id integer primary key autoincrement,"
                        + "examen_id integer,"
                        + "dni text,"
                        + "tipo integer,"
                        + "grupo integer,"
                        + "aciertos integer,"
                        + "fallos integer,"
                        + "blancos integer,"
                        + "dobles integer,"
                        + "nota real,"
                        + "respuestas text,"
                        + "FOREIGN KEY (examen_id) references Examenes(id) ON DELETE CASCADE"
                        + ");";
                stmt.execute(sql);

                ///////// CREAR TABLA Respuestas
                sql = "CREATE TABLE IF NOT EXISTS Respuestas ("
                        + "id integer primary key autoincrement,"
                        + "examen_id integer,"
                        + "numPregunta integer,"
                        + "respuesta text,"
                        + "FOREIGN KEY (examen_id) references Examenes(id) ON DELETE CASCADE"
                        + ");";
                stmt.execute(sql);

                ///////// CREAR TABLA Equivalencias, tipos
                sql = "CREATE TABLE IF NOT EXISTS Equivalencias ("
                        + "id integer primary key autoincrement,"
                        + "respuesta_id integer,"
                        + "tipo integer,"
                        + "equivalencia integer,"
                        + "FOREIGN KEY (respuesta_id) references Respuestas(id) ON DELETE CASCADE"
                        + ");";
                stmt.execute(sql);

            } catch (SQLException e) {
                log.error(idioma.getString("BaseDatos.error.text") + " - " + e.getMessage());
            }
        }
    }

    // Agrego la lista de examenes en el modelo que recibo

    /**
     *
     * @param modelo Modelo de tabla sobre el que cargarán los registros
     * @return MiModelo resultante, con los registros cargados
     */
    public static MiModeloTabla CargaListaExamenes(MiModeloTabla modelo) {
        try (Connection con = DriverManager.getConnection(URL_BASE_DATOS)) {
            /// Obtengo la lista de exámenes
            sql = "select * from Examenes;";
            stmt = con.createStatement();
            resul = stmt.executeQuery(sql);
            modelo.setRowCount(0);
            while (resul.next()) {
                Object[] datos = {resul.getInt(1), resul.getString(2), resul.getString(3),
                    resul.getString(4), resul.getInt(5), resul.getInt(6)};
                modelo.addRow(datos);
            }
        } catch (SQLException e) {
            log.error(idioma.getString("BaseDatos.error.leyendo.lista.examenes.text") + " - " + e.getMessage());
            modelo = null;
        }
        return modelo;
    }

    // Agrego la lista de test en el modelo que recibo
    /**
     *
     * @param modelo Modelo de datos de la tabla de Tests
     * @param idExamen Identificador del examen a cargar
     * @return Modelo de datos de la tabla de Tests Actualizado
     */
    public static MiModeloTabla CargaTestsExamen(MiModeloTabla modelo, int idExamen) {
        try (Connection con = DriverManager.getConnection(URL_BASE_DATOS)) {
            /// Obtengo la lista de exámenes
            sql = "SELECT * FROM Tests WHERE examen_id = " + idExamen + ";";
            stmt = con.createStatement();
            resul = stmt.executeQuery(sql);
            // El 1 es el id, el campo 2 es la clave, pero no la necesito
            // En lugar del id original, pongo el siguiente a partir del último que haya en la tabla de test de examen
            int idConsec = Procesador.modeloTablaTestsCorregidos.getRowCount() + 1;
            while (resul.next()) {
                Object[] datos = {idConsec, resul.getString(3), resul.getString(4), resul.getString(5),
                    resul.getString(6), resul.getString(7), resul.getString(8), resul.getString(9),
                    resul.getString(10), resul.getString(11)};
                modelo.addRow(datos);
                idConsec++;
            }
        } catch (SQLException e) {
            log.error(idioma.getString("BaseDatos.error.leyendo.lista.tests.text") + " - " + e.getMessage());
            JOptionPane.showOptionDialog(null, idioma.getString("BaseDatos.error.leyendo.lista.tests.text"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            return null;
        }
        return modelo;
    }

    // Agrego la lista de test en el modelo que recibo

    /**
     *
     * @param modelo Modelo de tabla de equivalencias donde se cargarán los registros.
     * @param idExamen Número de examen a cargar
     */
    public static void CargaEquivalencias(DefaultTableModel modelo, int idExamen) {
        try (Connection con = DriverManager.getConnection(URL_BASE_DATOS)) {
            /// Obtengo la lista de respuestas y equivalencias
            sql = "SELECT * FROM Respuestas WHERE examen_id = " + idExamen + ";";
            stmt = con.createStatement();
            sql2 = "SELECT * FROM Equivalencias WHERE respuesta_id = ?"; // + idRespuesta + ";";
            preStmt = con.prepareStatement(sql2);
            resul = stmt.executeQuery(sql);
            // El 1 es el id, el campo 2 es el id del examen al que pertenece y el 3 es la respuesta correcta
            int idRespuesta;
            // Borro la tabla de tipos y equivalencias
            ArrayList datos = new ArrayList<>();
            while (resul.next()) {
                datos.clear();
                idRespuesta = resul.getInt("id");
                datos.add(resul.getString("numPregunta"));
                datos.add(resul.getString("respuesta"));
                preStmt.setString(1, String.valueOf(idRespuesta));
                resul2 = preStmt.executeQuery();
                // Cargo las equivalencias de esa respuesta
                while (resul2.next()) {
                    datos.add(resul2.getInt("equivalencia"));
                }
                modelo.addRow(datos.toArray());
            }
        } catch (SQLException e) {
            log.error(idioma.getString("BaseDatos.error.leyendo.lista.tests.text") + " - " + e.getMessage());
            JOptionPane.showOptionDialog(null, idioma.getString("BaseDatos.error.leyendo.lista.tests.text"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }

    // Guardo el examen en la tabla de examenes con los datos recibidos

    /**
     *
     * @param nombre Nombre del examen a guardar
     * @param descr Descripción del examen a guardar
     * @param modeloTabla Modelo de la tabla que contiene los test
     * @param modeloRespTipos Modelo de la tabla que contiene los tipoa y equivalencias
     * @param preguntas Número de preguntas
     * @param tipos Número de tipos
     */
    public static void GuardarExamen(String nombre, String descr, MiModeloTabla modeloTabla, DefaultTableModel modeloRespTipos, int preguntas, int tipos) {
        LocalDateTime ahora = LocalDateTime.now();
        fechaHora = ahora.getDayOfMonth() + "-" + ahora.getMonthValue() + "-" + ahora.getYear()
                + "_" + ahora.getHour() + ":" + ahora.getMinute() + ":" + ahora.getSecond();

        try (Connection con = DriverManager.getConnection(URL_BASE_DATOS)) {

            ///////// Inserto el examen en la tabla de Examenes, el primero no lo pongo, es id, es un autoincremento
            sql = "INSERT INTO Examenes (fecha_Hora,Nombre_Examen,descripcion,NumPreguntas,NumTipos) VALUES (?,?,?,?,?);";
            preStmt = con.prepareStatement(sql);
            preStmt.setString(1, fechaHora);
            preStmt.setString(2, nombre);
            preStmt.setString(3, descr);
            preStmt.setInt(4, preguntas);
            preStmt.setInt(5, tipos);
            preStmt.executeUpdate();

            /// Obtengo el id, secuencia ya que es un campo autoincrementado, del último examen insertado, será la clave foránea
            sql = "select seq from sqlite_sequence where name=\"Examenes\";";
            stmt = con.createStatement();
            resul = stmt.executeQuery(sql);
            if (resul.next()) {
                int ultimoId = resul.getInt("seq");
                ///////// Guardo los tests del examen en la tabla de Tests
                // relleno los campos del registro, el primero no lo pongo, es id, es un autoincremento
                sql = "INSERT INTO Tests (examen_id,dni,tipo,grupo,aciertos,fallos,blancos,dobles,nota,respuestas) VALUES (?,?,?,?,?,?,?,?,?,?);";
                preStmt = con.prepareStatement(sql);
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    preStmt.setString(1, String.valueOf(ultimoId));
                    for (int k = 1; k < modeloTabla.getColumnCount(); k++) {
                        preStmt.setString(k + 1, String.valueOf(modeloTabla.getValueAt(i, k)));
                    }
                    // Agrego la ruta completa, que va al final
                    preStmt.executeUpdate();
                }
                ///////// Guardo las respuestas correctas del examen en la tabla de Respuestas
                /// Obtengo el id siguiente de la última pregunta insertada, será la clave foránea en la tabla de tipos
                sql = "select seq from sqlite_sequence where name=\"Respuestas\";";
                stmt = con.createStatement();
                resul = stmt.executeQuery(sql);
                // Próxima respuesta que se insertara
                int ultimaRespuesta = resul.getInt("seq") + 1;
                // relleno los campos del registro, el primero no lo pongo, es id, es un autoincremento
                sql = "INSERT INTO Respuestas (examen_id,numPregunta,respuesta) VALUES (?,?,?);";
                preStmt = con.prepareStatement(sql);
                // Guardo los tipos en la tabla de Equivalencias
                sql2 = "INSERT INTO Equivalencias (respuesta_id,tipo,equivalencia) VALUES (?,?,?);";
                preStmt2 = con.prepareStatement(sql2);
                for (int i = 0; i < modeloRespTipos.getRowCount(); i++) {
                    preStmt.setInt(1, ultimoId);
                    preStmt.setString(2, String.valueOf(modeloRespTipos.getValueAt(i, 0)));
                    preStmt.setString(3, String.valueOf(modeloRespTipos.getValueAt(i, 1)));
                    preStmt.executeUpdate();
                    // Guardo las Equivalencias de tipos para esta respuesta de pregunta
                    preStmt2.setInt(1, ultimaRespuesta);
                    // Tipo 1 en la columna 2
                    for (int equivTipo = 2; equivTipo < modeloRespTipos.getColumnCount(); equivTipo++) {
                        preStmt2.setInt(2, (equivTipo - 1));
                        preStmt2.setString(3, String.valueOf(modeloRespTipos.getValueAt(i, equivTipo)));
                        preStmt2.executeUpdate();
                    }
                    // Incremento el contador de pregunta, al que va asociada la equivalencia, un id global de preguntas, no del examen este sólo
                    ultimaRespuesta++;
                }
            } else {
                log.error(idioma.getString("BaseDatos.error.indices.text"));
                JOptionPane.showOptionDialog(null, idioma.getString("BaseDatos.error.indices.noguardados.text"), idioma.getString("Error.text"),
                        JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            }
        } catch (SQLException e) {
            log.error(idioma.getString("BaseDatos.error.text") + " - " + e.getMessage());
            JOptionPane.showOptionDialog(null, idioma.getString("BaseDatos.error.indices.noguardados.text"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
        }
    }

    // Borro el examen con el id recibido, y todos los tests relacionados
    // gracias que usan como clave foránea el id del examen al que pertenece
    // cada test

    /**
     *
     * @param modelo Modelo de la tabla de examenes
     * @param idExamen Id del examen a borrar
     * @return Boolean true si todo va bien, false si hay error
     */
    public static Boolean BorrarExamen(MiModeloTabla modelo, int idExamen) {
        try (Connection con = DriverManager.getConnection(URL_BASE_DATOS)) {
            /// Borro el examen enviado en idExamen
            sql = "DELETE FROM Examenes WHERE id = " + idExamen + ";";
            stmt = con.createStatement();
            // Habilito el control de claves foráneas para la conexión, ya que por defecto no lo está 
            // y no se haría el "on delete cascade", de los tests
            stmt.execute("PRAGMA foreign_keys=ON");
            // Ahora borro el examen y los tests relacionados
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            log.error(idioma.getString("BaseDatos.error.leyendo.lista.tests.text") + " - " + e.getMessage());
            JOptionPane.showOptionDialog(null, idioma.getString("BaseDatos.error.leyendo.lista.tests.text"), idioma.getString("Error.text"),
                    JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, Config.OPCION_ACEPTAR, null);
            return false;
        }
    }

}
