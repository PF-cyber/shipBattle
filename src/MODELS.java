import org.w3c.dom.ls.LSInput;

import java.awt.*;
import java.net.InterfaceAddress;
import java.util.*;
import java.util.List;

public class MODELS {
}

abstract class APlayer {
    String name;
    Pool pool;
    List<Ship> ships;

    APlayer() {
        pool = new Pool();
    }

    public void shoot(int x, int y) {
        Object cell = (Ship) this.pool.pool.get(x).get(y);

        if (cell instanceof Ship) {
            ((Ship) cell).getDamage();
        } else if (!(Boolean) this.pool.pool.get(x).get(y)) {
            this.pool.pool.get(x).put(y, false);
            System.out.println("Miss");
        } else {
            System.out.println("Agan?");
        }
    }
}

class Player extends APlayer {
}

class Bot extends APlayer {

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
                System.out.println("\t\t"+"x: " + sx + " y:" + sy + "  ");

                if ((sx < 0 || sx > 9) || (sy < 0 || sy > 9)) {
                    System.out.println("Out of range:"+ "sx"+sx+"sy"+sy);
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
        this.hp = size;
        this.size = size;
    }

    private void addList(Pool pool) {
        if (!pool.ships.contains(this)) {
            pool.ships.add(this);
        }
    }

    public void getDamage() {
        this.hp -= 1;
        if (this.hp == 0) {
            System.out.println("Sunk!");
        } else {
            System.out.println("Get hit!");
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