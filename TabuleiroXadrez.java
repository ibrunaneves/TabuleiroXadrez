import java.util.ArrayList;
import java.util.Scanner;

public class TabuleiroXadrez {

    // Declaração da matriz que representa o tabuleiro de xadrez 8x8
    private static String[][] tabuleiro;

    public static String[][] inicializarTabuleiro() {
        String[][] tabuleiro = new String[8][8];

        // Preenche o tabuleiro com espaços em branco
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tabuleiro[i][j] = "   ";
            }
        }

        // Coloca as peças brancas na linha 7
        tabuleiro[7][0] = "TB"; tabuleiro[7][1] = "CB"; tabuleiro[7][2] = "BB";
        tabuleiro[7][3] = "DB"; tabuleiro[7][4] = "RB"; tabuleiro[7][5] = "BB";
        tabuleiro[7][6] = "CB"; tabuleiro[7][7] = "TB";

        // Coloca os peões brancos na linha 6
        for (int i = 0; i < 8; i++) {
            tabuleiro[6][i] = "PB";
        }

        // Coloca as peças pretas na linha 0
        tabuleiro[0][0] = "TP"; tabuleiro[0][1] = "CP"; tabuleiro[0][2] = "BP";
        tabuleiro[0][3] = "DP"; tabuleiro[0][4] = "RP"; tabuleiro[0][5] = "BP";
        tabuleiro[0][6] = "CP"; tabuleiro[0][7] = "TP";

        // Coloca os peões pretos na linha 1
        for (int i = 0; i < 8; i++) {
            tabuleiro[1][i] = "PP";
        }

        return tabuleiro;
    }

    public static void exibirTabuleiro() {
        System.out.println("----------------------------");
        System.out.println("   A  B  C  D  E  F  G  H");

        // Exibe o tabuleiro linha por linha
        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " "); // Exibe o número da linha
            for (int j = 0; j < 8; j++) {
                System.out.print(tabuleiro[i][j] + " "); // Exibe o conteúdo de cada casa
            }
            System.out.println();
        }

        System.out.println("----------------------------");
    }

    public static boolean validarMovimento(String movimento, boolean vezJogadorBranco) {
        // Verifica se o movimento tem exatamente 4 caracteres
        if (movimento.length() != 4) {
            System.out.println("Erro: O movimento deve ter exatamente 4 caracteres (ex: E2E4).");
            return false;
        }

        char colunaInicial = movimento.charAt(0);
        char linhaInicial = movimento.charAt(1);
        char colunaFinal = movimento.charAt(2);
        char linhaFinal = movimento.charAt(3);

        // Verifica se as posições estão dentro do intervalo válido (A-H, 1-8)
        if (colunaInicial < 'A' || colunaInicial > 'H' || linhaInicial < '1' || linhaInicial > '8' ||
                colunaFinal < 'A' || colunaFinal > 'H' || linhaFinal < '1' || linhaFinal > '8') {
            System.out.println("Erro: Posição inválida! Use letras de A a H e números de 1 a 8.");
            return false;
        }

        // Converte as posições de coluna e linha em índices da matriz
        int colunaInicialIndex = colunaInicial - 'A';
        int linhaInicialIndex = 8 - (linhaInicial - '0');
        int colunaFinalIndex = colunaFinal - 'A';
        int linhaFinalIndex = 8 - (linhaFinal - '0');

        // Verifica se há uma peça na posição inicial
        String peca = tabuleiro[linhaInicialIndex][colunaInicialIndex];
        if (peca.trim().isEmpty()) {
            System.out.println("Erro: Não há peça na posição inicial!");
            return false;
        }

        // Verifica se o jogador está tentando mover a peça correta (branco para peão branco e preto para peão preto)
        if (vezJogadorBranco && !peca.equals("PB")) {
            System.out.println("Erro: Jogador branco só pode mover peões brancos!");
            return false;
        }

        if (!vezJogadorBranco && !peca.equals("PP")) {
            System.out.println("Erro: Jogador preto só pode mover peões pretos!");
            return false;
        }

        // Valida o movimento do peão de acordo com as regras de movimento (1 ou 2 casas para frente, ou captura na diagonal)
        if (colunaInicialIndex == colunaFinalIndex) {
            if (vezJogadorBranco) {
                // Peão branco pode mover 1 casa para frente ou 2 casas na primeira jogada
                if (linhaFinalIndex == linhaInicialIndex - 1 && tabuleiro[linhaFinalIndex][colunaFinalIndex].equals("   ")) {
                    return true;
                } else if (linhaInicialIndex == 6 && linhaFinalIndex == linhaInicialIndex - 2 && tabuleiro[linhaFinalIndex][colunaFinalIndex].equals("   ")) {
                    return true;
                }
            } else {
                // Peão preto pode mover 1 casa para frente ou 2 casas na primeira jogada
                if (linhaFinalIndex == linhaInicialIndex + 1 && tabuleiro[linhaFinalIndex][colunaFinalIndex].equals("   ")) {
                    return true;
                } else if (linhaInicialIndex == 1 && linhaFinalIndex == linhaInicialIndex + 2 && tabuleiro[linhaFinalIndex][colunaFinalIndex].equals("   ")) {
                    return true;
                }
            }
        } else if (Math.abs(colunaInicialIndex - colunaFinalIndex) == 1 && tabuleiro[linhaFinalIndex][colunaFinalIndex].equals("   ")) {
            // Verifica se o movimento é para a diagonal, mas não está sendo realizado um ataque (a casa está vazia)
            return false;
        }

        return false; // Caso o movimento não seja válido, retorna falso
    }

    public static void moverPeca(String movimento, ArrayList<String> historico) {
        // Converte as posições de coluna e linha em índices da matriz
        int colunaInicial = movimento.charAt(0) - 'A';
        int linhaInicial = 8 - (movimento.charAt(1) - '0');
        int colunaFinal = movimento.charAt(2) - 'A';
        int linhaFinal = 8 - (movimento.charAt(3) - '0');

        // Captura a peça que foi movida
        String pecaMovida = tabuleiro[linhaInicial][colunaInicial];

        // Move a peça no tabuleiro
        tabuleiro[linhaFinal][colunaFinal] = pecaMovida;
        tabuleiro[linhaInicial][colunaInicial] = "   "; // A posição inicial fica vazia

        // Adiciona o movimento ao histórico
        historico.add(movimento);

        // Exibe uma mensagem sobre o movimento realizado
        System.out.println("A peça " + obterNomePeca(pecaMovida) +
                " foi movida de " + movimento.substring(0,2) +
                " para " + movimento.substring(2,4));
    }

    // Método que retorna o nome da peça com base no código da peça
    public static String obterNomePeca(String peca) {
        switch (peca.trim()) {
            case "TP": return "Torre preta";
            case "CP": return "Cavalo preto";
            case "BP": return "Bispo preto";
            case "DP": return "Dama preta";
            case "RP": return "Rei preto";
            case "PP": return "Peão preto";
            case "TB": return "Torre branca";
            case "CB": return "Cavalo branco";
            case "BB": return "Bispo branco";
            case "DB": return "Dama branca";
            case "RB": return "Rei branco";
            case "PB": return "Peão branco";
            default: return "Peça desconhecida"; // Caso não seja uma peça válida
        }
    }

    // Método principal que executa o jogo de xadrez
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        tabuleiro = inicializarTabuleiro();
        ArrayList<String> historico = new ArrayList<>();
        boolean vezJogadorBranco = true;

        System.out.println("\nBem-vindo ao Jogo de Xadrez!");
        System.out.println("Digite os movimentos no formato: E2E4");
        System.out.println("Para sair, digite 'sair'.\n");

        exibirTabuleiro();

        // Loop do jogo
        while (true) {
            System.out.println("\nVez do Jogador " +
                    (vezJogadorBranco ? "Branco" : "Preto") +
                    ", digite o movimento: ");
            String movimento = scanner.nextLine().toUpperCase();

            if (movimento.equals("SAIR")) {
                System.out.println("Jogo encerrado!");
                break;
            }

            if (validarMovimento(movimento, vezJogadorBranco)) {
                moverPeca(movimento, historico);
                exibirTabuleiro();

                vezJogadorBranco = !vezJogadorBranco;
            }
        }

        scanner.close(); 
    }
}
