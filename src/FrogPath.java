/**
 * Implementation of a FrogPath class that finds the best path for a frog to traverse a pond.
 * @author Matt Farzaneh
 */
public class FrogPath {
    private Pond pond;

    /**
     * Constructs a FrogPath object with the specified filename for the pond configuration.
     * @param filename The filename of the pond configuration.
     */
    public FrogPath(String filename) {
        try {
            pond = new Pond(filename);
        } catch (Exception e) {
            System.out.println("Error initializing Pond: " + e.getMessage());
        }
    }

    /**
     * Finds the best next hexagon for the frog to move to based on priority.
     * @param currCell The current hexagon where the frog is located.
     * @return The best next hexagon to move to, or null if no available moves.
     */
    public Hexagon findBest(Hexagon currCell) {
        ArrayUniquePriorityQueue<Hexagon> priorityQueue = new ArrayUniquePriorityQueue<>();
        
        int numNeighbours = 6;
        
        for (int i = 0; i < numNeighbours; i++) {
            Hexagon neighbour = currCell.getNeighbour(i);
            
            if (neighbour != null && isAvailable(neighbour)) {
                priorityQueue.add(neighbour, priority(neighbour));
                
                if (currCell.isLilyPadCell()) {
                    Hexagon nextNeighbour = neighbour.getNeighbour(i);
                    
                    if (nextNeighbour != null && isAvailable(nextNeighbour)) {
                        priorityQueue.add(nextNeighbour, priority(nextNeighbour) + 0.5);
                    }
                    
                    Hexagon nextNextNeighbour = neighbour.getNeighbour((i + 1) % 6);
                    
                    if (nextNextNeighbour != null && isAvailable(nextNextNeighbour)) {
                        priorityQueue.add(nextNextNeighbour, priority(nextNextNeighbour) + 1.0);
                    }
                }
            }
        }
        
        if (priorityQueue.isEmpty()) {
            return null;
        } else {
            return priorityQueue.removeMin();
        }
    }

    /**
     * Finds the path for the frog to traverse the pond.
     * @return A string representation of the path and the number of flies eaten.
     */
    public String findPath() {
        int fliesEaten = 0;
        ArrayStack<Hexagon> stack = new ArrayStack<>();
        stack.push(pond.getStart());
        pond.getStart().markInStack();

        StringBuilder path = new StringBuilder();

        while (!stack.isEmpty()) {
            Hexagon curr = stack.peek();
            path.append(curr.getID()).append(" ");

            if (curr.isEnd()) {
                break;
            }

            if (curr instanceof FoodHexagon) {
                fliesEaten += ((FoodHexagon) curr).getNumFlies();
                ((FoodHexagon) curr).removeFlies();
            }

            Hexagon next = findBest(curr);
            if (next == null) {
                stack.pop();
                curr.markOutStack();
            } else {
                stack.push(next);
                next.markInStack();
            }
        }

        if (stack.isEmpty()) {
            return "No solution";
        } else {
            return path.append("ate ").append(fliesEaten).append(" flies").toString();
        }
    }

    /**
     * Calculates the priority of a given hexagon for the frog to move to.
     * @param cell The hexagon to calculate the priority for.
     * @return The priority value for the hexagon.
     */
    private double priority(Hexagon cell) {
        if (cell instanceof FoodHexagon) {
            int numFlies = ((FoodHexagon) cell).getNumFlies();
            if (numFlies == 3) {
                return 0.0;
            } else if (numFlies == 2) {
                return 1.0;
            } else if (numFlies == 1) {
                return 2.0;
            }
        }

        if (cell.isEnd()) {
            return 3.0;
        } else if (cell.isLilyPadCell()) {
            return 4.0;
        } else if (cell.isReedsCell()) {
            return 5.0;
        } else if (cell.isAlligator() && isNearAlligator(cell)) {
            return 10.0;
        } else if (cell.isMudCell()) {
            return 50.0;
        } else {
            return 6.0;
        }
    }

    /**
     * Checks if there is an alligator near the given hexagon.
     * @param curr The hexagon to check for nearby alligators.
     * @return True if there is an alligator nearby, otherwise false.
     */
    private boolean isNearAlligator(Hexagon curr) {
        int numNeighbours = 6;
        for (int i = 0; i < numNeighbours; ++i) {
            if (curr.getNeighbour(i) != null && curr.getNeighbour(i).isAlligator()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given hexagon is available for the frog to move to.
     * @param curr The hexagon to check for availability.
     * @return True if the hexagon is available, otherwise false.
     */
    private boolean isAvailable(Hexagon curr) {
        if (curr.isMudCell() && !curr.isLilyPadCell()) {
            return true; 
        }
        return !curr.isAlligator() && !curr.isMarked() && !isNearAlligator(curr);
    }
    
    /**
     * The main method to run the FrogPath program.
     * @param args The command-line arguments, where args[0] is the filename of the pond configuration.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("No map file specified in the arguments");
            return;
        }

        FrogPath fp = new FrogPath(args[0]);
        Hexagon.TIME_DELAY = 500;

        String result = fp.findPath();
        System.out.println(result);
    }
}