package environment;

import java.util.ArrayList;


import bacteria.Bacteria;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.*;


public class Surface{
    private static ArrayList<Bacteria> population = new ArrayList<>();
    private static SurfaceBlock[][] surfaceBlocks;
    private static ArrayList<SurfaceBlock> list = new ArrayList<>();
    private static ArrayList<int[]> epsNodes = new ArrayList<>();
    

    public void repaint(WritableImage img,Canvas canvas){
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        Platform.runLater(()->{
            graphics.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
            graphics.setFill(Color.valueOf("BLACK"));
            graphics.drawImage(img, 0, 0);
            
            graphics.setFill(Color.valueOf("YELLOW"));
            for(int i=0;i<population.size();i++){
                int[] position = population.get(i).get2DPosition();
                graphics.fillOval(canvas.getWidth()/50 + position[0]* 50 , canvas.getHeight()/50 + position[1] * 50, 25, 25);
            }

            for(int i =0;i<list.size();i++){
                if(list.get(i).getNutrient()==1) graphics.setFill(Color.valueOf("GREEN"));
                else graphics.setFill(Color.valueOf("RED"));
                int[] pos = list.get(i).getPosition();
                graphics.fillOval(canvas.getWidth()/50 + pos[0]*50,canvas.getHeight()/50 + pos[1]*50,5,5);
            }
           
            graphics.setFill(Color.rgb(40, 150, 70, 0.3));
            for(int i = 0; i < epsNodes.size();i++){
                int[] pos = epsNodes.get(i);
                graphics.fillOval(canvas.getWidth()/50 + pos[0]*50,canvas.getHeight()/50 + pos[1]*50,300,150);
                
            }
        });
        list.removeIf((e)->{
            return e.getNutrient()== 0;

        });
    }

    public static ArrayList<SurfaceBlock> getNutritiousBlocks(){
        return list;
    }

    public static ArrayList<int[]> epsNodePositions(){
        return epsNodes;
    }

    public static SurfaceBlock getSurface(int[] position){
        try{return surfaceBlocks[position[0]][position[1]];}
        catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public static ArrayList<Bacteria> getPopulation(){
        return population;
    }

    public static void addBacteria(Bacteria b){
        population.add(b);
    }

    public static void setSurfaceBlocks(SurfaceBlock[][] surfaceBlocks2) {
       surfaceBlocks = surfaceBlocks2;
    }
}
