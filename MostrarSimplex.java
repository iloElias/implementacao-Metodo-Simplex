import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MostrarSimplex extends JFrame {
    private static final int WINDOW_WIDTH = 520;    // Constantes a ser utilizadas ao longo do codigo
    private static final int WINDOW_HEIGHT = 450;
    private static final int TABLE_HEIGHT = 30;
    private static final int SCROLL_INCREMENT = 5;

    public MostrarSimplex(List<float[][]> matriz, int numVariaveis, int numRestricoes) {
        setTitle("Matriz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setIconImage(Toolkit.getDefaultToolkit().getImage("Images/icon.png"));

        JPanel painel = new JPanel();
        painel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        add(painel);

        int n = matriz.get(0).length;
        int m = matriz.get(0)[0].length;

        String[] colunas = new String[m];   // Inicia um vetor que ira determinar as colunas das tabelas mas a frente.
        for (int i = 0; i < m; i++) {
            if (i == 0) {
                colunas[i] = "Z";
            } else if (i == m - 1) {
                colunas[i] = "B";
            } else {
                if (i < numVariaveis + 1) {
                    colunas[i] = "X" + (i);
                } else {
                    colunas[i] = "F" + (i - numVariaveis);
                }
            }
        }

        int alturaPanel = ((numRestricoes + 1) * TABLE_HEIGHT + 10);  // Definição de tamanho para os paineis.
        JPanel panelTabelas = new JPanel(new GridLayout(matriz.size(), 1));
        panelTabelas.setPreferredSize(new Dimension(490, ((alturaPanel * m) / 2) - 10));

        DecimalFormat decimalFormat = new DecimalFormat("#.#"); // Formatação decimal para exibição dos numeros.

        for (int i = 0; i < matriz.size(); i++) {
            Object[][] dados = new Object[n][m];
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    dados[j][k] = decimalFormat.format(matriz.get(i)[j][k]);
                }
            }

            JLabel titulo;  // Titulo de cada tabela.
            if (i == 0) {   // Define se é a primeira tabela ou não.
                titulo = new JLabel("Tabela Inícial", JLabel.CENTER);
            } else {
                titulo = new JLabel("Passo " + (i), JLabel.CENTER);
            }

            JTable tabela = new JTable(dados, colunas);

            JPanel panelTabela = new JPanel();
            panelTabela.add(titulo, BorderLayout.NORTH);
            panelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);
            panelTabelas.add(panelTabela);
        }

        JScrollPane scrollPane = new JScrollPane(panelTabelas);
        scrollPane.setPreferredSize(new Dimension(510, 270)); // Definição de tamanho do painel das tabelas.

        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setUnitIncrement(SCROLL_INCREMENT); // Define a força de rolagem.
            }
        });

        painel.add(scrollPane);

        float[][] lastMatrix = matriz.get(matriz.size() - 1);
        String text = "<html>"; // Inicia formação de um texto em html para mostrar a solução otima ao final.
        text += "<br><br>Max Z = " + lastMatrix[0][m - 1];
        int numRows = lastMatrix.length;
        int numCols = lastMatrix[0].length;
        
        for (int col = 1; col < (numVariaveis + 1); col++) {    // Aqui será escrita a solução otima, o trecho verifica se
            int countOnes = 0;                                  // onde esta o valor X1 ate o Xn e escreve este, se os outros
            int rowWithOne = -1;                                // não forem diferentes de 0.
        
            for (int row = 1; row < numRows; row++) {
                if (lastMatrix[row][col] == 1) {
                    countOnes++;
                    rowWithOne = row;
                } else if (lastMatrix[row][col] != 0) {
                    break;
                }
            }
        
            if (countOnes == 1) {
                text += "<br>X" + col + " = " + decimalFormat.format(lastMatrix[rowWithOne][numCols - 1]);
            } else {
                text += "<br>X" + col + " = 0.0";
            }
        }
        
        text += "</html>";

        JLabel info = new JLabel(text); // Aplica o texto ao final da pagina com a formatação
        painel.add(info, BorderLayout.SOUTH);
        setVisible(true);
    }
}