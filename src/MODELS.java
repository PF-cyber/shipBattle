import java.text.MessageFormat;
import java.util.*;
import java.util.List;

public class MODELS {
    Player player = new Player();
    Bot bot = new Bot();

    MODELS() {
        player.enemy = bot;
        bot.enemy = player;
    }
}

abstract class APlayer {
    String name;
    Pool pool;
    MainFrame.PoolFrame poolFrame;
    APlayer enemy;

    APlayer() {
        pool = new Pool();
    }

    public Integer shoot(int x, int y) {
        Object cell = this.enemy.pool.pool.get(x).get(y);
        System.out.println(MessageFormat.format("{0} shoot x:{1} y:{2}", this.name, x, y));
        if (cell instanceof Ship) {
            int result = ((Ship) cell).getDamage();
            enemy.poolFrame.damageCell(x, y, result);
            System.out.println("Result: " + result);
            return result;

        } else if ((Boolean) this.enemy.pool.pool.get(x).get(y)) {
            int result = 1;
            this.enemy.pool.pool.get(x).put(y, false);
            enemy.poolFrame.damageCell(x, y, result);
            System.out.println("Miss");
            return result;

        } else {
            int result = 0;
            enemy.poolFrame.damageCell(x, y, result);
            System.out.println("Again?");
            return result;
        }
    }
}

class Player extends APlayer {
    Player() {
        this.name = "player_1";
    }
}

class Bot extends APlayer {
    private HUNTER hunter = new HUNTER();

    Bot() {
        this.name = "bot_1";
    }

    public Integer makeMove() {
        List<Integer> target = hunter.getNextTarget();
        int x = target.get(0);
        int y = target.get(1);
        int result = shoot(x, y);
        hunter.updateTargetMap(x, y, result);
        return result;
    }

    class HUNTER {
        private int huntingMode = 1; 
        private List<Integer> lastHit = new ArrayList<>();
        private List<List<Integer>> possibleTargets = new ArrayList<>();
        private int lastShotResult = 0; 
        private HashMap<Integer, List<Integer>> targetMap = new HashMap<>();

        HUNTER(){
            generateTargetMap();
        }

        public void updateTargetMap(int x, int y, int result) {
            lastShotResult = result;

            if (result == 2) { 
                if (huntingMode == 1) { 
                    huntingMode = 2; 
                    lastHit = Arrays.asList(x, y); 
                    generatePossibleTargets(x, y); 
                } else if (huntingMode == 2 || huntingMode == 3) { 
                    huntingMode = 3; 
                    lastHit = Arrays.asList(x, y); 
                    filterPossibleTargets(x, y); 
                }
            } else if (result == 1) { 
                if (huntingMode == 3) { 
                    huntingMode = 2; 
                }
            } else if (result == 3) { 
                huntingMode = 1; 
                lastHit.clear(); 
                possibleTargets.clear(); 
            }
        }

        private void generateTargetMap(){
            for (int i = 0; i < 10; i++) {
                List<Integer> line = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    line.add(j);
                }
                targetMap.put(i, line);
            }
        }

        private List<Integer> getRandomXY(){
            Random r = new Random();
            List<Map.Entry<Integer, List<Integer>>> entries = new ArrayList<>(targetMap.entrySet());
            Map.Entry<Integer, List<Integer>> re = entries.get(r.nextInt(entries.size()));
            int x = re.getKey();
            int y = re.getValue().get(r.nextInt(re.getValue().size()));
            return new ArrayList<>(List.of(x, y));
        }

        private void removeCell(int x, int y){
            targetMap.get(x).remove(Integer.valueOf(y));
            if(targetMap.get(x).isEmpty()){
                targetMap.remove(x);
            }
        }

        private void generatePossibleTargets(int x, int y) {
            possibleTargets.clear(); 
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; 

            for (int[] dir : directions) { 
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10) { 
                    possibleTargets.add(Arrays.asList(newX, newY));
                }
            }
        }


        private void filterPossibleTargets(int x, int y) {
            if (possibleTargets.isEmpty()) return; 

            int prevX = lastHit.get(0);
            int prevY = lastHit.get(1);


            int dx = x - prevX; 
            int dy = y - prevY;

            List<List<Integer>> newTargets = new ArrayList<>();
            for (List<Integer> target : possibleTargets) {
                int tx = target.get(0);
                int ty = target.get(1);


                if ((dx != 0 && tx - x == dx) || (dy != 0 && ty - y == dy)) { 
                    newTargets.add(Arrays.asList(tx, ty)); 
                }
            }
            possibleTargets = newTargets; 
        }


        public List<Integer> getNextTarget() {
            if (huntingMode == 1) { 
                System.out.println(name + " get random xy");
                List<Integer> grxy = getRandomXY(); 
                int x = grxy.getFirst();
                int y = grxy.getLast();
                removeCell(x, y); 
                return Arrays.asList(x, y); 
            } else if (huntingMode == 2 || huntingMode == 3) { 
                if (!possibleTargets.isEmpty()) { 
                    return possibleTargets.remove(0); 
                } else { 
                    huntingMode = 1;
                    return getNextTarget(); 
                }
            }
            return Arrays.asList(0, 0);
        }
    }
}

class Pool {
    HashMap<Integer, HashMap<Integer, Object>> pool = new HashMap<>();
    List<Integer> tag_x = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    List<Integer> tag_y = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    List<Ship> ships = new ArrayList<>();

    Pool() {
        createPool();
        createShips();
        this.visualPool();
    }

    private void createPool() {
        for (int i = 0; i < 10; i++) {
            HashMap<Integer, Object> innerMap = new HashMap<>();
            for (int j = 0; j < 10; j++) {
                innerMap.put(j, true);
            }
            this.pool.put(i, innerMap);
        }
    }

    private boolean checkAround(HashMap<Integer, List<Integer>> cells) {
        int fx = Collections.min(new ArrayList<Integer>(cells.keySet()));
        int fy = cells.get(fx).getFirst();
        int lx = Collections.max(new ArrayList<Integer>(cells.keySet()));
        int ly = cells.get(lx).getLast();

        fy = (fy > 0) ? fy - 1 : fy;
        fx = (fx > 0) ? fx - 1 : fx;
        lx = (lx < 9) ? lx + 1 : lx;
        ly = (ly < 9) ? ly + 1 : ly;
        System.out.println("fy:" + fy + " fx:" + fx + " lx:" + lx + " ly" + ly);

        for (int sx = fx; sx <= lx; sx++) {
            for (int sy = fy; sy <= ly; sy++) {
                System.out.println("\t\t" + "x: " + sx + " y:" + sy + "  ");

                if ((sx < 0 || sx > 9) || (sy < 0 || sy > 9)) {
                    System.out.println("Out of range:" + "sx" + sx + "sy" + sy);
                    return false;
                } else if (pool.get(sx).get(sy) == null) {
                    System.out.println("Check is null");
                    return false;
                } else if (pool.get(sx).get(sy) instanceof Ship) {
                    System.out.println("Place is ship");
                    return false;
                } else if (!(boolean) pool.get(sx).get(sy)) {
                    System.out.println("Place is busy");
                    return false;
                }
            }
        }
        System.out.println();
        System.out.println("INSERT SHIP");
        System.out.println(cells);
        return true;
    }

    private boolean createVertical(int x, int y, int size) {
        HashMap<Integer, List<Integer>> map_string = new HashMap<>();
        for (int mx = x + size; x < mx; x++) {
            map_string.put(x, new ArrayList<>(List.of(y)));
        }
        if (checkAround(map_string)) {
            new Ship(size, map_string, this);
            return true;
        }
        return false;
    }

    private boolean createHorizontal(int x, int y, int size) {
        HashMap<Integer, List<Integer>> map_string = new HashMap<>();

        for (int my = y + size; y < my; y++) {
            if (!map_string.containsKey(x)) {
                map_string.put(x, new ArrayList<>());
            }
            map_string.get(x).add(y);
        }

        if (checkAround(map_string)) {
            new Ship(size, map_string, this);
            return true;
        } else {
            return false;
        }
    }

    public void visualPool() {
        for (int x : pool.keySet()) {
            System.out.print(x + ": ");
            for (int y : pool.get(x).keySet()) {
                if (pool.get(x).get(y) instanceof Ship) {
                    System.out.print(" S ");
                } else if ((boolean) pool.get(x).get(y)) {
                    System.out.print(" T ");
                } else if (!(boolean) pool.get(x).get(y)) {
                    System.out.print(" F ");
                } else {
                    System.out.print(" ? ");
                }
            }
            System.out.println("|");
        }
    }

    private void createShips() {
        HashMap<Integer, Integer> quantity_size = new HashMap<>();
        quantity_size.put(1, 4);
        quantity_size.put(2, 3);
        quantity_size.put(3, 2);
        quantity_size.put(4, 1);

        for (int quan : quantity_size.keySet()) {
            int size = quantity_size.get(quan);
            for (int i = 0; i < quan; i++) {
                if (new Random().nextBoolean()) {
                    while (!createVertical(new Random().nextInt(10),
                            new Random().nextInt(10), size)) ;
                } else {
                    while (!createHorizontal(new Random().nextInt(10),
                            new Random().nextInt(10), size)) ;
                }
            }
        }
    }
}

class Ship {
    HashMap<Integer, List<Integer>> location;
    Integer size;
    Integer hp;

    Ship(Integer size, HashMap<Integer, List<Integer>> cells, Pool pool) {
        if (checkAround(cells, pool.pool)) {
            createShip(size, cells, pool.pool);
            addList(pool);
        }
        this.location = cells;
        this.hp = size;
        this.size = size;
    }

    private void addList(Pool pool) {
        if (!pool.ships.contains(this)) {
            pool.ships.add(this);
        }
    }

    public int getDamage() {
        this.hp -= 1;
        if (this.hp == 0) {
            System.out.println("Sunk!");
            return 3;
        } else {
            System.out.println("Get hit!");
            return 2;
        }
    }

    private void createShip(Integer size,
                            HashMap<Integer, List<Integer>> cells,
                            HashMap<Integer, HashMap<Integer, Object>> pool) {
        for (Map.Entry<Integer, List<Integer>> entry : cells.entrySet()) {
            int key = entry.getKey();
            for (Integer value : entry.getValue()) {
                pool.get(key).put(value, this);
            }
        }
    }

    private boolean checkAround(HashMap<Integer, List<Integer>> cells,
                                HashMap<Integer, HashMap<Integer, Object>> pool) {
        HashMap<Integer, HashMap<Integer, Object>> map = new HashMap<>();

        int fx = Collections.min(new ArrayList<Integer>(cells.keySet()));
        int fy = cells.get(fx).getFirst();
        int lx = Collections.max(new ArrayList<Integer>(cells.keySet()));
        int ly = cells.get(lx).getLast();

        fy = (fy > 0) ? fy - 1 : fy;
        fx = (fx > 0) ? fx - 1 : fx;
        lx = (lx < 9) ? lx + 1 : lx;
        ly = (ly < 9) ? ly + 1 : ly;

        for (; fx <= lx; fx++) {
            for (; fy <= ly; fy++) {
                if (pool.get(fx).get(fy) instanceof Ship) {
                    return false;
                } else if (!(boolean) pool.get(fx).get(fy)) {
                    return false;
                } else if (pool.get(fx).get(fy) == null) {
                    return false;
                }
            }
        }
        return true;
    }
}