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
public class Casilla {

    private int coordX;

    /**
     * Get the value of coordX
     *
     * @return the value of coordX
     */
    public int getCoordX() {
        return coordX;
    }

    /**
     * Set the value of coordX
     *
     * @param coordX new value of coordX
     */
    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    private int coordY;

    /**
     * Get the value of coordY
     *
     * @return the value of coordY
     */
    public int getCoordY() {
        return coordY;
    }

    /**
     * Set the value of coordY
     *
     * @param coordY new value of coordY
     */
    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    //Estado de la casilla, por defecto no está marcada
    private boolean marcada = false;

    /**
     * Get the value of marcada
     *
     * @return the value of marcada
     */
    public boolean isMarcada() {
        return marcada;
    }

    /**
     * Set the value of marcada
     *
     * @param marcada new value of marcada
     */
    public void setMarcada(boolean marcada) {
        this.marcada = marcada;
    }

    public Casilla() {
    }

    public Casilla(int coordX, int coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }

}
