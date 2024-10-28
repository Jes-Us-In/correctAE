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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Jesus.delBuey
 */
public class ModeloImprimible extends JLabel implements Printable {
    
    private BufferedImage imagenModelo;
    public BufferedImage getImagenModelo() {
        return imagenModelo;
    }
    
    public void setImagenModelo(BufferedImage img) {
        this.setIcon(new ImageIcon(img));
    }
    
    /**
     *
     * @param imgCargo Imagen que se usa como modelo
     */
    public ModeloImprimible(BufferedImage imgCargo) {
        this.setIcon(new ImageIcon(imgCargo));
        imagenModelo = imgCargo;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex == 0) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            // escalo la imagen al acho de la página
            double factorEscala = pageFormat.getImageableWidth() / this.getIcon().getIconWidth();
            // El factor de escala con A4 es 0.6, mayor que 0.5, por tanto no hago escalado progresivo.
            g2d.scale(factorEscala, factorEscala);
            //
            this.printAll(graphics);
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }
}
