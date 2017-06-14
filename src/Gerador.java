import java.util.Random;

public class Gerador {
	private int _qtddProcessadores;
	private int _Amount;
	private int _TMT;
	private Controlador _controlador;
	
	Gerador(int qtddProcessadores, int Amount, int TMT, Controlador controlador){
		_qtddProcessadores = qtddProcessadores;
		_Amount = Amount;
		_TMT = TMT;
		_controlador = controlador;
	}
	
	public void criaTarefas(int instante){
		for(int i = 0; i < _Amount; i++){
			int CPU = (int)(Math.random()*_qtddProcessadores);
			
			int max = _TMT-1;
			int min = -max;
			int rand = new Random().nextInt(max + 1 - min) + min;
			
			Tarefa nova = new Tarefa(CPU, instante, _TMT+rand);
			_controlador.atribuiTarefa(CPU, nova);
		}
	}
}
