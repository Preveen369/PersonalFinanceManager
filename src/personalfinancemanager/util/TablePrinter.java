package personalfinancemanager.util;

import java.util.List;

public class TablePrinter {
    public static void printGrid(List<String[]> rows, int[] widths) {
        String horizontal = buildLine(widths, '+', '-');
        System.out.println(horizontal);
        for (int i = 0; i < rows.size(); i++) {
            printRow(rows.get(i), widths);
            System.out.println(horizontal);
        }
    }

    private static void printRow(String[] row, int[] widths) {
        StringBuilder line = new StringBuilder("|");
        for (int i = 0; i < row.length; i++) {
            line.append(String.format(" %-"+widths[i]+"s |", row[i]));
        }
        System.out.println(line.toString());
    }

    private static String buildLine(int[] widths, char edge, char fill) {
        StringBuilder line = new StringBuilder();
        line.append(edge);
        for (int width : widths) {
            line.append(String.valueOf(fill).repeat(width + 2)).append(edge);
        }
        return line.toString();
    }
}
