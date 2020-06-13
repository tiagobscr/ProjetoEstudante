package controle;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import dao.AlunoDao;
import dao.AlunoDaoImpl;
import dao.AtividadeDao;
import dao.AtividadeDaoImpl;
import dao.MateriaDao;
import dao.MateriaDaoImpl;
import entidade.Aluno;
import entidade.Atividade;

import entidade.Materia;

@ManagedBean(name = "MateriaBean")
@ViewScoped
public class MateriaBean {

	private Aluno aluno;
	private Materia materia;
	private Atividade atividade;
	private List<Materia> materias;
	private AlunoDao alunoDao;
	private MateriaDao materiaDao;
	private String nomePesquisa;
	private int quantPesquisada;
	private String materiaSelecionada;
	private List<Atividade> atividades;
	private AtividadeDao atividadeDao;
	private List<Aluno> alunos;
	private List<Atividade> novaAtividades;
	private String emailPesquisa;
	private LocalDate dataAtividade;
	private String data;
	private Aluno usuarioLogado;
	private String cursoPesquisa;

	private static final String CRIAR = "manterMateria.xhtml";
	private static final String MATERIA = "pesquisarMaterias.xhtml";
	private static final String Login = "index.xhtml";

	public MateriaBean() {
		this.aluno = new Aluno();
		this.materia = new Materia();
		this.atividade = new Atividade();
		this.atividades = new ArrayList<Atividade>();
		this.materias = new ArrayList<Materia>();
		// Instanciando a interface com a implementa��o, passando a conex�o
		this.materiaDao = new MateriaDaoImpl();
		this.alunoDao = new AlunoDaoImpl();
		this.atividadeDao = new AtividadeDaoImpl();
		// Recupera a lista de todos as receitas e ingredientes
		this.materias = this.materiaDao.listarTodos();
		this.alunos = this.alunoDao.listarTodos();
		this.materia.setAluno(this.aluno);
		this.atividades = this.atividadeDao.listarTodos();
		this.novaAtividades = new ArrayList<Atividade>();
		if (atualizarUsuarioLogado() != null)
			this.usuarioLogado = atualizarUsuarioLogado();
		this.materias = this.materiaDao.listarPorAluno(usuarioLogado);

	}

	public Aluno atualizarUsuarioLogado() {
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		this.usuarioLogado = (Aluno) sessao.getAttribute("usuarioLogado");
		System.out.println(this.usuarioLogado);
		return this.usuarioLogado;
	}

	public void pesquisar() {
		this.materias = this.materiaDao.listarPorAluno(usuarioLogado);

		System.out.println("Teste");

	}

	public void pesquisarAtividade() {
		System.out.println(this.nomePesquisa);
		this.materias = this.materiaDao.listarPorAtividade(nomePesquisa);
		this.quantPesquisada = this.materias.size();

	}
	
	public void pesquisaGeral() {
		Materia materiaPesquisa = new Materia();
		materiaPesquisa.setNome(this.nomePesquisa);
		materiaPesquisa.setCurso(this.cursoPesquisa);
		materiaPesquisa.setAluno(this.usuarioLogado);
		
		
		this.materias = this.materiaDao.pesquisarMateria(materiaPesquisa);
		this.quantPesquisada = this.alunos.size();

	}

	public void pesquisarUsuario() {

		if (this.alunoDao.pesquisar(this.emailPesquisa) != null) {
			Aluno alunoAtual = this.alunoDao.pesquisar(this.emailPesquisa);
			this.materias.clear();
			this.materias = this.materiaDao.listarPorAluno(alunoAtual);

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", " email inválido "));
		}

	}

	public void salvar() throws IOException {

		if (this.alunoDao.pesquisar(this.usuarioLogado.getEmail()) != null) {

			this.materia.setAluno(this.usuarioLogado);

			if (materiaDao.inserir(this.materia)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						this.materia.getNome() + " salvo com sucesso."));
				System.out.println("=======salvou ======");
				this.materia = new Materia();
				this.atividade = new Atividade();
				abrirPesquisarMateria();

			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao inserir."));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Email inválido."));
		}
	}

	public void exibirMateria() {

		Materia materiaAtual = this.materiaDao.pesquisar(this.materia.getId());
		this.materia = materiaAtual;
		materiaDao.alterar(materiaAtual);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", materiaAtual.getNome() + " alterado com sucesso."));
		pesquisar();
		System.out.println("alterado");

	}

	public void removerMateria() {

		Materia materiaAtual = this.materiaDao.pesquisar(this.materia.getId());
		this.materia = materiaAtual;

		materiaDao.remover(materiaAtual);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", materiaAtual.getNome() + " Excluido com sucesso."));
		System.out.println("removido");
		pesquisar();

	}

	public void novaAtividade() {
		Atividade atividadeNova = new Atividade();

		atividadeNova.setNome(this.atividade.getNome());
		atividadeNova.setTipo(this.atividade.getTipo());
		atividadeNova.setDataEntrega(this.atividade.getDataEntrega());
		atividadeNova.setDescricao(this.atividade.getDescricao());
		atividadeNova.setStatus(this.atividade.getStatus());
		//atividadeNova.setArquivo(arquivo);
		this.atividades.add(atividadeNova);
	}

	public void adicionarAtividade() {
		Atividade atividadeNova = new Atividade();
		atividadeNova.setNome(this.atividade.getNome());
		atividadeNova.setTipo(this.atividade.getTipo());
		atividadeNova.setDataEntrega(this.atividade.getDataEntrega());
		atividadeNova.setDescricao(this.atividade.getDescricao());
		atividadeNova.setStatus(this.atividade.getStatus());
        atividadeNova.setDataAtribuido(Calendar.getInstance().getTime());
		if(this.materia.getAtividades()==null)this.materia.setAtividades(this.novaAtividades);
		this.materia.setAluno(this.usuarioLogado);
        
		atividadeNova.setMateria(this.materia);

		if (!this.existeAtividade(atividadeNova)) {
			this.materia.getAtividades().add(atividadeNova);
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", " Atividade já cadastrado para "));

		}

		this.atividade = new Atividade();
	}

	private boolean existeAtividade(Atividade atividade) {
		boolean resultado = false;

		for (Atividade atividadeAtual : this.materia.getAtividades()) {
			if (atividadeAtual.equals(atividade)) {
				// itemAtual.getNome().equalsIgnoreCase(item.getNome())
				// && itemAtual.getTipo().equalsIgnoreCase(item.getTipo(
				resultado = true;
			}
		}

		return resultado;
	}

	public void removerAtividade() {

		int i = 0;
		while (i < this.materia.getAtividades().size()) {

			if (this.materia.getAtividades().get(i).getId() == this.atividade.getId()) {
				this.materia.getAtividades().remove(i);
			}
			i = i + 1;
		}

	}
	
	public void atualizaData() {
		this.atividade.setDataAtribuido(Calendar.getInstance().getTime());
		System.out.println(this.atividade.getDataAtribuido());
	}

	public void abrirManterMateria() throws IOException {

		if (atualizarUsuarioLogado() != null)
			FacesContext.getCurrentInstance().getExternalContext().redirect(CRIAR);
		else
			abrirLogin();
	}

	public void abrirPesquisarMateria() throws IOException {
		if (atualizarUsuarioLogado() != null)
			FacesContext.getCurrentInstance().getExternalContext().redirect(MATERIA);
		else
			abrirLogin();
	}

	public void abrirLogin() throws IOException {
		this.usuarioLogado = new Aluno();
		FacesContext.getCurrentInstance().getExternalContext().redirect(Login);

	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public List<Materia> getMaterias() {
		return materias;
	}

	public void setMaterias(List<Materia> materias) {
		this.materias = materias;
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

	public int getQuantPesquisada() {
		return quantPesquisada;
	}

	public void setQuantPesquisada(int quantPesquisada) {
		this.quantPesquisada = quantPesquisada;
	}

	public List<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<Atividade> atividades) {
		this.atividades = atividades;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public List<Atividade> getNovaAtividades() {
		return novaAtividades;
	}

	public void setNovaAtividades(List<Atividade> novaAtividades) {
		this.novaAtividades = novaAtividades;
	}

	public String getEmailPesquisa() {
		return emailPesquisa;
	}

	public void setEmailPesquisa(String emailPesquisa) {
		this.emailPesquisa = emailPesquisa;
	}

	public LocalDate getDataAtividade() {
		return dataAtividade;
	}

	public void setDataAtividade(LocalDate dataAtividade) {
		this.dataAtividade = dataAtividade;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Aluno getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Aluno usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public String getMateriaSelecionada() {
		return materiaSelecionada;
	}

	public void setMateriaSelecionada(String materiaSelecionada) {
		this.materiaSelecionada = materiaSelecionada;
	}

	public String getCursoPesquisa() {
		return cursoPesquisa;
	}

	public void setCursoPesquisa(String cursoPesquisa) {
		this.cursoPesquisa = cursoPesquisa;
	}
	
	

}
