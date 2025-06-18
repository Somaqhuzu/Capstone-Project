import java.util.concurrent.ThreadLocalRandom;
import environment.Surface;
import environment.SurfaceBlock;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;

public class App extends Application{
    private Canvas canvas;
    private WritableImage img;
    private Surface surface;
    private static int population;
    
    public static void main(String[] args){
        population = Integer.parseInt(args[0]);
        launch(args);
           
    }

     private WritableImage createGridImage(int cellSize) {
         //Drawing Grid
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.valueOf("BLACK"));

        SurfaceBlock[][] surfaceBlocks = new SurfaceBlock[(int)(canvas.getWidth()/cellSize)][(int)(canvas.getHeight()/cellSize)];

        for(int i=0;i<canvas.getWidth();i = i + cellSize){

            graphics.strokeLine(i,0,i,canvas.getHeight());
            for(int j = 0;j<canvas.getHeight(); j = j + cellSize){
                if(j ==0){
                    graphics.setFill(Color.valueOf("BROWN"));
                    graphics.fillRect(i,j,cellSize,cellSize);
                }
                graphics.strokeLine(0,j,canvas.getWidth(),j);
                int rand = ThreadLocalRandom.current().nextInt(3) - 1;
                if (rand==1){
                    SurfaceBlock e = new SurfaceBlock(i/cellSize, j/cellSize,1);
                    surfaceBlocks[i/cellSize][j/cellSize] = e;
                    Surface.getNutritiousBlocks().add(e);
                }
                else if(rand==0){
                    SurfaceBlock e = new SurfaceBlock(i/cellSize, j/cellSize,-1);
                    surfaceBlocks[i/cellSize][j/cellSize] = e;
                    Surface.getNutritiousBlocks().add(e);
                }
                else
                    surfaceBlocks[i/cellSize][j/cellSize] = new SurfaceBlock(i/cellSize, j/cellSize);
            }
        }
        
        surface = new Surface(surfaceBlocks,population);
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

        img = createGridImage( 50);

        AnimationTimer anime = new AnimationTimer() {
            @Override
            public void handle(long now) {
                surface.repaint(img,canvas);
            }
        };

        container.getChildren().add(canvas); 
        Scene scene = new Scene (container, 1280, 600);
        stage.setScene(scene);
        stage.show();
        anime.start(); // Start the animation timer
        for(int i=0;i<surface.getPopulation().length;i++){
            surface.getPopulation()[i].start();
        };

    }
}