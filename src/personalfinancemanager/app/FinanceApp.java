package personalfinancemanager.app;

import java.io.File;
import personalfinancemanager.view.ConsoleUI;
import personalfinancemanager.util.BannerPrinter;

public class FinanceApp {
    
    public static void main(String[] args) {
        new File("exports").mkdirs();
        BannerPrinter.printBanner();
        new ConsoleUI().run();
    }
}
