
public class Tarefa {
	private int _CPU;
	private int _hora;
	private int _duracao;
	
	Tarefa(int CPU, int hora, int duracao){
		_CPU = CPU;
		_hora = hora;
		_duracao = duracao;
	}
	
	public int getDuracao(){
		return _duracao;
	}
}
