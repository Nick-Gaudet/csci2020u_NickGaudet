package sample;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.*;

public class Controller {
    @FXML
    private Canvas canvas;

    final private int WIDTH = 1000;
    final private int HEIGHT = 700;
    private int finalSize;
    private float max;

    public void initialize(){
        GraphicsContext g = canvas.getGraphicsContext2D();

        ArrayList<Float> google = downloadStockPrices("GOOG");
        ArrayList<Float> apple = downloadStockPrices("AMZN");
        this.finalSize = google.size() + apple.size();
        max = Collections.max(google);
        if (Collections.max(apple) > max){
            max = Collections.max(apple);
        }
        drawLinePlot(g,google,apple);
    }
    private ArrayList<Float> downloadStockPrices(String stock){
        ArrayList<Float> closingPrices = new ArrayList<Float>();

        String url1 = "https://query1.finance.yahoo.com/v7/finance/download/"
                + stock
                + "?period1=1262322000&period2=1451538000&interval=1mo&events=history&includeAdjustedClose=true";
        try{
            URL oracle = new URL(url1);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            in.readLine();
            while ((inputLine = in.readLine()) != null){
                closingPrices.add(Float.parseFloat(inputLine.split(",")[4]));
//                System.out.println(Float.parseFloat(inputLine.split(",")[4]));
            }
            in.close();

            System.out.println(closingPrices);
        }catch(Exception e){
            e.printStackTrace();
        }
        return closingPrices;

    }
    private void drawLinePlot(GraphicsContext g,ArrayList<Float> prices1, ArrayList<Float> prices2){


        g.strokeLine(50,50,50,HEIGHT-50); // Y AXIS
        g.strokeLine(50,HEIGHT-50,WIDTH-50,HEIGHT-50); // x - axis

        plotLine(g,prices1,Color.RED);
        plotLine(g,prices2,Color.BLUE);
    }
    private void plotLine(GraphicsContext g , ArrayList<Float> prices, Color color){
        double xScale = (double) ((WIDTH-100) / (this.finalSize)) * 2.1; // hard coded, took a while to tweak
        double yScale = ((double) (HEIGHT - 100) / (this.max));
        List<Point2D> graphPoints = new ArrayList<Point2D>();
        for (int i = 0; i < prices.size(); i++){
            int x0 = (int)(i * xScale+50);
            int y0 = (int)((max - prices.get(i)) * yScale + 50);
            graphPoints.add(new Point2D(x0,y0));
        }

        for(int i = 0; i < graphPoints.size()-1; i++){
            int x1 = (int) graphPoints.get(i).getX();
            int y1 = (int) graphPoints.get(i).getY();
            int x2 = (int) graphPoints.get(i + 1).getX();
            int y2 = (int) graphPoints.get(i + 1).getY();
            g.setStroke(color);
            g.strokeLine(x1,y1,x2,y2);


        }
    }
}
