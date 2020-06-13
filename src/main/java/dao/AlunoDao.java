package dao;

import java.util.List;

import entidade.Aluno;

public interface AlunoDao {

	public boolean inserir(Aluno aluno);

	public void alterar(Aluno aluno);

	public void remover(Aluno usua);

	public Aluno pesquisar(String email);

	public List<Aluno> listarTodos();

	public List<Aluno> listarNome(String nome);

	public List<Aluno> pesquisarUsuario(Aluno aluno);

}
