//Surface where the Bacteria form a biofilm
//Vector Field of some sort

import java.util.ArrayList;

import bacteria.Bacteria;
import environment.SurfaceBlock;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;

public class Surface extends Application implements Runnable{
    Canvas canvas;
    Bacteria bact = new Bacteria(3,4);
    Bacteria bact2 = new Bacteria(8, 9);
    SurfaceBlock[][] surfaceBlocks;
    ArrayList<SurfaceBlock> list = new ArrayList<>();
    WritableImage img;
    public Surface(){
        super();
    }

    public void repaint(WritableImage img,GraphicsContext graphics){
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

    @Override
    public void run(){
        repaint(img,canvas.getGraphicsContext2D());
    }
    
    public static void main(String[] args){
        launch(args);
        
    }

     private WritableImage createGridImage(double width, double height, int cellSize) {
         //Drawing Grid
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.valueOf("BLACK"));

        surfaceBlocks = new SurfaceBlock[(int)(width/cellSize)][(int)(height/cellSize)];
        for(int i=0;i<canvas.getWidth();i = i + cellSize){
            graphics.strokeLine(i,0,i,canvas.getHeight());
            for(int j = 0;j<canvas.getHeight(); j = j + cellSize){
                graphics.strokeLine(0,j,canvas.getWidth(),j);
                if (i/(j+1) % 2 ==0){
                    SurfaceBlock e = new SurfaceBlock(i/50, j/50,1);
                    surfaceBlocks[i/50][j/50] = e;
                    list.add(e);
                }
                else
                    surfaceBlocks[i%50][j%50] = new SurfaceBlock(i/50, j/50);
            }
        }
        return canvas.snapshot(null, null);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Biofilm Simulation");
        stage.setWidth(1280);
        stage.setHeight(700);
        //Setting up the scenary
        StackPane container = new StackPane();
        canvas = new Canvas(1000,650);
        GraphicsContext graphics = canvas.getGraphicsContext2D();

        //Setting background of the canvas
        graphics.setFill(Color.valueOf("WHITE"));
        graphics.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        img = createGridImage(canvas.getWidth(),canvas.getHeight() , 50);

        AnimationTimer anime = new AnimationTimer() {
            @Override
            public void handle(long now) {
                repaint(img,graphics);
            }
        };

        container.getChildren().add(canvas); 
        Scene scene = new Scene (container, 1280, 600);
        stage.setScene(scene);
        stage.show();
        bact.start(); // Start the bacteria thread
        bact2.start();
        anime.start(); // Start the animation timer
    }
}