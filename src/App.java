import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CountDownLatch;
import environment.Surface;
import environment.SurfaceBlock;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;


public class App extends Application{
    private WritableImage img;
    private Surface surface;
    
    private static Canvas canvas;
    private static int population;
    private static int cellSize;
    

    private static CountDownLatch latch = new CountDownLatch(1);
    
    public static void main(String[] args){
        population = Integer.parseInt(args[0]);
        cellSize = Integer.parseInt(args[3]);
        canvas = new Canvas(Integer.parseInt(args[1]) * cellSize,Integer.parseInt(args[2]) * cellSize);
        launch(args);
           
    }

     private WritableImage createGridImage() {
         //Drawing Grid
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.valueOf("BLACK"));

        SurfaceBlock[][] surfaceBlocks = new SurfaceBlock[300][100];
        
        graphics.setLineWidth(1);

        for(int i=0;i<canvas.getWidth();i = i + cellSize){

            graphics.strokeLine(i,0,i,canvas.getHeight());
            for(int j = 0;j<canvas.getHeight(); j = j + cellSize){
                if(j ==0){
                    graphics.setFill(Color.valueOf("BROWN")); //Drawing up the surface wall
                    graphics.fillRect(i,j,cellSize,cellSize);
                }
                graphics.strokeLine(0,j,canvas.getWidth(),j);
                
                if(ThreadLocalRandom.current().nextFloat() > 0.7){
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
            }
                else
                    surfaceBlocks[i/cellSize][j/cellSize] = new SurfaceBlock(i/cellSize, j/cellSize);
            }
        }
        
        surface = new Surface();
        Surface.setSurfaceBlocks(surfaceBlocks);
        return canvas.snapshot(null, null);
    }

    @SuppressWarnings("unused")
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Biofilm Simulation");
        stage.setWidth(1280);
        stage.setHeight(700);
        //Setting up the scenary
        StackPane container = new StackPane();
        ScrollPane environment = new ScrollPane();
        Button start = new Button("Start");

        start.setOnAction((e)->{
            if(latch.getCount() != 0) {
                latch.countDown();
                for(int i=0;i<Surface.getPopulation().size();i++){
                    Surface.getPopulation().get(i).start(); // Start each bacteria thread
                }

                Surface.started = true;            }   
            else{
            if(Surface.started){
                Surface.started = false;
                start.setText("Start");
            } else {
                Surface.started = true;
                start.setText("Pause");
            }
        }
        });
        GraphicsContext graphics = canvas.getGraphicsContext2D();

        //Setting background of the canvas
        graphics.setFill(Color.valueOf("WHITE"));
        graphics.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        img = createGridImage( );

        AnimationTimer anime = new AnimationTimer() {
            @Override
            public void handle(long now) {
                surface.repaint(img,canvas);
            }
        };

        environment.setContent(canvas);
        container.getChildren().add(environment);
        StackPane.setAlignment(start,Pos.BOTTOM_CENTER);
        container.getChildren().add(start); 

        Scene scene = new Scene (container, 1280, 600);
        stage.setScene(scene);
        for(int i=0;i<population;i++){
            Surface.getPopulation().add(new bacteria.Bacteria(i, ThreadLocalRandom.current().nextInt(300), ThreadLocalRandom.current().nextInt(100)));
        };

        stage.show();
        anime.start(); // Start the animation timer
    }
}