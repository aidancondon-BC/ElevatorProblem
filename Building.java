import java.util.HashMap;
import java.util.Random;
import java.util.LinkedHashSet;
import java.lang.Thread;

public class Building {

    private int numFloors;
    private int numElevators;
    private HashMap<Integer, Floor> floors;
    private HashMap<Integer, Elevator> elevators;

    public Building(int numFloors, int numElevators) {
        this.numFloors = numFloors;
        this.numElevators = numElevators;
        this.floors = new HashMap<Integer, Floor>(this.numFloors);
        this.elevators = new HashMap<Integer, Elevator>(this.numElevators);
        for(int i = 0; i < this.numFloors; i++) this.floors.put(i, new Floor(i));
        for(int i = 0; i < this.numElevators; i++) this.elevators.put(i, new Elevator(i));
    }

    public void assign(Floor target) {
        int shortestPath = 0;
        int bestElevator = 0;
        for(Integer elevatorNum : this.elevators.keySet()) {
            Elevator elevator = this.elevators.get(elevatorNum);
            int path = elevator.currentPath();
            if (elevator.currentPath() == 0) {
                this.elevators.get(elevatorNum).addFloor(target);
                return;
            }
            else {
                int NCFN = elevator.getNextChangeOfDir().getNumFloor();
                int TFN = target.getNumFloor();
                int CFN = elevator.getCurrentFloorNum();

                boolean isPassing = ((TFN > CFN) && (TFN < NCFN)) || ((TFN < CFN) && (TFN > NCFN));
                boolean tarWantsFinalDir = elevator.getIsGoingUp() == target.getNeedsUp();

                if (isPassing && tarWantsFinalDir) {
                    path = elevator.pathOfInterestINSIDE();
                }
                else {
                    path = elevator.pathOfInterestOUTSIDE(target);
                }

            }
            if (path < shortestPath) {
                shortestPath = path;
                bestElevator = elevatorNum;
            }
        }
        this.elevators.get(bestElevator).addFloor(target);
    }

    public void setInitialSignals() {
        Random r = new Random(); 
        
        LinkedHashSet<Integer> floorsWithElevators = new LinkedHashSet<Integer>();
        for(Elevator elevator: this.elevators.values()) {
            floorsWithElevators.add(elevator.getCurrentFloorNum());
        }
        
        int floorNum = r.nextInt(this.numFloors);
        while (this.floors.get(floorNum).getNeedsElevator() || floorsWithElevators.contains(floorNum))
            floorNum = r.nextInt(this.numFloors);
        
        int floorDestination = r.nextInt(this.numFloors);
        while (floorNum == floorDestination || floorsWithElevators.contains(floorNum))
            floorDestination = r.nextInt(this.numFloors);
        
        this.floors.get(floorNum).askForElevator(floorDestination);
        this.assign(this.floors.get(floorNum));
    }

    public void sendSignal() {
        Random r = new Random();
        int floorNumToSignal = r.nextInt(this.numFloors);
        while (this.floors.get(floorNumToSignal).getNeedsElevator()) {
            floorNumToSignal = r.nextInt(this.numFloors);
        }
        this.floors.get(floorNumToSignal).askForElevator(r.nextInt(this.numFloors));
        this.assign(this.floors.get(floorNumToSignal));
    }

    public int movesBeforeNextSignal() {
        int max = 0;
        for(Elevator elevator: this.elevators.values()) {
            int curPath = elevator.currentPath();
            if (curPath > max) {
                max = curPath;
            }
        }
        int roundedPortion = (int) (this.numFloors / 2);
        if (max <= roundedPortion) return max;
        int num = new Random().nextInt(max - roundedPortion) + roundedPortion;
        return num;
    }

    public void printFloorStatus() {
        for(Floor floor : this.floors.values()) {
            floor.printFloor();
        }
    }

    public void printElevators() {
        for(Elevator elevator : this.elevators.values()) {
            elevator.printElevator();
        }
        System.out.println("––––––––––––––––––");
    }

    public void moveElevators() {
        int numMoves = this.movesBeforeNextSignal();
        for(Elevator elevator : this.elevators.values()) {
            elevator.makeMoves(numMoves);
        }
    }

    public void startSignaling() throws InterruptedException {
        this.setInitialSignals();
        this.printElevators();
        while (true) {
            this.moveElevators();
            this.sendSignal();
            this.printElevators();
            Thread.sleep(5000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("\033[H\033[2J");
        int numberOfFloors = 10;
        int numberOfElevators = 2;
        Building b = new Building(numberOfFloors, numberOfElevators);
        b.startSignaling();
    } 
}