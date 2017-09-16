package Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luke on 03/10/2015.
 */
public class PathFinder {
    private int Width, Height;
    private static int SquareSize = 20;
    public static BufferedImage redSquare;
    public static BufferedImage greenSquare;
    public static BufferedImage greySquare;
    public static BufferedImage green;
    public static BufferedImage blue;
    private Tile[][] tiles;;
    private TileTypes selectedTile;
    private int selectedTileX;
    private int selectedTileY;
    private TileTypes previousType = TileTypes.White;
    private TileTypes paintType = null;
    private ArrayList<Node> nodes = new ArrayList<>();
    private List openList = new ArrayList<>();
    private List closedList = new ArrayList<>();
    private int StartnodeNumber;
    private int EndNodeNumber;
    private boolean Calculating = false;
    private boolean Initialized = false;
    private boolean Finished = false;
    private int FinalTile;
    private int HeuristicNumber = 10;
    private boolean Menu = true;
    private Font TitleFont = new Font("Arial", Font.PLAIN, 80);
    private Font OtherFont = new Font("Arial", Font.PLAIN, 50);
    private Font SignFont = new Font("Arial", Font.PLAIN, 20);
    private int Result;
    private boolean Printed = false;


    public PathFinder(int Width,int Height){
        this.Width = Width;
        this.Height = Height;
        tiles = new Tile[Width/SquareSize][Height/SquareSize];
        redSquare = LoadImage("resources/RedSquare.gif");
        greenSquare = LoadImage("resources/GreenSquare.gif");
        greySquare = LoadImage("resources/GreySquare.gif");
        green = LoadImage("resources/Green.gif");
        blue = LoadImage("resources/Blue.gif");
        Init();
    }

    public void Init(){
        if(!Calculating) {
            for (int y = 0; y < Height / SquareSize; y++) {
                for (int x = 0; x < Width / SquareSize; x++) {
                    tiles[x][y] = new Tile(TileTypes.White);
                }
            }
            tiles[4][4].setType(TileTypes.Green);
            tiles[8][4].setType(TileTypes.Red);
        }
    }


    public BufferedImage LoadImage(String fileLocation){
        BufferedImage image;
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            image = ImageIO.read((cl.getResource(fileLocation)));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(){
        int newNumber = 9;
        Node node = new Node(50000);
        if (Initialized && !Finished) {
            node = nodes.get(FindLowestFValue());
            RemoveFromOpenList(node.getNumber());
            closedList.add(node.getNumber());
            for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    newNumber = node.getNumber() + x + y * Width / SquareSize;
                    if (node.getNumber() < 0) {
                        System.out.println("Stop");
                    }
                    if (!(newNumber < 0 || newNumber > Width / SquareSize * Height / SquareSize - 1 || newNumber == node.getNumber() || onClosedList(newNumber)  || (x == -1 && newNumber % (Width / SquareSize) > node.getNumber() % (Width / SquareSize))  || (x == 1 && newNumber % (Width / SquareSize) < node.getNumber() % (Width / SquareSize)))) {
                        if(!(nodes.get(node.getNumber() + x).isBlocked() && nodes.get(node.getNumber() + y * Width / SquareSize).isBlocked())) {
                            Node newNode = nodes.get(newNumber);
                            if (!newNode.isBlocked()) {
                                if (!onOpenLIst(newNumber)) {
                                    //System.out.println("adding.... " + newNumber);
                                    openList.add(newNumber);
                                    newNode.setParent(node);
                                }
                                if (newNode.getHeuristic() == HeuristicNumber) {
                                    FinalTile = newNode.getNumber();
                                    RemoveFromOpenList(newNumber);
                                    closedList.add(newNumber);
                                    Finished = true;
                                }
                                if (newNode.getMovementCost() == 0) {
                                    if (x != 0 && y != 0) {
                                        newNode.setMovementCost(node.getMovementCost() + 14);
                                    } else {
                                        newNode.setMovementCost(node.getMovementCost() + 10);
                                    }
                                } else {
                                    if (x != 0 && y != 0) {
                                        if (newNode.getMovementCost() > node.getMovementCost() + 14) {
                                            newNode.setMovementCost(node.getMovementCost() + 14);
                                            newNode.setParent(node);
                                        }
                                    } else {
                                        if (newNode.getMovementCost() > node.getMovementCost() + 10) {
                                            newNode.setMovementCost(node.getMovementCost() + 10);
                                            newNode.setParent(node);
                                        }
                                    }
                                }
                                newNode.CalculateFValue();
                                onClosedList(6);
                            }
                        }
                    }
                }
            }
        }
    }

    private void RemoveFromOpenList(int number) {
        for(int i = 0; i < openList.size();i++){
            if((int)openList.get(i) == number){
                openList.remove(i);
                return;
            }
        }
    }

    /*private void RemoveFromClosedList(int number) {
        for(int i = 0; i < openList.size();i++){
            if((int)openList.get(i) == number){
                openList.remove(i);
                return;
            }
        }
    }*/

    private int FindLowestFValue() {
        int lowest = 100000;
        int index = 0;
        if(openList.size() == 0){
            System.out.println("No Path!");
            Finished = true;
            return StartnodeNumber;
        }
        for(int i = 0; i < openList.size(); i++){
            if(nodes.get((int)openList.get(i)).getFValue() < lowest){
                lowest = nodes.get((int)openList.get(i)).getFValue();
                index = (int)openList.get(i);
            }
        }
        return index;
    }

    private boolean onOpenLIst(int number){
        for(int i = 0; i < openList.size(); i ++){
            if((int)openList.get(i) == number)return true;
        }
        return false;
    }
    private boolean onClosedList(int number){
        for(int i = 0; i < closedList.size(); i ++){
            if((int)closedList.get(i) == number)return true;
        }
        return false;
    }


    public void StartSearch(){
        if(Menu) {
            Menu = false;
        } else if(!Calculating) {
            nodes.clear();
            Calculating = true;

            for (int y = 0; y < Height / SquareSize; y++) {
                for (int x = 0; x < Width / SquareSize; x++) {
                    Node node = new Node(x + y * Width / SquareSize);
                    if (getTile(x, y).getType() == TileTypes.Grey) {
                        node.setBlocked(true);
                    }
                    nodes.add(node);
                    if (getTile(x, y).getType() == TileTypes.Green) {

                        StartnodeNumber = x + y * Width / SquareSize;
                    }
                    if (getTile(x, y).getType() == TileTypes.Red) {
                        EndNodeNumber = x + y * Width / SquareSize;
                    }
                }
            }
            System.out.println(EndNodeNumber);
            int GridWidth = Width / SquareSize;
            for (int i = 0; i < nodes.size(); i++) {
                nodes.get(i).setHeuristic((Math.abs(EndNodeNumber / GridWidth - nodes.get(i).getNumber() / GridWidth) + Math.abs( (nodes.get(i).getNumber() % GridWidth) - (EndNodeNumber % GridWidth)))*HeuristicNumber);
            }


            for (int i = 0; i < nodes.size(); i++) {
                //System.out.println("node " + i + " number: " + nodes.get(i).getNumber());
                //System.out.println("Heuristic " + nodes.get(i).getHeuristic());
            }
            openList.clear();
            closedList.clear();
            nodes.get(StartnodeNumber).CalculateFValue();
            System.out.println("Start number " + StartnodeNumber);
            openList.add(StartnodeNumber);
            Initialized = true;
        }else if(Finished){
            Calculating = false;
            Finished = false;
            Initialized = false;
            Printed = false;
        }
    }



    public void draw(Graphics2D g){
        if(!Menu) {
            g.setColor(Color.BLACK);

            for (int x = 0; x <= Width; x += SquareSize) {
                g.setStroke(new BasicStroke(2));
                g.drawLine(x, 0, x, Height);
            }
            for (int y = 0; y <= Height; y += SquareSize) {
                g.setStroke(new BasicStroke(2));
                g.drawLine(0, y, Width, y);
            }
            for (int y = 0; y < Height / SquareSize; y++) {
                for (int x = 0; x < Width / SquareSize; x++) {
                    g.drawImage(tiles[x][y].getImage(), x * SquareSize + 1, y * SquareSize + 1, SquareSize - 2, SquareSize - 2, null);
                }
            }
            if (Initialized) {
                for (int i = 0; i < openList.size(); i++) {
                    g.drawImage(blue, ((int) openList.get(i) % (Width / SquareSize)) * SquareSize + 1, ((int) openList.get(i) / (Width / SquareSize)) * SquareSize + 1, SquareSize - 2, SquareSize - 2, null);
                }
                for (int i = 0; i < closedList.size(); i++) {
                    g.drawImage(green, ((int) closedList.get(i) % (Width / SquareSize)) * SquareSize + 1, ((int) closedList.get(i) / (Width / SquareSize)) * SquareSize + 1, SquareSize - 2, SquareSize - 2, null);
                }
                g.drawImage(redSquare,( EndNodeNumber % (Width/SquareSize))* SquareSize + 1,(EndNodeNumber /(Width/SquareSize))*SquareSize + 1,SquareSize - 2,SquareSize - 2,null);
                g.drawImage(greenSquare,(StartnodeNumber % (Width/SquareSize)) * SquareSize + 1,(StartnodeNumber /(Width/SquareSize))*SquareSize + 1,SquareSize - 2,SquareSize - 2,null);
            }
            if (Finished) {
                g.setColor(Color.YELLOW);
                g.setStroke(new BasicStroke(5));
                g.drawLine((EndNodeNumber % (Width / SquareSize)) * SquareSize + SquareSize / 2, (EndNodeNumber / (Width / SquareSize)) * SquareSize + SquareSize / 2, (FinalTile % (Width / SquareSize)) * SquareSize + SquareSize / 2, (FinalTile / (Width / SquareSize)) * SquareSize + SquareSize / 2);
                Node checkTile = nodes.get(FinalTile);
                Result = nodes.get(FinalTile).getMovementCost();
                while (checkTile.getParent() != null) {
                    g.drawLine((checkTile.getNumber() % (Width / SquareSize)) * SquareSize + SquareSize / 2, (checkTile.getNumber() / (Width / SquareSize)) * SquareSize + SquareSize / 2, (checkTile.getParent().getNumber() % (Width / SquareSize)) * SquareSize + SquareSize / 2, (checkTile.getParent().getNumber() / (Width / SquareSize)) * SquareSize + SquareSize / 2);
                    checkTile = checkTile.getParent();
                }
                if(!Printed) {
                    System.out.println("The result is " + Result);
                    Printed = true;
                }
            }
        }else{
            g.setFont(TitleFont);
            g.setColor(Color.BLACK);
            g.drawString("A* Path Finding",150,100);
            g.setFont(OtherFont);
            g.drawImage(greenSquare, 150, 200, 80, 80, null);
            g.drawString("Start of Search",250,260);
            g.drawImage(redSquare,150,350,80,80,null);
            g.drawString("End of Search",250,410);
            g.drawImage(greySquare,150,500,80,80,null);
            g.drawString("Search Obstacle",250,560);
            g.drawString("Use Enter To Start",200,700);
            g.setFont(SignFont);
            g.drawString("Luke Ely", 700, 750);


        }
    }

    public int getTileX(int x){
        return x / SquareSize;
    }
    public int getTileY(int y){
        return y / SquareSize;
    }

    public Tile getTile(int x, int y){
        try{
            return tiles[x][y];
        }catch(ArrayIndexOutOfBoundsException exception ){

        }
        return null;
    }

    public void mouseClicked(MouseEvent e) {
        if(!Calculating) {
            int x = e.getX();
            int TileX = getTileX(x);
            int y = e.getY();
            int TileY = getTileY(y);
            Tile tile = getTile(TileX, TileY);
            TileTypes type = tile.getType();
            System.out.println("Clicked");
            if (type == TileTypes.White) {
                paintType = TileTypes.Grey;
                tile.setType(paintType);
            }
            if (type == TileTypes.Grey) {
                paintType = TileTypes.White;
                tile.setType(paintType);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("Released");
        selectedTile = null;
        paintType = null;
        System.out.println("changed to " + paintType);
        selectedTileX = -1;
        selectedTileY = -1;

    }

    public void mouseDragged(MouseEvent e) {
        if(!Calculating) {
            int x = e.getX();
            int TileX = getTileX(x);
            int y = e.getY();
            int TileY = getTileY(y);
            Tile tile = getTile(TileX, TileY);
            if (tile != null) {
                TileTypes type = tile.getType();
                if (selectedTile == null) {
                    if (paintType == null) {
                        System.out.println("tile " + type);
                        System.out.println("paint " + paintType);
                        if (type == TileTypes.Green || type == TileTypes.Red) {
                            selectedTile = type;
                            selectedTileX = TileX;
                            selectedTileY = TileY;
                        }

                        if (type == TileTypes.White) {
                            System.out.println("White");
                            paintType = TileTypes.Grey;
                            tile.setType(TileTypes.Grey);
                            System.out.println("after " + type);
                        } else if (type == TileTypes.Grey) {
                            paintType = TileTypes.White;
                            tile.setType(TileTypes.White);
                        }
                    }
                }
                if (selectedTile == TileTypes.Green || selectedTile == TileTypes.Red) {
                    if (selectedTileX != TileX || selectedTileY != TileY) {
                        if (type == TileTypes.White) {
                            tiles[selectedTileX][selectedTileY].setType(previousType);
                            previousType = tiles[TileX][TileY].getType();
                            tiles[TileX][TileY].setType(selectedTile);
                            selectedTileX = TileX;
                            selectedTileY = TileY;
                        }
                    }
                }
                if (paintType != null) {
                    if (type != paintType && type != TileTypes.Red && type != TileTypes.Green) {
                        tile.setType(paintType);
                    }
                }

            }
        }
    }

    public void up() {
        if(!Calculating) {
            HeuristicNumber++;
            System.out.println("Heuristic " + HeuristicNumber);
        }
    }
    public void down() {
        if(!Calculating) {
            HeuristicNumber--;
            System.out.println("Heuristic " + HeuristicNumber);
        }
    }

    public void cancel() {
        Calculating = false;
        Finished = false;
        Initialized = false;
        Printed = false;
    }
}
