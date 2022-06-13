public class Floor {

    private int numFloor;
    private int destination;
    private boolean destinationEntered;
    private boolean needsElevator;
    private boolean needsUp;

    public Floor(int numFloor) {
        this.numFloor = numFloor;
        this.destinationEntered = false;
        this.destination = 0;
        this.needsElevator = false;
        this.needsUp = false;
    }

    public int getNumFloor() {
        return this.numFloor;
    }

    public int getDistination() {
        return this.destination;
    }

    public boolean getDestinationEntered() {
        return this.destinationEntered;
    }

    public boolean getNeedsElevator() {
        return this.needsElevator;
    }

    public boolean getNeedsUp() {
        return this.needsUp;
    }

    public void setNeedsElevator(boolean bool) {
        this.needsElevator = bool;
    }

    public void setDestinationEntered(boolean bool) {
        this.destinationEntered = bool;
    }

    public void askForElevator(int destination) {
        this.destination = destination;
        this.needsUp = destination - this.numFloor > 0;
        this.destinationEntered = true;
        this.needsElevator = true;
    }

    public void printFloor() {
        String elevatorLight = this.needsElevator ? "1" : "0";
        System.out.println("(" + this.numFloor + ") " + elevatorLight);
    }
}