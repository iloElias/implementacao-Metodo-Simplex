import java.util.ArrayList;
import java.util.List;

public class Simplex {
    private final float[][] tabelaSimplex;      // Variável responsável por armazenar a tabela simplex.
    private final List<float[][]> historico;    // Lista responsável por armazenar as alterações de calculo na tabela.

    public Simplex(float[][] tabela) {  // Método construtor.
        this.tabelaSimplex = tabela;
        this.historico = new ArrayList<>();
        this.historico.add(copyMatrix(this.tabelaSimplex));
    }

    public List<float[][]> solveSimplexGUI() {  // Função responsável por calcular o problema simplex(versão adaptada para o GUI).
        while (!isOtima(this.tabelaSimplex)) {  // Atravez de iterações, a função verifica se o problema é otimo ou não.
            int indexColunaPivo = getIndexColunaPivo();
            int indexElementoPivo = getIndexElementoPivo(indexColunaPivo);

            transformaLinha(indexElementoPivo, indexColunaPivo);
            zeraLinha(indexElementoPivo, indexColunaPivo);
            this.historico.add(copyMatrix(this.tabelaSimplex)); // Para cada alteração, é adicionado o estado atual da tabela na lista "historico".
        }
        return this.historico;  // Retorna a lista para o GUI para que ele possa mostrar em tabelas o passo a passo.
    }

    public void solveSimplex() {    // Método responsável por calcular o problema simplex.
        System.out.println("A tabela será: ");
        printTable();   // Printa a tabela inicial.
        System.out.println("Modificações de calculo:");
        while (!isOtima(this.tabelaSimplex)) {  // Atravez de iterações, a função verifica se o problema é otimo ou não.
            int indexColunaPivo = getIndexColunaPivo();
            int indexElementoPivo = getIndexElementoPivo(indexColunaPivo);

            transformaLinha(indexElementoPivo, indexColunaPivo);
            zeraLinha(indexElementoPivo, indexColunaPivo);
            System.out.println("\t-----------------------------");
            printTable();
        }
        printResult();  // Printa a solução otima;
    }

    private float[][] copyMatrix(float[][] matrix) {    // Função responsável por copiar o estado atual da tabela simplex.
        if (matrix == null) {   // Exceções.
            throw new NullPointerException("Matriz de origem é nula.");
        }        
        float[][] copy = new float[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = new float[matrix[i].length];
            System.arraycopy(matrix[i], 0, copy[i], 0, matrix[i].length);
        }
        return copy;
    }

    private boolean isOtima(float[][] tabela) { // Função responsável por verificar se o problema está otima.
        for (float v : tabela[0]) {
            if (v < 0.0f) {
                return false;
            }
        }
        return true;
    }

    private void transformaLinha(int indiceLinhaPivo, int indiceColunaPivo) {   // Método responsavel por transformar o elemento pivô em 1, e aplicar o calculo na linha toda.
        if (tabelaSimplex[indiceLinhaPivo][indiceColunaPivo] == 0) {    // Exceção.
            throw new ArithmeticException("Elemento pivo é zero.");
        }        
        float pivo = tabelaSimplex[indiceLinhaPivo][indiceColunaPivo];
        for (int i = 0; i < tabelaSimplex[indiceLinhaPivo].length; i++) {
            tabelaSimplex[indiceLinhaPivo][i] /= pivo;    // Lógica de calculo a ser aplicada.
        }
    }

    private void zeraLinha(int indiceLinhaPivo, int indiceColunaPivo) { // Método responsável por transformar todos os elementos da coluna pivô(menos o elemento pivô) em 0.
        for (int i = 0; i < tabelaSimplex.length; i++) {
            if (i == indiceLinhaPivo) {
                continue;
            }
            float fator = tabelaSimplex[i][indiceColunaPivo];
            for (int j = 0; j < tabelaSimplex[i].length; j++) {
                tabelaSimplex[i][j] -= fator * tabelaSimplex[indiceLinhaPivo][j]; // Lógica de calculo a ser aplicada.
            }
        }
    }

    private int getIndexColunaPivo() {   // Função responsável por encontrar o index da coluna pivõ.
        int indexColunaPivo = -1;
        float menor = Float.MAX_VALUE;
        for (int i = 0; i < tabelaSimplex[0].length - 1; i++) {
            if (tabelaSimplex[0][i] < menor) {
                menor = tabelaSimplex[0][i];
                indexColunaPivo = i;
            }
        }
        return indexColunaPivo;
    }

    private int getIndexElementoPivo(int indiceColunaPivo) {   // Função responsável por encontrar o index do elemento pivõ.
        if (indiceColunaPivo < 0 || indiceColunaPivo > tabelaSimplex[0].length - 2) {   // Exceção
            throw new IllegalArgumentException("Índice de coluna pivo fora do intervalo válido.");
        }     
        int indiceLinhaPivo = -1;
        float menorRazao = Float.MAX_VALUE;
        for (int i = 1; i < tabelaSimplex.length; i++) {
            if (tabelaSimplex[i].length - 1 < 0 || tabelaSimplex[i].length - 1 > tabelaSimplex[0].length - 1) { // Exceção.
                throw new IllegalArgumentException("Índice de elemento b fora do intervalo válido.");
            }
            float valorB = tabelaSimplex[i][tabelaSimplex[0].length - 1];
            float elementoColunaPivo = tabelaSimplex[i][indiceColunaPivo];
            float razao = valorB / elementoColunaPivo;
            if (elementoColunaPivo > 0 && razao < menorRazao) {
                menorRazao = razao;
                indiceLinhaPivo = i;
            }
        }
        return indiceLinhaPivo;
    }

    private void printTable() { // Método responsável por escrever a tabela em um prompt.
        for (float[] linha : tabelaSimplex) {
            for (float valor : linha) {
                System.out.print(valor + "\t");
            }
            System.out.println();
        }
    }

    private void printResult() {    // Método responsável por printar a solução otima.
        System.out.println("\n\nResultado final:");
        System.out.println("Z = " + tabelaSimplex[0][tabelaSimplex[0].length - 1]); // Max Z = ...
        for (int i = 1; i < tabelaSimplex.length; i++) {
            boolean encontrou = false;
            int indexUltimoElemento = tabelaSimplex[i].length - 1;  // Indice de posição do ultimo elemento.
            for (int j = 0; j < indexUltimoElemento; j++) { // Explicação grande:
                if (tabelaSimplex[i][j] == 1) {             // Esse trecho tem como função percorrer pelas colunas dos valores de X, vão de X1 até Xn
                    if (encontrou) {                        // Se ela encontrar algum numero que seja diferente de 0 ou 1, o valor final do X vai ser 0.0
                        encontrou = false;                  // senão, o valor a ser mostrado será o ultimo elemento da linha onde o numero 1 foi encontrado
                        break;                              // Lembrete: a coluna não pode conter outro numero alem de um número 1 e os restantes 0.
                    }
                    encontrou = true;
                } else if (tabelaSimplex[i][j] != 0) {
                    encontrou = false;
                    break;
                }
            }
            if (encontrou) {    // Caso não encontre outros números, será printado: "Xn = valor do ultimo da linha".
                System.out.println("X" + i + " = " + tabelaSimplex[i][indexUltimoElemento]);
            } else {    // Caso encontre, será printado: "Xn = 0.0".
                System.out.println("X" + i + " = 0.0");
            }
        }
    }
}