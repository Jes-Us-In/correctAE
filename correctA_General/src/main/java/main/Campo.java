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

/**
 *
 * @author Jesus.delBuey
 */
public class Campo {

    public Campo() {}

    /**
     *
     * @param nombreCampo nombre del campo
     * @param digitos Cantidad de dígitos del campo
     * @param largoDigitos Logitud de los dígitos
     * @param valoresDigito Valores que puede tomar el dígito
     * @param posInicPlant Posición incial en la plantilla de coordenadas del test
     * @param posFinPlant Posición final en la plantilla de coordenadas del test
     */
    public Campo(String nombreCampo, int digitos, int largoDigitos, String valoresDigito, int posInicPlant, int posFinPlant) {
        this.nombreCampo = nombreCampo;
        this.digitos = digitos;
        this.largoDigito = largoDigitos;
        this.valoresDigito = valoresDigito;
        this.posIncicioPlantilla = posInicPlant;
        this.posFinPlantilla = posFinPlant;
    }
    
    private String nombreCampo;
    private int digitos;
    private int largoDigito;
    private int posIncicioPlantilla;
    private int posFinPlantilla;
    private String valoresDigito;
    private String resultado;

    /**
     *
     * @return resultado
     */
    public String getResultado() {
        return resultado;
    }

    /**
     *
     * @param resultado Valor que toma el campo, tras la corrección
     */
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    /**
     *
     * @return posIncicioPlantilla
     */
    public int getPosIncicioPlantilla() {
        return posIncicioPlantilla;
    }

    /**
     *
     * @param posIncicioPlantilla Posición incial en la plantilla de coordenadas del test
     */
    public void setPosIncicioPlantilla(int posIncicioPlantilla) {
        this.posIncicioPlantilla = posIncicioPlantilla;
    }

    /**
     *
     * @return posFinPlantilla
     */
    public int getPosFinPlantilla() {
        return posFinPlantilla;
    }

    /**
     *
     * @param posFinPlantilla Posición final en la plantilla de coordenadas del test
     */
    public void setPosFinPlantilla(int posFinPlantilla) {
        this.posFinPlantilla = posFinPlantilla;
    }
    
    /**
     *
     * @return nombreCampo
     */
    public String getNombreCampo() {
        return nombreCampo;
    }

    /**
     *
     * @param nombreCampo Nombre del campo
     */
    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    /**
     *
     * @return digitos
     */
    public int getDigitos() {
        return digitos;
    }

    /**
     *
     * @param digitos Cantidad de dígitos del campo
     */
    public void setDigitos(int digitos) {
        this.digitos = digitos;
    }

    /**
     *
     * @return largoDigito
     */
    public int getLargoDigitos() {
        return largoDigito;
    }

    /**
     *
     * @param largoDigitos Logitud de los dígitos
     */
    public void setLargoDigitos(int largoDigitos) {
        this.largoDigito = largoDigitos;
    }

    /**
     *
     * @return valoresDigito
     */
    public String getValoresDigito() {
        return valoresDigito;
    }

    /**
     *
     * @param valoresDigito Valores que puede tomar el dígito
     */
    public void setValoresDigito(String valoresDigito) {
        this.valoresDigito = valoresDigito;
    }

}
