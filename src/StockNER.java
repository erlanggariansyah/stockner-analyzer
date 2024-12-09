import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

public class StockNER extends JFrame {
    private JButton analyzeButton;
    private JPanel stockNERPanel;
    private JTextPane textArticle;
    private JLabel info;
    private JTextField urlTextField;
    private JButton resetButton;
    private JTable resultTable;

    public StockNER() {
        setContentPane(stockNERPanel);
        setTitle("StockNER Analyzer");
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        analyzeButton.addActionListener(_ -> {
            try {
                Set<Stock> stockList;

                if (urlTextField.getText() != null && !urlTextField.getText().equalsIgnoreCase("")) {
                    String articleText = getFromUrl(urlTextField.getText());
                    stockList = detect(articleText);
                } else {
                    stockList = detect(textArticle.getText());
                }

                DefaultTableModel defaultTableModel = new DefaultTableModel();
                defaultTableModel.addColumn("Kode");
                defaultTableModel.addColumn("Nama Perusahaan");
                defaultTableModel.addColumn("Total Saham Terlisting");
                defaultTableModel.addColumn("Status");

                for (Stock stock : stockList) {
                    defaultTableModel.addRow(new String[]{stock.getCode(), stock.getCompany(), stock.getTotalListedStock(), stock.getStatus()});
                }

                resultTable.setModel(defaultTableModel);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        resetButton.addActionListener(_ -> {
            urlTextField.setText("");
            textArticle.setText("");
        });
    }

    public static void main(String[] args) {
        new StockNER();
    }

    private Set<Stock> detect(String text) throws IOException {
        InputStream inputStream = new FileInputStream("model.bin");
        SentenceModel sentenceModel = new SentenceModel(inputStream);

        BufferedReader br = new BufferedReader(new FileReader("sahamihsgdata.csv"));
        ArrayList<Stock> stockList = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            String[] stockCsv = line.split(",");

            Stock stock = new Stock(
                    stockCsv[0],
                    stockCsv[1],
                    stockCsv[2],
                    stockCsv[3],
                    stockCsv[4]
            );

            stockList.add(stock);
        }

        br.close();

        SentenceDetectorME sentenceDetectorME = new SentenceDetectorME(sentenceModel);

        String[] sentences = sentenceDetectorME.sentDetect(text);
        Set<Stock> stockResult = new HashSet<>();
        for (String sent : sentences) {
            for (Stock stock : stockList) {
                if (sent.toLowerCase().contains(stock.getCompany().toLowerCase())) {
                    stockResult.add(stock);
                }
            }
        }

        SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE;
        sentences = simpleTokenizer.tokenize(text);
        for (String sent : sentences) {
            for (Stock stock : stockList) {
                if (sent.equals(stock.getCode())) {
                    stockResult.add(stock);
                }
            }
        }

        return stockResult;
    }

    public static String getFromUrl(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }
}
