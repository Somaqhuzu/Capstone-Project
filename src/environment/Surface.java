package environment;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import bacteria.Bacteria;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.*;


public class Surface{
    Bacteria[] population;
    private static SurfaceBlock[][] surfaceBlocks;
    private static ArrayList<SurfaceBlock> list = new ArrayList<>();
    private static ArrayList<int[]> epsNodes = new ArrayList<>();
    
    public Surface(SurfaceBlock[][] blocks,int population){
        Surface.surfaceBlocks = blocks;
        this.population = new Bacteria[population];
        for(int i=0;i<population;i++){
            this.population[i] = new Bacteria(i,(int)ThreadLocalRandom.current().nextInt(20),(int)ThreadLocalRandom.current().nextInt(13));
        }
    }

    public void repaint(WritableImage img,Canvas canvas){
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        Platform.runLater(()->{
            graphics.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
            graphics.setFill(Color.valueOf("BLACK"));
            graphics.drawImage(img, 0, 0);
            
            graphics.setFill(Color.valueOf("YELLOW"));
            for(int i=0;i<population.length;i++){
                int[] position = population[i].get2DPosition();
                graphics.fillOval(canvas.getWidth()/50 + position[0]* 50 , canvas.getHeight()/50 + position[1] * 50, 25, 25);
                System.out.println(population[i]);
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

    public Bacteria[] getPopulation(){
        return population;
    }
}
