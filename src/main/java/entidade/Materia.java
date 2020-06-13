package entidade;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "MATERIA")
public class Materia {

	@Id
	@Column(name = "id_materia")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "nome", nullable = false)
	private String nome;
	@Column(name = "observacao")
	private String observacao;
	@OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Atividade> atividades;
	@Column(name = "curso")
	private String curso;
	@ManyToOne
	@JoinColumn(name = "email_aluno", referencedColumnName = "email", nullable = false)
	private Aluno aluno;

	public Materia() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<Atividade> atividades) {
		this.atividades = atividades;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Materia other = (Materia) obj;
		if (curso == null) {
			if (other.curso != null)
				return false;
		} else if (!curso.equalsIgnoreCase(other.curso))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equalsIgnoreCase(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return curso + " " + nome;
	}

}
