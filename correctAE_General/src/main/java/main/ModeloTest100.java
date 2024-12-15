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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jesus.delBuey
 */
public class ModeloTest100 {

    // Fichero con las casillas de los campos
    // Estructura del test
    // campo para las Respuestas
    private Campo respuestas;
    // campo para el NIF, numero
    private Campo nifNumero;
    // campo para el NIF, letra
    private Campo nifLetra;
    // campo para el Tipo
    private Campo tipo;
    // campo para el Grupo
    private Campo grupo;
    
    // Archivo de imagen del Test, ruta completa
    private String nombreArchivo;

    // Campos del test
    private List<Campo> camposTest;
    /*Campos:
        Respuestas:   0-99  (de la 1 a la 100)
        NIFNumero:    100-179
        NIFLetra:     180-202 una lentra entre 23 posibles, ABCDEFGHJKLMNPQRSTVWXYZ
        Tipo:         203-208
        Grupo:        209-214
     */
    public List<Campo> getCamposTest() {
        return camposTest;
    }

    private List<Casilla> casillasMarcadas = new ArrayList<>();

    public List<Casilla> getCasillasMarcadas() {
        return casillasMarcadas;
    }

    public void setCasillasMarcadas(List<Casilla> casillasMarcadas) {
        this.casillasMarcadas = casillasMarcadas;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArch) {
        this.nombreArchivo = nombreArch;
    }


    private void inicializarTest() {
        // campo para las Respuestas
        respuestas = new Campo("Respuestas", 100, 5, "ABCDE", 0, 499);
        // Número del NIF
        nifNumero = new Campo("NIFNumero", 8, 10, "0123456789", 500, 179);
        // Letra del NIF
        nifLetra = new Campo("NIFLetra", 1, 23, "ABCDEFGHJKLMNPQRSTVWXYZ", 580, 602);
        // campo para el Tipo
        tipo = new Campo("Tipo", 1, 6, "123456", 603, 608);
        // campo para el Grupo
        grupo = new Campo("Grupo", 1, 6, "123456", 609, 614);
        // Campos del test
        camposTest = List.of(respuestas,nifNumero, nifLetra,tipo, grupo);
    }

    public ModeloTest100() {
        inicializarTest();
    }

    public ModeloTest100(String nombre) {
        inicializarTest();
        nombreArchivo = nombre;
    }

}
