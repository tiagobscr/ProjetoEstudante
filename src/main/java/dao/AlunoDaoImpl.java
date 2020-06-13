package dao;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Aluno;

import util.JpaUtil;

public class AlunoDaoImpl implements AlunoDao {

	EntityManager ent = JpaUtil.getEntityManager();
	private EntityTransaction tx;

	@Override
	public boolean inserir(Aluno aluno) {
		tx = ent.getTransaction();
		tx.begin();

		ent.persist(aluno);
		tx.commit();

		// ent.close();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage("Salvo com sucesso");
		facesContext.addMessage(null, facesMessage);

		return true;

	}

	@Override
	public void alterar(Aluno aluno) {
		tx = ent.getTransaction();
		tx.begin();

		ent.merge(aluno);
		tx.commit();

	}

	@Override
	public void remover(Aluno aluno) {
		tx = ent.getTransaction();
		tx.begin();
		System.out.println("remover");
		ent.remove(aluno);
		tx.commit();
		// ent.close();
		System.out.println("removido");

	}

	@Override
	public Aluno pesquisar(String email) {
		Aluno aluno = ent.find(Aluno.class, email);
		// ent.close();
		return aluno;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Aluno> listarTodos() {
		Query query = ent.createQuery("from Aluno a order by nome asc");

		List<Aluno> alunos = query.getResultList();

		return alunos;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Aluno> listarNome(String nome) {
		Query query = ent.createQuery("From  Aluno Where nome LIKE '%" + nome + "%' ORDER BY nome asc");

		List<Aluno> alunos = query.getResultList();
		
		return alunos;

		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Aluno> pesquisarUsuario(Aluno aluno) {
      Query query = ent.createQuery("from Aluno a where 1=1 " + this.montarQuery(aluno));
		
		List<Aluno> alunos = query.getResultList();
	
		return alunos;
		
	}
	
private String montarQuery(Aluno aluno) {
		
		String query = "";
		
		
		if (aluno.getNome() != null && !aluno.getNome().isEmpty()) {
			query += "and Upper(a.nome) like Upper('%" + aluno.getNome() + "%')";
		}
		
		if (aluno.getEmail() != null && !aluno.getEmail().isEmpty()) {
			query += "and a.email='"+aluno.getEmail()+"'" ; 
					 
		}
		
		if (aluno.getCurso() != null && !aluno.getNome().isEmpty()) {
			query += "and Upper(a.curso) like Upper('%"+ aluno.getCurso() +"%')";
		}
		
		
		
		return query;
	}

}
