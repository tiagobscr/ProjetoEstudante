package dao;

import java.util.List;

import entidade.Aluno;

import entidade.Materia;



public interface MateriaDao {
	
	public boolean inserir(Materia materia);

	public void alterar(Materia materia);

	public void remover(Materia materia);

	public Materia pesquisar(int id);

	public List<Materia> listarTodos();

	public List<Materia> listarPorAtividade(String nome);

	public List<Materia> listarPorAluno(Aluno aluno);
	
	public List<Materia> pesquisarMateria(Materia materia);

}
