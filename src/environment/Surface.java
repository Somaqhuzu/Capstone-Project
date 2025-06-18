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
    public static volatile boolean started = false;

    public void repaint(WritableImage img,Canvas canvas){
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        Platform.runLater(()->{
            graphics.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
            graphics.setFill(Color.valueOf("BLACK"));
            graphics.drawImage(img, 0, 0);
            
            graphics.setFill(Color.valueOf("BLUE"));
            for(int i=0;i<population.size();i++){
                int[] position = population.get(i).get2DPosition();
                graphics.fillOval( 3 + position[0] * 10 , 1 + position[1] * 10 , 7, 7);
            }

            for(int i =0;i<list.size();i++){
                if(list.get(i).getNutrient()==1) graphics.setFill(Color.valueOf("GREEN"));
                else graphics.setFill(Color.valueOf("RED"));
                int[] pos = list.get(i).getPosition();
                graphics.fillOval(2 + pos[0] * 10,2 + 10 * pos[1],3,3);
            }
           
            // graphics.setFill(Color.rgb(40, 150, 70, 0.3));
            // for(int i = 0; i < epsNodes.size();i++){
            //     int[] pos = epsNodes.get(i);
            //     graphics.fillOval(canvas.getWidth() + pos[0],canvas.getHeight() + pos[1],300,150);
                
            // }
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
