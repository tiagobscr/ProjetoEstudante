package entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ATIIDADE")
public class Atividade {

	@Id
	@Column(name = "id_atividade")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "nome", nullable = false)
	private String nome;
	@Column(name = "tipo", nullable = false)
	private String tipo;
	@Column(name = "dataEntrega")
	private Date dataEntrega;
	@Column(name = "dataAtribuido")
	private Date dataAtribuido;
	@Column(name = "status")
	private String status;
	@Column(name = "descricao", length = 1337)
	private String descricao;
	@ManyToOne
	@JoinColumn(name = "id_materia", referencedColumnName = "id_materia", nullable = false)
	private Materia materia;
	@Column
	@Lob
	private byte[] arquivo;

	public Atividade() {

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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public Date getDataAtribuido() {
		return dataAtribuido;
	}

	public void setDataAtribuido(Date dataAtribuido) {
		this.dataAtribuido = dataAtribuido;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataEntrega == null) ? 0 : dataEntrega.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		Atividade other = (Atividade) obj;
		if (dataEntrega == null) {
			if (other.dataEntrega != null)
				return false;
		} else if (!dataEntrega.equals(other.dataEntrega))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equalsIgnoreCase(other.nome))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equalsIgnoreCase(other.tipo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}

}
