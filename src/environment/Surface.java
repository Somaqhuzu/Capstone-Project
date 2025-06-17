package environment;

import java.util.ArrayList;

import bacteria.Bacteria;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.*;

public class Surface{
    Bacteria bact = new Bacteria(3,4);
    Bacteria bact2 = new Bacteria(8, 9);
    private static SurfaceBlock[][] surfaceBlocks;
    private static ArrayList<SurfaceBlock> list = new ArrayList<>();
    
    public Surface(SurfaceBlock[][] blocks){
        Surface.surfaceBlocks = blocks;
    }

    public void repaint(WritableImage img,Canvas canvas){
        GraphicsContext graphics = canvas.getGraphicsContext2D();
         //Fill oval in the middle of the grid
        graphics.setFill(Color.valueOf("RED"));
        Platform.runLater(()->{
            graphics.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
            graphics.setFill(Color.valueOf("BLACK"));
            graphics.drawImage(img, 0, 0);
            graphics.setFill(Color.valueOf("GREEN"));
            for(int i =0;i<list.size();i++){
                int[] pos = list.get(i).getPosition();
                graphics.fillOval(canvas.getWidth()/50 + pos[0]*50,canvas.getHeight()/50 + pos[1]*50,10,10);
            }
            graphics.setFill(Color.valueOf("RED"));
            graphics.fillOval(canvas.getWidth()/50 + bact.getPosX() * 50 , canvas.getHeight()/50 + bact.getPosY() * 50, 25, 25);
            graphics.fillOval(canvas.getWidth()/50 + bact2.getPosX() * 50 , canvas.getHeight()/50 + bact2.getPosY() * 50, 25, 25);
        });
        list.removeIf((e)->{
            return e.getNutrient()== 0;

        });
    }

    public static ArrayList<SurfaceBlock> getNutritiousBlocks(){
        return list;
    }

    public static SurfaceBlock getSurface(int[] position){
        try{return surfaceBlocks[position[0]][position[1]];}
        catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
}
