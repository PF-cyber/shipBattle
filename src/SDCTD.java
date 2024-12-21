import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class SDCTD {
    Player player;
    Player bot;

    public SDCTD() {
        System.out.println("___SDCTD: starting backends...");
        this.player = new Player();
        System.out.println(player.pool.pool);
    }

    public static Player create_bot() {
        Player bot = new Player();
        bot.name = "Bot";

        return bot;
    }
}


class Player {
    Pool pool;
    String name = "Player1";

    Player() {
        pool = new Pool();
    }

    public void shoot(Integer x, Integer y, Player player) {
        if ((x == null) || (y == null)) {
            Scanner sc = new Scanner(System.in);
            x = Integer.parseInt(sc.next());
            y = Integer.parseInt(sc.next());
            sc.close();
        }
    }
}


class Pool {
    String className = new Object() {
    }.getClass().getName();

    HashMap<Integer, HashMap<Integer, Object>> pool = new HashMap<>();
    List<Ship> ships = new ArrayList<>();
    LOG log = new LOG();

    Pool() {
        for (int i = 0; i < 10; i++) {
            pool.put(i, createMap_string());
        }
        prepare_ships();
    }

    private HashMap<Integer, Object> createMap_string() {
        HashMap<Integer, Object> map_string = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map_string.put(i, true);
        }
        return map_string;
    }

    public void prepare_ships() {
        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        HashMap<Integer, Integer> dict_ships = new HashMap<>();
        dict_ships.put(1, 4);
        dict_ships.put(2, 3);
        dict_ships.put(3, 2);
        dict_ships.put(4, 1);
        for (Map.Entry<Integer, Integer> ship : dict_ships.entrySet()) {
            for (int i = 0; i < ship.getKey(); i++) {
                this.createShip(ship.getValue());
            }
        }

    }


    private void createShip(int ship_size) {
        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        Random random = new Random();
        HashMap<Integer, List<Integer>> cells;
        boolean isHorizontal = true;
        if (isHorizontal) {
            cells = checkCellH(ship_size);
            Ship a = new Ship(cells, this, ship_size);
        } else {
            cells = checkCellV(ship_size);
            Ship a = new Ship(cells, this, ship_size);
        }
    }

    public HashMap<Integer, List<Integer>> checkCellV(int ship_size) {
        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        for (int num = 0; num < 10; num++) {
            HashMap<Integer, List<Integer>> ships_cells = new HashMap<>();
            List<Integer> map_string = new ArrayList<>();

            for (int abc = 0; abc < 10 - ship_size; abc++) {
                for (int len = 0; len < ship_size; len++) {
                    if (BoolORShip(pool.get(abc + len).get(num))) {
                        ships_cells.put(abc + len, Collections.singletonList(num));
                        if (ships_cells.size() == ship_size) {
                            return ships_cells;
                        }
                    }
                }
            }
        }
        return checkCellH(ship_size);
    }


    public HashMap<Integer, List<Integer>> checkCellH(int ship_size) {
        HashMap<Integer, List<Integer>> cells = new HashMap<>();

        for (int abc = 0; abc < 10; abc++) {
            List<Integer> map_string = new ArrayList<>(); // Создаем новый список для строки

            for (int num = 0; num <= 10 - ship_size; num++) {
                boolean isValid = true;

                // Проверяем, что все ячейки свободны
                for (int len = 0; len < ship_size; len++) {
                    int snum = num + len;
                    if (!BoolORShip(pool.get(abc).get(snum))) {
                        isValid = false;
                        break; // Прерываем цикл, если найдено препятствие
                    }
                }

                // Если все ячейки валидны, добавляем их в map_string
                if (isValid) {
                    for (int len = 0; len < ship_size; len++) {
                        map_string.add(num + len);
                    }
                    cells.put(abc, new ArrayList<>(map_string)); // Добавляем копию списка
                    System.out.println(cells);
                    return cells; // Возвращаем координаты
                }

                // Сбрасываем map_string для новой попытки
                map_string.clear();
            }
        }

        return checkCellV(ship_size); // Если не удалось, переключаемся на вертикальную проверку
    }


    public boolean BoolORShip(Object value) {
        if (value instanceof boolean) {
            return (boolean) value;
        }
        if (value instanceof Ship) {
            return false;
        }
        return false;
    }
}


class Ship {
    Integer live;
    HashMap<Integer, List<Integer>> location = new HashMap<>();

    public Ship(HashMap<Integer, List<Integer>> cells, Pool pool, Integer ship_size) {
        this.location = cells;
        this.live = ship_size;
        createShip(pool);
    }


    private void createShip(Pool pool) {

        blockCells(pool, location);
        for (Map.Entry<Integer, List<Integer>> t_ship : this.location.entrySet()) {
            for (Integer tt_ship : t_ship.getValue()) {
                pool.pool.get(t_ship.getKey()).put(tt_ship, this);
            }
        }
        pool.ships.add(this);
    }


    public void getDamage(Integer x, Integer y, Player player) {
        this.live -= 1;
        player.pool.pool.get(x).put(y, true);

        if (this.live == 0) {
            System.out.println("Sunk!");
        } else {
            System.out.println("Got it!");
        }
    }


    public void blockCells(Pool pool, HashMap<Integer, List<Integer>> cells) {
        List<Integer> keys = new ArrayList<>(cells.keySet());
        List<Integer> values = new ArrayList<>(new ArrayList<>(cells.values()).getFirst());

        List<Integer> mKeys = bDist(keys);
        List<Integer> mValues = bDist(values);

        for (int k = mKeys.getFirst(); k <= mKeys.getLast(); k++){
            for (int v = mValues.getFirst(); v <= mValues.getLast(); v++){
                System.out.println(k + "false" + v);
                pool.pool.get(k).put(v, false);
            }
        }
    }


    public List<Integer> bDist(List<Integer> list) {
        List<Integer> radius = new ArrayList<>();
        int mx = list.stream().max(Integer::compareTo).orElse(100);
        int mn = list.stream().min(Integer::compareTo).orElse(100);

        if (0 < mn) {
            radius.add(mn - 1);
        } else { radius.add(mn); }

        if (mx < 9) {
            radius.add(mx + 1);
            System.out.println(true);
        } else { radius.add(mx); }

        return radius;
    }
}

