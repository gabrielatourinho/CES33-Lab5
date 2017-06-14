import java.util.ArrayList;
import java.util.List;

public class CPU {
	private int ID;
	private List<Tarefa> listaDeProcessos;
	private Tarefa processoAtual;
	private int tempoDeExecucao;
	private boolean finalizouProcesso;
	
	CPU(int id){
		ID = id;
		listaDeProcessos = new ArrayList<Tarefa>();
		tempoDeExecucao = 0;
		finalizouProcesso = false;
	}
	
	public void criouTarefa(Tarefa tarefa){
		listaDeProcessos.add(tarefa);
	}
	
	public int numeroDeTarefas(){
		return listaDeProcessos.size();
	}
	
	public boolean ociosa(){
		if(listaDeProcessos.isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean finalizouProcesso(){
		return finalizouProcesso;
	}
	
	public Tarefa retiraTarefa(){
		Tarefa tarefa = listaDeProcessos.get(1);
		listaDeProcessos.remove(1);
		return tarefa;
	}
	
	public void executa(){
		if (!listaDeProcessos.isEmpty()){
			finalizouProcesso = false;
			processoAtual = listaDeProcessos.get(0);
			tempoDeExecucao++;
			System.out.println("CPU " + ID + " executando tarefa " +tempoDeExecucao+"/"+processoAtual.getDuracao());
			if (tempoDeExecucao == processoAtual.getDuracao()){
				System.out.println("CPU " + ID + " concluiu tarefa.");
				finalizouProcesso = true;
				listaDeProcessos.remove(0);
				tempoDeExecucao = 0;
			}
		}	
	}

}
