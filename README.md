<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<h1>Implementação do Método Simplex</h1>
	<p>Este é um repositório que contém uma implementação do Método Simplex, utilizado em programação linear para resolver problemas de maximização ou minimização de uma função objetivo, sujeito a restrições.</p>
 <h2>Modos de Uso</h2>
<p>Existem duas formas de utilizar esta implementação: a primeira é através do prompt de comando, utilizando o arquivo <code>MainSimplex.java</code>, e a segunda é utilizando o aplicativo gráfico, com o arquivo <code>MainSimplexGUI.java</code>.</p>

<h2>Preparando a Tabela</h2>
<p>Para utilizar a classe Simplex, a tabela (matriz/vetor de duas dimensões) deve estar formatada da seguinte forma:</p>
<ul>
	<li>A primeira coluna deve ser reservada aos valores de Z;</li>
	<li>A última coluna deve ser reservada para os resultados, tanto da função objetivo (normalmente 0) quanto para as inequações;</li>
	<li>A primeira linha deve conter os valores dos coeficientes da função objetivo;</li>
	<li>O restante dos espaços deve ser reservado para os coeficientes de X e para as variáveis de folga.</li>
</ul>

<p>Por exemplo, considere o seguinte problema:</p>

<pre><code>Max Z: 4 X1 + 3 X2
s.a. 2 X1 + 1 X2 >= 1000
     1 X1 + 1 X2 >= 800
     1 X1        >= 400
</code></pre>
 <p>Neste caso, a matriz deve ser construída da seguinte forma:</p>

<pre><code>float[][] simplexMatrix = {
    {  1,  -4, -3, 0,  0,  0,  0},
    {  0,   2,  1, 1,  0,  0,  1000},
    {  0,   1,  1, 0,  1,  0,  800},
    {  0,   1,  0, 0,  0,  1,  400}
};
</code></pre>
<p>Agora para implementar no codigo, devemos fazer assim:</p>
<pre><code>Simplex simplex = new Simplex(simplexMatrix);
simplex.solveSimplex();
</code></pre>
 <p>Caso esteja utilizando a implementação com interface gráfica, utilize o método <code>solveSimplexGUI()</code>, que retorna uma lista de modificações a serem mostradas na tela.</p>

<p>O programa irá realizar os cálculos necessários e imprimir o passo a passo, juntamente com a resolução final.</p>
</body>
</html>
