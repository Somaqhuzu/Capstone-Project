//Surface where the Bacteria form a biofilm
//Vector Field of some sort

import bacteria.Bacteria;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;

public class Surface extends Application implements Runnable{
    Canvas canvas;
    Bacteria bact = new Bacteria(3,4);
    public Surface(){
        super();
    }

    public void repaint(GraphicsContext graphics){
         //Fill oval in the middle of the grid
        graphics.setFill(Color.valueOf("RED"));
        Platform.runLater(()->{
            graphics.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
            graphics.setFill(Color.valueOf("BLACK"));
            for(int i=0;i<canvas.getWidth();i = i + 50){
                graphics.strokeLine(i,0,i,canvas.getHeight());
                for(int j = 0;j<canvas.getHeight(); j = j + 50){
                graphics.strokeLine(0,j,canvas.getWidth(),j);
                }
            }
            graphics.setFill(Color.valueOf("RED"));
            graphics.fillOval(canvas.getWidth()/50 + bact.getPosX() * 50 + 2, canvas.getHeight()/50 + bact.getPosY() * 50, 25, 25);

        });
        
    }

    @Override
    public void run(){
        repaint(canvas.getGraphicsContext2D());
    }
    
    public static void main(String[] args){
        launch(args);
        
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Biofilm Simulation");
        stage.setWidth(800);
        stage.setHeight(600);
        //Setting up the scenary
        StackPane container = new StackPane();
        canvas = new Canvas(500,600);
        GraphicsContext graphics = canvas.getGraphicsContext2D();

        //Setting background of the canvas
        graphics.setFill(Color.valueOf("WHITE"));
        graphics.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        //Drawing Grid
        graphics.setFill(Color.valueOf("BLACK"));
        for(int i=0;i<canvas.getWidth();i = i + 50){
            graphics.strokeLine(i,0,i,canvas.getHeight());
            for(int j = 0;j<canvas.getHeight(); j = j + 50){
                graphics.strokeLine(0,j,canvas.getWidth(),j);
            }
        }

        AnimationTimer anime = new AnimationTimer() {
            @Override
            public void handle(long now) {
                repaint(graphics);
            }
        };

        container.getChildren().add(canvas); 
        Scene scene = new Scene (container, 800, 600);
        stage.setScene(scene);
        stage.show();
        bact.start(); // Start the bacteria thread
        anime.start(); // Start the animation timer
    }
}