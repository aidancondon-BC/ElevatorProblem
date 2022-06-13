import java.util.ArrayList;

public class Elevator {

    private int elevatorNum;
    private int currentFloorNum;
    private int moves;
    private boolean isGoingUp;
    private ArrayList<Floor> floorSchedule;

    public Elevator (int elevatorNum) {
        this.elevatorNum = elevatorNum;
        this.moves = 0;
        this.currentFloorNum = 0;
        this.isGoingUp = false;
        this.floorSchedule = new ArrayList<Floor>();
    }

    public int getElevatorNum() {
        return this.elevatorNum;
    }

    public int getCurrentFloorNum() {
        return this.currentFloorNum;
    }

    public int getMoves() {
        return this.moves;
    }

    public boolean getIsGoingUp() {
        return this.isGoingUp;
    }

    public ArrayList<Floor> getFloorSchedule() {
        return this.floorSchedule;
    }

    public int getFloorScheduleSize() {
        return this.floorSchedule.size();
    }

    // this is the next floor that it would stop at, not necessarily the next change in direction
    private int getNextFloorNum() {
        if (this.floorSchedule.size() == 0) return 0;
        return this.floorSchedule.get(0).getNumFloor();
    }

    // returns the next floor the elevator will change directions on;
    // if not scheduled to change directions, it just returns the last floor in schedule
    public Floor getNextChangeOfDir() {
        boolean currentDirection = this.getIsGoingUp();
        for(Floor floor : this.floorSchedule) {
            if (floor.getNeedsUp() != currentDirection) {
                return floor;
            }
        }
        return this.floorSchedule.get(this.floorSchedule.size()-1);
    }

    public int stopsBetween(Floor a) {
        int stops = 0;
        int start = this.currentFloorNum;
        int end = a.getNumFloor();
        for(Floor floor: this.floorSchedule) {
            int num = floor.getNumFloor();
            if (((num > start) && (num < end)) || ((num < start) && (num > end))) {
                stops++;
            }
        }
        return stops;
    }

    public int currentPath() {
        if (this.floorSchedule.size() == 0) return 0;
        Floor nextChange = this.getNextChangeOfDir();
        int start = this.currentFloorNum;
        int end = nextChange.getNumFloor();
        return Math.abs(end - start) + this.stopsBetween(nextChange);
    }

    public int pathOfInterestINSIDE() {
        return this.currentPath() + 1;
    }

    public int pathOfInterestOUTSIDE(Floor a) {
        int start = this.currentFloorNum;
        int end = this.floorSchedule.get(this.getFloorScheduleSize()-1).getNumFloor();
        int stops = this.stopsBetween(this.floorSchedule.get(this.getFloorScheduleSize()-1));
        int initialPath = Math.abs(end - start) + stops;
        return initialPath + Math.abs(end - a.getNumFloor());
    }

    public void setIsGoingUp(boolean isUp) {
        this.isGoingUp = isUp;
    }

    public void setCurrentFloorNum(int num) {
        this.currentFloorNum = num;
    }

    public void setMoves(int num) {
        this.moves = num;
    }

    public void addFloor(Floor floor) {
        this.floorSchedule.add(floor);
    } 
    
    private boolean isOnNext() {
        return this.floorSchedule.get(0).getNumFloor() == this.currentFloorNum;
    }

    public void move() {
        if (this.floorSchedule.isEmpty()) return; 
        Floor next = this.floorSchedule.get(0);
        if (next.getNumFloor() > this.currentFloorNum) {
            this.currentFloorNum++;
        }
        else {
            this.currentFloorNum--;
        }
        if (this.isOnNext()) {
            if (this.floorSchedule.get(0).getDestinationEntered()) {
                Floor destination = new Floor(this.floorSchedule.get(0).getDistination());
                this.floorSchedule.add(destination);
            }
            this.floorSchedule.get(0).setNeedsElevator(false);
            this.floorSchedule.remove(0);
        }
    }

    public void makeMoves(int numMoves) {
        for(int i = 0; i < numMoves; i++) {
            this.move();
        }
    }

    public void moveToNext() {
        int start = this.currentFloorNum;
        int end = this.getNextFloorNum();
        for(int i = 0; i < Math.abs(end - start); i++) {
            this.move();
        }
    }

    public void printElevator() {
        System.out.println(this.elevatorNum + " has a next destination of " + this.getNextFloorNum() + ", and is on " + this.currentFloorNum);
        for (Floor floor : this.floorSchedule) {
            System.out.println(this.currentFloorNum + " -> " + floor.getNumFloor());
        }
    }
}