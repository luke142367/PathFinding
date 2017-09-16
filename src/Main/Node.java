package Main;

/**
 * Created by LUKE_2 on 03/10/2015.
 */
public class Node {
    private int Number;
    private int Heuristic;
    private int MovementCost;
    private int FValue;
    private Node Parent;
    private boolean blocked = false;


    public Node(int Number){
        this.Number = Number;

    }

    public int getNumber(){
        return Number;
    }

    public void setHeuristic(int set){
        this.Heuristic = set;
    }
    public int getHeuristic(){
        return Heuristic;
    }

    public void setMovementCost(int set){
        this.MovementCost = set;
    }

    public int getMovementCost() {
        return MovementCost;
    }

    public void CalculateFValue(){
        FValue = Heuristic + MovementCost;
    }

    public int getFValue() {
        return FValue;
    }

    public void setParent(Node Parent){
        this.Parent = Parent;
    }

    public Node getParent() {
        return Parent;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isBlocked(){
        return blocked;
    }
}
