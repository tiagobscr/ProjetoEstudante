package controle;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import dao.AlunoDao;
import dao.AlunoDaoImpl;
import entidade.Aluno;
import entidade.Endereco;
import entidade.Telefone;

import util.BuscaCEP;
import util.CalculoIdade;

@ManagedBean(name = "AlunoBean")
@ViewScoped
public class AlunoBean {

	private Aluno aluno;
	private Telefone telefone;
	private List<Aluno> alunos;
	private AlunoDao alunoDao;
	private String emailSelecionado;
	private String confirmaSenha;
	private String nomePesquisa;
	private String emailPesquisa;
	private String cursoPesquisa;
	private String foneMask;
	private int quantPesquisada;
	private BuscaCEP buscaCep;
	private String CEP;
	private Endereco endereco;
	private LocalDate dataNascimento;
	private String data;
	private Aluno usuarioLogado;

	private static final String PESQUISAR = "pesquisarUsuario.xhtml";

	private static final String Manter = "manterUsuario.xhtml";

	private static final String Login = "index.xhtml";
	
	

	public AlunoBean() {
		this.aluno = new Aluno();
		this.telefone = new Telefone();
		this.endereco = new Endereco();

		this.alunos = new ArrayList<Aluno>();
		// Instanciando a interface com a implementa��o, passando a conex�o
		this.alunoDao = new AlunoDaoImpl();
		// Recupera a lista de todos os usuarios
		this.alunos = this.alunoDao.listarTodos();
		this.quantPesquisada = this.alunos.size();
		this.aluno = new Aluno();
		this.aluno.setTelefones(new ArrayList<Telefone>());
		this.aluno.setEnderecos(new ArrayList<Endereco>());
		this.foneMask = "99999-9999";
		if (atualizarUsuarioLogado() != null)
			this.usuarioLogado = atualizarUsuarioLogado();

	}

	public Aluno atualizarUsuarioLogado() {
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		this.usuarioLogado = (Aluno) sessao.getAttribute("usuarioLogado");
		System.out.println(this.usuarioLogado);
		return this.usuarioLogado;
	}

	public boolean habilitaBotao() {
		return this.aluno.getEmail().equals(this.usuarioLogado.getEmail());
	}

	/**
	 * Metodo pesquisar, vai realizar a consulta na base e retornar a lista completa
	 */
	public void pesquisar() {
		this.alunos = this.alunoDao.listarTodos();
		this.quantPesquisada = this.alunos.size();
		System.out.println("Teste");

	}

	/**
	 * Metodo pesquisarNome, vai realizar a consulta na base e retornar a lista por
	 * nome
	 */

	public void pesquisarNome() {
		System.out.println(this.nomePesquisa);
		this.alunos = this.alunoDao.listarNome(this.nomePesquisa);
		this.quantPesquisada = this.alunos.size();
	}

	/**
	 * Metodo pesquisarEmail, vai realizar a consulta na base e retornar a lista por
	 * email
	 */

	public void pesquisarEmail() {
		if (this.alunoDao.pesquisar(this.emailPesquisa) != null) {
			Aluno alunoAtual = this.alunoDao.pesquisar(this.emailPesquisa);
			this.alunos.clear();
			this.alunos.add(alunoAtual);
			this.quantPesquisada = this.alunos.size();
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", " email inválido "));
		}
	}

	public void pesquisaGeral() {
		Aluno usuarioPesquisa = new Aluno();
		usuarioPesquisa.setNome(this.nomePesquisa);
		usuarioPesquisa.setEmail(this.emailPesquisa);
		
		System.out.println(usuarioPesquisa);
		this.alunos = this.alunoDao.pesquisarUsuario(usuarioPesquisa);
		this.quantPesquisada = this.alunos.size();

	}

	public void cadastrar() {
		System.out.println(aluno);
		alunoDao.inserir(aluno);

		System.out.println("Cadastrado");
	}

	public void editar() {
		Aluno alunoAtual = this.alunoDao.pesquisar(this.aluno.getEmail());
		alunoAtual = this.aluno;
		alunoDao.alterar(alunoAtual);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", alunoAtual.getNome() + " alterado com sucesso."));

		System.out.println("alterado");
	}

	public void remover() {

		Aluno alunoAtual = this.alunoDao.pesquisar(this.aluno.getEmail());
		// usuarioAtual=this.usuario;

		alunoDao.remover(alunoAtual);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", alunoAtual.getNome() + " Excluido com sucesso."));
		System.out.println("removido");
		pesquisar();
	}

	/**
	 * Metodo setarMask, vai setar a m�scara do telefone para celular ou fixo (9 ou
	 * 8 digitos)
	 */

	public void setarMask() {

		System.out.println(this.telefone.getTipo());
		if (this.telefone.getTipo().equals("CELULAR")) {
			this.foneMask = "99999-9999";
		} else {
			this.foneMask = "9999-9999";
		}

	}

	public void adicionarTelefone() {
		Telefone telefoneNovo = new Telefone();
		telefoneNovo.setDdd(this.telefone.getDdd());
		telefoneNovo.setNumero(this.telefone.getNumero());
		telefoneNovo.setTipo(this.telefone.getTipo());
		telefoneNovo.setAluno(this.aluno);

		if (!this.existeTelefone(telefoneNovo)) {
			this.aluno.getTelefones().add(telefoneNovo);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					" Telefone já cadastrado para " + this.aluno.getNome()));

		}

		this.telefone = new Telefone();

	}

	private boolean existeTelefone(Telefone telefone) {
		boolean resultado = false;

		for (Telefone telefoneAtual : this.aluno.getTelefones()) {
			if (telefoneAtual.getNumero().equals(telefone.getNumero()) && telefoneAtual.getDdd() == telefone.getDdd()) {
				resultado = true;
			}
		}

		return resultado;
	}

	public void removerTelefone() {

		int i = 0;
		while (i < this.aluno.getTelefones().size()) {

			if (this.aluno.getTelefones().get(i).getId() == this.telefone.getId()) {
				this.aluno.getTelefones().remove(i);
			}
			i = i + 1;
		}

		System.out.println(this.aluno.getTelefones());

	}

	public void salvar() throws IOException {
		Aluno alunoAtual = this.alunoDao.pesquisar(this.aluno.getEmail());

		if (alunoAtual != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Usuário " + this.aluno.getEmail() + " já cadastrado"));
		} else {
			if (alunoDao.inserir(this.aluno)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						this.aluno.getEmail() + " salvo com sucesso."));
				System.out.println("=======salvou ======");
				this.aluno = new Aluno();
				this.telefone = new Telefone();
				this.usuarioLogado = atualizarUsuarioLogado();
				System.out.println(this.usuarioLogado);
				if (this.usuarioLogado != null)
					abrirPesquisarUsuario();
				else
					abrirLogin();

			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao inserir."));
			}
		}

	}

	public void abrirManterUsuario() throws IOException {

		if (atualizarUsuarioLogado() != null)
			FacesContext.getCurrentInstance().getExternalContext().redirect(Manter);
		else
			abrirLogin();
	}

	public void abrirPesquisarUsuario() throws IOException {
		if (atualizarUsuarioLogado() != null)
			FacesContext.getCurrentInstance().getExternalContext().redirect(PESQUISAR);
		else
			abrirLogin();

	}

	public void abrirLogin() throws IOException {
		this.usuarioLogado = new Aluno();
		FacesContext.getCurrentInstance().getExternalContext().redirect(Login);

	}

	public void cepBusca() {
		String array[] = new String[30];

		this.buscaCep = new BuscaCEP();
		System.out.println(this.CEP);

		if (this.CEP.length() < 9) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Digite um CEP válido."));

		} else {
			System.out.println(this.CEP);
			System.out.println(this.CEP.length());

			if (this.CEP.length() == 9)
				array = this.buscaCep.buscaCEP(this.CEP);

			if (array[1].equals("erro")) {
				System.out.println("Digite1 um CEP Valido");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Digite um CEP válido"));
			} else {
				if (array != null)
					this.endereco.setRua(array[7]);

				if (array != null)
					this.endereco.setComplemento(array[15] + " " + array[19] + " " + array[23] + " CEP: " + this.CEP);
				System.out.println(this.endereco);
			}

		}
	}

	public void adicionarEndereco() {
		Endereco enderecoNovo = new Endereco();
		enderecoNovo.setRua(this.endereco.getRua());
		enderecoNovo.setNumero(this.endereco.getNumero());
		enderecoNovo.setComplemento(this.endereco.getComplemento());
		enderecoNovo.setAluno(this.aluno);

		if (!this.existeEndereco(enderecoNovo)) {
			this.aluno.getEnderecos().add(enderecoNovo);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					" Endereço já cadastrado para " + this.aluno.getNome()));

		}

		this.endereco = new Endereco();

	}

	private boolean existeEndereco(Endereco endereco) {
		boolean resultado = false;

		for (Endereco enderecoAtual : this.aluno.getEnderecos()) {
			if (enderecoAtual.getRua().equals(endereco.getRua()) && enderecoAtual.getNumero() == endereco.getNumero()) {
				resultado = true;
			}
		}

		return resultado;
	}

	public void removerEndereco() {

		int i = 0;
		while (i < this.aluno.getEnderecos().size()) {

			if (this.aluno.getEnderecos().get(i).getId() == this.endereco.getId()) {
				this.aluno.getEnderecos().remove(i);
			}
			i = i + 1;
		}

		System.out.println(this.aluno.getEnderecos());

	}

	public void convertDate() throws ParseException {

		System.out.println(data);
		dataNascimento = CalculoIdade.format(this.data);
		this.aluno.setIdade(CalculoIdade.idade(dataNascimento));
		System.out.println(this.aluno.getIdade());

		this.aluno.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse(data));

	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public String getEmailSelecionado() {
		return emailSelecionado;
	}

	public void setEmailSelecionado(String emailSelecionado) {
		this.emailSelecionado = emailSelecionado;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

	public String getEmailPesquisa() {
		return emailPesquisa;
	}

	public void setEmailPesquisa(String emailPesquisa) {
		this.emailPesquisa = emailPesquisa;
	}

	public String getFoneMask() {
		return foneMask;
	}

	public void setFoneMask(String foneMask) {
		this.foneMask = foneMask;
	}

	public int getQuantPesquisada() {
		return quantPesquisada;
	}

	public void setQuantPesquisada(int quantPesquisada) {
		this.quantPesquisada = quantPesquisada;
	}

	public BuscaCEP getBuscaCep() {
		return buscaCep;
	}

	public void setBuscaCep(BuscaCEP buscaCep) {
		this.buscaCep = buscaCep;
	}

	public String getCEP() {
		return CEP;
	}

	public void setCEP(String cEP) {
		CEP = cEP;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
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

	public String getCursoPesquisa() {
		return cursoPesquisa;
	}

	public void setCursoPesquisa(String cursoPesquisa) {
		this.cursoPesquisa = cursoPesquisa;
	}

}
