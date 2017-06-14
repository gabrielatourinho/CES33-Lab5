import java.util.ArrayList;
import java.util.List;

public class Controlador {
	private List<CPU> processadores;
	private Gerador gerador;
	private int _N;
	private int _Amount;
	private int _TMT;
	private int hora;
	private boolean emissor = true;
	private boolean receptor = false;
	private static final int RETRY = 3;
	
	private static List<Integer> transmitidas;
	private static List<Integer> recebidas;
	
	Controlador(int N, int Amount, int TMT){
		_N = N;
		_Amount = Amount;
		_TMT = TMT;
		hora = 0;
		//Criar as CPUs
		processadores = new ArrayList<CPU>();
		transmitidas = new ArrayList<Integer>();
		recebidas = new ArrayList<Integer>();
		for(int i = 0; i < N; i++){
			CPU novaCPU = new CPU(i);
			processadores.add(novaCPU);
			transmitidas.add(0);
			recebidas.add(0);
		}
	}
	
	private void criaGerador(){
		gerador = new Gerador(_N, _Amount, _TMT, this); //modificar pra _Amount/3 quando for gerador leve
	}
	
	public void atribuiTarefa(int CPU, Tarefa tarefa){
		
		//Heurística do emissor
		if(emissor && processadores.get(CPU).numeroDeTarefas() >= 5){
			//realiza sondagem
			boolean alocouTarefa = false;
			
			for(int i =  0; i < RETRY && !alocouTarefa; i++){
				//gera número aleatório de cpu que não seja o da cpu atual
				int outraCPU = (int)(Math.random()*_N);
				while(outraCPU == CPU){
					outraCPU = (int)(Math.random()*_N);
				}
				System.out.println("CPU emissora "+CPU+" sondando CPU "+outraCPU);
				transmitidas.set(CPU, transmitidas.get(CPU)+1);
				recebidas.set(outraCPU, recebidas.get(outraCPU)+1);
				//verifica o número de tarefas da cpu alternativa
				//se o número de tarefas for pequeno: atribui a tarefa à cpu alternativa e sai do loop
				if (processadores.get(outraCPU).numeroDeTarefas() < 5){
					processadores.get(outraCPU).criouTarefa(tarefa);
					alocouTarefa = true;
				}
			}
			
			if(!alocouTarefa){
				processadores.get(CPU).criouTarefa(tarefa);
			}
		}
		else{
			processadores.get(CPU).criouTarefa(tarefa);
		}
	}
	
	private void segundo(){
		//atualizar o contador de segundos do gerador e se for o caso, criar mais tarefas
		if(hora%_TMT == 0){
			System.out.println("Criando tarefas.");
			gerador.criaTarefas(hora);
		}
		//todo processador tem que rodar 1 segundo do seu processo atual
		for(int i = 0; i < _N; i++){
			processadores.get(i).executa();
		}
		//atualiza a hora
		hora++;
		
		//Heurística do receptor
		for(int CPU = 0; receptor && CPU < _N; CPU++){
			if(hora%_TMT == 0 && processadores.get(CPU).ociosa()){
				
				boolean alocouTarefa = false;
				for(int i = 0; i < RETRY && !alocouTarefa; i++){
					//gerar número aleatório de CPU que não seja o da CPU atual
					int outraCPU = (int)(Math.random()*_N);
					while(outraCPU == CPU){
						outraCPU = (int)(Math.random()*_N);
					}
					System.out.println("CPU receptora "+CPU+" sondando CPU "+outraCPU);
					transmitidas.set(CPU, transmitidas.get(CPU)+1);
					recebidas.set(outraCPU, recebidas.get(outraCPU)+1);
					//sondagem: a outra CPU tem pelo menos 2 processos?
					//se sim: retira processo de índice 1 da outra CPU e adiciona à CPU que finalizou o processo
					if (processadores.get(outraCPU).numeroDeTarefas() >= 2){
						Tarefa tarefa = processadores.get(outraCPU).retiraTarefa();
						processadores.get(CPU).criouTarefa(tarefa);
						alocouTarefa = true;
					}
				}
			}
			else if(processadores.get(CPU).finalizouProcesso() && processadores.get(CPU).numeroDeTarefas() < 5){
				boolean alocouTarefa = false;
				for (int i = 0; i < RETRY && !alocouTarefa; i++){
					//gerar número aleatório de CPU que não seja o da CPU atual
					int outraCPU = (int)(Math.random()*_N);
					while(outraCPU == CPU){
						outraCPU = (int)(Math.random()*_N);
					}
					System.out.println("CPU receptora "+CPU+" sondando CPU "+outraCPU);
					transmitidas.set(CPU, transmitidas.get(CPU)+1);
					recebidas.set(outraCPU, recebidas.get(outraCPU)+1);
					//sondagem: a outra CPU tem pelo menos 2 processos?
					//se sim: retira processo de índice 1 da outra CPU e adiciona à CPU que finalizou o processo
					if (processadores.get(outraCPU).numeroDeTarefas() >= 2 
							&& processadores.get(CPU).numeroDeTarefas() < processadores.get(outraCPU).numeroDeTarefas()){
						Tarefa tarefa = processadores.get(outraCPU).retiraTarefa();
						processadores.get(CPU).criouTarefa(tarefa);
						alocouTarefa = true;
					}
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException{
		System.out.println("Criando o controlador");
		Controlador controlador = new Controlador(5, 15, 5);
		System.out.println("Criando o gerador");
		controlador.criaGerador();
		for(int i = 0; i < 60; i++){
			controlador.segundo();
		}
		
		for(int i = 0; i < 5; i++){
			System.out.println("Mensagens transmitidas pela CPU "+(i+1)+": "+ transmitidas.get(i));
		}
		for(int i = 0; i < 5; i++){
			System.out.println("Mensagens recebidas pela CPU "+(i+1)+": "+ recebidas.get(i));
		}
	}

}
