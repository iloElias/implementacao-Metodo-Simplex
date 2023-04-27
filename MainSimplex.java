import java.util.Scanner;

public class MainSimplex {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nDigite o número de variáveis de decisão: ");
        int numVariables = scanner.nextInt();
        System.out.print("Digite o número de restrições: ");
        int numConstraints = scanner.nextInt();

        int numRows = numConstraints + 1;
        int numCols = numVariables + numConstraints + 2;
        float[][] simplexMatrix = new float[numRows][numCols];
        boolean[] isMaior = new boolean[numCols];

        boolean sair = false;
        do {
            System.out.print("Defina se a função objetivo será de Max[1] ou Min[2]: ");
            String opt = scanner.next();

            switch (opt) {
                case "1":
                    isMaior[0] = true;
                    System.out.println("A função objetiva foi definida como de Maximização\n");
                    sair = true;
                    break;
                case "2":
                    isMaior[0] = false;
                    System.out.println("A função objetiva foi definida como de Minimização\n");
                    sair = true;
                    break;

                default:
                    System.out.println("Opção invalida");
                    break;
            }
        } while (!sair);

        System.out.println("Digite os coeficientes da função objetivo:");
        for (int j = 1; j <= numVariables; j++) {
            System.out.print("x" + j + ": ");
            if (isMaior[0]) {
                simplexMatrix[0][j] = scanner.nextFloat() * -1;
                simplexMatrix[0][0] = 1;
            } else {
                simplexMatrix[0][j] = scanner.nextFloat();
                simplexMatrix[0][0] = -1;
            }
        }

        for (int i = 1; i <= numConstraints; i++) {
            System.out.println("Digite os coeficientes da restrição " + i + ":");
            for (int j = 1; j <= numVariables; j++) {
                System.out.print("x" + j + ": ");
                simplexMatrix[i][j] = scanner.nextFloat();
            }
            sair = false;
            do {
                System.out.print("Defina o sinal da inequação >=[1] ou <=[2]: ");
                String opt = scanner.next();

                switch (opt) {
                    case "1":
                        isMaior[i] = true; 
                        sair = true;
                        break;
                    case "2":
                        isMaior[i] = false;
                        sair = true;
                        break;

                    default:
                        System.out.println("Opção invalida");
                        break;
                }
            } while (!sair);
            System.out.print("Resultado " + i + ": ");
            simplexMatrix[i][numCols - 1] = scanner.nextFloat();
        }
        
        // adiciona as variáveis de folga às colunas correspondentes da matriz
        int slackVariable = numVariables + 1;
        for (int i = 1; i <= numConstraints; i++) {
            if (isMaior[i])
                simplexMatrix[i][slackVariable++] = -1;
            else
                simplexMatrix[i][slackVariable++] = 1;
        }
        System.out.println();
        Simplex simplex = new Simplex(simplexMatrix);
        simplex.solveSimplex();

        scanner.close();
    }
}