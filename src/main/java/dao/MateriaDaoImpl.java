package dao;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Aluno;

import entidade.Materia;
import util.JpaUtil;

public class MateriaDaoImpl implements MateriaDao {

	EntityManager ent = JpaUtil.getEntityManager();
	private EntityTransaction tx;

	@Override
	public boolean inserir(Materia materia) {
		tx = ent.getTransaction();
		tx.begin();

		ent.persist(materia);
		tx.commit();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage("Salvo com sucesso");
		facesContext.addMessage(null, facesMessage);

		return true;

	}

	@Override
	public void alterar(Materia materia) {
		tx = ent.getTransaction();
		tx.begin();

		ent.merge(materia);
		tx.commit();

	}

	@Override
	public void remover(Materia materia) {
		tx = ent.getTransaction();
		tx.begin();

		ent.remove(materia);
		tx.commit();

	}

	@Override
	public Materia pesquisar(int id) {
       Materia materia = ent.find(Materia.class, id);
		
		return materia;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Materia> listarTodos() {
		Query query = ent.createQuery("from Materia m order by m.nome ");

		List<Materia> materias = query.getResultList();
		
		
		return materias;

		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Materia> listarPorAtividade(String nome) {
		Query query = ent.createQuery("Select m FROM Materia m,Atividade i WHERE m.id = i.materia AND i.nome like '%" + nome
				+ "%'   ORDER BY m.nome ASC");

		List<Materia> materias = query.getResultList();
		
		return materias;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Materia> listarPorAluno(Aluno aluno) {
		Query query = ent.createQuery("from Materia m Where m.aluno = '"+ aluno.getEmail() +"'  ");

		List<Materia> materias = query.getResultList();
		
		return materias;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Materia> pesquisarMateria(Materia materia) {
      Query query = ent.createQuery("from Materia m where 1 = 1"  + this.montarQuery(materia));
      
     // where 1=1 "Select m FROM Materia r,Atividade i WHERE m.aluno="'+ materia.getAluno().getEmail()+'" m.id = i.receita AND i.nome like '%" + nome + "%'   ORDER BY r.nome ASC"
		
		List<Materia> materias = query.getResultList();
	
		return materias;
		
	}
	
private String montarQuery(Materia materia) {
		
		String query = "";
		
		
		if (materia.getNome() != null && !materia.getNome().isEmpty()) {
			query += "and Upper(m.nome) like Upper('%" + materia.getNome() + "%')";
		}
		
		if (materia.getAluno().getEmail() != null && !materia.getAluno().getEmail().isEmpty()) {
			query += "and m.aluno='"+materia.getAluno().getEmail()+"'" ; 
					 
		}
		
		if (materia.getCurso() != null && !materia.getCurso().isEmpty()) {
			query += "and Upper(m.curso) like Upper('%"+ materia.getCurso() +"%')";
		}
		
		
		
		return query;
	}


}
