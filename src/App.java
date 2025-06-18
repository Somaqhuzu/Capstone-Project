import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CountDownLatch;
import environment.Surface;
import environment.SurfaceBlock;
import environment.SurfaceFacade;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;


public class App extends Application{
    private WritableImage img;
    private WritableImage imgTemp;
    private WritableImage imgHumidity;
    private SurfaceFacade surfaceType = SurfaceFacade.NULL;
    
    private static Canvas canvas;

    

    private static CountDownLatch latch = new CountDownLatch(1);
    
    public static void main(String[] args){
        Surface.Population = Integer.parseInt(args[0]);
        Surface.cellSize = Integer.parseInt(args[3]);
        canvas = new Canvas(Integer.parseInt(args[1]) * Surface.cellSize,Integer.parseInt(args[2]) * Surface.cellSize);
        launch(args);
           
           
    }

    private void initEnvironment(){
        SurfaceBlock[][] surfaceBlocks = new SurfaceBlock[(int)canvas.getWidth()/Surface.cellSize][(int)canvas.getHeight()/Surface.cellSize];
        for(int i=0;i<canvas.getWidth();i = i + Surface.cellSize){
            for(int j = 0;j<canvas.getHeight(); j = j + Surface.cellSize){

                if(ThreadLocalRandom.current().nextFloat() > 0.7){
                    int rand = ThreadLocalRandom.current().nextInt(3) - 1;
                    if (rand==1){
                        SurfaceBlock e = new SurfaceBlock(i/Surface.cellSize, j/Surface.cellSize,1);
                        surfaceBlocks[i/Surface.cellSize][j/Surface.cellSize] = e;
                        Surface.getNutritiousBlocks().add(e);
                    }
                    else if(rand==0){
                        SurfaceBlock e = new SurfaceBlock(i/Surface.cellSize, j/Surface.cellSize,-1);
                        surfaceBlocks[i/Surface.cellSize][j/Surface.cellSize] = e;
                        Surface.getNutritiousBlocks().add(e);
                    }
                }
                else
                    surfaceBlocks[i/Surface.cellSize][j/Surface.cellSize] = new SurfaceBlock(i/Surface.cellSize, j/Surface.cellSize);
                }
            }
            Surface.setSurfaceBlocks(surfaceBlocks);
        }



    private WritableImage createGridImage( environment.SurfaceFacade type) {
        float opacity;
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Paint.valueOf("BLACK"));

            for(int i=0;i<canvas.getWidth()/Surface.cellSize;i++){
                int k = i * Surface.cellSize;
                graphics.strokeLine(k,0,k,canvas.getHeight());

                for(int j=0;j<canvas.getHeight()/Surface.cellSize;j++){
                    int x = j * Surface.cellSize;
                    if(j ==0){
                        graphics.setFill(Color.valueOf("BROWN")); //Drawing up the surface wall
                        graphics.fillRect(k,x,Surface.cellSize,Surface.cellSize);
                        continue;
                    }
                    graphics.strokeLine(0,x,canvas.getWidth(),x);
                    SurfaceBlock block = Surface.getSurface(new int[]{i,j});
                    if(block != null){
                        if( type== SurfaceFacade.TEMPERATURE){
                            double temp = block.getTemperature();
                            if(0<=temp && temp<=200){
                                opacity = ThreadLocalRandom.current().nextFloat(0.01f);
                            }
                            else if(200<temp && temp<273){
                                opacity = ThreadLocalRandom.current().nextFloat(0.1f);
                            }
                            else if(273<=temp && temp<350){
                                opacity = ThreadLocalRandom.current().nextFloat(0.7f);
                            }
                            else {
                                opacity = 1;
                            }
                            graphics.setFill(Color.rgb(255, 165, 0, opacity));
                            graphics.fillRect(i * Surface.cellSize, j * Surface.cellSize, Surface.cellSize, Surface.cellSize);
                        }
                        
                        else if(type == SurfaceFacade.HUMIDITY){
                            double humidity = block.getHumidity();
                            graphics.setFill(Color.rgb(0, 0, 255, humidity));
                            graphics.fillRect(i * Surface.cellSize, j * Surface.cellSize, Surface.cellSize, Surface.cellSize);
                        }
                        else{
                            graphics.setFill(Color.valueOf("WHITE"));
                            graphics.fillRect(i * Surface.cellSize, j * Surface.cellSize, Surface.cellSize, Surface.cellSize);
                        }
                    }
                }
            }
        
        return canvas.snapshot(null,null);
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
        ComboBox<String> surfaceChange = new ComboBox<>();
        surfaceChange.getItems().addAll("Temperature","Humidity","None");

        surfaceChange.setOnAction(e->{
            String selected = surfaceChange.getValue();
            if(selected.equals("Temperature")){
                surfaceType = SurfaceFacade.TEMPERATURE;
                //img = createGridImage(SurfaceFacade.TEMPERATURE);
            } else if(selected.equals("Humidity")){
                surfaceType = SurfaceFacade.HUMIDITY;
                //img = createGridImage(SurfaceFacade.HUMIDITY);
            } else {
                surfaceType = SurfaceFacade.NULL;
                img = createGridImage(SurfaceFacade.NULL);
            }
           //surface.repaint(img, canvas);
        });

        initEnvironment();

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

        img = createGridImage( SurfaceFacade.NULL );
        imgTemp = createGridImage(SurfaceFacade.TEMPERATURE);
        imgHumidity = createGridImage(SurfaceFacade.HUMIDITY);

        AnimationTimer anime = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(surfaceType == SurfaceFacade.NULL){
                    Surface.repaint(img,canvas);
                }
                else if(surfaceType == SurfaceFacade.TEMPERATURE){
                    Surface.repaint(imgTemp,canvas);
                } 
                else if(surfaceType == SurfaceFacade.HUMIDITY){
                    Surface.repaint(imgHumidity,canvas);
                }
            }
        };

        environment.setContent(canvas);
        container.getChildren().add(environment);
        StackPane.setAlignment(start,Pos.BOTTOM_CENTER);
        container.getChildren().add(start); 
        StackPane.setAlignment(surfaceChange,Pos.TOP_CENTER);
        container.getChildren().add(surfaceChange);

        Scene scene = new Scene (container, 1280, 600);
        stage.setScene(scene);
        for(int i=0;i<Surface.Population;i++){
            Surface.getPopulation().add(new bacteria.Bacteria(i, ThreadLocalRandom.current().nextInt(300), ThreadLocalRandom.current().nextInt(100)));
        };

        stage.show();
        anime.start(); // Start the animation timer
    }
}