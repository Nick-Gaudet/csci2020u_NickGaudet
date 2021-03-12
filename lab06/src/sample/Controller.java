package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;


public class Controller {

    @FXML
    private Canvas canvas;
    private static double[] avgHousingPricesByYear = {
            247381.0,264171.4,287715.3,294736.1,
            308431.4,322635.9,340253.0,363153.7
    };
    private static double[] avgCommercialPricesByYear = {
            1121585.3,1219479.5,1246354.2,1295364.8,
            1335932.6,1472362.0,1583521.9,1613246.3
    };
    private static String[] ageGroups = {
            "18-25", "26-35", "36-45", "46-55", "56-65", "65+"
    };
    private static int[] purchasesByAgeGroup = {
            648, 1021, 2453, 3173, 1868, 2247
    };
    private static Color[] pieColours = {
            Color.AQUA, Color.GOLD, Color.DARKORANGE,
            Color.DARKSALMON, Color.LAWNGREEN, Color.PLUM
    };

    @FXML
    private void initialize(){

        GraphicsContext g = canvas.getGraphicsContext2D();
        makePieChart(g);
        makeBarChart(g);
    }
    public void makeBarChart(GraphicsContext g){
        double max = avgCommercialPricesByYear[7];
        int x = 75, y;
        for(int i = 0; i < avgCommercialPricesByYear.length; i++){

            //set scales
            double heightHouse = (avgHousingPricesByYear[i]/max) * 425;
            double heightCommercial = (avgCommercialPricesByYear[i]/max) * 425;

            g.setFill(Color.RED);
            y = 450 - (int)heightHouse;
            g.fillRect(x,y,20,heightHouse);

            y = 450 - (int)heightCommercial;
            g.setFill(Color.BLUE);

            g.fillRect(x+20,y,20,heightCommercial);
            x += 50;
        }



    }
    public void makePieChart(GraphicsContext g){
        double  sumOfPurchases = 0;
        for (int age : purchasesByAgeGroup){
            sumOfPurchases += age;
        }

        double startingAngle = 0;
        int colourNum = 0;
        for(int purchase : purchasesByAgeGroup){
            double pieSlice = purchase/sumOfPurchases;
            double growingAngle = pieSlice * 360;
            Arc a = new Arc(700,150,250,250,startingAngle,growingAngle);
            a.setFill(pieColours[colourNum]);
            a.setType(ArcType.ROUND);
            g.setFill(a.getFill());
            g.fillArc(a.getCenterX(),a.getCenterY(),a.getRadiusX(),a.getRadiusY(),a.getStartAngle(),a.getLength(),a.getType());
            startingAngle += growingAngle;
            colourNum++;
        }



    }
}
