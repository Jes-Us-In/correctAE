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

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
//import org.openide.util.Exceptions;

/**
 *
 * @author Jesus.delBuey
 */
public class Loguero {

    static final Logger log = Logger.getLogger("correctAE.log");
    private FileHandler fh;

    public Loguero() {
        try {
            fh = new FileHandler("correctAE.log", true);
            log.addHandler(fh);
            SimpleFormatter formato = new SimpleFormatter();
            fh.setFormatter(formato);
        } catch (IOException | SecurityException ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
    }

    public void info(String mensaje) {
        log.log(Level.INFO, mensaje);
    }

    public void error(String mensaje) {
        log.log(Level.SEVERE, mensaje);
    }

    public void aviso(String mensaje) {
        log.log(Level.WARNING, mensaje);
    }

}
