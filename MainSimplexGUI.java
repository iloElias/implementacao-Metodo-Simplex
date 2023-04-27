import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

public class MainSimplexGUI {
    private int numVariaveis; //----------------------------------// Número de variaveis de decisão.
    private int numRestricoes; //---------------------------------// Número de restrições.
    private List<JTextField> funcaoObj; //------------------------// Lista para os coficientes da função objetivo(Em formato de box para texto).
    private List<List<JTextField>> numCoficientesFields; //-------// Lista em tabela com valores de coficientes das inequações(Em formato de textBox).
    private List<JTextField> numResultados; //--------------------// Lista para guardar os resultados das inequações(Em formato de textBox).
    private List<JComboBox<String>> comboOperacoes; //------------// Lista para guardar os operadores de inequação das inequações(Em formato de optionBox).
    private JComboBox<String> comboMimMax; //---------------------// Variavel que guarda se o objetivo e de maximização ou de minimização(Em formato de optionBox).
    private List<float[][]> simplexHistorico = new ArrayList<>(); // Lista que recebe o historico de calculos feitos na tabela.

    public MainSimplexGUI() {
        // Inicializações:
        funcaoObj = new ArrayList<>();
        numCoficientesFields = new ArrayList<>();
        comboOperacoes = new ArrayList<>();
        comboMimMax = new JComboBox<>();
        numResultados = new ArrayList<>();

        JPanel painel1 = new JPanel(new GridLayout(2, 2, 5, 5)); // Primeiro painel para entrada de informações como quantidade de variaveis de decisão e restrições
        painel1.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField numVariaveisField = new JTextField();
        JTextField numRestricoesField = new JTextField();

        painel1.add(new JLabel("Número de variaveis:"));
        painel1.add(numVariaveisField);
        painel1.add(new JLabel("Número de restrições:"));
        painel1.add(numRestricoesField);

        int frontOptionPane = JOptionPane.showConfirmDialog(null, painel1, "Problema Simplex", JOptionPane.OK_CANCEL_OPTION);
        if (frontOptionPane != JOptionPane.OK_OPTION) {
            System.exit(0);
        }
        try {
            numVariaveis = Integer.parseInt(numVariaveisField.getText());
            numRestricoes = Integer.parseInt(numRestricoesField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Informação invalida", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        JPanel painel2 = new JPanel(new GridLayout(1, numVariaveis + 1, 5, 5)); // Cria um painel para inserção de informações dos coficientes da função objetivo.
        painel2.setBorder(new EmptyBorder(10, 0, 10, 0));
        painel2.add(new JLabel("Função objetiva:"));

        JComboBox<String> mimMaxBox = new JComboBox<>();
        mimMaxBox.addItem("Max");
        mimMaxBox.addItem("Min");
        comboMimMax.add(mimMaxBox);
        painel2.add(mimMaxBox);

        for (int i = 0; i < numVariaveis; i++) {
            JTextField textField = new JTextField();
            painel2.add(textField);
            funcaoObj.add(textField);
        }

        JPanel painel3 = new JPanel(new GridLayout(numRestricoes + 1, numVariaveis + 3, 5, 5));
        painel3.setBorder(new EmptyBorder(10, 0, 10, 0)); // Cria um painel para a inserção das informações sobre as inequações.

        painel3.add(new JLabel("Inequações:")); // Adiciona colunas no cabeçalho(coluna da função objetivo).

        for (int i = 0; i < numVariaveis; i++) {
            painel3.add(new JLabel("X" + (i + 1) + ":", JLabel.CENTER));
        }

        painel3.add(new JLabel("Operação:"));
        painel3.add(new JLabel("Resultado:"));

        for (int i = 0; i < numRestricoes; i++) { // Adiciona as linhas das inequações e seus resultados.
            painel3.add(new JLabel("Restrição " + (i + 1) + ":"));
            List<JTextField> numVariaveisRow = new ArrayList<>();
            for (int j = 0; j < numVariaveis; j++) {
                JTextField textField = new JTextField();
                painel3.add(textField);
                numVariaveisRow.add(textField);
            }
            numCoficientesFields.add(numVariaveisRow);

            JComboBox<String> comboBox = new JComboBox<>();
            comboBox.addItem("≤");
            comboBox.addItem("≥");
            comboOperacoes.add(comboBox);
            painel3.add(comboBox);

            JTextField resultTextField = new JTextField();
            painel3.add(resultTextField);
            numResultados.add(resultTextField);
        }

        JButton solveButton = new JButton("Calcular");
        solveButton.addActionListener(e -> { // Ação a ser tomada quando o usuario aperta o botão "Calcular".
            float[][] simplexMatrix = new float[numRestricoes + 1][numVariaveis + numRestricoes + 2]; // Cria a tabela simplex

            for (int i = 0; i < numVariaveis; i++) { // Preenche a primeira linha com os coficientes da  função objetiva.
                simplexMatrix[0][i + 1] = -Float.parseFloat(funcaoObj.get(i).getText());
            }
            simplexMatrix[0][0] = 1;

            for (int i = 0; i < numRestricoes; i++) { // Preenche as linhas restantes com os coficientes e resultados das restrições.
                List<JTextField> numVariaveisRow = numCoficientesFields.get(i);
                for (int j = 0; j < numVariaveis; j++) {
                    simplexMatrix[i + 1][j + 1] = Float.parseFloat(numVariaveisRow.get(j).getText());
                }
                if (comboOperacoes.get(i).getSelectedItem() == "≥"){ // Verifica qual deve ser o sinal da variavel de folga e a insere.
                    simplexMatrix[i + 1][numVariaveis + i + 1] = -1.0f;
                } else {
                    simplexMatrix[i + 1][numVariaveis + i + 1] = 1.0f;
                }

                simplexMatrix[i + 1][numVariaveis + numRestricoes + 1] = Float.parseFloat(numResultados.get(i).getText());
            }

            if (comboMimMax.getSelectedItem() == "Min"){
                for (int i = 0; i > simplexMatrix.length; i++) {
                    simplexMatrix[0][i] *= -1;
                }
            }

            simplexHistorico = new Simplex(simplexMatrix).solveSimplexGUI();
            new MostrarSimplex(simplexHistorico, numVariaveis, numRestricoes);
        });

        JPanel mainPanel = new JPanel(new BorderLayout()); // Cria a frame principal onde tudo será mostrado
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(painel2, BorderLayout.NORTH);
        mainPanel.add(painel3, BorderLayout.CENTER);
        mainPanel.add(solveButton, BorderLayout.SOUTH);

        JFrame frame = new JFrame("Problema Simplex"); // Cria a tela principal onde tudo será mostrado
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("Images/icon.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainSimplexGUI();
    }
}