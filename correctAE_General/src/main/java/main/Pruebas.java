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

import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static main.Procesador.idioma;
import static main.Procesador.log;

/**
 *
 * @author Jesus.delBuey
 */
public class Pruebas {

    public static void main(String args[]) {

        Map<Integer, String> map = Map.of(1, "uno", 2, "dos", 3, "Tres");
        map.forEach((Integer x, String st) -> LOG.log(Level.INFO, "{0} - {1}", new Object[]{String.valueOf(x), st}));

        System.out.println("Nombre Clase = " + Pruebas.class.getName());
        System.out.println("Descripcion Clase = " + Pruebas.class.descriptorString());
        System.out.println("Nombre canonigo Clase = " + Pruebas.class.getCanonicalName());
        System.out.println("Camino recursos Clase = " + Pruebas.class.getResource("").getPath());
        System.out.println("Camino recursos ClassLoader Clase = " + Pruebas.class.getClassLoader().getResource("").getPath());
        System.out.println("Carpeta usuario = " + System.getProperty("user.dir"));
        System.out.println("Carpeta ejecucion = " + Paths.get("").toAbsolutePath().toString());
        System.out.println("Propiedades del Sistema = " + Procesador.mostrarPropiedadesSistema());

//        File sourceDir = new File(Procesador.class.getResource(System.getProperty("file.separator") + "ayuda").getFile());
//        File destinationDir = new File(Paths.get("").toAbsolutePath().toString() + System.getProperty("file.separator") + "ayuda");
//        System.out.println("Origen = " + sourceDir.getAbsolutePath());
//        System.out.println("Destino = " + destinationDir.getAbsolutePath());
//        URL resourceUrl = Pruebas.class.getResource("ayuda");
//        System.out.println("Ruta de la carpeta: Loader " + resourceUrl.getPath());

        Desktop dt = Desktop.getDesktop();

        //if (dt.isSupported(Desktop.Action.BROWSE)) {
            URI lauri;
            try {
                String ruta = "/home/jesus/Documentos/Programacion/Java/Correcta_General_GitHub/correctAE_General/correctAEyuda/es/pgInicio.html";
                lauri = new URI(ruta);
                Desktop.getDesktop().browse(lauri);
            } catch (URISyntaxException | IOException ex) {
                log.error(ex.getLocalizedMessage());
            }
//            EventQueue.invokeLater(() -> {
//                try {
//                    Desktop.getDesktop().browse(lauri);
//                } catch (IOException ex) {
//                    log.error(ex.getLocalizedMessage());
//                }
//            });
            
//            EventQueue.invokeLater(() -> {
//                try {
//                    Desktop.getDesktop().browse(lauri);
//                } catch (IOException ex) {
//                    log.error(ex.getLocalizedMessage());
//                }
//            });
//        } else {
//            log.aviso(idioma.getString("VentanaInicio.errorNavegador.text"));
//        }

    }

    private static final Logger LOG = Logger.getLogger(Pruebas.class.getName());

}
