package entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TELEFONE")
public class Telefone {

	@Id
	@Column(name = "id_telefone")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "ddd")
	private int ddd;
	@Column(name = "numero")
	private String numero;
	@Column(name = "tipo")
	private String tipo;
	@ManyToOne
	@JoinColumn(name = "email_usuario", referencedColumnName = "email", nullable = false)
	private Aluno aluno;

	public int getDdd() {
		return ddd;
	}

	public void setDdd(int ddd) {
		this.ddd = ddd;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	@Override
	public String toString() {
		return "(" + ddd + ")" + numero;
	}

}
