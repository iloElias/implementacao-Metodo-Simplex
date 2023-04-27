# Implementacao Metodo Simplex
 # Para usar, existem duas formas, a implementação para prompt e implementação em formato aplicativo. A primeira forma se encontra no MainSimplex.java, e a outra, MainSimplexGUI.java.
  Para utilizar a classe Simplex, a tabela (matriz/vetor de duas dimenções) deve se encontrar da seguinte forma:
  A primeira coluna deve ser reservada aos valores de Z;
  A última coluna deve ser reservada para os resultados, tando da função objetivo(normalmente 0) quanto para as inequações;
  A primeira linha, deve conter os valores de coficientes da função objetiva;
  O restante dos espaços devem ser reservados para os coficientes de X e para as variaveis de folga.
 #
  Como exemplo, seguindo as "regras" anteriores, uma matriz deve ser contruida da seguite forma:
  Problema simplex do exemplo:
  Max Z: 4 X1 + 3 X2
  s.a    2 X1 + 1 X2 >= 1000
         1 X1 + 1 X2 >= 800
         1 X1        >= 400
 
 # Como na ultima restrição o coficiente X2 não aparece, seu valor deve ser transformado em 0. Segue tabela:
     float[][] simplexMatrix = {
            {  1,  -4, -3, 0,  0,  0,  0},
            {  0,   2,  1, 1,  0,  0,  1000},
            {  0,   1,  1, 0,  1,  0,  800},
            {  0,   1,  0, 0,  0,  1,  400},
     };
  Agora, com a matriz montada, vamos fazer o calculo:
 
  Simplex simplex = new Simplex(simplexMatrix);  // Instancia o objeto Simplex, e ja passa como construtor a nossa tabela construida.simplex.solveSimplex();  // Realiza a chamada da função para resolver o problema.
  # A implementação com interface utiliza o metodo .solveSimplexGUI, pois ele retorna uma lista de modificações, que serão mostradas mas a frente.
  Com isso, se o seu modo de execução for o de prompt, o programa fará os calculos nescessarios, e vai imprimir o passo a passo, juntamente com a resolução final.
