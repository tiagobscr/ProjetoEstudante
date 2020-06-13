package dao;

import java.util.List;

import entidade.Atividade;



public interface AtividadeDao {
	
	public boolean inserir(Atividade atividade);

	public void alterar(Atividade atividade);

	public void remover(Atividade atividade);

	public Atividade pesquisar(int id);

	public List<Atividade> listarTodos();

}
