
package huaiquilican.main;

import huaiquilican.framework.Framework;

public class Main {
    public static void main(String[] args) {
        String configFile = "config.txt";
        Framework fw = new Framework(configFile);
        fw.ejecutar();
    }
}
