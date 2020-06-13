package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Atividade;

import util.JpaUtil;

public class AtividadeDaoImpl implements AtividadeDao {

	EntityManager ent = JpaUtil.getEntityManager();
	private EntityTransaction tx;

	@Override
	public boolean inserir(Atividade atividade) {
		tx.begin();

		ent.persist(atividade);
		tx.commit();
		return true;
	}

	@Override
	public void alterar(Atividade atividade) {
		tx.begin();

		ent.merge(atividade);
		tx.commit();

	}

	@Override
	public void remover(Atividade atividade) {
		tx.begin();

		ent.remove(atividade);
		tx.commit();

	}

	@Override
	public Atividade pesquisar(int id) {
		Atividade atividade = ent.find(Atividade.class, id);
		ent.close();
		return atividade;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Atividade> listarTodos() {
		Query query = ent.createQuery("from Atividade i order by nome asc");

		List<Atividade> atividades = query.getResultList();
		return atividades;
	}

}
