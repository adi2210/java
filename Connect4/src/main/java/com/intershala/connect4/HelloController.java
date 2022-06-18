package com.intershala.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloController implements Initializable {

    private static final int Column=7;
    private static final int Row=6;
    public static final int circle_d=80;

    private static final String disColour1="#24303E";
    private static final String disColour2="#4CAA88";

    public static String player1="Player One";
    public static String player2="Player Two";

    private boolean Isplayer1=true;
    private Disc[][] insertDiscsArray=new Disc[Row][Column];


    @FXML
   public GridPane gpane;

    @FXML
    public Pane dpane;

    @FXML
    public Label playerin;

    private boolean isallowedtoinsert=true;


    public void creatplayground(){

        Shape rectangleWithholes= new Rectangle(Column*circle_d,Row*circle_d);

        List<Rectangle> rectangleList= clickablecol();
        for (Rectangle rectangle:rectangleList) {

            gpane.add(rectangle,0,1);

        }


               for(int c=0;c<Column;c++) {
                   for (int r = 0; r < Row; r++) {

                       Circle circle = new Circle();
                       circle.setRadius(circle_d / 2);
                       circle.setCenterX(circle_d / 2);
                       circle.setCenterY(circle_d / 2);
                       circle.setSmooth(true);

                       circle.setTranslateY(r*circle_d);
                       circle.setTranslateX(c*circle_d);

                       rectangleWithholes = Shape.subtract(rectangleWithholes, circle);
                   }
               }
        rectangleWithholes.setFill(Paint.valueOf("white"));
        gpane.add(rectangleWithholes,0,1);


            }

          private List<Rectangle> clickablecol(){

              List<Rectangle> rectangleList= new ArrayList<>();

           for (int c=0;c<Column;c++) {

               Rectangle rectangle = new Rectangle(circle_d, Row * circle_d);
               rectangle.setFill(Color.TRANSPARENT);
               rectangle.setTranslateX(c*circle_d);
               rectangle.setOnMouseEntered(mouseEvent -> rectangle.setFill(Color.valueOf("#eeeeee26")));
               rectangle.setOnMouseExited(mouseEvent -> {
                   rectangle.setFill(Color.TRANSPARENT);
               });
               final int column=c;
               rectangle.setOnMouseClicked(mouseEvent ->{
                   if (isallowedtoinsert) {
                       isallowedtoinsert=false;
                       InsertDisc(new Disc(Isplayer1), column);
                   }
               } );
             rectangleList.add(rectangle);

           }
              return rectangleList;
          }

          private  void InsertDisc(Disc disc,int column){

             int row=Row-1;
             while (row>=0){
                 if (getDiscIfPresnt(row,column)==null)
                     break;
                 row--;
             }

             if (row<0)
                 return;

               insertDiscsArray[row][column]=disc;
               dpane.getChildren().add(disc);
               disc.setTranslateX(column*circle_d);
               int currentrow=row;
              TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
              translateTransition.setToY(row*circle_d);
              translateTransition.setOnFinished(actionEvent -> {
                  isallowedtoinsert=true;
                  if (gameend(currentrow,column)){
                      gameover();
                      return;
                  }
                  Isplayer1=!Isplayer1;
                  playerin.setText(Isplayer1?player1:player2);

              });
              translateTransition.play();

          }



    private boolean gameend(int row, int column) {
     List<Point2D> verticalPoints= IntStream.rangeClosed(row-3,row+3).mapToObj(r-> new Point2D(r,column)).collect(Collectors.toList());

     List<Point2D> horizentalPoints= IntStream.rangeClosed(column-3,column+3).mapToObj(col-> new Point2D(row,col)).collect(Collectors.toList());

     Point2D startPoint1=new Point2D(row-3,column+3);
     List<Point2D> diagonal1Points=IntStream.rangeClosed(0,6).mapToObj(i->startPoint1.add(i,-i)).collect(Collectors.toList());

     Point2D startPoint2=new Point2D(row-3,column-3);
        List<Point2D> diagonal2Points=IntStream.rangeClosed(0,6).mapToObj(i->startPoint2.add(i,i)).collect(Collectors.toList());

      boolean IsEnd=checkCombitions(verticalPoints)||checkCombitions(horizentalPoints)||checkCombitions(diagonal1Points)||checkCombitions(diagonal2Points);
     return IsEnd;
    }

    private boolean checkCombitions(List<Point2D>points){

        int chain=0;
        for (Point2D point:points){

            int rowIndexForArray=(int) point.getX();
            int columnIndexForArray=(int) point.getY();

            Disc disc=getDiscIfPresnt(rowIndexForArray,columnIndexForArray);
            if (disc!=null&&disc.Isplayer1move == Isplayer1){
                   chain++;
                   if (chain==4){
                       return true;
                   }
            }else {
                chain=0;
            }

        }
        return false;
    }

    private Disc getDiscIfPresnt(int row,int column){
        if (row>=Row||row<0||column>=Column||column<0)
            return null;

        return insertDiscsArray[row][column];
    }

    private static class Disc extends Circle{
         private final boolean Isplayer1move;
         public Disc(boolean Isplayer1move){

             this.Isplayer1move =Isplayer1move;
             setRadius(circle_d/2);
             setFill(Isplayer1move?Color.valueOf(disColour1):Color.valueOf(disColour2));
             setCenterX(circle_d/2);
             setCenterY(circle_d/2);

         }

        }

    private void gameover() {
        String winner= Isplayer1?player1:player2;
        System.out.println("Winner is " + winner);

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText("The Winner is" + winner);
        alert.setContentText("Want to play again");

        ButtonType yesbtn=new ButtonType("Yes");
        ButtonType nobtn=new ButtonType("No,Exit");
        alert.getButtonTypes().setAll(yesbtn,nobtn);

       Platform.runLater(()->{
           Optional<ButtonType>btnclick=alert.showAndWait();
           if (btnclick.isPresent()&&btnclick.get()==yesbtn){
               resetgame();
           }else {
               Platform.exit();
               System.exit(0);
           }
       });


    }

    public void resetgame() {

        dpane.getChildren().clear();

        for (int row = 0; row < insertDiscsArray.length; row++) {
            for (int col = 0; col < insertDiscsArray[row].length; col++) {
                insertDiscsArray[row][col]=null;

            }

        }

        Isplayer1=true;
        playerin.setText(player1);
        creatplayground();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}