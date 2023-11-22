package Programa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Processo {
    private String nome;
    private int tempoTotal;
    private int tempoRestante;
    private String status;

    public Processo(String nome, int tempoTotal) {
        this.nome = nome;
        this.tempoTotal = tempoTotal;
        this.tempoRestante = tempoTotal;
        this.status = "Pronto";
    }

    public String getNome() {
        return nome;
    }

    public int getTempoTotal() {
        return tempoTotal;
    }

    public int getTempoRestante() {
        return tempoRestante;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean estaEncerrado() {
        return tempoRestante == 0;
    }

    public int executar(int quantum) {
        if (tempoRestante <= quantum) {
            tempoRestante = 0;
            status = "Encerrado";
            return 0;
        } else {
            tempoRestante -= quantum;
            return tempoRestante;
        }
    }
}

public class App2 {
    private static List<Processo> tabelaProcessos = new ArrayList<>();
    private static int quantum;

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        while (true) {
            limparTela();
            System.out.println("---Escalonamento - Round Robin---\n");
            System.out.print("Informe o tamanho do Quantum (em segundos): ");
            quantum = ler.nextInt();

            while (true) {
                System.out.println("\nMENU:");
                System.out.println("1. Criar novo processo");
                System.out.println("2. Verificar tabela de processos");
                System.out.println("3. Executar processos");
                System.out.println("4. Sair\n");

                int opcao = ler.nextInt();

                switch (opcao) {
                    case 1:
                        limparTela();                                                                                                                                                                                                                                                                                                   
                        criarProcesso(ler);
                        break;
                    case 2:
                        if (!tabelaProcessos.isEmpty()) {
                            mostrarTabelaProcessos(ler);
                        } else {
                            limparTela();
                            System.out.println("Nenhum processo adicionado.");
                            ler.nextLine();
                        }
                        break;
                    case 3:
                        if (!tabelaProcessos.isEmpty()) {
                            executarProcessos(ler);
                        } else {
                            limparTela();
                            System.out.println("Nenhum processo para executar.");
                            ler.nextLine();
                        }
                        break;
                    case 4:
                        System.out.println("Encerrando o programa.");
                        ler.close();
                        System.exit(0);
                    default:
                        System.out.println("Opção inválida.");
                        ler.nextLine();
                }
            }
        }
    }

    private static void criarProcesso(Scanner ler) {
        System.out.print("Nome do processo: ");
        String nome = ler.next();

        System.out.print("Tamanho do processo (em segundos): ");
        int tempoTotal = ler.nextInt();

        Processo novoProcesso = new Processo(nome, tempoTotal);
        tabelaProcessos.add(novoProcesso);
        System.out.println("Processo '" + nome + "' criado e pronto.");
        ler.nextLine();
    }

    private static void executarProcessos(Scanner ler) {
        int currentIndex = 0;
        int processosEncerrados = 0;

        while (processosEncerrados < tabelaProcessos.size()) {
            Processo processo = tabelaProcessos.get(currentIndex);

            if (!processo.estaEncerrado()) {
                processo.setStatus("Em Execução");
                System.out.println("Processo '" + processo.getNome() + "' está em execução.");
                int tempoRestante = processo.executar(quantum);
                mostrarTabelaProcessos(ler);


                if (tempoRestante == 0) {
                    processosEncerrados++;
                }
            }

            currentIndex = (currentIndex + 1) % tabelaProcessos.size();
        }

        System.out.println("Todos os processos foram encerrados.");
        System.out.println("Pressione ENTER para voltar.");
        ler.nextLine();
        ler.nextLine();
    }

    private static void mostrarTabelaProcessos(Scanner ler) {
        if (tabelaProcessos.isEmpty()) {
            System.out.println("Nenhum processo adicionado.");
        } else {
            System.out.println("\nTabela de Processos:");
            System.out.println("--------------------------------------------------------------");
            System.out.printf("%-15s%-15s%-15s%-15s\n", "Nome", "Tempo Total", "Tempo Restante", "Status");
            System.out.println("--------------------------------------------------------------");

            for (Processo processo : tabelaProcessos) {
                String nome = processo.getNome();
                int tempoTotal = processo.getTempoTotal();
                int tempoRestante = processo.getTempoRestante();
                String status = processo.getStatus();

                System.out.printf("%-15s%-15s%-15s%-15s\n", nome, tempoTotal, tempoRestante, status);
            }

            System.out.println("--------------------------------------------------------------");
        }
    }

    private static void limparTela() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}