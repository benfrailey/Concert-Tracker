/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcfdkpconcerttracker;

import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author benfrailey
 */
public interface Visualizer {
    public void start(AnchorPane vizPane, ArrayList<Event> events);
    public void end();
    public void update(ArrayList<Event> events);
    public String getName();
}